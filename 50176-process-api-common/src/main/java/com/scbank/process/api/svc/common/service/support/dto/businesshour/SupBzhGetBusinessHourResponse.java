package com.scbank.process.api.svc.common.service.support.dto.businesshour;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.common.dao.dto.BusinessHourResult;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupBzhGetBusinessHourResponse", type = Type.RESPONSE)
public class SupBzhGetBusinessHourResponse implements IMessageObject {

    @MessageField(id = "businessHourResultList", name = "이용시간안내 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<BusinessHourResult> businessHourResultList;

}
