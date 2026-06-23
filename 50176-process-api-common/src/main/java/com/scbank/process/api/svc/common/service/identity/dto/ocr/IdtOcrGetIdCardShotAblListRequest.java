package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetIdCardShotAblListRequest", description = "신분증촬영 가능 목록조회 요청 DTO", type = Type.REQUEST)
public class IdtOcrGetIdCardShotAblListRequest implements IMessageObject {
	
	@MessageField(id = "bizType", name = "업무구분", example = "CASA")
	private String bizType;
	
	@MessageField(id = "paramJsonString", name = "jsonString파라미터", example = "")
	private String paramJsonString;
	
	@MessageField(id = "isCommonEntered", name = "신분증공통진입여부", example = "N")
	private String isCommonEntered;
	
	@MessageField(id = "isOverSeasKorean", name = "재외국인여부", example = "N")
	private String isOverSeasKorean;
}
