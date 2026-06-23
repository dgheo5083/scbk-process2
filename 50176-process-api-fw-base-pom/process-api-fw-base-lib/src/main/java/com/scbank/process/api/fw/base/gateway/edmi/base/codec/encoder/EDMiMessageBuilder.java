package com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder;

import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_CAPTURE_SYSTEM;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_CONTURY_CODE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_MESSAGE_SENDER_BODY;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_MESSAGE_TYPENAME;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_MESSAGE_VERSION;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_PAYLOAD_FORMAT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.DEFAULT_PAYLOAD_VERSION;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_BODY;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_CAPTURESYSTEM;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_CHECKSUM;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_COUNTRY_CODE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_DOMAIN_NAME;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_EVENT_TYPE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_HEADER;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_INITIATED_TIMESTAMP;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_MESSAGE_DETAILS;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_MESSAGE_SENDER;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_MESSAGE_TYPE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_MESSAGE_VERSION;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_MULTI_MESSAGE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_MULTI_MESSAGE_KNOWN;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_ORIGINATION_DETAILS;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_PAYLOAD_FORMAT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_PAYLOAD_ROOT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_PAYLOAD_VERSION;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_POSSIBLE_DUPLICATE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_PROCESS;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_REQUESTMESASSAGE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_ROOT;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SCBML_PAYLOAD;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SENDER_DOMAIN;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SERVICE_BUS_ID;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SUB_DOMAIN_NAME;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_SUB_TYPE;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_TRAKING_ID;
import static com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants.FLD_TYPE_NAME;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.scbank.process.api.fw.core.utils.DateUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * EDMi 요청 메시지 빌더 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@Setter
@Getter
public class EDMiMessageBuilder {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSS");

    private String payloadFormat = DEFAULT_PAYLOAD_FORMAT;
    private String payloadVersion = DEFAULT_PAYLOAD_VERSION;
    private String captureSystem = DEFAULT_CAPTURE_SYSTEM;
    private String conturyCode = DEFAULT_CONTURY_CODE;
    private String messageTypeName = DEFAULT_MESSAGE_TYPENAME;
    private String messageSenderBody = DEFAULT_MESSAGE_SENDER_BODY;
    private String messageSenderDomainBody = "";
    private String serviceBusId;
    private String trackingId;
    private Object requestMessage;
    private String systemId;

    /**
     * 연계시스템 ID를 설정한다.
     * 
     * @param systemId
     * @return
     */
    public EDMiMessageBuilder systemId(String systemId) {
        this.systemId = systemId;
        return this;
    }

    /**
     * 요청 추적ID를 설정한다.
     * 
     * @param trackingId
     * @return
     */
    public EDMiMessageBuilder trackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    /**
     * payload 포맷을 설정한다.
     * 
     * @param payloadFormat payload 포맷
     * @return
     */
    public EDMiMessageBuilder payloadFormat(String payloadFormat) {
        this.payloadFormat = payloadFormat;
        return this;
    }

    /**
     * payload 버전을 설정한다.
     * 
     * @param payloadVersion payload 버전
     * @return
     */
    public EDMiMessageBuilder payloadVersion(String payloadVersion) {
        this.payloadVersion = payloadVersion;
        return this;
    }

    /**
     * 캡쳐시스템을 설정한다.
     * 
     * @param captureSystem 캡쳐시스템
     * @return
     */
    public EDMiMessageBuilder captureSystem(String captureSystem) {
        this.captureSystem = captureSystem;
        return this;
    }

    /**
     * 국가코드를 설정한다.
     * 
     * @param conturyCode 국가코드
     * @return
     */
    public EDMiMessageBuilder conturyCode(String conturyCode) {
        this.conturyCode = conturyCode;
        return this;
    }

