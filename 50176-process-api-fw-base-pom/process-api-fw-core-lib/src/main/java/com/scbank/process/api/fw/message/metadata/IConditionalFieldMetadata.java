package com.scbank.process.api.fw.message.metadata;

/**
 * 조건부 필드 처리를 위한 메타데이터 인터페이스입니다.
 * 조건부를 spEL로 작성하여 사용
 */
public interface IConditionalFieldMetadata extends IMessageFieldMetadata {

    String getCondition();
}
