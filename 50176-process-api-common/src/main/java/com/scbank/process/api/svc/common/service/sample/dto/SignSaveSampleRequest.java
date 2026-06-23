package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SignSaveSampleRequest", type = Type.REQUEST)
public class SignSaveSampleRequest implements IMessageObject {

    @MessageField(id = "signedData", name = "서명데이터", example = "")
    private String signedData;

}
