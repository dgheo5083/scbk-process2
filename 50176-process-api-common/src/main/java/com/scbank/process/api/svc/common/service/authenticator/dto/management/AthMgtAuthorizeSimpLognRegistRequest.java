package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 간편로그인 가입
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeSimpLognRegistRequest", type = Type.REQUEST)
public class AthMgtAuthorizeSimpLognRegistRequest implements IMessageObject {

	@MessageField(id = "yiDVINF", name = "모바일OTP인증 Device Id")
	private String yiDVINF;

	@MessageField(id = "yiDVGBN", name = "모바일OTP인증 Device Cd")
	private String yiDVGBN;

	@MessageField(id = "yiOTPNO", name = "모바일OTP 일련번호")
	private String yiOTPNO;

	@MessageField(id = "pinBb01", name = "모바일OTP PIN비밀번호")
	private String pinBb01;

	@MessageField(id = "clientRnd", name = "Client랜덤값")
	private String clientRnd;
	
	@MessageField(id = "noLogin", name = "noLogin")
	private String noLogin;

}