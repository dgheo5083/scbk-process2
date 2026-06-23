package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterNonFaceScreenInfoResponse", type = Type.RESPONSE)
public class RegisterNonFaceScreenInfoResponse implements IMessageObject {
	@MessageField(id = "successYn", name = "성공여부")
    private String successYn;
}
