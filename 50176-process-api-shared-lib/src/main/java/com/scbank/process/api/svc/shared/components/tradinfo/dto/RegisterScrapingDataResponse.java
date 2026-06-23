package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterScrapingDataResponse", type = Type.RESPONSE)
public class RegisterScrapingDataResponse implements IMessageObject {
	@MessageField(id = "updateResult", name = "")
	private Integer updateResult;
}
