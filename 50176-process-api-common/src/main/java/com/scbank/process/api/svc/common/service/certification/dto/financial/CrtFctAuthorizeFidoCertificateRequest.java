package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 생체인증 가입
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeFidoCertificateRequest", type = Type.REQUEST)
public class CrtFctAuthorizeFidoCertificateRequest implements IMessageObject {

	@MessageField(id = "verifyType", name = "인증서 구분")
	private String verifyType;

	@MessageField(id = "issueType", name = "요청구분")
	private String issueType;

	@MessageField(id = "deviceId", name = "deviceId")
	private String deviceId;

	@MessageField(id = "appId", name = "appId")
	private String appId;

}