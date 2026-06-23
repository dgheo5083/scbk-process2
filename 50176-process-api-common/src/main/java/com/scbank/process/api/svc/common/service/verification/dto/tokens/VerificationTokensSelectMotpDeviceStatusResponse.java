package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensSelectMotpDeviceStatusResponse", description = "MOTP 기기상태조회 응답 DTO", type = Type.RESPONSE)
public class VerificationTokensSelectMotpDeviceStatusResponse implements IMessageObject {

    @MessageField(id = "motpDeviceStatus", name = "MOTP 기기상태", defaultValue = "00")
    private String motpDeviceStatus;
}
