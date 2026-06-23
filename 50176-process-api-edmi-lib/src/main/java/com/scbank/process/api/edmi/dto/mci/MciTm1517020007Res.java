package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciTm1517020007Res", type = Type.RESPONSE, description = "급여통장해지 인증 응답부")
public class MciTm1517020007Res implements IMessageObject {

}
