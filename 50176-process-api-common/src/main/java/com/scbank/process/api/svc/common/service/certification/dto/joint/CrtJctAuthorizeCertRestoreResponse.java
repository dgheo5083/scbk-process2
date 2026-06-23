package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 인증서 효력회복 완료
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeCertRestoreResponse", type = Type.RESPONSE)
public class CrtJctAuthorizeCertRestoreResponse implements IMessageObject {

	@MessageField(id = "resCode", name = "본거래 응답코드")
	private String resCode;

	@MessageField(id = "resMsg", name = "본거래 응답메시지")
	private String resMsg;

}