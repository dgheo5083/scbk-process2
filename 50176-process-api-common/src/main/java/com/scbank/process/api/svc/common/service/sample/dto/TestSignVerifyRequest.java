package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "TestSignVerifyRequest", type = Type.REQUEST)
public class TestSignVerifyRequest implements IMessageObject {

    @MessageField(id = "connectType", name = "")
    private String connectType;

    @MessageField(id = "signedData", name = "")
    private String signedData;

}