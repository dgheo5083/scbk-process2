package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 타기관 등록 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtJctValidateOtherCertRegCertificateRequest", type = Type.REQUEST)
public class CrtJctValidateOtherCertRegCertificateRequest implements IMessageObject {

	@MessageField(id = "pkcs7SignedData", name = "서명 데이터")
	private String pkcs7SignedData;

	@MessageField(id = "vidRandom", name = "vidRandom")
	private String vidRandom;

}