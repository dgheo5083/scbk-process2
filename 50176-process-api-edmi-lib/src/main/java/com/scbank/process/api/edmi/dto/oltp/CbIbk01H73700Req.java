package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H73700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "증명서>금융소득원천징수영수증")
public class CbIbk01H73700Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PerBusNo", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String PerBusNo;

    @MessageField(id = "AcctNo", name = "계좌번호", length = 15, masking = true, maskingType = "02")
    private String AcctNo;

    @MessageField(id = "IncomeGB", name = "소득구분(1금융소득 2사업소득 3사업소득합계)", length = 1)
    private String IncomeGB;

    @MessageField(id = "JHYear", name = "조회년도", length = 4)
    private String JHYear;

    @MessageField(id = "JHStartDate", name = "조회시작일자", length = 8)
    private String JHStartDate;

    @MessageField(id = "JHEndDate", name = "조회종료일자", length = 8)
    private String JHEndDate;

    @MessageField(id = "HPage", name = "현재Page", length = 3)
    private Integer HPage;

    @MessageField(id = "ReqCnt", name = "요구건수", length = 3)
    private Integer ReqCnt;

}
