package com.scbank.process.api.fw.common.servicetime.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;
import com.scbank.process.api.fw.core.cache.ICacheManager;

/**
 * {@link DefaultServiceTimeManager} 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class DefaultServiceTimeManagerTest {

    @Mock
    private ICacheManager cacheManager;

    private DefaultServiceTimeManager managerWith(String location) {
        return new DefaultServiceTimeManager(cacheManager, new Config(true, location));
    }

    private IServiceTimeInfo info(ServiceTimeGroup group, String code) {
        return DefaultServiceTimeInfo.builder().group(group).code(code)
                .startTime("0900").endTime("1800").build();
    }

    @Test
    @DisplayName("init 은 XML 을 파싱하여 서비스 이용시간 목록을 캐시에 적재한다")
    @SuppressWarnings("unchecked")
    void initLoadsIntoCache() {
        DefaultServiceTimeManager manager = managerWith("classpath:common-test/servicetime.xml");

        manager.init();

        ArgumentCaptor<List<IServiceTimeInfo>> captor = ArgumentCaptor.forClass(List.class);
        verify(cacheManager).put(eq("csl-servicetime"), eq("csl-servicetime"), captor.capture());
        List<IServiceTimeInfo> loaded = captor.getValue();
        assertThat(loaded).hasSize(2);
        assertThat(loaded.get(0).getGroup()).isEqualTo(ServiceTimeGroup.MENU);
        assertThat(loaded.get(0).getCode()).isEqualTo("M001");
        assertThat(loaded.get(0).getStartTime()).isEqualTo("0900");
        assertThat(loaded.get(0).getEndTime()).isEqualTo("1800");
    }

    @Test
    @DisplayName("getServiceTimeList 는 그룹으로 필터링한 목록을 반환")
    void getServiceTimeListFiltersByGroup() {
        DefaultServiceTimeManager manager = managerWith("classpath:common-test/servicetime.xml");
        when(cacheManager.get("csl-servicetime", "csl-servicetime", List.class)).thenReturn(List.of(
                info(ServiceTimeGroup.MENU, "M001"),
                info(ServiceTimeGroup.MESSAGE, "MSG1")));

        assertThat(manager.getServiceTimeList(ServiceTimeGroup.MENU)).hasSize(1);
        assertThat(manager.getServiceTimeList(ServiceTimeGroup.MESSAGE)).hasSize(1);
    }

    @Test
    @DisplayName("getServiceTimeList 는 캐시가 비어 있으면 빈 목록")
    void getServiceTimeListEmpty() {
        DefaultServiceTimeManager manager = managerWith("classpath:common-test/servicetime.xml");
        when(cacheManager.get("csl-servicetime", "csl-servicetime", List.class)).thenReturn(null);

        assertThat(manager.getServiceTimeList(ServiceTimeGroup.MENU)).isEmpty();
    }

    @Test
    @DisplayName("getServiceTime 은 그룹+코드로 단건을 조회, 없으면 null")
    void getServiceTimeByCode() {
        DefaultServiceTimeManager manager = managerWith("classpath:common-test/servicetime.xml");
        when(cacheManager.get("csl-servicetime", "csl-servicetime", List.class)).thenReturn(List.of(
                info(ServiceTimeGroup.MENU, "M001")));

        assertThat(manager.getServiceTime(ServiceTimeGroup.MENU, "M001")).isNotNull();
        assertThat(manager.getServiceTime(ServiceTimeGroup.MENU, "UNKNOWN")).isNull();
    }

    @Test
    @DisplayName("reload 는 캐시를 비우고 재적재한다")
    void reloadClearsAndReloads() {
        DefaultServiceTimeManager manager = managerWith("classpath:common-test/servicetime.xml");

        manager.reload();

        verify(cacheManager).clear("csl-servicetime");
        verify(cacheManager).put(eq("csl-servicetime"), eq("csl-servicetime"), any());
    }
}
