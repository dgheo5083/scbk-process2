package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "DeviceAuthInfo", description = "단말기 지정서비스 등록정보", type = Type.RESPONSE)
public class DeviceAuthInfo implements IMessageObject {

    @MessageField(id = "deviceAlias", name = "단말기 별명")
    private String deviceAlias;

    @MessageField(id = "macAddr", name = "macAddr")
    private String macAddr;

}
