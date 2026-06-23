package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import java.util.ArrayList;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetNonFaceInfoResponse", description = "비대면정보조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetNonFaceInfoResponse implements IMessageObject {
	
	@MessageField(id = "tradNoList", name = "거래번호리스트")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private ArrayList<String> tradNoList;
	
	@MessageField(id = "seqNoList", name = "시퀀스리스트")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private ArrayList<String> seqNoList;
	
	@MessageField(id = "businessDateList", name = "영업일리스트")
	private ArrayList<String> businessDateList;
	
	@MessageField(id = "seqNo", name = "시퀀스")
	private String seqNo;
	
	@MessageField(id = "passCode", name = "pass코드")
	private String passCode;
	
	@MessageField(id = "calsBusinessDay", name = "계산영업일")
	private String calsBusinessDay;
	
	@MessageField(id = "productName", name = "상품명")
	private String productName;
}
