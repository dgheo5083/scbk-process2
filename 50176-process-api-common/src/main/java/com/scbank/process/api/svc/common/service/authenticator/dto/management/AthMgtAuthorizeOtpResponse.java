package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * OTP 이용등록/해제
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeOtpResponse", type = Type.RESPONSE)
public class AthMgtAuthorizeOtpResponse implements IMessageObject {
	
	@MessageField(id = "OTPREGCODE", name = "OTPREGCODE")
	private String OTPREGCODE;
	
	@MessageField(id = "OTPVDCODE", name = "OTPVDCODE")
	private String OTPVDCODE;
	
	@MessageField(id = "YOVDCD", name = "YOVDCD")
	private String YOVDCD;
	
	@MessageField(id = "YOIRCD", name = "YOIRCD")
	private String YOIRCD;
	
	@MessageField(id = "YOOTPNO", name = "YOOTPNO")
	private String YOOTPNO;
}