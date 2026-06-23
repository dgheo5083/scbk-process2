package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciTm1517020009Res", type = Type.RESPONSE, description = "급여통장등록 응답부")
public class MciTm1517020009Res implements IMessageObject {

}
