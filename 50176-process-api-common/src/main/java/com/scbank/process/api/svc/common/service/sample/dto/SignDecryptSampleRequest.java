package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.svc.common.dao.dto.SignInfoParameter;

import lombok.Data;

@Data
@IntegrationMessage(id = "SignDecryptSampleRequest", type = Type.REQUEST)
public class SignDecryptSampleRequest implements IMessageObject {

    @MessageField(id = "paramType", name = "조회조건(1: 서명db조회조건 / 2: 암호화된문자열)", example = "1(서명db조회조건)/2(암호화된문자열)")
    private String paramType;

    @MessageField(id = "selectDBCondition", name = "db조회조건", example = "")
    private SignInfoParameter selectDBCondition;

    @MessageField(id = "encryptedSignString", name = "암호화된서명문자열", example = "")
    private String encryptedSignString;

}
