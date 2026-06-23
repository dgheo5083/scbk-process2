package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 강제폐기
 */
@Data
@IntegrationMessage(id = "CrtDctAuthorizeDigitalcertForceRevokeResponse", type = Type.RESPONSE)
public class CrtDctAuthorizeDigitalcertForceRevokeResponse implements IMessageObject {

	@MessageField(id = "resCode", name = "응답코드")
	private String resCode;

	@MessageField(id = "resMsg", name = "응답메시지")
	private String resMsg;

}