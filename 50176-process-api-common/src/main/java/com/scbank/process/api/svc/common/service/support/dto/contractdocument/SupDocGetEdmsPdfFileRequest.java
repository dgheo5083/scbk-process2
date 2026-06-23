package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 EDMS 계약서류조회 PDF 다운로드진행
 */
@Data
@IntegrationMessage(id = "SupDocGetEdmsPdfFileRequest", type = Type.REQUEST)
public class SupDocGetEdmsPdfFileRequest implements IMessageObject {

    @MessageField(id = "edmsXvarmDocMeta", name = "edmsXvarmDocMeta")
    private String edmsXvarmDocMeta;

    @MessageField(id = "elementId", name = "elementId")
    private String elementId;

    @MessageField(id = "docCode", name = "DocCode")
    private String docCode;

    @MessageField(id = "acctNo", name = "계좌번호")
    private String acctNo;

    @MessageField(id = "chnnlMk", name = "채널구분 01:UI 02:모바일 03:MP 04:PPR")
    private String chnnlMk;

    @MessageField(id = "jobMk", name = "업무구분 01:여신 02:펀드")
    private String jobMk;

    @MessageField(id = "barcodeData", name = "바코드데이터(UI이면서펀드일떄)")
    private String barcodeData;

    @MessageField(id = "isApp", name = "isApp")
    private String isApp;
}