package com.scbank.process.api.svc.common.service.verification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFtcInquiryCiInfoRequest", type = Type.REQUEST)
public class VrfFtcInquiryCiInfoRequest implements IMessageObject {
}