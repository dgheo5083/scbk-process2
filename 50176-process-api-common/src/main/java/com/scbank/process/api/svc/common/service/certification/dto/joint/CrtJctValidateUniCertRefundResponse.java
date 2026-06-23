package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 수수료 납부 취소 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateUniCertRefundResponse", type = Type.RESPONSE)
public class CrtJctValidateUniCertRefundResponse implements IMessageObject {

	@MessageField(id = "custName", name = "고객명")
	private String custName;

}