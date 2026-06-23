package com.scbank.process.api.fw.integration.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * 외부 시스템 연계 시 사용되는 공통 컨텍스트 객체.
 * 각 통신 요청 마다 생성되어 시스템, 인터페이스, 요청 정보, 로케일 및 추가 속성(attributes)을 포함한다.
 * </pre>
 *
 * @author sungdon.choi
 */
@Data
@Builder
public class IntegrationContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 연계 대상 시스템 ID (예: MCI, HOST, FEP)
     */
    private String systemId;

    /**
     * 처리 대상의 로케일 (예: ko_KR, en_US)
     */
    private Locale locale;

    /**
     * 요청 식별자 (trace/log 연계용)
     */
    private String requestId;

    /**
     * 인터페이스 ID (업무 시스템 내 정의된 연계 인터페이스 식별자)
     */
    private String interfaceId;

    /**
     * 캡처 시스템명 (ex: OLTP, BATCH)
     */
    private String captureSystem;
    
    /**
     * 
     */
    private String typeName;
    
    /**
     * 
     */
    private String messageSenderBody;
    
    /**
     * 
     */
    private String senderDomainBody;

    /**
     * 문자 인코딩 (예: UTF-8, EUC-KR)
     */
    private String charset;

    /**
     * 요청 전반에서 공유되는 속성 집합
     */
    private Map<String, Object> attributes;

    /**
     * 전문 메시지 직렬화 처리 옵션
     */
    private SerializationOptions serializationOptions;

    /**
     * 전문 메시지 역직렬화 처리 옵션
     */
    private DeserializationOptions deserializationOptions;

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
    
    /**
     * 
     * @param attrName
     */
    public void removeAttribute(String attrName) {
    	if (this.attributes == null) {
    		return;
    	}
    	this.attributes.remove(attrName);
    }
}
