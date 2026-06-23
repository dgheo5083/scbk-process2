package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 갱신완료 통지
 */
@Data
@IntegrationMessage(id = "CrtFctConfirmFincertRenewRequest", type = Type.REQUEST)
public class CrtFctConfirmFincertRenewRequest implements IMessageObject {

	@MessageField(id = "renewCertSeqNum", name = "인증서 일련번호")
	private String renewCertSeqNum;

	@MessageField(id = "renewSimpleKeyToken", name = "간편인증등록토큰")
	private String renewSimpleKeyToken;

	@MessageField(id = "renewSimpleKeyTokenChanged", name = "renewSimpleKeyTokenChanged")
	private String renewSimpleKeyTokenChanged;
	
	@MessageField(id = "onAgreeChk", name = "onAgreeChk")
	private String onAgreeChk;

}