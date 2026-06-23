package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 폐기 계좌확인
 */
@Data
@IntegrationMessage(id = "CrtFctValidateFincertRevokeAcctRequest", type = Type.REQUEST)
public class CrtFctValidateFincertRevokeAcctRequest implements IMessageObject {

	@MessageField(id = "accountNum", name = "계좌번호")
	private String accountNum;

	@MessageField(id = "accountBb", name = "계좌비밀번호")
	private String accountBb;

}