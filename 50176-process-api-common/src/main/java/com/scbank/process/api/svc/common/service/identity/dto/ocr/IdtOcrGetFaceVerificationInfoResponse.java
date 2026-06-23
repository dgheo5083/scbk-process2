package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetFaceVerificationInfoResponse", description = "안면인식정보조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetFaceVerificationInfoResponse implements IMessageObject {
	
	@MessageField(id = "videoCallTargetYn", name = "영상통화대상여부")
	private String videoCallTargetYn;
	
	@MessageField(id = "faceRecgTryCnt", name = "안면인식시도횟수")
	private String faceRecgTryCnt;
	
	@MessageField(id = "faceResultYn", name = "안면인식결과")
	private String faceResultYn;
	
}
