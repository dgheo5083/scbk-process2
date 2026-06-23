package com.scbank.process.api.svc.common.service.identity.dto.liveness;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtLivGetLivenessTargetResponse", description = "실명확인 안면인식 대상여부 응답 DTO", type = Type.RESPONSE)
public class IdtLivGetTargetResponse implements IMessageObject {

    @MessageField(id = "livenessTargetYn", name = "안면인식 대상업무 여부(Y:대상, N:미대상)")
    private String livenessTargetYn;
}
