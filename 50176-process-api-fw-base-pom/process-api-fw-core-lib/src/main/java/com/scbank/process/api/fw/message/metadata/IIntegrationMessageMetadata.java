package com.scbank.process.api.fw.message.metadata;

import java.util.List;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

/**
 * 통합 메시지에 대한 메타데이터를 정의하는 인터페이스입니다.
 * 
 * <p>
 * 메시지 ID, 타입, 매핑 대상 클래스, 하위 필드 메타데이터 목록 등을 제공합니다.
 * </p>
 * 
 * <p>
 * 메시지 직렬화/역직렬화 시, 이 메타정보를 기준으로 메시지 구조를 해석합니다.
 * </p>
 * 
 * @see IMessageMetadata
 * @see Type
 * @since 25. 4. 14.
 * @version 1.0
 * 
 *          작성자: sungdon.choi
 */
public interface IIntegrationMessageMetadata {

    /**
     * 통합 메시지 ID를 반환합니다.
     *
     * @return 메시지 ID (식별자)
     */
    String getId();

    /**
     * 통합 메시지 타입을 반환합니다.
     *
     * @return 메시지 타입 ({@link Type})
     */
    Type getType();

    /**
     * 통합 메시지가 매핑되는 대상 클래스를 반환합니다.
     *
     * @return 매핑 대상 클래스
     */
    Class<?> getTargetClass();

    /**
     * 통합 메시지의 하위 필드 메타데이터 목록을 반환합니다.
     *
     * @return 하위 필드 메타데이터 목록
     */
    List<IMessageMetadata> getChildren();

    String getXmlRootName();

    boolean isXmlRootWrap();

    /**
     * EDMI 캡쳐시스템
     * 
     * @return
     */
    String getCaptureSystem();
}
