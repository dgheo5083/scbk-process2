package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 인증서 효력정지 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertSuspensionUserResponse", type = Type.RESPONSE)
public class CrtJctValidateCertSuspensionUserResponse implements IMessageObject {

	@MessageField(id = "custName", name = "고객명")
	private String custName;

	@MessageField(id = "handPhone1", name = "핸드폰번호1")
	private String handPhone1;

	@MessageField(id = "handPhone2", name = "핸드폰번호2")
	private String handPhone2;

	@MessageField(id = "handPhone3", name = "핸드폰번호3")
	private String handPhone3;

	@MessageField(id = "yoHpIn", name = "휴대폰인증 서비스 가입 여부")
	private String yoHpIn;

}