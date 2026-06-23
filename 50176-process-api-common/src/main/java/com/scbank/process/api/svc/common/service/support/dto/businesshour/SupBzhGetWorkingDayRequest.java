package com.scbank.process.api.svc.common.service.support.dto.businesshour;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupBzhGetWorkingDayRequest", type = Type.REQUEST)
public class SupBzhGetWorkingDayRequest implements IMessageObject {

    @MessageField(id = "yiYOGUHs", name = "요구일수BIT")
    private String yiYOGUHs;

    @MessageField(id = "yiYOGU", name = "요구일수")
    private String yiYOGU;

    @MessageField(id = "yiSILJA", name = "시작일자")
    private String yiSILJA;
}
