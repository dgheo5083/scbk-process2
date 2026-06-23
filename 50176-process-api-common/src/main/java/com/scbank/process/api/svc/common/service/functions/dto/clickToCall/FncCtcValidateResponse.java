package com.scbank.process.api.svc.common.service.functions.dto.clickToCall;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncCtcValidateResponse", description = "전화상담 검증 응답", type = Type.RESPONSE)
public class FncCtcValidateResponse implements IMessageObject {

    @MessageField(id = "name", name = "이름", defaultValue = "")
    private String name;

    @MessageField(id = "mobileNum", name = "휴대폰 전화번호", defaultValue = "")
    private String mobileNum;
}
