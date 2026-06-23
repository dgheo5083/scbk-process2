package com.scbank.process.api.fw.base.gateway.edmi.base.constant;

/**
 * EDMI 메시지 상수 모음 인터페이스
 * 
 * @author sungdon.choi
 */
public interface EDMiConstants {

    String FLD_ROOT = "SCBML";
    String FLD_PAYLOAD_ROOT = "payload";
    String FLD_SCBML_PAYLOAD = "scbmlPayload";
    String FLD_RESPONSE_MESSAGE = "RESPONSEMESSAGE";
    String FLD_REQUESTMESASSAGE = "REQUESTMESSAGE";
    String FLD_TRANDATA = "TRANDATA";
    String FLD_MESSAGE = "MESSAGE";
    String FLD_HEADER = "ns:header";
    String FLD_EXCEPTIONS = "ns:exceptions";
    String FLD_EXCEPTION = "ns:exception";
    String FLD_CAPTURESYSTEM = "ns:captureSystem";
    String FLD_CODE = "ns:code";
    String FLD_DESCRIPTION = "ns:description";
    String FLD_MESSAGE_SENDER = "ns:messageSender";
    String FLD_COUNTRY_CODE = "ns:countryCode";
    String FLD_MESSAGE_VERSION = "ns:messageVersion";
    String FLD_TYPE_NAME = "ns:typeName";
    String FLD_SUB_TYPE = "ns:subType";
    String FLD_MULTI_MESSAGE_KNOWN = "ns:multiMessageKnown";
    String FLD_MESSAGE_TYPE = "ns:messageType";
    String FLD_MULTI_MESSAGE = "ns:multiMessage";
    String FLD_MESSAGE_DETAILS = "ns:messageDetails";
    String FLD_DOMAIN_NAME = "ns:domainName";
    String FLD_SUB_DOMAIN_NAME = "ns:subDomainName";
    String FLD_SENDER_DOMAIN = "ns:senderDomain";
    String FLD_INITIATED_TIMESTAMP = "ns:initiatedTimestamp";
    String FLD_TRAKING_ID = "ns:trackingId";
    String FLD_CHECKSUM = "ns:checksum";
    String FLD_POSSIBLE_DUPLICATE = "ns:possibleDuplicate";
    String FLD_SERVICE_BUS_ID = "ns:serviceBusID";
    String FLD_ORIGINATION_DETAILS = "ns:originationDetails";
    String FLD_EVENT_TYPE = "ns:eventType";
    String FLD_PROCESS = "ns:process";
    String FLD_PAYLOAD_FORMAT = "ns:payloadFormat";
    String FLD_PAYLOAD_VERSION = "ns:payloadVersion";

    String FLD_BODY = "*body";

    String VAL_COMPLETED = "Completed";
    String CAPTURE_SYSTEM_OLTP = "OLTP";
    String DEFAULT_PAYLOAD_FORMAT = "json";
    String DEFAULT_MESSAGE_VERSION = "1.0";
    String DEFAULT_PAYLOAD_VERSION = "1.0";
    String DEFAULT_CONTURY_CODE = "KR";
    String DEFAULT_CAPTURE_SYSTEM = CAPTURE_SYSTEM_OLTP;
    String DEFAULT_MESSAGE_TYPENAME = "CoreBanking:mbOLTPCommonRoute";
    String DEFAULT_MESSAGE_SENDER_BODY = "MB";
    
    String FLD_SYSTEMHEADER = "SYSTEMHEADER";
    String FLD_PROC_RSLT_DV_CD = "PROC_RSLT_DV_CD";
    String PATH_PROC_RSLT_DV_CD = "/SYSTEMHEADER/PROC_RSLT_DV_CD";
    String PATH_YOGHERR = "/TRANDATA/YOPBGTB/YOGHERR";
    String PATH_FOERRCOD = "/TRANDATA/FOAPIHDINF/FOERRCOD";
}
