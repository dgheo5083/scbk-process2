package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H00400Req", type = Type.REQUEST, captureSystem = "OLTP", description = "로그아웃 요청 전문")
public class CbIbk01H00400Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "FinalState", name = "이용등급", length = 1)
    private String FinalState;
}
