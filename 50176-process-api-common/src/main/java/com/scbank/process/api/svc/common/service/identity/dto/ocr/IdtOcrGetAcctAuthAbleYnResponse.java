package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtGetAcctAuthAbleYnResponse", description = "계좌인증 가능여부 조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetAcctAuthAbleYnResponse implements IMessageObject {
	@MessageField(id = "cntrprtyAuthntFailCnt", name = "타행인증실패횟수")
	private int cntrprtyAuthntFailCnt;
	
	@MessageField(id = "cntrprtyRemitReqCnt", name = "타행송금요청건수")
	private int cntrprtyRemitReqCnt;
	
	@MessageField(id = "custNm", name = "고객명")
	private String custNm;
	
}
