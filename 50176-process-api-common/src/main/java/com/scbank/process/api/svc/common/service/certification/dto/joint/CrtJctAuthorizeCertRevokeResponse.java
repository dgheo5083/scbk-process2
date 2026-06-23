package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 폐기 완료
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeCertRevokeResponse", type = Type.RESPONSE)
public class CrtJctAuthorizeCertRevokeResponse implements IMessageObject {

	@MessageField(id = "serialValue", name = "인증서 시리얼 값")
	private String serialValue;

}