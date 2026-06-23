package com.scbank.process.api.svc.common.service.privacy.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "InquiryExposedPersonRequest", type = Type.REQUEST)
public class InquiryExposedPersonRequest implements IMessageObject {

    // secureData
    @MessageField(id = "perBusNo", name = "고객식별번호(주민등록번호)")
    private String perBusNo;

}