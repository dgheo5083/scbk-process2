package com.scbank.process.api.svc.common.service.verification.dto.additional;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalGetDeviceAuthResponse", description = "단말기지정서비스 등록정보 조회", type = Type.RESPONSE)
public class VerificationAdditionalGetDeviceAuthResponse implements IMessageObject {

    @MessageField(id = "macAddress", name = "MAC Address")
    private String macAddress;

    @MessageField(id = "maskMacAddress", name = "마스킹 MAC Address")
    private String maskMacAddress;

    @MessageField(id = "deviceCount", name = "단말기 등록건수")
    private int deviceCount;

    @MessageField(id = "deviceAuthList", name = "단말기 지정서비스 등록정보 목록")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<DeviceAuthInfo> deviceAuthList;
}
