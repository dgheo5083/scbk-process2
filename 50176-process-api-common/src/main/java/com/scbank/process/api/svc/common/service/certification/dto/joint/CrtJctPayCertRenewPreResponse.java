package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 갱신 수수료 납부 예비거래
 */
@Data
@IntegrationMessage(id = "CrtJctPayCertRenewPreResponse", type = Type.RESPONSE)
public class CrtJctPayCertRenewPreResponse implements IMessageObject {

	@MessageField(id = "deptPersonName", name = "고객명")
	private String deptPersonName;

}