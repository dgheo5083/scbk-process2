package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 갱신 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtFctValidateFincertRenewResponse", type = Type.RESPONSE)
public class CrtFctValidateFincertRenewResponse implements IMessageObject {

	@MessageField(id = "yoAGREEGB", name = "온라인 발급 사전동의여부")
	private String yoAGREEGB;
	
	@MessageField(id = "yoCFJMGB", name = "조합번호여부 3=차단")
	private String yoCFJMGB;

}