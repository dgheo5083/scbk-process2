package com.scbank.process.api.fw.message.metadata;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 전문 메타데이터의 기본 추상 클래스입니다.
 * 
 * <p>
 * {@link IMessageMetadata} 인터페이스를 구현하며,
 * 메시지 ID, 이름, 타입, 필수 여부, 경로, 순서 등의 기본 속성을 제공합니다.
 * </p>
 * 
 * @see IMessageMetadata
 * @since 25. 4. 22.
 * @version 1.0
 *          작성자: sungdon.choi
 */
@Getter
@Setter
@SuperBuilder
public abstract class DefaultMessageMetadata implements IMessageMetadata {

    private static final long serialVersionUID = 1L;

    /** 메시지 템플릿 ID (식별자) */
    private String id;

    /** 메시지 템플릿 이름 (한글명 등) */
    private String name;

    /** 메시지 템플릿 타입 ({@link MessageType}) */
    private MessageType type;

    /** 메시지 필드의 필수 여부 (true = 필수, false = 선택) */
    private boolean isRequired;

    /** 필드 정렬 순서 */
    private int order;

    /**
     * 메시지 필드 경로 (XPath 스타일)
     * 
     * <p>
     * 예: {@code mciHeader/tranCommon/targetSystem}
     * </p>
     */
    private String path;

    /** 필드 데이터 타입 (예: String.class, Integer.class 등) */
    private Class<?> fieldType;

    private String fieldName;

    /**
     * 테스트 example 데이터
     */
    private String example;
}
