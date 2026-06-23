package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 간편로그인 가입 본인확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateSimpLognRegistResponse", type = Type.RESPONSE)
public class AthMgtValidateSimpLognRegistResponse implements IMessageObject {

	@MessageField(id = "safeCardKind", name = "보안카드종류")
	private String safeCardKind;

	@MessageField(id = "smartOTP")
	private String smartOTP;
	
	@MessageField(id = "safeCardIssueNum")
	private String safeCardIssueNum;
	
	@MessageField(id = "noLogin")
	private String noLogin;
	
}