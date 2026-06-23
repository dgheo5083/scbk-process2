package com.scbank.process.api.fw.common.holiday.impl;

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
import com.scbank.process.api.fw.common.holiday.IHolidayInfo;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.cache.ICacheManager;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.concurrent.lock.ReadWriteLockSupport;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 공통 휴일 관리 매니저 구현 클래스
 */
@Slf4j
@RequiredArgsConstructor
@CommonComponent(name = "프레임워크/공통 휴일관리 매니저 컴포넌트", description = "", author = "sungdon.choi")
public class DefaultHolidayManager extends ReadWriteLockSupport implements IHolidayManager {

    static final String CACHE_NAME = "csl-holiday";

    /**
     * 프레임워크 캐시 매니저
     */
    private final ICacheManager cacheManager;

    /**
     * 휴일정보 관리 XML 경로
     */
    private final Config config;
    
    @Override
    @ComponentOperation(name = "휴일정보 목록 획득 메소드", description = "전체휴일정보 목록을 가져온다.", author = "sungdon.choi")
	public List<IHolidayInfo> getHolidayList() {
    	List<IHolidayInfo> holidayInfoList = this.getHolidayCache();
        if (CollectionUtils.isEmpty(holidayInfoList)) {
            return Collections.emptyList();
        }
        return holidayInfoList;
	}

    @Override
    @ComponentOperation(name = "휴일정보 획득 메소드", description = "yyyyMMdd 형식의 날짜 문자열이 휴일로 등록되었는지 확인 후, 휴일정보를 가져온다.", author = "sungdon.choi")
    public IHolidayInfo getHoliday(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        List<IHolidayInfo> holidayInfoList = this.getHolidayCache();
        if (CollectionUtils.isEmpty(holidayInfoList)) {
            return null;
        }

        return holidayInfoList.stream()
                .filter(h -> date.equals(h.getDate()))
                .findFirst()
                .orElse(null);
    }

    @Override
    @ComponentOperation(name = "휴일여부 메소드", description = "yyyyMMdd 형식의 날짜 문자열이 휴일로 등록되었는지 확인한다.", author = "sungdon.choi")
    public boolean isHoliday(String date) {
    	if (date == null) {
    		return false;
    	}
    	//토요일/일요일여부 체크 추가
        return DateUtils.isHoliday(date) || getHoliday(date) != null;
    }

    @Override
    public void init() {
        this.withWrite(() -> loadHolidayCache());
    }

    @Override
    public void reload() {
        this.withWrite(() -> {
            log.info("# 휴일 캐시 리로드 수행");
            loadHolidayCache();
        });
    }

    @SuppressWarnings("unchecked")
    private List<IHolidayInfo> getHolidayCache() {
        return this.withRead(() -> cacheManager.get(CACHE_NAME, CACHE_NAME, List.class));
    }

    private void loadHolidayCache() {
        try {
            String configLocation = config.configLocation();
            if (log.isInfoEnabled()) {
                log.info("# 프레임워크 휴일정보 목록 로드, 경로: [{}]", configLocation);
            }

            if (StringUtils.isEmpty(configLocation)) {
                log.info("# 프레임워크 휴일정보 목록 로드 실패, 설정 파일 경로 미설정");
                return;
            }

            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(configLocation);
            if (resources.length == 0) {
                log.info("# 프레임워크 휴일정보 설정 파일이 없습니다. 경로: [{}]", configLocation);
                return;
            }

            List<IHolidayInfo> merged = new ArrayList<>();
            for (Resource resource : resources) {
                merged.addAll(this.parse(resource));
            }

            this.cacheManager.clear(CACHE_NAME);
            this.cacheManager.put(CACHE_NAME, CACHE_NAME, merged);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<IHolidayInfo> parse(Resource resource) throws Exception {
        try (InputStream in = resource.getInputStream()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(in);
            Element root = document.getRootElement();

            boolean use = "Y".equals(StringUtils.defaultIfEmpty(root.getAttributeValue("useYn"), "N"));
            if (!use) {
                log.info("# 프레임워크 휴일정보 관리 미사용 처리 경로: [{}]", resource.getURI());
                return List.of();
            }

            List<Element> holidayList = root.getChildren("holiday");

            return holidayList.stream()
                    .map(h -> DefaultHolidayInfo.builder()
                            .date(StringUtils.defaultIfEmpty(h.getAttributeValue("date"), ""))
                            .description(StringUtils.defaultIfEmpty(h.getAttributeValue("description"), ""))
                            .build())
                    .filter(h -> StringUtils.isNotEmpty(h.getDate()))
                    .collect(Collectors.toList());
        }
    }
}
