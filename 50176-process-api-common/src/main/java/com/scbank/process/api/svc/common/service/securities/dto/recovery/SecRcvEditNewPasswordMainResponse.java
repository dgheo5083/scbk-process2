package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SecRcvEditNewPasswordMainResponse", type = Type.RESPONSE)
public class SecRcvEditNewPasswordMainResponse implements IMessageObject {

    // 전문
    @MessageField(id = "userID", name = "이용자번호")
    private String userID;

    //
    @MessageField(id = "paramJsonString", name = "paramJsonString")
    private String paramJsonString;

    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

    @MessageField(id = "isLoginYN", name = "isLoginYN")
    private String isLoginYN;

}
