package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalValidateSimpleRequest", description = "추가인증 - 간편인증 등록검증 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalValidateSimpleRequest implements IMessageObject {

    @MessageField(id = "telNo", name = "전화번호")
    private String telNo;

    @MessageField(id = "moNumber", name = "고객센터?")
    private String moNumber;

}
