package com.scbank.process.api.svc.shared.constants;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PRCSharedEnumsTest {

    @Test
    void connectTypeFromCode() {

        assertEquals(
                PRCSharedEnums.ConnectType.JOINT_CERT,
                PRCSharedEnums.ConnectType.fromCode("1"));

        assertNull(
                PRCSharedEnums.ConnectType.fromCode("999"));
    }

    @Test
    void connectTypeIsEquals() {

        assertTrue(
                PRCSharedEnums.ConnectType.JOINT_CERT
                        .isEquals("1"));

        assertFalse(
                PRCSharedEnums.ConnectType.JOINT_CERT
                        .isEquals("2"));

        assertTrue(
                PRCSharedEnums.ConnectType.MOBILE_OTP
                        .isEquals("g"));
    }

    @Test
    void langTypeFromCode() {

        assertEquals(
                PRCSharedEnums.LangType.KOREAN,
                PRCSharedEnums.LangType.fromCode("ko"));

        assertEquals(
                PRCSharedEnums.LangType.ENGLISH,
                PRCSharedEnums.LangType.fromCode("en"));

        assertNull(
                PRCSharedEnums.LangType.fromCode("jp"));
    }

    @Test
    void osTypeFromValue() {

        assertEquals(
                PRCSharedEnums.OsType.IOS,
                PRCSharedEnums.OsType.fromValue("ios"));

        assertEquals(
                PRCSharedEnums.OsType.ANDROID,
                PRCSharedEnums.OsType.fromValue("android"));

        assertNull(
                PRCSharedEnums.OsType.fromValue("linux"));
    }

    @Test
    void channelTypeFromId() {

        assertEquals(
                PRCSharedEnums.ChannelType.INTERNET,
                PRCSharedEnums.ChannelType.fromId("IB"));

        assertEquals(
                PRCSharedEnums.ChannelType.SMART,
                PRCSharedEnums.ChannelType.fromId("MB"));

        assertEquals(
                PRCSharedEnums.ChannelType.MOBILE,
                PRCSharedEnums.ChannelType.fromId("MW"));

        assertNull(
                PRCSharedEnums.ChannelType.fromId("XX"));
    }

    @Test
    void getterCoverage() {

        assertEquals(
                "1",
                PRCSharedEnums.ConnectType.JOINT_CERT.getCode());

        assertEquals(
                "공동인증서",
                PRCSharedEnums.ConnectType.JOINT_CERT.getDescription());

        assertEquals(
                "ko",
                PRCSharedEnums.LangType.KOREAN.getCode());

        assertEquals(
                "ios",
                PRCSharedEnums.OsType.IOS.getValue());

        assertEquals(
                "IB",
                PRCSharedEnums.ChannelType.INTERNET.getId());

        assertEquals(
                "01",
                PRCSharedEnums.ChannelType.INTERNET.getCode());

        assertEquals(
                "01",
                PRCSharedEnums.UserType.USER_IB_TYPE.getCode());

        assertEquals(
                "15",
                PRCSharedEnums.VanType.KR_IB_VAN_TYPE.getCode());
    }

    @Test
    void enumValuesCoverage() {

        assertEquals(
                7,
                PRCSharedEnums.ConnectType.values().length);

        assertEquals(
                2,
                PRCSharedEnums.LangType.values().length);

        assertEquals(
                4,
                PRCSharedEnums.OsType.values().length);

        assertEquals(
                3,
                PRCSharedEnums.ChannelType.values().length);

        assertEquals(
                5,
                PRCSharedEnums.UserType.values().length);

        assertEquals(
                4,
                PRCSharedEnums.VanType.values().length);
    }
}