package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 휴대폰 인증 서비스 가입자 추가인증
 */
@Data
@IntegrationMessage(id = "CrtJctGetCertAuthPhoneRequest", type = Type.REQUEST)
public class CrtJctGetCertAuthPhoneRequest implements IMessageObject {

	@MessageField(id = "callSvc", name = "호출 서비스[1:공동인증서 갱신, 2:공동인증서 효력정지]")
	private String callSvc;

	@MessageField(id = "phone1", name = "휴대폰번호1")
	private String phone1;

	@MessageField(id = "phone2", name = "휴대폰번호2")
	private String phone2;

	@MessageField(id = "phone3", name = "휴대폰번호3")
	private String phone3;
}