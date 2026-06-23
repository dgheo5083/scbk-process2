package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * OTP보안카드 오류해제
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeOtpErrorClearResponse", type = Type.RESPONSE)
public class AthMgtAuthorizeOtpErrorClearResponse implements IMessageObject {
	

}