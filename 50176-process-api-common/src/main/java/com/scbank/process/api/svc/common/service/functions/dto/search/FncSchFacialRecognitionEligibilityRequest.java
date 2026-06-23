package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtFacialRecognitionEligibilityRequest", description = "안면인식 대상 여부 응답 DTO", type = Type.REQUEST)
public class FncSchFacialRecognitionEligibilityRequest implements IMessageObject {

    @MessageField(id = "bizType", name = "")
    private String bizType;
}
