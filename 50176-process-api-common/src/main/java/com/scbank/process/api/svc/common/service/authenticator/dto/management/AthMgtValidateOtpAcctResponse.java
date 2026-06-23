package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * OTP 이용등록/해제 계좌확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateOtpAcctResponse", type = Type.RESPONSE)
public class AthMgtValidateOtpAcctResponse implements IMessageObject {

	@MessageField(id = "userId", name = "이용자아이디")
	private String userId;

	@MessageField(id = "safeCardKind", name = "보안매체 종류")
	private String safeCardKind;

	@MessageField(id = "smartOTP", name = "스마트OTP ")
	private String smartOTP;

	@MessageField(id = "custName", name = "고객명")
	private String custName;

}