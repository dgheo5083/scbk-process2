package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "RevolvingInfoDto", type = Type.RESPONSE, description = "마이데이터 > 현대카드 리볼빙 RevolvingInfoDto")
public class RevolvingInfoDto implements IMessageObject {

    @MessageField(id = "reqDt", name = "reqDt")
    private String reqDt;

    @MessageField(id = "endDt", name = "endDt")
    private String endDt;

    @MessageField(id = "closeDt", name = "closeDt")
    private String closeDt;

    @MessageField(id = "agreedPayAmt", name = "agreedPayAmt")
    private String agreedPayAmt;

    @MessageField(id = "agreedPayRate", name = "agreedPayRate")
    private String agreedPayRate;

    @MessageField(id = "minPayAmt", name = "minPayAmt")
    private String minPayAmt;

    @MessageField(id = "minPayRate", name = "minPayRate")
    private String minPayRate;

    @MessageField(id = "remainedAmt", name = "remainedAmt")
    private String remainedAmt;

}
