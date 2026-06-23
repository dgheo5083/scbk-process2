package com.scbank.process.api.svc.common.service.support.dto.event;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupEvtApplyEventResponse", type = Type.RESPONSE)
public class SupEvtApplyEventResponse implements IMessageObject {

    @MessageField(id = "result", name = "result")
    String result;

}
