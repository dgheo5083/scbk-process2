package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrRequestFaceVerificationResponse", description = "안면인식 요청 응답 DTO", type = Type.RESPONSE)
public class IdtOcrRequestFaceVerificationResponse implements IMessageObject {
	
	@MessageField(id = "resultYn", name = "결과여부")
	private String resultYn;
	
	@MessageField(id = "resultCode", name = "결과코드")
	private String resultCode;
	
}
