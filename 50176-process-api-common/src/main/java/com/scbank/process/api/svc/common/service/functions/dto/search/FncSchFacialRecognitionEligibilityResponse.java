package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtFacialRecognitionEligibilityResponse", description = "안면인식 대상 여부 응답 DTO", type = Type.RESPONSE)
public class FncSchFacialRecognitionEligibilityResponse implements IMessageObject {

    @MessageField(id = "faceTargetYn", name = "안면인식 대상업무 여부(Y:대상, N:미대상)")
    private String faceTargetYn;
}
