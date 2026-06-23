package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalValidateSimpleResponse", description = "추가인증 - 간편인증 등록검증 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalValidateSimpleResponse implements IMessageObject {

    @MessageField(id = "resultYn", name = "응답결과여부", defaultValue = "N")
    private String resultYn;
}
