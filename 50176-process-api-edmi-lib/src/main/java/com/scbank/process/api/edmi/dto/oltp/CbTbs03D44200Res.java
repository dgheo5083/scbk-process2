package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03D44200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드 환매신청 예비/본거래")
public class CbTbs03D44200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "CustName", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "CloseAcctNum", name = "", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CloseAcctNum;

    @MessageField(id = "ReceiptDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ReceiptDate;

    @MessageField(id = "IncomeGubun", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IncomeGubun;

    @MessageField(id = "TranGubun", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "CloseAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal CloseAmt;

    @MessageField(id = "CloseAcctSu", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal CloseAcctSu;

    @MessageField(id = "CGAcctGubun", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CGAcctGubun;

    @MessageField(id = "CGAcctNum", name = "", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CGAcctNum;

    @MessageField(id = "IncomeNameH", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IncomeNameH;

    @MessageField(id = "InterestAmtSign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InterestAmtSign;

    @MessageField(id = "InterestAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal InterestAmt;

    @MessageField(id = "TaxProfitSign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TaxProfitSign;

    @MessageField(id = "TaxProfit", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TaxProfit;

    @MessageField(id = "SumTax", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SumTax;

    @MessageField(id = "IncomeTax", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal IncomeTax;

    @MessageField(id = "ResidualTax", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ResidualTax;

    @MessageField(id = "FarmTax", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FarmTax;

    @MessageField(id = "EduTax", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal EduTax;

    @MessageField(id = "FeeAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FeeAmt;

    @MessageField(id = "SumIncomeAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SumIncomeAmt;

    @MessageField(id = "FundCode", name = "", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundCode;

    @MessageField(id = "FundName", name = "", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundName;

    @MessageField(id = "StandAdaptedDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer StandAdaptedDate;

    @MessageField(id = "TranAdaptedDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer TranAdaptedDate;

    @MessageField(id = "YOJILYB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJILYB;

    @MessageField(id = "YOOHMGB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOHMGB;

    @MessageField(id = "YOCRDGB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCRDGB;

}