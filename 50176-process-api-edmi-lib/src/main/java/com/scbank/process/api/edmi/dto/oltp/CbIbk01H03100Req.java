package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H03100Req", type = Type.REQUEST, description = "전계좌조회 요청 전문", captureSystem = "OLTP")
public class CbIbk01H03100Req implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ContinueInfo", name = "연속정보", length = 90)
    private String ContinueInfo;

    @MessageField(id = "PrintCount", name = "출력명세수 TOT", length = 3)
    private Integer PrintCount;

}
