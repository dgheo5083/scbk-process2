package com.scbank.process.api.fw.integration.request;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * 프레임워크 외부 시스템 연계 요청 인터페이스
 *
 * <p>
 * 외부 시스템 연계 시 사용하는 요청 객체의 공통 구조를 정의 합니다.
 * <ul>
 * <li>헤더 정보와 메시지 바디를 분리하여 전달할 수 있도록 분리된 구조</li>
 * <li>바디는 반드시 {@link IMessageObject}를 구현한 객체여야 합니다</li>
 * <li>프레임워크의 {@code IntegrationManager}에서 이 인터페이스를 기준으로 메시지를 조립/전송합니다</li>
 * </ul>
 *
 * @param <H> 헤더 타입 (IMessageObject 구현체)
 * @param <B> 메시지 바디 타입 (IMessageObject 구현체)
 * @author sungdon.choi
 */
public interface IntegrationRequest<H extends IMessageObject, B extends IMessageObject> {

    /**
     * 요청 전문의 헤더 정보를 반환 합니다.
     * <p>
     * 예: 인터페이스 ID, 거래일시, 사용자 정보 등 메타데이터가 포함된 구조
     * </p>
     *
     * @return 요청 헤더 객체
     */
    H getHeader();

    /**
     * 요청 메시지 바디 객체를 반환 합니다.
     * <p>
     * 업무 데이터를 담고 있는 실제 요청 메시지이며, 반드시 {@link IMessageObject}를 구현해야 합니다.
     * </p>
     *
     * @return 요청 메시지 바디 DTO
     */
    B getRequestMessage();
}
