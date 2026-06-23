package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H865Req", type = Type.REQUEST, description = "1회용 비밀번호 검증 요청 전문")
public class Ti1ibk01H865Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TranGubun", name = "거래구분", length = 1)
    private String TranGubun;

    @MessageField(id = "OTPassword", name = "일회용비밀번호", length = 8)
    private String OTPassword;

}
