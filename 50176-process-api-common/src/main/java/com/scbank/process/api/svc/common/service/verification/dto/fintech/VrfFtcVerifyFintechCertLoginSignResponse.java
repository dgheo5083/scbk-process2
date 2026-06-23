package com.scbank.process.api.svc.common.service.verification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFtcVerifyFintechCertLoginSignResponse", type = Type.RESPONSE)
public class VrfFtcVerifyFintechCertLoginSignResponse implements IMessageObject {

    @MessageField(id = "resultCode", name = "")
    private String resultCode;

}