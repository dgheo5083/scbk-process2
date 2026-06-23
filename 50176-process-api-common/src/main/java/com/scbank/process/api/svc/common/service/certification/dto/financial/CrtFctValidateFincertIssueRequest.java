package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 발급 본인확인
 */
@Data
@IntegrationMessage(id = "CrtFctValidateFincertIssueRequest", type = Type.REQUEST)
public class CrtFctValidateFincertIssueRequest implements IMessageObject {

	@MessageField(id = "custJumin1", name = "실명번호")
	private String custJumin1;

	@MessageField(id = "custJumin2", name = "실명번호")
	private String custJumin2;

	@MessageField(id = "acctNum", name = "계좌번호")
	private String acctNum;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;

}