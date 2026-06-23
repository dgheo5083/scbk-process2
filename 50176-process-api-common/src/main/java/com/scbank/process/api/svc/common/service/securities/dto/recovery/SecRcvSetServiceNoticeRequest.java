package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SecRcvSetServiceNoticeRequest", type = Type.REQUEST)
public class SecRcvSetServiceNoticeRequest implements IMessageObject {
    // input
    @MessageField(id = "paramJsonString", name = "paramJsonString")
    private String paramJsonString;

    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

    @MessageField(id = "screenFlag", name = "screenFlag")
    private String screenFlag;

}
