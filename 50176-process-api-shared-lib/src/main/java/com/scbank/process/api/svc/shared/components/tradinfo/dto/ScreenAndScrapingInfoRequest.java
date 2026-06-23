package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ScreenAndScrapingInfoRequest", type = Type.REQUEST)
public class ScreenAndScrapingInfoRequest implements IMessageObject {

    @MessageField(id = "custNo", name = "고객번호")
    private String custNo;

    @MessageField(id = "tradNo", name = "거래번호")
    private String tradNo;

    @MessageField(id = "bizType", name = "업무구분")
    private String bizType;
}
