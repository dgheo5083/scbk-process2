package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1tbs03H985Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "OTP보정 응답 전문")
public class Ti1tbs03H985Res implements IMessageObject {

	@MessageField(id = "UserID", name = "UserID", length = 10)
	private String UserID;

	@MessageField(id = "RESULTVAL", name = "RESULTVAL", length = 1)
	private String RESULTVAL;
}