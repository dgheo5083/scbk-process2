package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrExtractUniquePointsResponse", description = "특장점 추출 응답 DTO", type = Type.RESPONSE)
public class IdtOcrExtractUniquePointsResponse implements IMessageObject {
	
	@MessageField(id = "resultCode", name = "결과코드")
	private String resultCode;
	
	@MessageField(id = "photoInfo", name = "사진정보")
	private String photoInfo;
	
	@MessageField(id = "photoInfoSize", name = "사진정보크기")
	private String photoInfoSize;
	
	@MessageField(id = "extractScore", name = "얼굴이미지점수")
	private String extractScore;
	
}
