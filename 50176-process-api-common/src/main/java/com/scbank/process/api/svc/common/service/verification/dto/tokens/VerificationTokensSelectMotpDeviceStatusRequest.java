package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensSelectMotpDeviceStatusRequest", description = "MOTP 기기상태조회 요청 DTO", type = Type.REQUEST)
public class VerificationTokensSelectMotpDeviceStatusRequest implements IMessageObject {

    @MessageField(id = "deviceId", name = "디바이스 ID", defaultValue = "")
    private String deviceId;
}
