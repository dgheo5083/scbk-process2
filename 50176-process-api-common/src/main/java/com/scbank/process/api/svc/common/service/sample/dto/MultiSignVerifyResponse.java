package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MultiSignVerifyResponse", type = Type.RESPONSE)
public class MultiSignVerifyResponse implements IMessageObject {

    @MessageField(id = "resCode", name = "응답코드", required = true)
    private String resCode;

}