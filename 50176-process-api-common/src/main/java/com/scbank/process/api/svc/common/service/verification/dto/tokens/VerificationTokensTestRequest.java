package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensTestRequest", description = "보안매체 - 테스트 DTO", type = Type.REQUEST)
public class VerificationTokensTestRequest implements IMessageObject {

    @MessageField(id = "dummy", name = "dummy", defaultValue = "181818")
    private String dummy;
}
