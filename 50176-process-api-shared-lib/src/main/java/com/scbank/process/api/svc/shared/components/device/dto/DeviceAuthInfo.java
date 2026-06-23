package com.scbank.process.api.svc.shared.components.device.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "DeviceAuthInfo", type = Type.RESPONSE, description = "단말기지정서비스 응답")
public class DeviceAuthInfo implements IMessageObject {

    @MessageField(id = "deviceAuthValue", name = "")
    private String deviceAuthValue;

    @MessageField(id = "otherDeviceYn", name = "")
    private String otherDeviceYn;

}
