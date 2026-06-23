package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 생체인증 가입
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeFidoCertificateResponse", type = Type.RESPONSE)
public class CrtFctAuthorizeFidoCertificateResponse implements IMessageObject {

	@MessageField(id = "resCode", name = "resCode")
	private String resCode;

	@MessageField(id = "resMsg", name = "resMsg")
	private String resMsg;

	@MessageField(id = "trId", name = "trId")
	private String trId;

	@MessageField(id = "svcTrId", name = "svcTrId")
	private String svcTrId;

	@MessageField(id = "userId", name = "userId")
	private String userId;

	@MessageField(id = "deviceId", name = "deviceId")
	private String deviceId;

	@MessageField(id = "appId", name = "appId")
	private String appId;

}