package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * OTP 이용등록/해제 정보조회
 */
@Data
@IntegrationMessage(id = "AthMgtGetOtpRequest", type = Type.REQUEST)
public class AthMgtGetOtpRequest implements IMessageObject {

	@MessageField(id = "yiGUBUN", name = "거래구분")
	private String yiGUBUN;
	
}