package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H929Req", type = Type.REQUEST, captureSystem = "OLTP", description = "개인사업자 조회 및 검증 요청 전문")
public class Ti1ibk01H929Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIMSSU", name = "요구명세수", length = 5)
    private String YIMSSU;

    @MessageField(id = "YIJHGB", name = "조회구분", length = 1)
    private String YIJHGB;

    @MessageField(id = "RegNo", name = "주민등록번호", length = 13)
    private String RegNo;

    @MessageField(id = "SaupjaNo", name = "사업자번호", length = 10)
    private String SaupjaNo;

    @MessageField(id = "YICONT", name = "연속거래중", length = 1)
    private String YICONT;

    @MessageField(id = "YIYSANO", name = "연속사업자번호", length = 10)
    private String YIYSANO;

    @MessageField(id = "YIYCIF", name = "연속CIF", length = 13)
    private String YIYCIF;
}
