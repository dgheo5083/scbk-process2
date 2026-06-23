package com.scbank.process.api.fw.common.errorcode.impl;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeManager;
import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;
import com.scbank.process.api.fw.core.cache.ICacheKey;
import com.scbank.process.api.fw.core.cache.ICacheManager;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.concurrent.lock.ReadWriteLockSupport;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@CommonComponent(name = "프레임워크/공통 에러코드 매니저 컴포넌트", description = "", author = "sungdon.choi")
public class DefaultErrorCodeManager extends ReadWriteLockSupport implements IErrorCodeManager {

    private static final String CACHE_NAME = "csl-errorcode";

    private final ICacheManager cacheManager;
    private final Config config;

    @Override
    public void init() {
        this.withWrite(() -> loadErrorCodeCache(false));
    }

    @Override
    public void reload() {
        this.withWrite(() -> {
            log.info("# 프레임워크 에러코드 캐시 리로드 수행");
            loadErrorCodeCache(true);
        });
    }

    /**
     * 프레임워크 에러코드 로드
     * @param clearBeforePut 캐시 초기화 여부
     */
    private void loadErrorCodeCache(boolean clearBeforePut) {
        try {
            String configLocation = config.configLocation();
            if (log.isInfoEnabled()) {
                log.info("# 프레임워크 에러코드 목록 로드, 경로: [{}]", configLocation);
            }

            if (StringUtils.isEmpty(configLocation)) {
                log.info("# 프레임워크 에러코드 목록 로드 실패, 설정 파일 경로 미설정");
                return;
            }

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(configLocation);
            if (resources.length == 0) {
                log.info("# 프레임워크 에러코드 설정 파일이 없습니다. 경로: [{}]", configLocation);
                return;
            }

            if (clearBeforePut) {
                cacheManager.clear(CACHE_NAME);
            }

            for (Resource resource : resources) {
                if (resource == null) {
                    continue;
                }

                String resourcePath = resource.getFilename();

                try {
                    List<IErrorCodeInfo> errorCodeInfoList = this.parse(resource);
                    for (IErrorCodeInfo e : errorCodeInfoList) {
                        cacheManager.put(CACHE_NAME, new ErrorCodeCacheKey(e.getCode(), e.getLangCode()), e);
                    }
                } catch (Exception ex) {
                    log.warn("에러코드 XML 파싱 실패: {}", resourcePath, ex);
                }

                log.info("# 프레임워크 에러코드 로드 완료 경로: {}", resourcePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @ComponentOperation(name = "에러코드정보 획득")
    public IErrorCodeInfo getErrorCodeInfo(String errorCode, String langCode) {
        return getErrorCodeCache(errorCode, langCode);
    }

    @Override
    @ComponentOperation(name = "에러메시지 획득")
    public String getErrorMessage(String errorCode, String langCode) {
        IErrorCodeInfo errorCodeInfo = this.getErrorCodeInfo(errorCode, langCode);
        return (errorCodeInfo != null) ? errorCodeInfo.getMessage() : StringUtils.EMPTY;
    }
    
    @Override
    @ComponentOperation(name = "에러메시지 획득 with message args")
	public String getErrorMessage(String errorCode, String langCode, Object... args) {
    	IErrorCodeInfo errorCodeInfo = this.getErrorCodeInfo(errorCode, langCode);
    	if (errorCodeInfo != null) {
    		String errorMessage = errorCodeInfo.getMessage();
    		if (!StringUtils.hasLength(errorMessage)) {
    			return StringUtils.EMPTY;
    		}
    		
    		if (args != null && args.length > 0) {
    			errorMessage = MessageFormat.format(errorMessage, args);
    			return errorMessage;
    		}
    	}
		return StringUtils.EMPTY;
	}

    @Override
    @ComponentOperation(name = "에러 가이드 메시지 목록 획득")
    public List<IErrorGuideMessageInfo> getErrorGuideMessages(String errorCode, String langCode) {
        IErrorCodeInfo errorCodeInfo = this.getErrorCodeInfo(errorCode, langCode);
        return (errorCodeInfo != null) ? errorCodeInfo.getErrorGuideMessages() : List.of();
    }

    @Override
    @ComponentOperation(name = "에러코드정보 획득")
    public IErrorCodeInfo getErrorCodeInfo(String errorCode) {
        return IErrorCodeManager.super.getErrorCodeInfo(errorCode);
    }

    @Override
    @ComponentOperation(name = "에러메시지 획득")
    public String getErrorMessage(String errorCode) {
        return IErrorCodeManager.super.getErrorMessage(errorCode);
    }

    @Override
    @ComponentOperation(name = "에러 가이드 메시지 목록 획득")
    public List<IErrorGuideMessageInfo> getErrorGuideMessages(String errorCode) {
        return IErrorCodeManager.super.getErrorGuideMessages(errorCode);
    }

    private IErrorCodeInfo getErrorCodeCache(String errorCode, String langCode) {
        return this.withRead(() -> this.cacheManager.get(CACHE_NAME, new ErrorCodeCacheKey(errorCode, langCode),
                IErrorCodeInfo.class));
    }

    private List<IErrorCodeInfo> parse(Resource resource) throws Exception {
        List<IErrorCodeInfo> errorCodeInfoList = new ArrayList<>();
        try (InputStream in = resource.getInputStream()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(in);
            Element root = document.getRootElement();

            String language = StringUtils.defaultIfEmpty(root.getAttributeValue("lang"), "").trim();
            List<Element> errorCodeList = root.getChildren("errorCode");
            if (CollectionUtils.isEmpty(errorCodeList)) {
                return errorCodeInfoList;
            }

            return errorCodeList.stream()
                    .map(e -> parseErrorCodeInfo(e, language))
                    .toList();
        }
    }

    private IErrorCodeInfo parseErrorCodeInfo(Element element, String language) {
        String errorCode = StringUtils.defaultIfEmpty(element.getAttributeValue("code"), "").trim();
        String errorMessage = StringUtils.defaultIfEmpty(element.getChildText("msg"), "").trim();

        List<IErrorGuideMessageInfo> errorGuideMessageInfoList = new ArrayList<>();
        Element guideMessageList = element.getChild("addMsgList");
        if (guideMessageList != null) {
            errorGuideMessageInfoList = this.parseErrorGuideMessageList(guideMessageList);
        }

        return DefaultErrorCodeInfo.builder()
                .code(errorCode)
                .langCode(language)
                .message(errorMessage)
                .errorGuideMessageInfos(errorGuideMessageInfoList)
                .build();
    }

    private List<IErrorGuideMessageInfo> parseErrorGuideMessageList(Element element) {
        List<Element> messageList = element.getChildren("addMsg");
        if (CollectionUtils.isEmpty(messageList)) {
            return List.of();
        }

        return messageList.stream()
                .map(e -> DefaultErrorGuideMessageInfo.builder()
                        .message(e.getText())
                        .order(Integer.parseInt(StringUtils.defaultIfEmpty(e.getAttributeValue("order"), "0")))
                        .build())
                .sorted()
                .collect(Collectors.toList());
    }

    private record ErrorCodeCacheKey(String errorCode, String langCode) implements ICacheKey {
    }
}
