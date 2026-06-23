package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 생체인증 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtFctValidateFidoCertificateRequest", type = Type.REQUEST)
public class CrtFctValidateFidoCertificateRequest implements IMessageObject {

	@MessageField(id = "pkcs7SignedData", name = "전자서명값")
	private String pkcs7SignedData;

	@MessageField(id = "certSeqNum", name = "인증서 일련번호")
	private String certSeqNum;

	@MessageField(id = "simpleKeyToken", name = "간편인증등록토큰")
	private String simpleKeyToken;

}