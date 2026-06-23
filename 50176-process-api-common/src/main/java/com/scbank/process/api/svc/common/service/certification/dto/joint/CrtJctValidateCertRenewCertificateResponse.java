package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 갱신 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRenewCertificateResponse", type = Type.RESPONSE)
public class CrtJctValidateCertRenewCertificateResponse implements IMessageObject {

	@MessageField(id = "combPopType", name = "팝업타입")
	private String combPopType;

	@MessageField(id = "yoAGREEGB", name = "온라인 발급 사전동의여부")
	private String yoAGREEGB;

	@MessageField(id = "yesSignCaIp", name = "금결원CA IP")
	private String yesSignCaIp;

	@MessageField(id = "yesSignCaPort", name = "금결원CA PORT")
	private String yesSignCaPort;

	@MessageField(id = "yoHpIn", name = "휴대폰인증 서비스 가입 여부")
	private String yoHpIn;

	@MessageField(id = "handPhone1", name = "핸드폰번호1")
	private String handPhone1;

	@MessageField(id = "handPhone2", name = "핸드폰번호2")
	private String handPhone2;

	@MessageField(id = "handPhone3", name = "핸드폰번호3")
	private String handPhone3;

}