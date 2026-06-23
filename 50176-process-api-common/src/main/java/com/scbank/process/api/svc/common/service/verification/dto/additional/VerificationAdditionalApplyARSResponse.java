package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalApplyARSResponse", description = "추가인증 - ARS 인증 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalApplyARSResponse implements IMessageObject {

    @MessageField(id = "resultCode", name = "resultCode")
    private String resultCode;

    @MessageField(id = "tranId", name = "tranId")
    private String tranId;

}
