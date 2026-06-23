package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * EDMS 계약서류조회 PDF 조회 , 다운로드횟수 증가
 */
@Data
@IntegrationMessage(id = "SupDocUpdateEdmsPdfFileDownloadCountRequest", type = Type.REQUEST)
public class SupDocUpdateEdmsPdfFileDownloadCountRequest implements IMessageObject {

    @MessageField(id = "elementId", name = "elementId")
    private String elementId;

    @MessageField(id = "docCode", name = "docCode")
    private String docCode;

    @MessageField(id = "acctNo", name = "acctNo")
    private String acctNo;

    @MessageField(id = "procMk", name = "procMk")
    private String procMk;

}
