package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 간편로그인 가입 모바일OTP 검사
 */
@Data
@IntegrationMessage(id = "AthMgtGetSimpLognRegistMotpRequest", type = Type.REQUEST)
public class AthMgtGetSimpLognRegistMotpRequest implements IMessageObject {

	@MessageField(id = "yiMOTPNO", name = "MOTP번호")
	private String yiMOTPNO;
	
	@MessageField(id = "noLogin", name = "noLogin")
	private String noLogin;
	
	

}