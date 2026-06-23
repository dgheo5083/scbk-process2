package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H73100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "증명서>연말정산증명서-개인연금")
public class CbIbk01H73100Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String AcctNum;

    @MessageField(id = "standarddate", name = "발급기준일자", length = 8)
    private String standarddate;

    @MessageField(id = "YSGubun", name = "연속구분", length = 1)
    private String YSGubun;

    @MessageField(id = "YSsequence", name = "거래회차", length = 3)
    private String YSsequence;

    @MessageField(id = "YSTotalSum", name = "합계금액", length = 11)
    private Integer YSTotalSum;

    @MessageField(id = "YIGRGB", name = "거래구분", length = 2)
    private String YIGRGB;

}
