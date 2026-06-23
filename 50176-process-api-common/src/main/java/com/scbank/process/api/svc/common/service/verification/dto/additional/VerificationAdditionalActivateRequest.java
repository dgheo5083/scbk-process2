package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalActivateRequest", description = "추가인증 - 초기화 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalActivateRequest implements IMessageObject {

    @MessageField(id = "additionalType", name = "인증구분(A:간편인증, B:SMS명의인증, D:ARS인증, F:해외출국여부조회)", defaultValue = "A:B")
    private String additionalType;

    @MessageField(id = "transType", name = "거래구분")
    private String transType;
}
