package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H44K01Req", type = Type.REQUEST, description = "신탁자산명 조회 요청 전문", captureSystem = "OLTP")
public class CbIbk01H44K01Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YI_UPGB", name = "업무구분", length = 1)
    private String YI_UPGB;

    @MessageField(id = "YI_GJNO", name = "계좌번호", length = 11)
    private String YI_GJNO;

    @MessageField(id = "YI_DUMMY", name = "DUMMY", length = 30)
    private String YI_DUMMY;

}
