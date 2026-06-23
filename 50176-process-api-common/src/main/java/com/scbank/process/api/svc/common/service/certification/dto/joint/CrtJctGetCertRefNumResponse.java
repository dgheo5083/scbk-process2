package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 참조번호 발급
 */
@Data
@IntegrationMessage(id = "CrtJctGetCertRefNumResponse", type = Type.RESPONSE)
public class CrtJctGetCertRefNumResponse implements IMessageObject {

	@MessageField(id = "refNum", name = "참조번호")
	private String refNum;

	@MessageField(id = "appCode", name = "인가코드")
	private String appCode;

	@MessageField(id = "issueType", name = "인증서 발급구분")
	private String issueType;

	@MessageField(id = "yesSignCaIp", name = "금결원CA IP")
	private String yesSignCaIp;

	@MessageField(id = "yesSignCaPort", name = "금결원CA PORT")
	private String yesSignCaPort;

}