package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 발급 완료
 */
@Data
@IntegrationMessage(id = "CrtJctConfirmCertIssueResponse", type = Type.RESPONSE)
public class CrtJctConfirmCertIssueResponse implements IMessageObject {

	@MessageField(id = "expireDate", name = "인증서 만료일")
	private String expireDate;

}