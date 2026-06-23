package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 간편로그인 가입 계좌확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateSimpLognRegistAcctRequest", type = Type.REQUEST)
public class AthMgtValidateSimpLognRegistAcctRequest implements IMessageObject {

	@MessageField(id = "userId", name = "고객아이디")
	private String userId;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;

	@MessageField(id = "selAcctNum", name = "계좌번호")
	private String selAcctNum;

}