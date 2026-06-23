package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 발급
 */
@Data
@IntegrationMessage(id = "CrtDctAuthorizeDigitalcertIssueResponse", type = Type.RESPONSE)
public class CrtDctAuthorizeDigitalcertIssueResponse implements IMessageObject {

	@MessageField(id = "trId", name = "거래아이디")
	private String trId;

	@MessageField(id = "userId", name = "이용자아이디")
	private String userId;

}