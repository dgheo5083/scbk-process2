package com.scbank.process.api.fw.common.code.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.code.ICodeInfo;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.cache.ICacheKey;
import com.scbank.process.api.fw.core.cache.ICacheManager;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.concurrent.lock.ReadWriteLockSupport;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.LocaleUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@CommonComponent(name = "프레임워크/공통 공통코드 매니저 컴포넌트", description = "", author = "sungdon.choi")
public class DefaultCodeManager extends ReadWriteLockSupport implements ICodeManager {

    private static final String CACHE_NAME = "csl-code";

    private final ICacheManager cacheManager;
    private final Config config;

    @Override
    public void init() {
        this.withWrite(() -> loadCodeCache(false));
    }

    @Override
    public void reload() {
        this.withWrite(() -> {
            log.info("# 공통코드 캐시 리로드 수행");
            loadCodeCache(true);
        });
    }

    /**
     * 공통코드 목록 캐시 로드
     * 
     * @param clearBeforePut
     */
    private void loadCodeCache(boolean clearBeforePut) {
        try {
            String configLocation = config.configLocation();
            log.info("# 프레임워크 공통코드 목록 로드, 경로: [{}]", configLocation);

            if (StringUtils.isEmpty(configLocation)) {
                log.info("# 프레임워크 공통코드 목록 로드 실패, 설정 파일 경로 미설정");
                return;
            }

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(configLocation);
            if (resources.length == 0) {
                log.info("# 프레임워크 공통코드 설정 파일이 없습니다. 경로: [{}]", configLocation);
                return;
            }

            if (clearBeforePut) {
                cacheManager.clear(CACHE_NAME);
            }

            for (Resource resource : resources) {
                try {
                    ICodeInfo codeInfo = this.parse(resource);
                    cacheManager.put(CACHE_NAME, new CodeCacheKey(codeInfo.getKey(), codeInfo.getLocale()), codeInfo);
                    // log.debug("# 공통코드 로드 완료: code={}, locale={}, itemCount={}",
                    // codeInfo.getKey(), codeInfo.getLocale(), codeInfo.getCodeItemList().size());
                } catch (Exception ex) {
                    log.warn("공통코드 XML 파싱 실패: {}", resource.getFilename(), ex);
                }

                if (log.isDebugEnabled()) {
                    log.debug("# 프레임워크 공통코드 로드 완료 경로: {}", resource.getFilename());
                }
            }
        } catch (Exception e) {
            log.error("공통코드 로딩 중 오류 발생", e);
        }
    }

    @Override
    @ComponentOperation(name = "공통코드 아이템 값 획득 메소드", trace = false)
    public String getCodeItem(String codeKey, String codeItemKey, String locale) {
        return this.getCodeItemList(codeKey, locale).stream()
                .filter(i -> codeItemKey.equals(i.getKey()))
                .findFirst()
                .map(ICodeItemInfo::getValue)
                .orElse(null);
    }

    @Override
    @ComponentOperation(name = "공통코드 아이템 목록 획득 메소드", trace = false)
    public List<ICodeItemInfo> getCodeItemList(String codeKey, String locale) {
        ICodeInfo codeInfo = this.withRead(() -> getCodeCache(codeKey, locale));
        if (codeInfo == null || CollectionUtils.isEmpty(codeInfo.getCodeItemList())) {
            return List.of();
        }
        return codeInfo.getCodeItemList();
    }

    @Override
    @ComponentOperation(name = "공통코드 아이템 값 획득 메소드", trace = false)
    public String getCodeItem(String codeKey, String codeItemKey) {
        return ICodeManager.super.getCodeItem(codeKey, codeItemKey);
    }

    @Override
    @ComponentOperation(name = "공통코드 하위 아이템 목록 획득 메소드", trace = false)
    public List<ICodeItemInfo> getCodeItemList(String codeKey) {
        return ICodeManager.super.getCodeItemList(codeKey);
    }

    private ICodeInfo getCodeCache(String codeKey, String langCode) {
        return this.cacheManager.get(CACHE_NAME, new CodeCacheKey(codeKey, langCode), DefaultCodeInfo.class);
    }

    private ICodeInfo parse(Resource resource) throws Exception {
        try (InputStream in = resource.getInputStream()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(in);
            Element category = document.getRootElement();

            String code = StringUtils.defaultIfEmpty(category.getAttributeValue("name"), "").trim();
            String language = StringUtils.defaultIfEmpty(category.getAttributeValue("lang"), "").trim();
            // 언어코드가 없다면 기본 로케일정보를 가져온다.
            if (StringUtils.isEmpty(language)) {
                Locale locale = LocaleUtils.toLocale(RuntimeContext.getDefaultLocale());
                language = locale.getLanguage();
            }

            List<Element> codeList = category.getChildren("code");
            if (CollectionUtils.isEmpty(codeList)) {
                return DefaultCodeInfo.builder()
                        .key(code)
                        .locale(language)
                        .codeItemList(List.of())
                        .build();
            }

            List<ICodeItemInfo> codeItemInfoList = codeList.stream()
                    .map(c -> {
                        String key = StringUtils.defaultIfEmpty(c.getAttributeValue("key"), "").trim();
                        int order = Integer
                                .parseInt(StringUtils.defaultIfEmpty(c.getAttributeValue("order"), "0").trim());
                        String value = StringUtils.defaultIfEmpty(c.getText(), "");
                        return DefaultCodeItemInfo.builder()
                                .key(key)
                                .order(order)
                                .value(value)
                                .build();
                    }).collect(Collectors.toList());

            return DefaultCodeInfo.builder()
                    .key(code)
                    .locale(language)
                    .codeItemList(codeItemInfoList)
                    .build();
        }
    }

    public record CodeCacheKey(String code, String lang) implements ICacheKey {
    }

    @Override
    public Map<String, Object> getAllCodes() {
        return this.withRead(() -> {
            Map<ICacheKey, ICodeInfo> all = cacheManager.getAll(CACHE_NAME, ICodeInfo.class);

            return all.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> {
                                CodeCacheKey key = (CodeCacheKey) entry.getKey();
                                return key.code() + "_" + key.lang();
                            },
                            entry -> entry.getValue().getCodeItemList().stream()
                                    .map(item -> Map.of(
                                            "key", item.getKey(),
                                            "value", item.getValue(),
                                            "order", item.getOrder()))
                                    .collect(Collectors.toList())));
        });
    }
}
