package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import org.json.JSONObject;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * EDMS 계약서류조회 PDF 다운로드진행
 */
@Data
@IntegrationMessage(id = "SupDocGetEdmsPdfFileResponse", type = Type.RESPONSE)
public class SupDocGetEdmsPdfFileResponse implements IMessageObject {

    @MessageField(id = "contractPdfData", name = "contractPdfData")
    private String contractPdfData;

    @MessageField(id = "filePath", name = "filePath")
    private String filePath;

    @MessageField(id = "isApp", name = "isApp")
    private String isApp;

    @MessageField(id = "decFile", name = "file")
    private String decFile;

    @MessageField(id = "edmsXvarmDoc", name = "edmsXvarmDoc")
    private JSONObject edmsXvarmDoc;

    @MessageField(id = "byteFile", name = "byteFile")
    private byte[] byteFile;

    @MessageField(id = "strFile", name = "strFile")
    private String strFile;

    @MessageField(id = "strEdmsXvarmDoc", name = "strEdmsXvarmDoc")
    private String strEdmsXvarmDoc;
}