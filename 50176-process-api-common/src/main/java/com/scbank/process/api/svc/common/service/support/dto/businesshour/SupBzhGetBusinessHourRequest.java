package com.scbank.process.api.svc.common.service.support.dto.businesshour;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupBzhGetBusinessHourRequest", type = Type.REQUEST)
public class SupBzhGetBusinessHourRequest implements IMessageObject {

}
