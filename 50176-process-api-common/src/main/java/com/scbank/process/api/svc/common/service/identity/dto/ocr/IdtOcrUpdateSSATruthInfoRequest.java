package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrUpdateSSATruthInfoRequest", description = "사본판별결과 저장 요청 DTO", type = Type.REQUEST)
public class IdtOcrUpdateSSATruthInfoRequest implements IMessageObject {
	
	@MessageField(id = "ssaTruth", name = "사본판별 결과", example = "")
	private String ssaTruth;
	
	@MessageField(id = "ssaConfidence", name = "사본판별 점수", example = "")
	String ssaConfidence;
}
