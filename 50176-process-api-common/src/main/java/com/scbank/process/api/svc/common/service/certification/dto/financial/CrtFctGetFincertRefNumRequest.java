package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 참조번호 발급
 */
@Data
@IntegrationMessage(id = "CrtFctGetFincertRefNumRequest", type = Type.REQUEST)
public class CrtFctGetFincertRefNumRequest implements IMessageObject {

	@MessageField(id = "email1", name = "email1")
	private String email1;

	@MessageField(id = "email2", name = "email2")
	private String email2;

	@MessageField(id = "tel1", name = "tel1")
	private String tel1;

	@MessageField(id = "tel2", name = "tel2")
	private String tel2;

	@MessageField(id = "tel3", name = "tel3")
	private String tel3;

}