package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSendIdCardImageResponse", description = "실물신분증 이미지 전송 응답 DTO", type = Type.RESPONSE)
public class IdtOcrSendIdCardImageResponse implements IMessageObject {
	@MessageField(id = "resultCode", name = "결과코드")
	private String resultCode;
	
	@MessageField(id = "code", name = "코드")
	private String code;
	
	@MessageField(id = "reqSeq", name = "요청순번")
	private String reqSeq;
	
	@MessageField(id = "idCardTruthCnt", name = "시도횟수")
	private String idCardTruthCnt;
}
