package com.scbank.process.api.fw.message.enums;

/**
 * 메시지 필드 또는 세그먼트에서 구분자(Delimiter)가 위치하는 방식을 정의하는 열거형입니다.
 *
 * 이 값은 고정 길이 메시지나 텍스트 기반 메시지를 파싱할 때, 특정 구간 또는 필드가
 * 구분자로 감싸져 있는 경우 이를 해석하는 데 사용됩니다.
 *
 * <ul>
 * <li>{@link #NONE} - 구분자가 사용되지 않음</li>
 * <li>{@link #PREFIX} - 필드 앞에 구분자가 존재</li>
 * <li>{@link #SUFFIX} - 필드 뒤에 구분자가 존재</li>
 * <li>{@link #WRAPPED} - 필드 앞뒤에 모두 구분자가 존재 (예: 따옴표로 감싸진 문자열)</li>
 * </ul>
 *
 * 예시:
 * 
 * <pre>
 *     "00123"        → NONE
 *     "|00123"       → PREFIX
 *     "00123|"       → SUFFIX
 *     "\"00123\""    → WRAPPED
 * </pre>
 * 
 * 이 enum은 파서 또는 직렬화 도구에서 필드 경계 추출 로직에 활용됩니다.
 * 
 * @author sungdon.choi
 */
public enum DelimiterPosition {

    /** 구분자 없음 */
    NONE,

    /** 필드 앞에 구분자 존재 */
    PREFIX,

    /** 필드 뒤에 구분자 존재 */
    SUFFIX,

    /** 필드 앞뒤에 모두 구분자 존재 */
    WRAPPED
}
