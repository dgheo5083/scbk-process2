package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 해지
 */
@Data
@IntegrationMessage(id = "CrtDctAuthorizeDigitalcertRevokeResponse", type = Type.RESPONSE)
public class CrtDctAuthorizeDigitalcertRevokeResponse implements IMessageObject {

	@MessageField(id = "trId", name = "거래아이디")
	private String trId;

	@MessageField(id = "resultCode", name = "응답코드")
	private String resultCode;

	@MessageField(id = "resultMsg", name = "응답메시지")
	private String resultMsg;

	@MessageField(id = "userId", name = "이용자아이디")
	private String userId;

}