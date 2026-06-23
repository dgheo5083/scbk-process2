package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetIdCardShotInfoRequest", description = "신분증촬영 정보 조회 요청 DTO", type = Type.REQUEST)
public class IdtOcrGetIdCardShotInfoRequest implements IMessageObject {
	
	@MessageField(id = "bizType", name = "업무구분", example = "CASA")
	private String bizType;
	
	@MessageField(id = "custNo", name = "고객번호", example = "")
	private String custNo;
	
	@MessageField(id = "tradNo", name = "거래번호", example = "")
	private String tradNo;
	
	@MessageField(id = "isRAS", name = "화상상담여부", example = "N")
	private String isRAS;
	
	@MessageField(id = "isForceVideo", name = "영상통화대상여부", example = "N")
	private String isForceVideo;
	
	@MessageField(id = "isForceFace", name = "안면인식대상여부", example = "N")
	private String isForceFace;
	
	@MessageField(id = "paramJsonString", name = "jsonString파라미터", example = "")
	private String paramJsonString;
	
	@MessageField(id = "isCommonEntered", name = "신분증공통진입여부", example = "N")
	private String isCommonEntered;
	
	@MessageField(id = "screenFlag", name = "이어하기 여부", example = "N")
	private String screenFlag;
}
