package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetNonFaceAuthCompleteYnRequest", description = "비대면 인증 완료여부 조회 DTO", type = Type.REQUEST)
public class IdtOcrGetNonFaceAuthCompleteYnRequest implements IMessageObject {
	
	@MessageField(id = "custNo", name = "고객번호", example = "")
	private String custNo;
	
	@MessageField(id = "tradNo", name = "거래번호", example = "")
	private String tradNo;
	
	
}
