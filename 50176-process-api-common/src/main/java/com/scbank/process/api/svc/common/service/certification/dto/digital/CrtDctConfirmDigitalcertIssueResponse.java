package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 발급 완료
 */
@Data
@IntegrationMessage(id = "CrtDctConfirmDigitalcertIssueResponse", type = Type.RESPONSE)
public class CrtDctConfirmDigitalcertIssueResponse implements IMessageObject {

	@MessageField(id = "updatedAndroidIdYn", name = "updatedAndroidIdYN")
	private String updatedAndroidIdYn;
	
	@MessageField(id = "resultCode", name = "resultCode")
	private String resultCode;
	
	@MessageField(id = "resultMsg", name = "resultMsg")
	private String resultMsg;
	
	@MessageField(id = "resultData", name = "resultData")
	private String resultData;

}