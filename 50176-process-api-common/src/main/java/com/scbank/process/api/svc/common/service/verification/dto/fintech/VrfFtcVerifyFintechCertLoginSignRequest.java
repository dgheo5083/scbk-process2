package com.scbank.process.api.svc.common.service.verification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFtcVerifyFintechCertLoginSignRequest", type = Type.REQUEST)
public class VrfFtcVerifyFintechCertLoginSignRequest implements IMessageObject {

    @MessageField(id = "vpcg", name = "서명 데이터")
    private String vpcg;

    @MessageField(id = "plainText", name = "원본 전자서명 데이터")
    private String plainText;

}