package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 비로그인 단말기지정 서비스 조회
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeFidoCertificateRequest", type = Type.REQUEST)
public class CrtFctGetPcFixRequest implements IMessageObject {

	@MessageField(id = "userId", name = "userId")
	private String userId;

}