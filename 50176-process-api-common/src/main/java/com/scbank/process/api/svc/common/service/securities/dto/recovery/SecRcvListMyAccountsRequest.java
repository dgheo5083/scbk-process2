package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SecRcvListMyAccountsRequest", type = Type.REQUEST)
public class SecRcvListMyAccountsRequest implements IMessageObject {

    // input
    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

    @MessageField(id = "screenFlag", name = "screenFlag")
    private String screenFlag;
}
