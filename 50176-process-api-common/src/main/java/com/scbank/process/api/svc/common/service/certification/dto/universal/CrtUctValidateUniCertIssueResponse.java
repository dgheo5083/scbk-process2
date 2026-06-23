package com.scbank.process.api.svc.common.service.certification.dto.universal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 범용인증서 발급 본인확인
 */
@Data
@IntegrationMessage(id = "CrtUctValidateUniCertIssueResponse", type = Type.RESPONSE)
public class CrtUctValidateUniCertIssueResponse implements IMessageObject {

	@MessageField(id = "yoAGREEGB", name = "온라인 발급 사전동의여부")
	private String yoAGREEGB;

	@MessageField(id = "yoAGIL", name = "인증서발급동의거부일")
	private String yoAGIL;

	@MessageField(id = "custName", name = "고객명")
	private String custName;

	@MessageField(id = "personOrCompanyCode", name = "개인법인 구분코드")
	private String personOrCompanyCode;

	@MessageField(id = "telCode", name = "전화번호")
	private String telCode;

}