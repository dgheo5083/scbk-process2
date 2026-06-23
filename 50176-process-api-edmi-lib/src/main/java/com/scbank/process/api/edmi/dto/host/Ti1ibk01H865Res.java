package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H865Res", type = Type.REQUEST, description = "1회용 비밀번호 검증 응답 전문")
public class Ti1ibk01H865Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "OTPStatus", name = "비밀번호상태", length = 1)
    private String OTPStatus;

    @MessageField(id = "OTPErrorCnt", name = "에러카운트", length = 1)
    private String OTPErrorCnt;

    @MessageField(id = "OTPRegisterDate", name = "등록일", length = 8)
    private String OTPRegisterDate;

    @MessageField(id = "OTPErrorDate", name = "에러발생일", length = 8)
    private String OTPErrorDate;

}
