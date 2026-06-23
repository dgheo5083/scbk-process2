package com.scbank.process.api.svc.shared.components.sign.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class SignEnums {

    /**
     * 전자서명 타입 정의
     */
    @Getter
    @RequiredArgsConstructor
    @ToString
    public static enum SignType {
        JOINT_CERT("1", "공동인증서"),
        DIGITAL_CERT("9", "디지털인증서"),
        FIN_CERT_PIN("B", "금융인증서_PIN"),
        FIN_CERT_BIO("C", "금융인증서_BIO"),
        FIN_TECH_TOSS("D", "핀테크인증서_TOSS");

        public static List<SignType> getAllVerifyTypes() {
            return List.of(values());
        }

        public static List<SignType> getStrictTypes() {
            return List.of(JOINT_CERT, FIN_CERT_PIN); // TODO: 가장 제한이 강한 경우로 수정 필요
        }

        public static List<SignType> getIBTypes() {
            return List.of(JOINT_CERT, FIN_CERT_PIN);
        }

        private static final Map<String, SignType> CODE_MAP = Arrays.stream(values())
                .collect(Collectors.toMap(SignType::getCode, t -> t));

        public static SignType fromCode(String code) {
            return CODE_MAP.get(code);
        }

        private final String code;
        private final String description;
    }

    /**
     * 전자서명 요청 타입
     */
    @Getter
    @RequiredArgsConstructor
    public static enum SignVerifyType {
        NONE(""),
        VERIFY("V"), // 서명검증
        VERIFY_N_SAVE("VNS"); // 서명검증 후 저장

        private final String code;
    }

    /**
     * 전자서명 업무 타입
     */
    @Getter
    @RequiredArgsConstructor
    public static enum SignActionType {
        // LOGIN("L"), // 로그인
        SIGN("S"); // 전자서명
        // AUTH("A"), // 인증 (추가인증등?)
        // NMCK("C"), // 미사용으로 추정(ID/PW 로그인시 인증서 데이터 검증)
        // NTB_SIGN("NS"); // NTB 전자서명

        private final String code;
    }

}
