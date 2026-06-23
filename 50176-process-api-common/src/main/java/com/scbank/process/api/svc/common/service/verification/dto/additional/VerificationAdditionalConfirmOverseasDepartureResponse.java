package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalConfirmOverseasDepartureResponse", description = "추가인증 - 해외출국사실확인 응답 DTO", type = Type.REQUEST)
public class VerificationAdditionalConfirmOverseasDepartureResponse implements IMessageObject {

    @MessageField(id = "resultCode", name = "응답결과코드")
    private String resultCode;

    @MessageField(id = "abroadYn", name = "출국여부코드")
    private String abroadYn;
}
