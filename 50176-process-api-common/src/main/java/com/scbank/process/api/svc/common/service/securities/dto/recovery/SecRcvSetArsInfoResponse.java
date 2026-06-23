package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SecRcvSetArsInfoResponse", type = Type.RESPONSE)
public class SecRcvSetArsInfoResponse implements IMessageObject {

    @MessageField(id = "paramJsonString", name = "paramJsonString")
    private String paramJsonString;

    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

    @MessageField(id = "screenFlag", name = "screenFlag")
    private String screenFlag;

    @MessageField(id = "yiGJNO", name = "yiGJNO")
    private String yiGJNO;

}
