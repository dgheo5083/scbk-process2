package com.scbank.process.api.svc.common.service.verification.dto.tokens;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationTokensCheckAbroadResponse", description = "해외접속 여부 응답 DTO", type = Type.RESPONSE)
public class VerificationTokensCheckAbroadResponse {
	@MessageField(id = "flagIsAbroadYN", name = "해외접속 여부")
    private String flagIsAbroadYN;
}
