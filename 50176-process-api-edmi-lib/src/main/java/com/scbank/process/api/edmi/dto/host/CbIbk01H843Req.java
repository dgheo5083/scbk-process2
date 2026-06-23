package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H843Req", type = Type.REQUEST, captureSystem = "OLTP", description = "CI조회 요청 전문")
public class CbIbk01H843Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIIPGB", name = "입력구분(1:이용자번호, 2:CI번호, 3:주민번호)", length = 1)
    private String YIIPGB;

    @MessageField(id = "YICINO", name = "CI번호", length = 88)
    private String YICINO;

    @MessageField(id = "YIJUMIN", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YIJUMIN;
}