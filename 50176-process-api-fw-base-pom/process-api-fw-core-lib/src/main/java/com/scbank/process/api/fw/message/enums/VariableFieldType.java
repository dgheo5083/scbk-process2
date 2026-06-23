package com.scbank.process.api.fw.message.enums;

/**
 * 가변 길이 필드 처리 방식을 정의하는 열거형입니다.
 *
 * 메시지 필드의 길이가 고정되지 않고 동적으로 결정될 경우,
 * 그 길이를 추론하는 방법에 따라 다음과 같은 유형으로 구분됩니다:
 *
 * <ul>
 * <li>{@link #LENGTH_PREFIX} - 앞에 길이를 나타내는 숫자(prefix)를 포함함</li>
 * <li>{@link #FIELD_REFERENCE} - 다른 필드의 값을 참조하여 길이 결정</li>
 * <li>{@link #DELIMITER} - 구분자(delimiter)를 기준으로 필드의 끝을 판단</li>
 * <li>{@link #FIXED} - 고정 길이 필드 (가변이 아님)</li>
 * </ul>
 *
 * 이 Enum은 고정 길이 메시지 파싱 또는 조립 시, 각 필드의 길이 추론 전략을 구분하는 데 사용됩니다.
 *
 * 예시:
 * 
 * <pre>
 *     &lt;field name="data" type="string" lengthType="LENGTH_PREFIX"/&gt;
 *     &lt;field name="list" type="list" lengthType="FIELD_REFERENCE" lengthRef="count"/&gt;
 * </pre>
 *
 * @author sungdon.choi
 */
public enum VariableFieldType {

    /**
     * 필드 앞에 길이(prefix)가 포함되어 있으며,
     * 해당 prefix 값을 기반으로 실제 데이터 길이를 결정
     */
    LENGTH_PREFIX,

    /**
     * 동일 메시지 내 다른 필드의 값을 참조하여 해당 필드의 길이를 결정
     */
    FIELD_REFERENCE,

    /**
     * 구분자(예: 세미콜론, 개행 등)를 기준으로 필드 경계를 판단
     */
    DELIMITER,

    /**
     * 고정 길이 필드 (가변이 아님)
     */
    FIXED
}