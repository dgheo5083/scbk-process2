package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSendAcctAuthResponse", description = "비대면 인증 정보 조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetNonFaceAuthCompleteYnResponse implements IMessageObject {
	
	@MessageField(id = "idCardCd", name = "신분증코드")
	private String idCardCd;
	
	@MessageField(id = "authntIndCd", name = "인증구분코드")
	private String authntIndCd;
	
	@MessageField(id = "cddReqCd", name = "cdd요청코드")
	private String cddReqCd;
	
	@MessageField(id = "docEvdcCd", name = "증빙서류확인")
	private String docEvdcCd;
	
	@MessageField(id = "kcddPollingYn", name = "KCDD자동화여부")
	private String kcddPollingYn;
	
	@MessageField(id = "cddFlag", name = "CDD여부")
	private String cddFlag;
	
}
