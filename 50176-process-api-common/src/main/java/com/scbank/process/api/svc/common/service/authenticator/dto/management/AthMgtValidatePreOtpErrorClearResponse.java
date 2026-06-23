package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스 
 * OTP보안카드 오류해제 페이지 진입시 체크
 */
@Data
@IntegrationMessage(id = "AthMgtValidatePreOtpErrorClearResponse", type = Type.RESPONSE)
public class AthMgtValidatePreOtpErrorClearResponse implements IMessageObject {
	@MessageField(id = "isCI", name = "CI값 정상여부")
	private String isCI;

	@MessageField(id = "userCiInfo", name = "userCiInfo")
	private String userCiInfo;

	@MessageField(id = "safeCardState", name = "safeCardState")
	private String safeCardState;

	@MessageField(id = "safeCardStateNm", name = "safeCardStateNm")
	private String safeCardStateNm;

	@MessageField(id = "safeCardKind", name = "safeCardKind")
	private String safeCardKind;

	@MessageField(id = "smartOTP", name = "smartOTP")
	private String smartOTP;
}