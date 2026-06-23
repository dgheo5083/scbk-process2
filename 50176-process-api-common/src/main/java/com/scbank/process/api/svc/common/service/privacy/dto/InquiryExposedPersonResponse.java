package com.scbank.process.api.svc.common.service.privacy.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "InquiryExposedPersonResponse", type = Type.RESPONSE)
public class InquiryExposedPersonResponse implements IMessageObject {

    @MessageField(id = "resCode", name = "응답코드")
    private String resCode;

    @MessageField(id = "yoLrsotGb", name = "개인정보노출자코드")
    private String yoLrsotGb;

}