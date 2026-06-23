package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensSendMotpPushResponse", description = "MOTP Push 발송 응답 DTO", type = Type.RESPONSE)
public class VerificationTokensSendMotpPushResponse implements IMessageObject {

    @MessageField(id = "resultCd", name = "발송결과코드", defaultValue = "00")
    private String resultCd;
}
