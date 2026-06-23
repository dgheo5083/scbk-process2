package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 인증서 효력회복 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRestoreUserResponse", type = Type.RESPONSE)
public class CrtJctValidateCertRestoreUserResponse implements IMessageObject {

	@MessageField(id = "handPhone", name = "휴대폰 전화번호")
	private String handPhone;

	@MessageField(id = "homeTele", name = "집 전화번호")
	private String homeTele;

	@MessageField(id = "jobTele", name = "직장 전화번호")
	private String jobTele;

	@MessageField(id = "custName", name = "고객명")
	private String custName;

}