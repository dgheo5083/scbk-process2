package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrUpdateNonFaceIdCardStatusRequest", description = "비대면인증상태 수정 요청 DTO", type = Type.REQUEST)
public class IdtOcrUpdateNonFaceIdCardStatusRequest implements IMessageObject {
	
	@MessageField(id = "tradNo", name = "거래번호", example = "")
	private String tradNo;
	
	@MessageField(id = "seqNo", name = "seqNo", example = "")
	private String seqNo;
	
	@MessageField(id = "idCardCd", name = "신분증코드", example = "")
	private String idCardCd;
	
	@MessageField(id = "bizType", name = "업무구분", example = "")
	private String bizType;
	
	@MessageField(id = "shootingType", name = "촬영구분", example = "")
	private String shootingType;
	
	@MessageField(id = "mobileSessionClearYn", name = "모바일세션클리어여부", example = "")
	private String mobileSessionClearYn;
	
	@MessageField(id = "idCardSessionClearYn", name = "신분증세션클리어여부", example = "")
	private String idCardSessionClearYn;
	
	@MessageField(id = "seqNoList", name = "seqNoList", example = "")
	private String seqNoList;
	
	@MessageField(id = "tradNoList", name = "거래번호", example = "")
	private String tradNoList;
	
	@MessageField(id = "callCntrStsCd", name = "콜센터상담코드", example = "")
	private String callCntrStsCd;
	
	@MessageField(id = "prgrssStsCd", name = "거래상태코드", example = "")
	private String prgrssStsCd;
	
	@MessageField(id = "testSkipYn", name = "테스트여부", example = "N")
	private String testSkipYn;
}
