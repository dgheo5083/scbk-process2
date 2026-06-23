package com.scbank.process.api.fw.base.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Generated unit test for {@link ThreadLocalStore}.
 */
class ThreadLocalStoreTest {

    private final ThreadLocalStore store = ThreadLocalStore.getInstance();

    @AfterEach
    void tearDown() {
        store.clearThreadLocalStore();
    }

    @Test
    void getInstanceReturnsSingleton() {
        assertThat(ThreadLocalStore.getInstance()).isSameAs(store);
    }

    @Test
    void setValueIgnoresNull() {
        store.setValue("k", null);
        assertThat(store.getValue("k")).isNull();

        store.setValue("k", "v");
        assertThat(store.getValue("k")).isEqualTo("v");
    }

    @Test
    void setAndGetMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "b");
        store.setMap(map);
        assertThat(store.getMap()).containsEntry("a", "b");
    }

    @Test
    void clearValueRemovesEntry() {
        store.setValue("k", "v");
        store.clearValue("k");
        assertThat(store.getValue("k")).isNull();
    }

    @Test
    void stringAccessorsRoundTrip() {
        store.setCSLInternalAccessTokenString("iat");
        store.setTrackingId("trk");
        store.setJSessionId("sid");
        store.setLanguageHeader("ko");
        store.setChannelId("MB");
        store.setDeviceId("dev");
        store.setAppVersion("1.0");
        store.setOsType("AOS");
        store.setOsVersion("14");
        store.setScreenId("scr");
        store.setMenuId("menu");
        store.setIpinsideIp("ip");
        store.setIpinsideAx("ax");
        store.setIpinsideMac("mac");
        store.setIpinsideHdd("hdd");
        store.setDeviceUUID("uuid");
        store.setSimSerial("sim");
        store.setAcType("ac");
        store.setForceCheckCode("fcc");

        assertThat(store.getCSLInternalAccessTokenString()).isEqualTo("iat");
        assertThat(store.getTrackingId()).isEqualTo("trk");
        assertThat(store.getJSessionId()).isEqualTo("sid");
        assertThat(store.getLanguageHeader()).isEqualTo("ko");
        assertThat(store.getChannelId()).isEqualTo("MB");
        assertThat(store.getDeviceId()).isEqualTo("dev");
        assertThat(store.getAppVersion()).isEqualTo("1.0");
        assertThat(store.getOsType()).isEqualTo("AOS");
        assertThat(store.getOsVersion()).isEqualTo("14");
        assertThat(store.getScreenId()).isEqualTo("scr");
        assertThat(store.getMenuId()).isEqualTo("menu");
        assertThat(store.getIpinsideIp()).isEqualTo("ip");
        assertThat(store.getIpinsideAx()).isEqualTo("ax");
        assertThat(store.getIpinsideMac()).isEqualTo("mac");
        assertThat(store.getIpinsideHdd()).isEqualTo("hdd");
        assertThat(store.getDeviceUUID()).isEqualTo("uuid");
        assertThat(store.getSimSerial()).isEqualTo("sim");
        assertThat(store.getAcType()).isEqualTo("ac");
        assertThat(store.getForceCheckCode()).isEqualTo("fcc");
    }

    @Test
    void decodedJwtAccessorRoundTrips() {
        DecodedJWT jwt = mock(DecodedJWT.class);
        store.setCSLInternalAccessToken(jwt);
        assertThat(store.getCSLInternalAccessToken()).isSameAs(jwt);
    }
}
