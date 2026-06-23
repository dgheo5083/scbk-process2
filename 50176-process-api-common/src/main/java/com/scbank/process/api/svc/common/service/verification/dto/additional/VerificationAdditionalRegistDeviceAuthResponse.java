package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalRegistDeviceAuthResponse", description = "단말기지정서비스 등록 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalRegistDeviceAuthResponse implements IMessageObject {

    @MessageField(id = "dummy", name = "더미", defaultValue = "")
    private String dummy;
}
