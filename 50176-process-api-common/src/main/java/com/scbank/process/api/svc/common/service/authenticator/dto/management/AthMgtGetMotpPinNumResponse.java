package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 모바일OTP PIN번호 확인
 */
@Data
@IntegrationMessage(id = "AthMgtGetMotpPinNumResponse", type = Type.RESPONSE)
public class AthMgtGetMotpPinNumResponse implements IMessageObject {

	@MessageField(id = "yoSEKEY", name = "OTP 비밀키")
	private String yoSEKEY;

	@MessageField(id = "yoMRAND", name = "랜덤질의값")
	private String yoMRAND;

}