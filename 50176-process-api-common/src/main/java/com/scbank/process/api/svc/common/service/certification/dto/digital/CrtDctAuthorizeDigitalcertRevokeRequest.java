package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 디지털인증서 해지
 */
@Data
@IntegrationMessage(id = "CrtDctAuthorizeDigitalcertRevokeRequest", type = Type.REQUEST)
public class CrtDctAuthorizeDigitalcertRevokeRequest implements IMessageObject {

	@MessageField(id = "verifyType", name = "인증서 구분")
	private String verifyType;

	@MessageField(id = "issueType", name = "발급구분")
	private String issueType;

}