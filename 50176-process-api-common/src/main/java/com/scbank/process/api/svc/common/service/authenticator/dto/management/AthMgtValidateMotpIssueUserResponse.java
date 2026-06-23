package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 모바일OTP 발급 고객확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateMotpIssueUserResponse", type = Type.RESPONSE)
public class AthMgtValidateMotpIssueUserResponse implements IMessageObject {
	@MessageField(id = "cnnctnWay", name = "cnnctnWay")
	private String cnnctnWay;
	
	@MessageField(id = "clerkNo", name = "clerkNo")
	private String clerkNo;
}