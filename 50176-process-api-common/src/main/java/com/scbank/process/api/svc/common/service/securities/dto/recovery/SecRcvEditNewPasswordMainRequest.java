package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SecRcvEditNewPasswordMainRequest", type = Type.REQUEST)
public class SecRcvEditNewPasswordMainRequest implements IMessageObject {

    // 전문
    @MessageField(id = "yiUPGB", name = "업무구분")
    private String yiUPGB;

    @MessageField(id = "yiGJNO", name = "계좌번호")
    private String yiGJNO;

    // input
    @MessageField(id = "paramJsonString", name = "paramJsonString")
    private String paramJsonString;

    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

}
