package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 갱신완료 통지
 */
@Data
@IntegrationMessage(id = "CrtFctConfirmFincertRenewResponse", type = Type.RESPONSE)
public class CrtFctConfirmFincertRenewResponse implements IMessageObject {

	@MessageField(id = "finCertFidoReg", name = "금융인증서 간편인증 사용여부")
	private String finCertFidoReg;

	@MessageField(id = "newExpireDate", name = "인증서 만료일")
	private String newExpireDate;

	@MessageField(id = "certSeqNum", name = "인증서 일련번호")
	private String certSeqNum;

	@MessageField(id = "simpleKeyToken", name = "간편인증등록토큰")
	private String simpleKeyToken;

}