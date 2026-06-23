package com.scbank.process.api.fw.integration.connector;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.exception.IntegrationException;

/**
 * 프레임워크 연계 시스템 클라이언트 인터페이스
 *
 * <p>연계 전문을 외부 시스템에 전송하고 응답을 수신하는 역할을 담당합니다.
 *
 * <p>프로토콜에 따라 TCP, HTTP, gRPC 등 다양한 방식으로 구현체를 구성할 수 있으며,
 * 본 인터페이스를 통해 연동 방식과 무관하게 공통된 호출 방식(send)을 제공합니다.
 *
 * @param <RS> 응답 타입 (Response)
 * @param <RQ> 요청 타입 (Request)
 * @author sungdon.choi
 */
public interface IntegrationConnector<RS, RQ> {

    /**
     * 연계 시스템과 전문 송/수신을 처리합니다.
     *
     * @param context 연계 시스템 송수신 컨텍스트
     * @param request RQ 타입의 요청 객체 (ex. ByteBuffWrap, String 등)
     * @return RS 타입의 응답 객체 (ex. ByteBuffWrap, String 등)
     * @throws IntegrationException 연계 통신 중 발생하는 예외
     * @see co.kr.scbank.framework.integration.exception.IntegrationSystemException
     * @see co.kr.scbank.framework.integration.exception.IntegrationTimeoutException
     */
    RS send(IntegrationContext context, RQ request) throws IntegrationException;
}
