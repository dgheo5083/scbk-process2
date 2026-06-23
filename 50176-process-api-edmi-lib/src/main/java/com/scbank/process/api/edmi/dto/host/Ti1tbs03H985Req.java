package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1tbs03H985Req", type = Type.REQUEST, captureSystem = "OLTP", description = "OTP보정 요청 전문")
public class Ti1tbs03H985Req implements IMessageObject {

	@MessageField(id = "UserID", name = "UserID", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "TSPassword", length = 8, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "PSSCD", name = "PSSCD", length = 6, align = AlignType.RIGHT)
	private String PSSCD; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_
}