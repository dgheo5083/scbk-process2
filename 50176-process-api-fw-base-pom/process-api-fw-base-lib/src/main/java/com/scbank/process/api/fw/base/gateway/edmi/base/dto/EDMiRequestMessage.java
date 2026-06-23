package com.scbank.process.api.fw.base.gateway.edmi.base.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * EDMi 요청 메시지 Wrapper 클래스
 * 
 * @author sungdon.choi
 */
@Data
@Builder
public class EDMiRequestMessage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 연계시스템 ID
     */
    private String systemId;

    /**
     * trackingId
     */
    private String trackingId;

    /**
     * 인터페이스 식별자
     */
    private String interfaceId;

    /**
     * Capture System
     */
    private String captureSystem;

    /**
     * 요청 메시지 (fixedlength/json/xml ...)
     */
    private Object requestMessage;

    /**
     * Hex 인코딩 여부
     */
    private boolean hexEncoding;
    
    /**
     * 타입명
     */
    private String typeName;
    
    /**
     * senderBody
     */
    private String senderBody;
    
    /**
     * senderDomainBody
     */
    private String senderDomainBody;
}
