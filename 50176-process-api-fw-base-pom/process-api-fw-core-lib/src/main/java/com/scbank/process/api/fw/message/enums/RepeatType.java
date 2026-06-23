package com.scbank.process.api.fw.message.enums;

/**
 * 반복 필드(리스트)의 반복 방식을 정의하는 열거형입니다.
 *
 * 메시지 필드 구조 중, 반복 가능한 필드가 존재할 경우,
 * 반복 개수를 결정하는 방식에 따라 다음 세 가지 방식으로 구분됩니다:
 *
 * <ul>
 * <li>{@link #REFERENCE} - 다른 필드의 값을 참조하여 반복 횟수 결정</li>
 * <li>{@link #FIXED} - 반복 횟수가 고정되어 있음</li>
 * <li>{@link #NONE} - 반복되지 않음 (단일 필드)</li>
 * </ul>
 *
 * 예시:
 * 
 * <pre>
 *     &lt;repeatedField type="REFERENCE" count-ref="countField"/&gt;
 *     &lt;repeatedField type="FIXED" count="5"/&gt;
 * </pre>
 *
 * @author sungdon.choi
 */
public enum RepeatType {

    /** 다른 필드 값을 참조하여 반복 횟수 결정 */
    REFERENCE,

    /** 고정된 반복 횟수 */
    FIXED,

    /** 반복되지 않음 */
    NONE;

    /**
     * 문자열로부터 {@link RepeatType} 값을 파싱합니다.
     *
     * @param typeString 반복 타입 문자열 ("reference", "fixed")
     * @return {@link RepeatType} enum 값
     */
    public static RepeatType of(String typeString) {
        if ("reference".equalsIgnoreCase(typeString)) {
            return RepeatType.REFERENCE;
        } else if ("fixed".equalsIgnoreCase(typeString)) {
            return RepeatType.FIXED;
        }
        return RepeatType.NONE;
    }
}
