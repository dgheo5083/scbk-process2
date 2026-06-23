package com.scbank.process.api.svc.shared.components.ipinside.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "PcFixInfo", type = Type.RESPONSE, description = "PC지정서비스 응답")
public class PcFixInfo implements IMessageObject {

    @MessageField(id = "pcFixValue", name = "")
    private String pcFixValue;

    @MessageField(id = "otherPcYes", name = "")
    private String otherPcYes;

}
