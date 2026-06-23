package com.scbank.process.api.svc.common.service.certification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 핀테크인증서 이용등록 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtFtcValidateFintechCertCertificateResponse", type = Type.RESPONSE)
public class CrtFtcValidateFintechCertCertificateResponse implements IMessageObject {

	@MessageField(id = "resultCode", name = "resultCode")
	private String resultCode;

	@MessageField(id = "joinFlag", name = "핀테크 가입 여부")
	private String joinFlag;

}