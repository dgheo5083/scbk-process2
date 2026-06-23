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
@IntegrationMessage(id = "CbIbk01H73600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>원천징수영수증-년도별금융소득정보조회")
public class CbIbk01H73600Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "Name", name = "소득자성명", length = 32, masking = true, maskingType = "03")
    private String Name;

    @MessageField(id = "JuMinNo", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String JuMinNo;

    @MessageField(id = "TotalPage", name = "총요청Page", length = 3)
    private Integer TotalPage;

    @MessageField(id = "HPage", name = "현재Page", length = 3)
    private Integer HPage;

    @MessageField(id = "ReqPage", name = "요청할 다음Page", length = 3)
    private Integer ReqPage;

    @MessageField(id = "RCount", name = "거래명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "YearFinInfoJHData", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H73600Res/RCount")
    private List<YearFinInfoJHData> YearFinInfoJHData;

    @Getter
    @Setter
    public static class YearFinInfoJHData implements IMessageObject {
        @MessageField(id = "CustAcctNo", name = "고객계좌번호", length = 11, masking = true, maskingType = "02")
        private String CustAcctNo;

        @MessageField(id = "AcctGMName", name = "예금과목명", length = 22)
        private String AcctGMName;

        @MessageField(id = "TaxGBName", name = "과세구분명", length = 12)
        private String TaxGBName;

        @MessageField(id = "IncomeGBName", name = "소득구분명", length = 12)
        private String IncomeGBName;

        @MessageField(id = "FIncomeAmt", name = "금융소득금액", length = 15)
        private BigDecimal FIncomeAmt;

        @MessageField(id = "ColIncome", name = "소액부징수소득", length = 15)
        private BigDecimal ColIncome;

        @MessageField(id = "TaxDSAmt", name = "과세대상금액", length = 15)
        private BigDecimal TaxDSAmt;

        @MessageField(id = "ImcomeTaxRate", name = "소득세율", length = 7)
        private Integer ImcomeTaxRate;

        @MessageField(id = "IncomeAmt", name = "소득세금액", length = 15)
        private BigDecimal IncomeAmt;

        @MessageField(id = "JMTaxAmt", name = "주민세금액", length = 15)
        private BigDecimal JMTaxAmt;

        @MessageField(id = "FarmTaxAmt", name = "농특세금액", length = 15)
        private BigDecimal FarmTaxAmt;

        @MessageField(id = "TaxSumAmt", name = "세금합계", length = 15)
        private BigDecimal TaxSumAmt;

        @MessageField(id = "IncomePayDate", name = "소득지급일자", length = 8)
        private String IncomePayDate;

        @MessageField(id = "IncomeSDate", name = "소득발생초일", length = 8)
        private String IncomeSDate;

        @MessageField(id = "IncomeEDate", name = "소득발생말일", length = 8)
        private String IncomeEDate;

        @MessageField(id = "BranchBusNo", name = "지점사업자번호", length = 10)
        private String BranchBusNo;

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