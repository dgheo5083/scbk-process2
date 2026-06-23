package com.scbank.process.api.svc.common.service.sample.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MultiSignVerifyRequest", type = Type.REQUEST)
public class MultiSignVerifyRequest implements IMessageObject {

    @MessageField(id = "userId", name = "", example = "TEST8502")
    private String userId;

    @MessageField(id = "acctCredential", name = "", example = "999999")
    private String acctCredential;

    @MessageField(id = "multiTransferInfoList", name = "")
    @RepeatedField
    private List<TestTransferInfo> multiTransferInfoList;

}