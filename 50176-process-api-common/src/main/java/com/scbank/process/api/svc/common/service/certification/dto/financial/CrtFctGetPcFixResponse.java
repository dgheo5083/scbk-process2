package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 비로그인 단말기지정 서비스 조회 응답
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeFidoCertificateResponse", type = Type.RESPONSE)
public class CrtFctGetPcFixResponse implements IMessageObject {

	@MessageField(id = "otherPcYes", name = "otherPcYes")
	private String otherPcYes;
	
	@MessageField(id = "pcFixValue", name = "pcFixValue")
	private String pcFixValue;

}