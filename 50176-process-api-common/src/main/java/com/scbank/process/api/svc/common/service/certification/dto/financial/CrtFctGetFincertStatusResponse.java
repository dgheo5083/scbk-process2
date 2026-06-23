package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 보유여부 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFincertStatusResponse", type = Type.RESPONSE)
public class CrtFctGetFincertStatusResponse implements IMessageObject {

	@MessageField(id = "regCertYn", name = "인증서 등록여부")
	private String regCertYn;

}