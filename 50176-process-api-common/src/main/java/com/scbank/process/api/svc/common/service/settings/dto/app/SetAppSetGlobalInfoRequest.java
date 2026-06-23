package com.scbank.process.api.svc.common.service.settings.dto.app;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetAppSetGlobalInfoRequest", description = "push param session 저장 DTO", type = Type.REQUEST)
public class SetAppSetGlobalInfoRequest implements IMessageObject {

    @MessageField(id = "cnnCtnWay", name = "제휴코드")
    private String cnnCtnWay;

    @MessageField(id = "clerkNo", name = "소개자행번")
    private String clerkNo;

    @MessageField(id = "reaCdn", name = "REA코드")
    private String reaCdn;


}
