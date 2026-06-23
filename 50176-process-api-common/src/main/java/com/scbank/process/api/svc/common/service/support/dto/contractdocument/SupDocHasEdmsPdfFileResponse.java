package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계약서류제공 EDMS 계약서류조회 PDF 파일이 존재하는지 체크
 */
@Data
@IntegrationMessage(id = "SupDocHasEdmsPdfFileResponse", type = Type.RESPONSE)
public class SupDocHasEdmsPdfFileResponse implements IMessageObject {

    @MessageField(id = "edmsXvarmDocList", name = "edmsXvarmDocList")
    private String edmsXvarmDocList;
}