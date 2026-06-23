package com.scbank.process.api.fw.message.enums;

/**
 * 가변길이필드 메타데이터 길이 참조 타입 열겨형 상수
 *
 * @author sungdon.choi
 */
public enum LengthType {
    BASIC, FIXED, REFERENCE, NONE;

    /**
     * 참조 타입 문자열과 매칭되는 길이 참조 타입 열겨형 상수를 가져온다.
     *
     * @param typeString 길이 참조타입 문자열
     * @return 길이 참조 타입 {@link LengthType}
     */
    public static LengthType of(String typeString) {
        if ("basic".equalsIgnoreCase(typeString)) {
            return LengthType.BASIC;
        } else if ("fixed".equalsIgnoreCase(typeString)) {
            return FIXED;
        } else if ("reference".equalsIgnoreCase(typeString)) {
            return REFERENCE;
        }
        return LengthType.NONE;
    }
}
