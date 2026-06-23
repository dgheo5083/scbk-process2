package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SignVerifyRequest", type = Type.REQUEST)
public class SignVerifyRequest implements IMessageObject {

    @MessageField(id = "userId", name = "TEST8502")
    private String userId;

    @MessageField(id = "acctCredential", name = "999999")
    private String acctCredential;

    @MessageField(id = "transferInfo", name = "")
    private TestTransferInfo transferInfo;

}