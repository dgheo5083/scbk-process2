package com.scbank.process.api.svc.common.service.functions.dto.clickToCall;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncCtcSendRequest", description = "전화상담 요청", type = Type.REQUEST)
public class FncCtcSendRequest implements IMessageObject {

    @MessageField(id = "callGroup", name = "", defaultValue = "")
    private String callGroup;

    @MessageField(id = "command", name = "", defaultValue = "")
    private String command;

    @MessageField(id = "servicePath", name = "", defaultValue = "")
    private String servicePath;

    @MessageField(id = "custName", name = "", defaultValue = "")
    private String custName;

    @MessageField(id = "custTelNo", name = "", defaultValue = "")
    private String custTelNo;

    @MessageField(id = "url", name = "", defaultValue = "")
    private String url;

    @MessageField(id = "timeChkYn", name = "", defaultValue = "N")
    private String timeChkYn;
}
