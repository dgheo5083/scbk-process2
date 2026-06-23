package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 간편로그인 가입
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeSimpLognRegistResponse", type = Type.RESPONSE)
public class AthMgtAuthorizeSimpLognRegistResponse implements IMessageObject {

	@MessageField(id = "yoSEKEY", name = "OTP 비밀키")
	private String yoSEKEY;

	@MessageField(id = "yoMRAND", name = "랜덤질의값")
	private String yoMRAND;

}