package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "AthMgtCustInfoLeakerTranBlockRequest", type = Type.REQUEST)
public class AthMgtCustInfoLeakerTranBlockRequest implements IMessageObject {

}