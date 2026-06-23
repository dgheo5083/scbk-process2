package com.scbank.process.api.fw.message.metadata;

import com.scbank.process.api.fw.message.enums.VariableFieldType;

/**
 * 가변 길이(Variable Length) 필드를 위한 전문 필드 메타데이터 인터페이스입니다.
 * 
 * <p>
 * 이 인터페이스는 필드 길이 표시 방식, 참조 필드, 구분자(delimiter), 길이 접두부(prefix) 크기 등
 * 가변 길이 필드를 처리하는 데 필요한 메타정보를 정의합니다.
 * </p>
 * 
 * <p>
 * 기본 필드 메타데이터({@link IMessageFieldMetadata})를 확장하여 가변 길이 특성을 추가합니다.
 * </p>
 * 
 * @see IMessageFieldMetadata
 * @see VariableFieldType
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: sungdon.choi
 */
public interface IVariableLengthMessageFieldMetadata extends IMessageFieldMetadata {

    /**
     * 가변 길이 필드 타입을 반환합니다.
     * 
     * @return 가변 길이 필드 타입 ({@link VariableFieldType})
     */
    VariableFieldType getVariableFieldType();

    /**
     * 길이를 참조할 외부 필드명을 반환합니다.
     * 
     * <p>
     * 참조 필드를 통해 실제 데이터 길이를 결정할 수 있습니다.
     * </p>
     *
     * @return 참조 필드명 (없을 경우 빈 문자열)
     */
    String getReferenceField();

    /**
     * 필드를 구분하는 구분자(delimiter) 값을 반환합니다.
     * 
     * @return 구분자 바이트 값
     */
    byte getDelimiter();

    /**
     * 길이 정보를 표현하는 접두부(prefix)의 크기를 반환합니다.
     * 
     * <p>
     * 예: 2 bytes 길이 접두부 → 최대 99 길이 지원
     * </p>
     *
     * @return 길이 접두부 크기 (byte 단위)
     */
    int getLengthPrefixSize();

    /**
     * 고정 길이를 가지는 경우 해당 길이 값을 반환합니다.
     * 
     * @return 고정 길이 (고정형이 아닌 경우 0)
     */
    int getFixedLength();

    /**
     * 가변 길이 필드의 인코딩 타입을 반환합니다.
     *
     * @return 인코딩 설정 문자열 (예: "UTF-8")
     */
    String getEncoding();
}
