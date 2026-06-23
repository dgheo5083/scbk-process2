package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrExtractUniquePointsRequest", description = "특장점 추출 요청 DTO", type = Type.REQUEST)
public class IdtOcrExtractUniquePointsRequest implements IMessageObject {
	
	@MessageField(id = "image", name = "이미지", example = "")
	private String image;
	
	@MessageField(id = "ocrType", name = "OCR구분", example = "")
	private String ocrType;
	
}
