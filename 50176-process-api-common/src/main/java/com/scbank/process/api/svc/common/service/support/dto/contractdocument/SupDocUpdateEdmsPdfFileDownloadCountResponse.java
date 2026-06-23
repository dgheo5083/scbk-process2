package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * EDMS 계약서류조회 PDF 조회 , 다운로드횟수 증가
 */
@Data
@IntegrationMessage(id = "SupDocUpdateEdmsPdfFileDownloadCountResponse", type = Type.RESPONSE)
public class SupDocUpdateEdmsPdfFileDownloadCountResponse implements IMessageObject {

    @MessageField(id = "dummyTemp1", name = "")
    private String dummyTemp1;

    @MessageField(id = "resCode", name = "정상코드")
    private String resCode;

    @MessageField(id = "bodyLength", name = "BODY부LENGTH")
    private int bodyLength;

    @MessageField(id = "aomacroNm", name = "마크로명으로 보임")
    private String aomacroNm;

    @MessageField(id = "dummyTemp2", name = "무슨데이터인지모름")
    private int dummyTemp2;

}