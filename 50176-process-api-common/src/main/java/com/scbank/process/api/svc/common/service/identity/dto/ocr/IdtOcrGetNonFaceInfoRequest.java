package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetNonFaceInfoRequest", description = "비대면정보조회 요청 DTO", type = Type.REQUEST)
public class IdtOcrGetNonFaceInfoRequest implements IMessageObject {
	@MessageField(id = "perBusNo1", name = "주민번호 앞번호", example = "")
	private String perBusNo1;
	
	@MessageField(id = "perBusNo2", name = "주민번호 뒷번호", example = "")
	private String perBusNo2;
	
	@MessageField(id = "perBusNo2Enc", name = "주민번호 뒷번호", example = "")
	private String perBusNo2Enc;
	
	@MessageField(id = "name", name = "이름", example = "")
	private String name;
	
	@MessageField(id = "ocrType", name = "ocr구분", example = "1")
	private String ocrType;
	
	@MessageField(id = "shootingType", name = "촬영구분", example = "")
	private String shootingType;
	
	@MessageField(id = "seqNo", name = "시퀀스", example = "1123")
	private String seqNo;
	
	@MessageField(id = "seqNoList", name = "시퀀스리스트", example = "112,333")
	private String seqNoList;
	
	@MessageField(id = "tradNoList", name = "거래번호리스트", example = "112,333")
	private String tradNoList;
	
	@MessageField(id = "licenseMiddleNumber", name = "운전면허중간번호", example = "123456")
	private String licenseMiddleNumber;
}
