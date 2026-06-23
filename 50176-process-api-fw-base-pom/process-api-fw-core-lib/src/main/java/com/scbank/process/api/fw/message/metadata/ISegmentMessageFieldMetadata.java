package com.scbank.process.api.fw.message.metadata;

import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.DelimiterType;

/**
 * 세그먼트(Segment) 필드를 위한 전문 필드 메타데이터 인터페이스입니다.
 * 
 * <p>
 * 세그먼트란 구분자(delimiter)로 감싸거나 구분되는 전문 필드 집합을 의미합니다.
 * 이 인터페이스는 세그먼트 구분 타입, 구분자 위치, 구분자 값 등을 정의합니다.
 * </p>
 * 
 * <p>
 * 일반 필드 메타데이터 ({@link IMessageFieldMetadata})를 확장합니다.
 * </p>
 * 
 * @see IMessageFieldMetadata
 * @see DelimiterType
 * @see DelimiterPosition
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: gasigol
 */
public interface ISegmentMessageFieldMetadata extends IMessageFieldMetadata {

    /**
     * 세그먼트 구분자(delimiter)의 타입을 반환합니다.
     * 
     * @return 구분자 타입 ({@link DelimiterType})
     */
    DelimiterType getDelimiterType();

    /**
     * 세그먼트 구분자 위치를 반환합니다.
     * 
     * <p>
     * 구분자가 세그먼트 앞(PREFIX), 뒤(SUFFIX), 양쪽(WRAPPED) 중 어디에 위치하는지를 나타냅니다.
     * </p>
     *
     * @return 구분자 위치 ({@link DelimiterPosition})
     */
    DelimiterPosition getDelimiterPosition();

    /**
     * 세그먼트를 구분하는 실제 구분자 바이트 배열을 반환합니다.
     *
     * @return 구분자 바이트 배열
     */
    byte[] getDelimiter();
}
