package com.scbank.process.api.fw.common.holiday.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.holiday.IHolidayInfo;
import com.scbank.process.api.fw.core.cache.ICacheManager;

/**
 * {@link DefaultHolidayManager} 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class DefaultHolidayManagerTest {

    @Mock
    private ICacheManager cacheManager;

    private DefaultHolidayManager managerWith(String location) {
        return new DefaultHolidayManager(cacheManager, new Config(true, location));
    }

    private IHolidayInfo holiday(String date) {
        return DefaultHolidayInfo.builder().date(date).description("d").build();
    }

    @Test
    @DisplayName("init 은 XML 을 파싱하여 빈 날짜를 제외한 휴일 목록을 캐시에 적재한다")
    @SuppressWarnings("unchecked")
    void initLoadsHolidaysIntoCache() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");

        manager.init();

        ArgumentCaptor<List<IHolidayInfo>> captor = ArgumentCaptor.forClass(List.class);
        verify(cacheManager).clear("csl-holiday");
        verify(cacheManager).put(eq("csl-holiday"), eq("csl-holiday"), captor.capture());
        assertThat(captor.getValue()).hasSize(2); // 빈 날짜 항목은 필터링됨
    }

    @Test
    @DisplayName("getHolidayList 는 캐시 목록을 반환하며 비어 있으면 빈 목록")
    void getHolidayList() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");
        when(cacheManager.get("csl-holiday", "csl-holiday", List.class))
                .thenReturn(List.of(holiday("20240101")));

        assertThat(manager.getHolidayList()).hasSize(1);
    }

    @Test
    @DisplayName("getHolidayList 는 캐시가 비어 있으면 빈 목록")
    void getHolidayListEmpty() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");
        when(cacheManager.get("csl-holiday", "csl-holiday", List.class)).thenReturn(null);

        assertThat(manager.getHolidayList()).isEmpty();
    }

    @Test
    @DisplayName("getHoliday: 날짜가 비어 있으면 null")
    void getHolidayBlankDate() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");

        assertThat(manager.getHoliday("")).isNull();
    }

    @Test
    @DisplayName("getHoliday: 일치하는 휴일을 반환, 없으면 null")
    void getHolidayMatch() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");
        when(cacheManager.get("csl-holiday", "csl-holiday", List.class))
                .thenReturn(List.of(holiday("20240101")));

        assertThat(manager.getHoliday("20240101")).isNotNull();
        assertThat(manager.getHoliday("20991231")).isNull();
    }

    @Test
    @DisplayName("isHoliday: null 은 false")
    void isHolidayNull() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");

        assertThat(manager.isHoliday(null)).isFalse();
    }

    @Test
    @DisplayName("isHoliday: 주말이면 캐시 조회 없이 true")
    void isHolidayWeekend() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");

        // 2024-01-06 은 토요일
        assertThat(manager.isHoliday("20240106")).isTrue();
    }

    @Test
    @DisplayName("isHoliday: 평일이며 등록되지 않은 날짜는 false, 등록된 날짜는 true")
    void isHolidayWeekday() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");
        // 2024-01-08 은 월요일
        lenient().when(cacheManager.get("csl-holiday", "csl-holiday", List.class))
                .thenReturn(List.of(holiday("20240108")));

        assertThat(manager.isHoliday("20240108")).isTrue();
        assertThat(manager.isHoliday("20240109")).isFalse();
    }

    @Test
    @DisplayName("reload 는 캐시를 재적재한다")
    void reloadReloadsCache() {
        DefaultHolidayManager manager = managerWith("classpath:common-test/holiday.xml");

        manager.reload();

        verify(cacheManager).clear("csl-holiday");
        verify(cacheManager).put(eq("csl-holiday"), eq("csl-holiday"), org.mockito.ArgumentMatchers.any());
    }
}
