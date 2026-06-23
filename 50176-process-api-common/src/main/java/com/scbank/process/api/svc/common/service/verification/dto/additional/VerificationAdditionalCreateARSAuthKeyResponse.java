package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalCreateARSAuthKeyResponse", description = "ARS 인증번호 생성 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalCreateARSAuthKeyResponse implements IMessageObject {

    @MessageField(id = "authNumber", name = "authNumber")
    private String authNumber;

}
