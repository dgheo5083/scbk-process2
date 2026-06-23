package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 EDMS 계약서류조회 PDF 파일이 존재하는지 체크
 */
@Data
@IntegrationMessage(id = "SupDocHasEdmsPdfFileRequest", type = Type.REQUEST)
public class SupDocHasEdmsPdfFileRequest implements IMessageObject {

    @MessageField(id = "edmsXvarmDocMeta", name = "edmsXvarmDocMeta")
    private String edmsXvarmDocMeta;
}