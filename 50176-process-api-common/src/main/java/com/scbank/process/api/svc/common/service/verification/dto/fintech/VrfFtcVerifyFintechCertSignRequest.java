package com.scbank.process.api.svc.common.service.verification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFtcVerifyFintechCertSignRequest", type = Type.REQUEST)
public class VrfFtcVerifyFintechCertSignRequest implements IMessageObject {

    @MessageField(id = "actionType", name = "", example = "AUTH")
    private String actionType; // certType

    @MessageField(id = "multiSignYn", name = "", example = "Y, N")
    private String multiSignYn; // isMultiSign (ASIS기준 쓰나?)

    @MessageField(id = "finTechVerifyYn", name = "검증실행여부?", example = "Y, N")
    private String finTechVerifyYn; // finTechVerifyYn (ASIS기준 무조건 Y 추정)

    @MessageField(id = "provider", name = "서명 데이터")
    private String provider;

    @MessageField(id = "vpcg", name = "서명 데이터")
    private String vpcg;

    @MessageField(id = "plainText", name = "원본 전자서명 데이터")
    private String plainText;

}