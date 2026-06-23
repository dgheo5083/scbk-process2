package com.scbank.process.api.svc.common.service.verification.dto.sign;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfSgnInitiateResponse", type = Type.RESPONSE)
public class VrfSgnInitiateResponse implements IMessageObject {

    @MessageField(id = "digitalSignSkipYn", name = "", example = "")
    private String digitalSignSkipYn;
    
    // @MessageField(id = "aaidFinger", name = "", example = "")
    // private List<String> enabledFinTechSignYn;

}
