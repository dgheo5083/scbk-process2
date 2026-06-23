package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 발급완료 통지
 */
@Data
@IntegrationMessage(id = "CrtFctConfirmFincertIssueResponse", type = Type.RESPONSE)
public class CrtFctConfirmFincertIssueResponse implements IMessageObject {

	@MessageField(id = "expireDate", name = "인증서 만료일")
	private String expireDate;

	@MessageField(id = "certSeqNum", name = "인증서 일련번호")
	private String certSeqNum;

	@MessageField(id = "simpleKeyToken", name = "간편인증등록토큰")
	private String simpleKeyToken;

}