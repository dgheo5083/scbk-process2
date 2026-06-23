package com.scbank.process.api.fw.base.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Generated unit test for {@link ThreadLocalStoreDelegator}.
 */
class ThreadLocalStoreDelegatorTest {

    private final ThreadLocalStore store = ThreadLocalStore.getInstance();

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown() {
        store.clearThreadLocalStore();
    }

    @Test
    void delegatesToThreadLocalStore() {
        assertThat(new ThreadLocalStoreDelegator()).isNotNull();
        assertThat(ThreadLocalStoreDelegator.threadLocalStore()).isSameAs(store);

        assertThat(ThreadLocalStoreDelegator.getCSLInternalAccessTokenString()).isEqualTo("iat");
        assertThat(ThreadLocalStoreDelegator.getTrackingId()).isEqualTo("trk");
        assertThat(ThreadLocalStoreDelegator.getJSessionId()).isEqualTo("sid");
        assertThat(ThreadLocalStoreDelegator.getLanguageHeader()).isEqualTo("ko");
        assertThat(ThreadLocalStoreDelegator.getChannelId()).isEqualTo("MB");
        assertThat(ThreadLocalStoreDelegator.getDeviceId()).isEqualTo("dev");
        assertThat(ThreadLocalStoreDelegator.getAppVersion()).isEqualTo("1.0");
        assertThat(ThreadLocalStoreDelegator.getOsType()).isEqualTo("AOS");
        assertThat(ThreadLocalStoreDelegator.getOsVersion()).isEqualTo("14");
        assertThat(ThreadLocalStoreDelegator.getScreenId()).isEqualTo("scr");
        assertThat(ThreadLocalStoreDelegator.getMenuId()).isEqualTo("menu");
        assertThat(ThreadLocalStoreDelegator.getIpinsideIp()).isEqualTo("ip");
        assertThat(ThreadLocalStoreDelegator.getIpinsideAx()).isEqualTo("ax");
        assertThat(ThreadLocalStoreDelegator.getIpinsideMac()).isEqualTo("mac");
        assertThat(ThreadLocalStoreDelegator.getIpinsideHdd()).isEqualTo("hdd");
        assertThat(ThreadLocalStoreDelegator.getDeviceUUID()).isEqualTo("uuid");
        assertThat(ThreadLocalStoreDelegator.getSimSerial()).isEqualTo("sim");
        assertThat(ThreadLocalStoreDelegator.getAcType()).isEqualTo("ac");
        assertThat(ThreadLocalStoreDelegator.getForceCheckCode()).isEqualTo("fcc");
    }

    @Test
    void delegatesAccessToken() {
        DecodedJWT jwt = mock(DecodedJWT.class);
        store.setCSLInternalAccessToken(jwt);
        assertThat(ThreadLocalStoreDelegator.getCSLInternalAccessToken()).isSameAs(jwt);
    }
}
