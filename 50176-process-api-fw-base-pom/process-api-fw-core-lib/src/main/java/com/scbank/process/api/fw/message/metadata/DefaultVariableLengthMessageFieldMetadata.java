package com.scbank.process.api.fw.message.metadata;

import com.scbank.process.api.fw.message.enums.VariableFieldType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 가변 길이(Variable Length) 필드 메타데이터의 기본 구현체입니다.
 * 
 * <p>
 * {@link DefaultMessageFieldMetadata}를 확장하여,
 * 가변 길이 필드에 특화된 속성(길이 타입, 참조 필드, 구분자, 인코딩, 접두부 크기 등)을 추가로 제공합니다.
 * </p>
 * 
 * <p>
 * {@link IVariableLengthMessageFieldMetadata} 인터페이스를 구현합니다.
 * </p>
 * 
 * @see IVariableLengthMessageFieldMetadata
 * @see VariableFieldType
 * @since 25. 4. 22.
 * @version 1.0
 * @author sungdon.choi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DefaultVariableLengthMessageFieldMetadata
        extends DefaultMessageFieldMetadata
        implements IVariableLengthMessageFieldMetadata {

    private static final long serialVersionUID = 1L;

    /** 가변 길이 필드 타입 (길이 기반, 구분자 기반 등) */
    private VariableFieldType variableFieldType;

    /** 길이를 참조하는 외부 필드명 */
    private String referenceField;

    /** 필드 종료를 나타내는 구분자(delimiter) */
    private byte delimiter;

    /** 필드 인코딩 타입 (예: "UTF-8") */
    private String encoding;

    /** 길이 정보를 나타내는 접두부(prefix) 크기 (byte 단위) */
    private int lengthPrefixSize;

    /** 고정된 필드 길이 (고정형일 경우에만 설정) */
    private int fixedLength;
}
