package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 간편로그인 해지
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeSimpLognDeleteRequest", type = Type.REQUEST)
public class AthMgtAuthorizeSimpLognDeleteRequest implements IMessageObject {

	@MessageField(id = "noLogin", name = "noLogin")
	private String noLogin;

}