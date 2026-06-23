package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 휴대폰 인증 서비스 가입자 추가인증
 */
@Data
@IntegrationMessage(id = "CrtJctGetCertAuthPhoneResponse", type = Type.RESPONSE)
public class CrtJctGetCertAuthPhoneResponse implements IMessageObject {

	@MessageField(id = "authNum", name = "인증번호")
	private String authNum;

}