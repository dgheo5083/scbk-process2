package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrUpdateSSATruthInfoResponse", description = "사본판별결과 저장 응답 DTO", type = Type.RESPONSE)
public class IdtOcrUpdateSSATruthInfoResponse implements IMessageObject {
	@MessageField(id = "updateResult", name = "저장결과")
	String updateResult;
}