    /**
     * 
     * @param messageTypeName
     * @return
     */
    public EDMiMessageBuilder messageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
        return this;
    }

    /**
     * 
     * @param messageSenderBody
     * @return
     */
    public EDMiMessageBuilder messageSenderBody(String messageSenderBody) {
        this.messageSenderBody = messageSenderBody;
        return this;
    }
    
    /**
     * 
     * @param messageSenderDomainBody
     * @return
     */
    public EDMiMessageBuilder messageSenderDomainBody(String messageSenderDomainBody) {
    	this.messageSenderDomainBody = messageSenderDomainBody;
    	return this;
    }

    /**
     * 
     * @param serviceBusId
     * @return
     */
    public EDMiMessageBuilder serviceBusId(String serviceBusId) {
        this.serviceBusId = serviceBusId;
        return this;
    }

    /**
     * 연계시스템 요청 전문 설정
     * 
     * @param requestMessage 연계시스템 요청 전문
     * @return
     */
    public EDMiMessageBuilder requestMessage(Object requestMessage) {
        this.requestMessage = requestMessage;
        return this;
    }

    /**
     * 
     * @return
     */
    public Map<String, Object> build() {
        if (log.isDebugEnabled()) {
            log.debug("# build captureSystem: {}, serviceBusID: {}", this.captureSystem, this.serviceBusId);
        }

        String trackingId = "";
        // ----------------------------------------------------
        // ns:header
        // ----------------------------------------------------
        Map<String, Object> header = new LinkedHashMap<String, Object>();

        // ----------------------------------------------------
        // messageDetails
        // ----------------------------------------------------
        Map<String, Object> messageDetails = new HashMap<>();
        messageDetails.put(FLD_MESSAGE_VERSION, DEFAULT_MESSAGE_VERSION);

        Map<String, Object> messageType = new HashMap<>();
        messageType.put(FLD_TYPE_NAME, this.messageTypeName);
        messageType.put(FLD_SUB_TYPE, new HashMap<String, Object>());

        Map<String, Object> multiMessage = new HashMap<>();
        multiMessage.put(FLD_MULTI_MESSAGE_KNOWN, new HashMap<String, Object>());

        messageDetails.put(FLD_MESSAGE_TYPE, messageType);
        messageDetails.put(FLD_MULTI_MESSAGE, multiMessage);

        header.put(FLD_MESSAGE_DETAILS, messageDetails);
        // ----------------------------------------------------
        // originationDetails
        // ----------------------------------------------------
        Map<String, Object> originationDetails = new HashMap<>();

        Map<String, Object> messageSender = new HashMap<>();

        Map<String, Object> messageSenderBody = new HashMap<>();
        messageSenderBody.put(FLD_BODY, this.messageSenderBody);

        messageSender.put(FLD_MESSAGE_SENDER, messageSenderBody);
        messageSender.put(FLD_COUNTRY_CODE, this.conturyCode);

        Map<String, Object> senderDomain = new HashMap<>();
        Map<String, Object> senderDomainBody = new HashMap<>();
        senderDomainBody.put(FLD_BODY, this.messageSenderDomainBody); //???

        senderDomain.put(FLD_DOMAIN_NAME, senderDomainBody);
        senderDomain.put(FLD_SUB_DOMAIN_NAME, new HashMap<>());

        messageSender.put(FLD_SENDER_DOMAIN, senderDomain);
        originationDetails.put(FLD_MESSAGE_SENDER, messageSender);

        String initiatedTimestamp = DateUtils.getCurrentDate(dateFormat);
        originationDetails.put(FLD_INITIATED_TIMESTAMP, initiatedTimestamp);

        originationDetails.put(FLD_TRAKING_ID, trackingId);
        originationDetails.put(FLD_CHECKSUM, new HashMap<>());
        originationDetails.put(FLD_POSSIBLE_DUPLICATE, "FALSE");
        originationDetails.put(FLD_SERVICE_BUS_ID, this.serviceBusId);

        header.put(FLD_ORIGINATION_DETAILS, originationDetails);

        // ----------------------------------------------------
        // ns:captureSystem
        // ----------------------------------------------------
        header.put(FLD_CAPTURESYSTEM, captureSystem);

        // ----------------------------------------------------
        // ns:process
        // ----------------------------------------------------
        Map<String, Object> process = new HashMap<>();
        process.put(FLD_EVENT_TYPE, "");
        header.put(FLD_PROCESS, process);

        // ----------------------------------------------------
        // payload
        // ----------------------------------------------------
        Map<String, Object> payload = new HashMap<>();
        payload.put(FLD_PAYLOAD_FORMAT, this.payloadFormat);
        payload.put(FLD_PAYLOAD_VERSION, this.payloadVersion);

        if (this.requestMessage != null) {
            Map<String, Object> requestMessage = new HashMap<>();
            requestMessage.put(FLD_REQUESTMESASSAGE, this.requestMessage);
            payload.put(FLD_SCBML_PAYLOAD, requestMessage);
        }

        Map<String, Object> root = new LinkedHashMap<>();
        root.put(FLD_HEADER, header);
        root.put(FLD_PAYLOAD_ROOT, payload);

        Map<String, Object> messageEnvelope = new HashMap<>();
        messageEnvelope.put(FLD_ROOT, root);

        return messageEnvelope;
    }

    /**
     * 
     * @return
     */
    public static EDMiMessageBuilder builder() {
        return new EDMiMessageBuilder();
    }
}
