package com.scbank.process.api.fw.message.metadata;

import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.DelimiterType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 세그먼트(Segment) 필드 메타데이터의 기본 구현체입니다.
 * 
 * <p>
 * {@link DefaultMessageFieldMetadata}를 확장하여,
 * 세그먼트 구분 타입, 구분자 위치, 구분자 값(byte[])을 추가로 제공합니다.
 * </p>
 * 
 * <p>
 * {@link ISegmentMessageFieldMetadata} 인터페이스를 구현합니다.
 * </p>
 * 
 * @see ISegmentMessageFieldMetadata
 * @see DelimiterType
 * @see DelimiterPosition
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: gasigol
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DefaultSegmentMessageFieldMetadata
        extends DefaultMessageFieldMetadata
        implements ISegmentMessageFieldMetadata {

    private static final long serialVersionUID = 1L;

    /** 세그먼트 구분자 타입 (예: 고정 구분자, 가변 구분자 등) */
    private DelimiterType delimiterType;

    /** 구분자의 위치 (PREFIX, SUFFIX, WRAPPED) */
    private DelimiterPosition delimiterPosition;

    /** 세그먼트를 구분하는 실제 구분자 바이트 배열 */
    private byte[] delimiter;
}
