package com.scbank.process.api.svc.shared.constants;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class PRCSharedEnums {

    @Getter
    @RequiredArgsConstructor
    public static enum ConnectType {

        JOINT_CERT("1", "공동인증서"),
        ID_PASSWORD("2", "아이디_비밀번호"),
        DIGITAL_CERT("9", "디지털인증서"),
        FIN_CERT_PIN("B", "금융인증서_PIN"),
        FIN_CERT_BIO("C", "금융인증서_BIO"),
        FIN_TECH_TOSS("D", "핀테크인증서_TOSS"),
        // FIN_CERT_KAKAO("F", "핀테크인증서_KAKAO"),
        MOBILE_OTP("G", "모바일OTP");

        private final String code;
        private final String description;

        public static ConnectType fromCode(String code) {
            return Arrays.stream(ConnectType.values())
                    .filter(c -> c.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        public boolean isEquals(String code) {
            return this.code.equalsIgnoreCase(code);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static enum LangType {
        KOREAN("ko"),
        ENGLISH("en");

        private final String code;

        public static LangType fromCode(String code) {
            return Arrays.stream(LangType.values())
                    .filter(c -> c.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static enum OsType {

        IOS("ios"),
        ANDROID("android"),
        WINDOW("window"),
        MAC("mac");

        private final String value;

        public static OsType fromValue(String value) {
            return Arrays.stream(OsType.values())
                    .filter(c -> c.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static enum ChannelType {

        INTERNET("IB", "01"), // 인터넷뱅킹
        SMART("MB", "02"), // 스마트뱅킹
        MOBILE("MW", "03"); // 모바일웹

        private final String id;
        private final String code;

        public static ChannelType fromId(String id) {
            return Arrays.stream(ChannelType.values())
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    // TODO: 필요여부 체크
    public static enum UserType {
        // IB_MTS(이체 가능 사용자)
        // public static String USER_IB_TYPE = "01";
        // IB_WEB(조회가능 상태)
        // public static String USER_WEB_TYPE = "02";
        // Portal
        // public static String USER_PORTAL_TYPE = "03";
        // IB_MTS(이체 가능 사용자)
        // public static String USER_IB_COMPANY_TYPE = "04";
        // CIB 사용자
        // public static String USER_CIB_TYPE = "05";

        USER_IB_TYPE("01"),
        USER_WEB_TYPE("02"),
        USER_PORTAL_TYPE("03"),
        USER_IB_COMPANY_TYPE("04"),
        USER_CIB_TYPE("05");

        private final String code;
    }

    @Getter
    @RequiredArgsConstructor
    public static enum VanType {

        // KR 인터넷뱅킹 VAN_TYPE
        //KR_IB_VAN_TYPE("58"),
        KR_IB_VAN_TYPE("15"),
        // EN 인터넷뱅킹 VAN_TYPE
        //EN_IB_VAN_TYPE("58"),
        EN_IB_VAN_TYPE("14"),

        // KR 스마트뱅킹 VAN_TYPE
        //KR_SB_VAN_TYPE("57"), // ASIS: KR_SMB_VAN_TYPE
        KR_SB_VAN_TYPE("47"), // ASIS: KR_SMB_VAN_TYPE
        // EN 스마트뱅킹 VAN_TYPE
        //EN_SB_VAN_TYPE("57"); // ASIS : EN_SMB_VAN_TYPE
        EN_SB_VAN_TYPE("47"); // ASIS : EN_SMB_VAN_TYPE

        private final String code;
    }

}