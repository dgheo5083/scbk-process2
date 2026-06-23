package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSetAuthInitInfoResponse", description = "인증기본정보세팅 응답 DTO", type = Type.RESPONSE)
public class IdtOcrSetAuthInitInfoResponse implements IMessageObject {
	
	@MessageField(id = "kcddPollingYn", name = "KCDD자동화여부")
	private String kcddPollingYn;
}
