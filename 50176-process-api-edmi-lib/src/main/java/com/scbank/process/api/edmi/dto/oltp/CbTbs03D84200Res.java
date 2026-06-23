package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03D842000Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "OTP/안전카드오류횟수해제")
public class CbTbs03D84200Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "YOGRMSG", name = "거래메세지", length = 40)
	private String YOGRMSG;
}