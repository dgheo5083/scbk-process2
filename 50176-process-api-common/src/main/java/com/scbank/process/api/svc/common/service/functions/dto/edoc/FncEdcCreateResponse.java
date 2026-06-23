package com.scbank.process.api.svc.common.service.functions.dto.edoc;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncEdcCreateResponse", type = Type.RESPONSE)
public class FncEdcCreateResponse implements IMessageObject {

    @MessageField(id = "edocList", name = "")
    @RepeatedField
    private List<EdocData> edocList;

}