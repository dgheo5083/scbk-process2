package com.scbank.process.api.fw.integration.cfg;

import java.util.Map;

/**
 * 프레임워크 연계 시스템 별 사용자 커스텀 설정 인터페이스
 *
 * @author sungdon.choi
 */
public interface IntegrationRequestOptions {

    /**
     * 연계 시스템 인터페이스 ID를 가져온다.
     *
     * @return 연계 시스템 인터페이스 ID
     */
    String getInterfaceId();

    /**
     * 캡쳐 시스템을 가져온다.
     *
     * @return 캡쳐 시스템
     */
    String getCaptureSystem();
    
    /**
     * 
     * @return
     */
    String getTypeName();
    
    /**
     * 
     * @return
     */
    String getMessageSenderBody();
    
    /*
     * 
     */
    String getSenderDomainBody();

    /**
     * 연결 타임아웃을 가져온다.
     *
     * @return 연결 타임아웃
     */
    long getConnectTimeout();

    /**
     * 수신 타임아웃을 가져온다.
     *
     * @return 수신 타임아웃
     */
    long getReadTimeout();

    /**
     * 전문 오류 응답 수신 시 예외 처리 여부를 가져온다.
     *
     * @return 전문 오류 응답 수신 시 예외 처리 여부
     */
    boolean isExceptionOnError();

    /**
     * 거래별 대응답 처리여부
     * 
     * @return 거래별 대응답 처리여부
     */
    boolean isSimulationMode();

    /**
     * 전문 송/수신 시 사용 속성
     *
     * @return 전문 송/수신 시 사용 속성
     */
    Map<String, Object> getAttributes();
}
