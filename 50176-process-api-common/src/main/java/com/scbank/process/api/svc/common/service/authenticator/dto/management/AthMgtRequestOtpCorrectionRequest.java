package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * OTP보정
 */
@Data
@IntegrationMessage(id = "AthMgtRequestOtpCorrectionRequest", type = Type.REQUEST)
public class AthMgtRequestOtpCorrectionRequest implements IMessageObject {

	@MessageField(id = "pssCd", name = "OTP응답값")
	private String pssCd;

}