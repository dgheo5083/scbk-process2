package com.scbank.process.api.svc.common.service.functions.dto.clickToCall;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncCtcSendResponse", description = "전화상담 응답", type = Type.RESPONSE)
public class FncCtcSendResponse implements IMessageObject {

    @MessageField(id = "timeOver", name = "", defaultValue = "N")
    private String timeOver;

    @MessageField(id = "errCode", name = "", defaultValue = "")
    private String errCode;
}
