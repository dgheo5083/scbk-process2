package com.scbank.process.api.svc.common.service.certification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 핀테크인증서 이용등록 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtFtcValidateFintechCertCertificateRequest", type = Type.REQUEST)
public class CrtFtcValidateFintechCertCertificateRequest implements IMessageObject {

	@MessageField(id = "vpcg", name = "서명 데이터")
	private String vpcg;

	@MessageField(id = "plainText", name = "원본 전자서명 데이터")
	private String plainText;

	@MessageField(id = "datatype", name = "L로그인,S전자서명,A본인인증")
	private String datatype;

}