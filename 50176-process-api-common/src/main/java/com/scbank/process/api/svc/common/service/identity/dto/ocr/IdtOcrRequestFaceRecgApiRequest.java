package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrRequestFaceRecgApiRequest", description = "안면인식API요청 요청 DTO", type = Type.REQUEST)
public class IdtOcrRequestFaceRecgApiRequest implements IMessageObject {
	
	@MessageField(id = "idCardData", name = "신분증촬영 사진 데이터", example = "")
	private String idCardData;
	
	@MessageField(id = "facialRecognitionData", name = "안면인식 데이터", example = "")
	private String facialRecognitionData;
	
	@MessageField(id = "idCardType", name = "신분증 구분", example = "2")
	private String idCardType;
	
	@MessageField(id = "isTruthYn", name = "재신고 여부", example = "N")
	private String isTruthYn;
	
	@MessageField(id = "base64IdCardData", name = "신분증촬영 사진 데이터 BASE64 암호화", example = "")
	private String base64IdCardData;
	
	@MessageField(id = "base64FacialRecognitionData", name = "안면인식 데이터 BASE64 암호화", example = "")
	private String base64FacialRecognitionData;
}
