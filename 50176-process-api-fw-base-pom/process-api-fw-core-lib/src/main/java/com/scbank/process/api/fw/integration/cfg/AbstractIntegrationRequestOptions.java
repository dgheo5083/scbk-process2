package com.scbank.process.api.fw.integration.cfg;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 프레임워크 연계 시스템 별 사용자 커스텀 설정 추상 클래스
 *
 * @author sungdon.choi
 */
@Getter
@Setter
public class AbstractIntegrationRequestOptions implements IntegrationRequestOptions {

    /**
     * 연계 시스템 인터페이스 ID
     */
    protected String interfaceId;

    /**
     * 캡쳐 시스템
     */
    protected String captureSystem;
    
    /**
     * 
     */
    protected String typeName;
    
    /**
     * 
     */
    protected String messageSenderBody;
    
    /**
     * 
     */
    protected String senderDomainBody;

    /**
     * 연결 타임아웃
     */
    protected long connectTimeout;

    /**
     * 수신 타임아웃
     */
    protected long readTimeout;

    /**
     * 전문 오류 응답 수신 시 예외 처리 여부
     */
    protected boolean exceptionOnError;

    /**
     * 거래별 대응답 처리여부
     */
    protected boolean simulationMode;
    
    /**
     * 거래 추적ID
     */
    protected String trackingId;

    /**
     * 전문 송/수신 시 사용 하는 속성
     */
    protected Map<String, Object> attributes;

    /**
     * <pre>
     * 문자열 형태로 속성 값을 가져온다.
     * 값이 존재하지 않으면 빈 문자열 반환
     * </pre>
     *
     * @param attrName 속성 키
     * @return 속성 값의 문자열 표현 또는 빈 문자열
     */
    public String getAttribute(String attrName) {
        Object value = attributes.get(attrName);
        return value != null ? value.toString() : "";
    }

    /**
     * <pre>
     * 지정된 타입으로 속성 값을 가져온다.
     * 값이 없으면 null 반환 타입이 일치하지 않으면 ClassCastException 발생
     * </pre>
     *
     * @param attrName     속성 키
     * @param requiredType 기대되는 타입
     * @return 캐스팅된 속성 값 또는 null
     * @throws ClassCastException 타입이 일치하지 않을 경우
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String attrName, Class<T> requiredType) {
        Object value = attributes.get(attrName);
        if (value == null) {
            return null;
        }
        if (!requiredType.isInstance(value)) {
            throw new ClassCastException("Attribute [" + attrName + "] is not of type " + requiredType.getName());
        }
        return (T) value;
    }

    /**
     * 
     * @param attrName
     * @param value
     */
    public void setAttribute(String attrName, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(attrName, value);
    }
}
