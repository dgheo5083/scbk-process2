package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalConfirmOverseasDepartureRequest", description = "추가인증 - 해외출국사실확인 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalConfirmOverseasDepartureRequest implements IMessageObject {

}
