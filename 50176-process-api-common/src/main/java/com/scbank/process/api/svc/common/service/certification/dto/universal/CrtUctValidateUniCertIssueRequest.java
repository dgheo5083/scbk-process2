package com.scbank.process.api.svc.common.service.certification.dto.universal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 범용인증서 발급 본인확인
 */
@Data
@IntegrationMessage(id = "CrtUctValidateUniCertIssueRequest", type = Type.REQUEST)
public class CrtUctValidateUniCertIssueRequest implements IMessageObject {

	@MessageField(id = "userId", name = "사용자 아이디")
	private String userId;

	@MessageField(id = "custJumin1", name = "생년월일")
	private String custJumin1;

	@MessageField(id = "custJumin2", name = "주민번호 뒷자리")
	private String custJumin2;

}