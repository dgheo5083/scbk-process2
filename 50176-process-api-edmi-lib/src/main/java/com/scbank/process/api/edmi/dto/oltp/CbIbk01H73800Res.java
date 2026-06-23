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
@IntegrationMessage(id = "CbIbk01H73800Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>사업소득원천징수영수증")
public class CbIbk01H73800Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "IncomeSHName", name = "소득자-상호명", length = 32)
    private String IncomeSHName;

    @MessageField(id = "IncomeBusNo", name = "소득자-사업자번호", length = 10, masking = true, maskingType = "01")
    private String IncomeBusNo;

    @MessageField(id = "IncomeBusAddr", name = "소득자-사업장소재지", length = 52, masking = true, maskingType = "05")
    private String IncomeBusAddr;

    @MessageField(id = "IncomeName", name = "소득자성명", length = 32, masking = true, maskingType = "03")
    private String IncomeName;

    @MessageField(id = "JuMinNo", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String JuMinNo;

    @MessageField(id = "IncomeCusAddr", name = "소득자-고객주소", length = 172, masking = true, maskingType = "05")
    private String IncomeCusAddr;

    @MessageField(id = "GJuGBNm", name = "거주자구분명", length = 12)
    private String GJuGBNm;

    @MessageField(id = "ForeignerYN", name = "외국인여부명", length = 10)
    private String ForeignerYN;

    @MessageField(id = "BusNo", name = "법인번호", length = 13, masking = true, maskingType = "01")
    private String BusNo;

    @MessageField(id = "IncomeYear", name = "소득귀속년도", length = 4)
    private String IncomeYear;

    @MessageField(id = "ColBranchNm", name = "징수자-영업점명", length = 22)
    private String ColBranchNm;

    @MessageField(id = "CollectBusNo", name = "징수자-사업자번호", length = 10, masking = true, maskingType = "01")
    private String CollectBusNo;

    @MessageField(id = "CollectName", name = "징수자-대표자명", length = 14, masking = true, maskingType = "03")
    private String CollectName;

    @MessageField(id = "ColBranchAddr", name = "징수자-지점주소", length = 172, masking = true, maskingType = "05")
    private String ColBranchAddr;

    @MessageField(id = "TypeGB", name = "업종구분", length = 2)
    private String TypeGB;

    @MessageField(id = "TotalPage", name = "총요청Page", length = 3)
    private Integer TotalPage;

    @MessageField(id = "HPage", name = "현재Page", length = 3)
    private Integer HPage;

    @MessageField(id = "ReqPage", name = "요청할 다음Page", length = 3)
    private Integer ReqPage;

    @MessageField(id = "RCount", name = "거래명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "BusIncPayRecData", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H73800Res/RCount")
    private List<BusIncPayRecData> BusIncPayRecData;

    @Getter
    @Setter
    public static class BusIncPayRecData implements IMessageObject {
        @MessageField(id = "PayDate", name = "지급일자", length = 8)
        private Integer PayDate;

        @MessageField(id = "CollectYM", name = "징수년월", length = 6)
        private Integer CollectYM;

        @MessageField(id = "TotPayAmt", name = "총지급금액", length = 15)
        private BigDecimal TotPayAmt;

        @MessageField(id = "EtcPayAmt", name = "기타지급금액", length = 15)
        private BigDecimal EtcPayAmt;

        @MessageField(id = "FIncomeAmt", name = "금융소득금액", length = 15)
        private BigDecimal FIncomeAmt;

        @MessageField(id = "ImcomeTaxRate", name = "소득세율", length = 7)
        private Integer ImcomeTaxRate;

        @MessageField(id = "IncomeAmt", name = "소득세금액", length = 15)
        private BigDecimal IncomeAmt;

        @MessageField(id = "IncomeAmt2", name = "소득세금액2", length = 15)
        private BigDecimal IncomeAmt2;

        @MessageField(id = "JMTaxAmt", name = "주민세금액", length = 15)
        private BigDecimal JMTaxAmt;

        @MessageField(id = "FarmTaxAmt", name = "농특세금액", length = 15)
        private BigDecimal FarmTaxAmt;

        @MessageField(id = "TaxSumAmt", name = "세금합계", length = 15)
        private BigDecimal TaxSumAmt;

    }
}