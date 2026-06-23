package com.scbank.process.api.svc.shared.components.ipinside.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IpinsideHddInfo", type = Type.RESPONSE, description = "IPInside HDD 정보")
public class IpinsideHddInfo implements IMessageObject {

    @MessageField(id = "macAddress", name = "MAC Address")
    private String macAddress;

    @MessageField(id = "hddSerials", name = "HDD Serials")
    private String hddSerials;

}
