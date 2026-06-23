package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 발급 본인확인
 */
@Data
@IntegrationMessage(id = "CrtDctValidateDigitalcertIssueUserResponse", type = Type.RESPONSE)
public class CrtDctValidateDigitalcertIssueUserResponse implements IMessageObject {

	@MessageField(id = "yoLRSOTGB", name = "개인정보 노출 여부 = 1 차단")
	private String yoLRSOTGB;

	@MessageField(id = "yoCFJMGB", name = "조합번호여부 3=차단")
	private String yoCFJMGB;

}