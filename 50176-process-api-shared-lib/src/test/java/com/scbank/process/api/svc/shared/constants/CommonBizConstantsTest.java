package com.scbank.process.api.svc.shared.constants;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class CommonBizConstantsTest {

    @Test
    void constantsValueTest() {

        assertEquals(
                "FIRST999",
                CommonBizConstants.DEFAULT_USER_ID);

        assertEquals(
                "111111",
                CommonBizConstants.DEFAULT_TS_PASS_WORD);

        assertEquals(
                "MAC",
                CommonBizConstants.IPINSIDE_MAC);

        assertEquals(
                "IMEI",
                CommonBizConstants.IPINSIDE_IMEI);

        assertEquals(
                "ANDROID_ID",
                CommonBizConstants.IPINSIDE_ANDROID_ID);

        assertNotNull(
                CommonBizConstants.REAL_PAYCO_PUBLIC_KEY);

        assertNotNull(
                CommonBizConstants.DEV_PAYCO_PUBLIC_KEY);
    }

    @Test
    void constructorCoverage() throws Exception {

        Constructor<CommonBizConstants> constructor =
                CommonBizConstants.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        CommonBizConstants instance =
                constructor.newInstance();

        assertNotNull(instance);
    }
}