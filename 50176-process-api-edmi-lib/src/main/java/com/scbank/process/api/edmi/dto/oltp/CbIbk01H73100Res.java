package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H73100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>연말정산증명서-개인연금")
public class CbIbk01H73100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "Name", name = "고객명", length = 32, masking = true, maskingType = "03")
    private String Name;

    @MessageField(id = "JuMinNo", name = "주민번호", length = 14, masking = true, maskingType = "01")
    private String JuMinNo;

    @MessageField(id = "OutAcctNum", name = "출력계좌번호", length = 13, masking = true, maskingType = "02")
    private String OutAcctNum;

    @MessageField(id = "StartDate", name = "신규일자", length = 8)
    private String StartDate;

    @MessageField(id = "EndDate", name = "만기일자", length = 8)
    private String EndDate;

    @MessageField(id = "address", name = "주소", length = 64, masking = true, maskingType = "05")
    private String address;

    @MessageField(id = "Year", name = "해당년", length = 4)
    private String Year;

    @MessageField(id = "TotalSum", name = "합계금액", length = 11)
    private BigDecimal TotalSum;

    @MessageField(id = "Makedate", name = "조작일시", length = 8)
    private String Makedate;

    @MessageField(id = "BranchName", name = "점명", length = 22)
    private String BranchName;

    @MessageField(id = "PageNo", name = "출력화면정보(1연금저축 2개인연금)", length = 1)
    private String PageNo;

    @MessageField(id = "FundNm", name = "펀드명", length = 52)
    private String FundNm;

    @MessageField(id = "YearAmt1", name = "세제지원1년차", length = 13)
    private BigDecimal YearAmt1;

    @MessageField(id = "YearAmt2", name = "세제지원2년차", length = 13)
    private BigDecimal YearAmt2;

    @MessageField(id = "YearAmt3", name = "세제지원3년차", length = 13)
    private BigDecimal YearAmt3;

    @MessageField(id = "GJDS", name = "소득공제대상금액", length = 13)
    private BigDecimal GJDS;

    @MessageField(id = "YSsequence", name = "거래회차", length = 3)
    private String YSsequence;

    @MessageField(id = "RCount", name = "거래명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "PersonalData", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H73100Res/RCount")
    private List<PersonalData> PersonalData;

    @Getter
    @Setter
    // 반복부
    public static class PersonalData implements IMessageObject {
        @MessageField(id = "JRdate", name = "적립일", length = 8)
        private String JRdate;

        @MessageField(id = "JRSum", name = "적립금액", length = 11)
        private BigDecimal JRSum;

        @MessageField(id = "GJWNO2", name = "계좌번호/세제비고", length = 11, masking = true, maskingType = "02")
        private String GJWNO2;

    }
}