package com.scbank.process.api.fw.common.servicetime.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeManager;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;
import com.scbank.process.api.fw.core.cache.ICacheManager;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.concurrent.lock.ReadWriteLockSupport;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@CommonComponent(name = "프레임워크/공통 메뉴/전문 서비스 이용시간 관리 공통 컴포넌트", description = "메뉴/전문 서비스 이용시간 정보 로드 및 조회 처리 담당", author = "sungdon.choi")
public class DefaultServiceTimeManager extends ReadWriteLockSupport implements IServiceTimeManager {

    private static final String CACHE_NAME = "csl-servicetime";

    private final ICacheManager cacheManager;
    private final Config config;

    @Override
    @ComponentOperation(name = "서비스이용시간 목록 획득", description = "서비스시간그룹의 서비스시간정보를 모두 가져온다.", author = "sungdon.choi")
    public List<IServiceTimeInfo> getServiceTimeList(ServiceTimeGroup group) {
        return this.withRead(() -> {
            List<IServiceTimeInfo> cache = getServiceTimeCache();
            if (CollectionUtils.isEmpty(cache))
                return Collections.emptyList();
            return cache.stream()
                    .filter(s -> group.equals(s.getGroup()))
                    .collect(Collectors.toList());
        });
    }

    @Override
    @ComponentOperation(name = "서비스이용시간 정보 획득", description = "서비스시간그룹과 메뉴ID 또는 전문ID를 이용하여, 서비스시간정보를 가져온다.", author = "sungdon.choi")
    public IServiceTimeInfo getServiceTime(ServiceTimeGroup group, String code) {
        return getServiceTimeList(group).stream()
                .filter(s -> code.equals(s.getCode()))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private List<IServiceTimeInfo> getServiceTimeCache() {
        return this.cacheManager.get(CACHE_NAME, CACHE_NAME, List.class);
    }

    @Override
    public void init() {
        this.withWrite(() -> loadServiceTimeCache(false));
    }

    @Override
    public void reload() {
        this.withWrite(() -> {
            log.info("# 프레임워크 서비스이용시간 캐시 리로드 수행");
            loadServiceTimeCache(true);
        });
    }

    private void loadServiceTimeCache(boolean clearBeforePut) {
        try {
            String configLocation = config.configLocation();
            log.info("# 프레임워크 서비스이용시간 목록 로드, 경로: [{}]", configLocation);

            if (StringUtils.isEmpty(configLocation)) {
                log.info("# 프레임워크 서비스이용시간 목록 로드 실패, 설정 파일 경로 미설정");
                return;
            }

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(configLocation);
            if (resources.length == 0) {
                log.info("# 프레임워크 서비스이용시간 설정 파일이 없습니다. 경로: [{}]", configLocation);
                return;
            }

            if (clearBeforePut) {
                this.cacheManager.clear(CACHE_NAME);
            }

            List<IServiceTimeInfo> merged = new ArrayList<>();
            for (Resource resource : resources) {
                try {
                    merged.addAll(this.parse(resource));
                } catch (Exception ex) {
                    log.warn("서비스이용시간 XML 파싱 실패: {}", resource.getFilename(), ex);
                }
            }

            if (log.isDebugEnabled()) {
            	merged.forEach(s -> log.debug("# 서비스이용시간 정보: {}-{}, 이용시간: ({} ~ {})",
                        s.getGroup(), s.getCode(), s.getStartTime(), s.getEndTime()));
            }

            this.cacheManager.put(CACHE_NAME, CACHE_NAME, merged);
        } catch (Exception e) {
            log.error("서비스이용시간 캐시 적재 중 오류", e);
        }
    }

    /**
     * 서비스 이용시간 xml 파싱처리
     * @param resource 서비스 이용시간 xml 경로 {@link Resource}
     * @return
     * @throws Exception
     */
    private List<IServiceTimeInfo> parse(Resource resource) throws Exception {
        try (InputStream in = resource.getInputStream()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(in);
            Element root = document.getRootElement();

            Element serviceTimeList = root.getChild("service-time-list");
            ServiceTimeGroup group = ServiceTimeGroup.of(
                    StringUtils.defaultIfEmpty(serviceTimeList.getAttributeValue("group"), ""));

            List<Element> serviceTimes = serviceTimeList.getChildren();
            if (CollectionUtils.isEmpty(serviceTimes)) {
                return List.of();
            }

            return serviceTimes.stream()
                    .map(e -> parse(group, e))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 서비스 이용시간 개별 XML 요소를 파싱하여 서비스 이용시간 정보 객체로 변환한다.
     * @param group 서비스 이용시간 그룹 {@link ServiceTimeGroup}
     * @param element Document 요소 {@link Element}
     * @return 서비스 이용시간 개별 XML 요소를 파싱하여 서비스 이용시간 정보 객체
     */
    private IServiceTimeInfo parse(ServiceTimeGroup group, Element element) {
        String code = StringUtils.defaultIfEmpty(element.getAttributeValue("code"), "");
        String checkYn = StringUtils.defaultIfEmpty(element.getAttributeValue("checkYn"), "");
        String description = StringUtils.defaultIfEmpty(element.getAttributeValue("description"), "");
        String nextPage = StringUtils.defaultIfEmpty(element.getAttributeValue("nextPage"), "");
        String nextPageParameter = StringUtils.defaultIfEmpty(element.getAttributeValue("nextPageParameter"), "");
        
        String timeArr[] = StringUtils.defaultIfEmpty(element.getText(), "").split("\\,");
        String startTime = StringUtils.defaultIfEmpty(timeArr[0], "");
        String endTime = StringUtils.defaultIfEmpty(timeArr[1], "");

        return DefaultServiceTimeInfo.builder()
                .group(group)
                .code(code)
                .chkYn(checkYn)
                .description(description)
                .nextPage(nextPage)
                .nextPageParameter(nextPageParameter)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
