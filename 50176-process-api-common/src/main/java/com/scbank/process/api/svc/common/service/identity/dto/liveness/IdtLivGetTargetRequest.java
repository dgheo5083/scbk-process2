package com.scbank.process.api.svc.common.service.identity.dto.liveness;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtLivGetLivenessTargetRequest", description = "실명확인 안면인식 대상여부 요청 DTO", type = Type.REQUEST)
public class IdtLivGetTargetRequest implements IMessageObject {

    @MessageField(id = "bizType", name = "업무구분", example = "CASA")
    private String bizType;
}
