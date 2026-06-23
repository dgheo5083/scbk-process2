package com.scbank.process.api.fw.integration.simulation;

/**
 * 연계시스템 시뮬레이션 응답 공통부 처리 전략 인터페이스
 * 연계시스템별로 인터페이스를 구현하여 Spring Bean으로 등록하여 사용한다.
 */
public interface IntegrationSimulationHeaderStrategy<H, E> {

    /**
     * 
     * @param systemId
     * @return
     */
    boolean supported(String systemId);

    /**
     * 공통부 처리
     * @param <H>
     * @param response
     * @return
     */
    H getHeader(String response);
    
    /**
     * 에러 메시지부 처리
     * @param response 응답 메시지
     * @return
     */
    E getErrorMsg(String response);
}
