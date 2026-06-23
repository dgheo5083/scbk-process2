package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 발급완료 통지
 */
@Data
@IntegrationMessage(id = "CrtFctConfirmFincertIssueRequest", type = Type.REQUEST)
public class CrtFctConfirmFincertIssueRequest implements IMessageObject {

	@MessageField(id = "email1", name = "이메일1")
	private String email1;

	@MessageField(id = "email2", name = "이메일2")
	private String email2;

	@MessageField(id = "certSeqNum", name = "인증서 일련번호")
	private String certSeqNum;

	@MessageField(id = "simpleKeyToken", name = "간편인증등록토큰")
	private String simpleKeyToken;
	
	@MessageField(id = "onAgreeChk", name = "인증서 발급 온라인 사전동의 여부")
	private String onAgreeChk;
	

}