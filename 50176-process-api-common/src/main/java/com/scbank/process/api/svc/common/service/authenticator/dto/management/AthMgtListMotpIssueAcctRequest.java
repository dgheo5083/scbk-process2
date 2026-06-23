package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 모바일OTP 발급 출금계좌목록 조회
 */
@Data
@IntegrationMessage(id = "AthMgtListMotpIssueAcctRequest", type = Type.REQUEST)
public class AthMgtListMotpIssueAcctRequest implements IMessageObject {
	
	@MessageField(id = "userId", name = "userId")
	private String userId;

}