package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 간편로그인 가입 모바일OTP 검사
 */
@Data
@IntegrationMessage(id = "AthMgtGetSimpLognRegistMotpResponse", type = Type.RESPONSE)
public class AthMgtGetSimpLognRegistMotpResponse implements IMessageObject {

	@MessageField(id = "yoCVMYN", name = "가입여부")
	private String yoCVMYN;
	
	@MessageField(id = "yoCVMHP", name = "가입일")
	private String yoCVMHP;

}