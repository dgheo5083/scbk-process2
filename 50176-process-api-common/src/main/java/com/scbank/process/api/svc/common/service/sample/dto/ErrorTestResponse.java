package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ErrorTestResponse", type = Type.RESPONSE)
public class ErrorTestResponse implements IMessageObject {

    @MessageField(id = "errorCode", name = "에러코드")
    private String errorCode;

}