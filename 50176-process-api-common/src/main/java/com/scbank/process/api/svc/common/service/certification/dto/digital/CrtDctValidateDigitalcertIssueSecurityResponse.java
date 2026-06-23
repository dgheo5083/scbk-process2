package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 발급 보안매체 검증
 */
@Data
@IntegrationMessage(id = "CrtDctValidateDigitalcertIssueSecurityResponse", type = Type.RESPONSE)
public class CrtDctValidateDigitalcertIssueSecurityResponse implements IMessageObject {

	@MessageField(id = "teleOne", name = "전화번호1")
	private String teleOne;

	@MessageField(id = "teleTwo", name = "전화번호2")
	private String teleTwo;

	@MessageField(id = "teleThree", name = "전화번호3")
	private String teleThree;

	@MessageField(id = "emailAddrF", name = "이메일앞")
	private String emailAddrF;

	@MessageField(id = "emailAddrE", name = "이메일뒤")
	private String emailAddrE;

}