package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetCheckAvailableResponse", type = Type.RESPONSE)
public class GetCheckAvailableResponse implements IMessageObject {
	@MessageField(id = "userAge", name = "사용자나이")
	private String userAge;
	
	@MessageField(id = "isLowAge", name = "최소나이여부")
	private String isLowAge;
	
	@MessageField(id = "isForeigner", name = "외국인여부")
	private String isForeigner;
}
