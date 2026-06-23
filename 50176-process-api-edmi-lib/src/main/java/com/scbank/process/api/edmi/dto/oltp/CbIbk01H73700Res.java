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
@IntegrationMessage(id = "CbIbk01H73700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>금융소득원천징수영수증")
public class CbIbk01H73700Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "IncomeName", name = "소득자성명", length = 32, masking = true, maskingType = "03")
    private String IncomeName;

    @MessageField(id = "IncomeEngName", name = "소득자영문성명", length = 30, masking = true, maskingType = "03")
    private String IncomeEngName;

    @MessageField(id = "Birthday", name = "생년월일", length = 8)
    private String Birthday;

    @MessageField(id = "JuMinNo", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String JuMinNo;

    @MessageField(id = "IncomeAddr", name = "소득자주소", length = 172, masking = true, maskingType = "05")
    private String IncomeAddr;

    @MessageField(id = "ForeignerYN", name = "외국인여부명", length = 10)
    private String ForeignerYN;

    @MessageField(id = "CustIncomeGB", name = "고객소득자구분", length = 3)
    private String CustIncomeGB;

    @MessageField(id = "BusNo", name = "법인번호", length = 13, masking = true, maskingType = "01")
    private String BusNo;

    @MessageField(id = "CollectBusNo", name = "징수자-사업자번호", length = 10, masking = true, maskingType = "01")
    private String CollectBusNo;

    @MessageField(id = "CollectName", name = "징수자-대표자명", length = 14, masking = true, maskingType = "03")
    private String CollectName;

    @MessageField(id = "ColBranchNm", name = "징수자-영업점명", length = 22)
    private String ColBranchNm;

    @MessageField(id = "ColBranchAddr", name = "징수자-지점주소", length = 172, masking = true, maskingType = "05")
    private String ColBranchAddr;

    @MessageField(id = "SMGBNm", name = "실명구분명", length = 10)
    private String SMGBNm;

    @MessageField(id = "GJuGBNm", name = "거주자구분명", length = 12)
    private String GJuGBNm;

    @MessageField(id = "TotalPage", name = "총요청Page", length = 3)
    private Integer TotalPage;

    @MessageField(id = "HPage", name = "현재Page", length = 3)
    private Integer HPage;

    @MessageField(id = "ReqPage", name = "요청할 다음Page", length = 3)
    private Integer ReqPage;

    @MessageField(id = "RCount", name = "거래명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "FinPayReceiptData", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H73700Res/RCount")
    private List<FinPayReceiptData> FinPayReceiptData;

    @Getter
    @Setter
    public static class FinPayReceiptData implements IMessageObject {
        @MessageField(id = "PayDate", name = "지급일자", length = 8)
        private Integer PayDate;

        @MessageField(id = "JNSYM", name = "귀속년월", length = 6)
        private Integer JNSYM;

        @MessageField(id = "IncomeGBName", name = "소득구분명", length = 8)
        private String IncomeGBName;

        @MessageField(id = "FinKindNm", name = "금융상품종류", length = 14)
        private String FinKindNm;

        @MessageField(id = "FinKindCode", name = "금융상품코드", length = 3)
        private String FinKindCode;

        @MessageField(id = "BondIJGB", name = "채권이자구분", length = 2)
        private String BondIJGB;

        @MessageField(id = "GRCode", name = "과세구분", length = 3)
        private String GRCode;

        @MessageField(id = "FIncomeAmt", name = "금융소득금액", length = 15)
        private BigDecimal FIncomeAmt;

        @MessageField(id = "IncomeSDate", name = "소득발생초일", length = 8)
        private String IncomeSDate;

        @MessageField(id = "IncomeEDate", name = "소득발생말일", length = 8)
        private String IncomeEDate;

        @MessageField(id = "ImcomeTaxRate", name = "소득세율", length = 7)
        private Integer ImcomeTaxRate;

        @MessageField(id = "IncomeAmt", name = "소득세금액", length = 15)
        private BigDecimal IncomeAmt;

        @MessageField(id = "CorpTaxAmt", name = "법인세금액", length = 15)
        private BigDecimal CorpTaxAmt;

        @MessageField(id = "JMTaxAmt", name = "주민세금액", length = 15)
        private BigDecimal JMTaxAmt;

        @MessageField(id = "FarmTaxAmt", name = "농특세금액", length = 15)
        private BigDecimal FarmTaxAmt;

        @MessageField(id = "TaxSumAmt", name = "세금합계", length = 15)
        private BigDecimal TaxSumAmt;

        @MessageField(id = "YOKWASS", name = "과세구분상세코드", length = 3)
        private String YOKWASS;

        @MessageField(id = "YOSDTY", name = "소득종류상세코드", length = 2)
        private String YOSDTY;

        @MessageField(id = "YOJSTR", name = "조세특례상세코드", length = 2)
        private String YOJSTR;

        @MessageField(id = "YOSPCDS", name = "금융상품상세코드", length = 3)
        private String YOSPCDS;

        @MessageField(id = "YOSTYB", name = "신탁이익여부코드", length = 1)
        private String YOSTYB;

    }

}