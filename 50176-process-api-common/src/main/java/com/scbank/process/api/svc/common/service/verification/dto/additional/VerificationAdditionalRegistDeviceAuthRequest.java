package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalRegistDeviceAuthRequest", description = "단말기지정서비스 등록 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalRegistDeviceAuthRequest implements IMessageObject {

    @MessageField(id = "allowOtherDeviceYn", name = "미지정 단말기 거래허용 여부", defaultValue = "N")
    private String allowOtherDeviceYn;

    @MessageField(id = "deviceAlias", name = "기기 별명")
    private String deviceAlias;

    @MessageField(id = "authTelNo", name = "인증전화번호")
    private String authTelNo;
}
