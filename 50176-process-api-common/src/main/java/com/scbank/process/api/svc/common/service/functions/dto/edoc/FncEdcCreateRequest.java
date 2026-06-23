package com.scbank.process.api.svc.common.service.functions.dto.edoc;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncEdcCreateRequest", type = Type.REQUEST)
public class FncEdcCreateRequest implements IMessageObject {

    @MessageField(id = "tranType", name = "")
    private String tranType;

    @MessageField(id = "bizType", name = "")
    private String bizType;

    @MessageField(id = "custNo", name = "")
    private String custNo;

    @MessageField(id = "tradNo", name = "")
    private String tradNo;

    @MessageField(id = "edocJsonStr", name = "")
    private String edocJsonStr;
}
