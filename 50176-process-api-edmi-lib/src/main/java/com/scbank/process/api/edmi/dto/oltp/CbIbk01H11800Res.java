package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H11800Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "해지예상조회 응답부")
public class CbIbk01H11800Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "ClosePart", name = "해약구분", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClosePart;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "CloseExpectDate", name = "해약예정일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CloseExpectDate;

    @MessageField(id = "PrinTotAmt", name = "원장총액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal PrinTotAmt;

    @MessageField(id = "TotInterSign", name = "총이자Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TotInterSign;

    @MessageField(id = "TotInter", name = "총이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotInter;

    @MessageField(id = "TotTax", name = "총세금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotTax;

    @MessageField(id = "AColTaxAmt", name = "환수세금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AColTaxAmt;

    @MessageField(id = "AdepInter", name = "환입이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AdepInter;

    @MessageField(id = "ApayTaxAmt", name = "환출세금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ApayTaxAmt;

    @MessageField(id = "InterSumTotSign", name = "이자합계Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InterSumTotSign;

    @MessageField(id = "InterSumTot", name = "이자합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal InterSumTot;

    @MessageField(id = "JSTaxAmtSign", name = "정산세액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JSTaxAmtSign;

    @MessageField(id = "JSTaxAmt", name = "정산세액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal JSTaxAmt;

    @MessageField(id = "TaxDeductAfterInterSign", name = "세금공제후이자Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TaxDeductAfterInterSign;

    @MessageField(id = "TaxDeductAfterInter", name = "세금공제후이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TaxDeductAfterInter;

    @MessageField(id = "DeductPayAmtSign", name = "차감지급액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DeductPayAmtSign;

    @MessageField(id = "DeductPayAmt", name = "차감지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal DeductPayAmt;

    @MessageField(id = "ItemName", name = "예금종류", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ItemName;

    @MessageField(id = "CustName", name = "고객명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "NewDate", name = "신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NewDate;

    @MessageField(id = "ExpiryDate", name = "만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ExpiryDate;

    @MessageField(id = "PayTime", name = "납입회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PayTime;

    @MessageField(id = "MonthPayAmt", name = "월부금", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal MonthPayAmt;

    @MessageField(id = "GigumAmt", name = "기금출연액", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GigumAmt;

    @MessageField(id = "Gigumrate", name = "기금출연율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Gigumrate;

    @MessageField(id = "TerminationFee", name = "중도해지수수료", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TerminationFee;

    @MessageField(id = "YOGIHAP", name = "기여금지급누계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGIHAP;

    @MessageField(id = "YOGIIJA", name = "기여금이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGIIJA;

    @MessageField(id = "YOGITOT", name = "기여금누계이자합", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGITOT;

    @MessageField(id = "YOZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZONG;

    @MessageField(id = "YOPNTGB", name = "1:M POINT, 3:SKYPASS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPNTGB;

    @MessageField(id = "YODUMMY", name = "", length = 97, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
