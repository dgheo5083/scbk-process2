package com.scbank.process.api.fw.message.metadata;

import com.scbank.process.api.fw.message.enums.RepeatType;

/**
 * 반복 필드 (List&lt;T&gt;)에 대한 메타데이터 인터페이스입니다.
 * 
 * <p>
 * 구현체는 {@link DefaultMessageFieldMetadata} 또는 별도 확장체입니다.
 * </p>
 * 
 * @author Min-jun
 */
public interface IRepeatFieldMetadata extends IMessageFieldMetadata {

    /**
     * 반복 타입 (NONE, FIXED, REFERENCE)
     * 
     * @return 반복 타입
     */
    RepeatType getRepeatType();

    /**
     * 반복 횟수 또는 참조 필드명 (예: "3", "itemCount")
     * 
     * @return 반복 카운트 또는 참조명
     */
    String getRepeatCount();

    /**
     * XML 구조에서 반복부를 감싸는 래퍼 요소명
     * 
     * @return 래퍼 이름
     */
    String getWrapperName();

    /**
     * 반복 항목의 요소명 (XML/JSON 등에서 사용)
     * 
     * @return 요소 이름
     */
    String getElementName();

    /**
     * 구분자 방식에서 항목 간 구분자
     * 
     * @return 구분자 문자열 (예: ";")
     */
    String getDelimiter();
}
