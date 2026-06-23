package com.scbank.process.api.svc.common.service.support.dto.businesshour;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupBzhGetWorkingDayResponse", type = Type.RESPONSE)
public class SupBzhGetWorkingDayResponse implements IMessageObject {

    @MessageField(id = "yoUSID", name = "이용자번호")
    private String yoUSID;

    @MessageField(id = "yoYOGUHs", name = "요구일수BIT")
    private String yoYOGUHs;

    @MessageField(id = "yoYOGU", name = "요구일수")
    private String yoYOGU;

    @MessageField(id = "yoSILJA", name = "시작일자")
    private String yoSILJA;

    @MessageField(id = "yoEILJA", name = "결과일자")
    private String yoEILJA;

    @MessageField(id = "yoPHGB", name = "평/휴일구분(1:평일,2:휴일(평일),3:휴일(휴일))")
    private String yoPHGB;

}
