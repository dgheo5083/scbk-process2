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
@IntegrationMessage(id = "CbIbk03H03400Res", type = Type.RESPONSE, description = "카드조회 응답부")
public class CbIbk03H03400Res implements IMessageObject {

    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YOBANKCD", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBANKCD;

    @MessageField(id = "YOBCGJNO", name = "", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBCGJNO;

    @MessageField(id = "YOGGIL", name = "결제일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGGIL;

    @MessageField(id = "YOYCIL", name = "최초연체일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYCIL;

    @MessageField(id = "YOWON", name = "결제원금", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOWON;

    @MessageField(id = "YOSUSU", name = "결제수수료", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSUSU;

    @MessageField(id = "YOIJA", name = "결제이자", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOIJA;

    @MessageField(id = "YOTOT", name = "결제합계", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTOT;

    @MessageField(id = "YOBRNO", name = "관리점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOBRNO;

    @MessageField(id = "YOCGIL", name = "청구일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCGIL;

    @MessageField(id = "YOREVGB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOREVGB;

    @MessageField(id = "YOHPNO", name = "", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHPNO;

    @MessageField(id = "YOBSBB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBSBB;

    @MessageField(id = "YOMAIL", name = "", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL;

    @MessageField(id = "YOBSBB2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBSBB2;

    @MessageField(id = "YOSJYB1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSJYB1;

    @MessageField(id = "YOSJYB2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSJYB2;

    @MessageField(id = "YearListRecord", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YearListRecord;

    @MessageField(id = "MonthName1", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName1;

    @MessageField(id = "SOverPerform1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform1;

    @MessageField(id = "OverPerform1", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform1;

    @MessageField(id = "SDivPerform1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform1;

    @MessageField(id = "DivPerform1", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform1;

    @MessageField(id = "SImePerform1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform1;

    @MessageField(id = "ImePerform1", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform1;

    @MessageField(id = "SCashPerform1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform1;

    @MessageField(id = "CashPerform1", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform1;

    @MessageField(id = "SLoanPerform1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform1;

    @MessageField(id = "LoanPerform1", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform1;

    @MessageField(id = "STotPerform1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform1;

    @MessageField(id = "TotPerform1", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform1;

    @MessageField(id = "MonthName2", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName2;

    @MessageField(id = "SOverPerform2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform2;

    @MessageField(id = "OverPerform2", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform2;

    @MessageField(id = "SDivPerform2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform2;

    @MessageField(id = "DivPerform2", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform2;

    @MessageField(id = "SImePerform2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform2;

    @MessageField(id = "ImePerform2", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform2;

    @MessageField(id = "SCashPerform2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform2;

    @MessageField(id = "CashPerform2", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform2;

    @MessageField(id = "SLoanPerform2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform2;

    @MessageField(id = "LoanPerform2", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform2;

    @MessageField(id = "STotPerform2", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform2;

    @MessageField(id = "TotPerform2", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform2;

    @MessageField(id = "MonthName3", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName3;

    @MessageField(id = "SOverPerform3", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform3;

    @MessageField(id = "OverPerform3", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform3;

    @MessageField(id = "SDivPerform3", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform3;

    @MessageField(id = "DivPerform3", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform3;

    @MessageField(id = "SImePerform3", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform3;

    @MessageField(id = "ImePerform3", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform3;

    @MessageField(id = "SCashPerform3", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform3;

    @MessageField(id = "CashPerform3", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform3;

    @MessageField(id = "SLoanPerform3", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform3;

    @MessageField(id = "LoanPerform3", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform3;

    @MessageField(id = "STotPerform3", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform3;

    @MessageField(id = "TotPerform3", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform3;

    @MessageField(id = "MonthName4", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName4;

    @MessageField(id = "SOverPerform4", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform4;

    @MessageField(id = "OverPerform4", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform4;

    @MessageField(id = "SDivPerform4", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform4;

    @MessageField(id = "DivPerform4", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform4;

    @MessageField(id = "SImePerform4", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform4;

    @MessageField(id = "ImePerform4", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform4;

    @MessageField(id = "SCashPerform4", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform4;

    @MessageField(id = "CashPerform4", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform4;

    @MessageField(id = "SLoanPerform4", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform4;

    @MessageField(id = "LoanPerform4", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform4;

    @MessageField(id = "STotPerform4", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform4;

    @MessageField(id = "TotPerform4", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform4;

    @MessageField(id = "MonthName5", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName5;

    @MessageField(id = "SOverPerform5", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform5;

    @MessageField(id = "OverPerform5", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform5;

    @MessageField(id = "SDivPerform5", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform5;

    @MessageField(id = "DivPerform5", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform5;

    @MessageField(id = "SImePerform5", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform5;

    @MessageField(id = "ImePerform5", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform5;

    @MessageField(id = "SCashPerform5", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform5;

    @MessageField(id = "CashPerform5", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform5;

    @MessageField(id = "SLoanPerform5", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform5;

    @MessageField(id = "LoanPerform5", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform5;

    @MessageField(id = "STotPerform5", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform5;

    @MessageField(id = "TotPerform5", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform5;

    @MessageField(id = "MonthName6", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName6;

    @MessageField(id = "SOverPerform6", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform6;

    @MessageField(id = "OverPerform6", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform6;

    @MessageField(id = "SDivPerform6", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform6;

    @MessageField(id = "DivPerform6", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform6;

    @MessageField(id = "SImePerform6", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform6;

    @MessageField(id = "ImePerform6", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform6;

    @MessageField(id = "SCashPerform6", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform6;

    @MessageField(id = "CashPerform6", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform6;

    @MessageField(id = "SLoanPerform6", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform6;

    @MessageField(id = "LoanPerform6", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform6;

    @MessageField(id = "STotPerform6", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform6;

    @MessageField(id = "TotPerform6", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform6;

    @MessageField(id = "MonthName7", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName7;

    @MessageField(id = "SOverPerform7", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform7;

    @MessageField(id = "OverPerform7", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform7;

    @MessageField(id = "SDivPerform7", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform7;

    @MessageField(id = "DivPerform7", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform7;

    @MessageField(id = "SImePerform7", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform7;

    @MessageField(id = "ImePerform7", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform7;

    @MessageField(id = "SCashPerform7", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform7;

    @MessageField(id = "CashPerform7", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform7;

    @MessageField(id = "SLoanPerform7", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform7;

    @MessageField(id = "LoanPerform7", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform7;

    @MessageField(id = "STotPerform7", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform7;

    @MessageField(id = "TotPerform7", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform7;

    @MessageField(id = "MonthName8", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName8;

    @MessageField(id = "SOverPerform8", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform8;

    @MessageField(id = "OverPerform8", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform8;

    @MessageField(id = "SDivPerform8", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform8;

    @MessageField(id = "DivPerform8", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform8;

    @MessageField(id = "SImePerform8", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform8;

    @MessageField(id = "ImePerform8", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform8;

    @MessageField(id = "SCashPerform8", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform8;

    @MessageField(id = "CashPerform8", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform8;

    @MessageField(id = "SLoanPerform8", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform8;

    @MessageField(id = "LoanPerform8", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform8;

    @MessageField(id = "STotPerform8", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform8;

    @MessageField(id = "TotPerform8", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform8;

    @MessageField(id = "MonthName9", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName9;

    @MessageField(id = "SOverPerform9", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform9;

    @MessageField(id = "OverPerform9", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform9;

    @MessageField(id = "SDivPerform9", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform9;

    @MessageField(id = "DivPerform9", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform9;

    @MessageField(id = "SImePerform9", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform9;

    @MessageField(id = "ImePerform9", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform9;

    @MessageField(id = "SCashPerform9", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform9;

    @MessageField(id = "CashPerform9", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform9;

    @MessageField(id = "SLoanPerform9", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform9;

    @MessageField(id = "LoanPerform9", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform9;

    @MessageField(id = "STotPerform9", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform9;

    @MessageField(id = "TotPerform9", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform9;

    @MessageField(id = "MonthName10", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName10;

    @MessageField(id = "SOverPerform10", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform10;

    @MessageField(id = "OverPerform10", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform10;

    @MessageField(id = "SDivPerform10", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform10;

    @MessageField(id = "DivPerform10", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform10;

    @MessageField(id = "SImePerform10", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform10;

    @MessageField(id = "ImePerform10", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform10;

    @MessageField(id = "SCashPerform10", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform10;

    @MessageField(id = "CashPerform10", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform10;

    @MessageField(id = "SLoanPerform10", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform10;

    @MessageField(id = "LoanPerform10", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform10;

    @MessageField(id = "STotPerform10", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform10;

    @MessageField(id = "TotPerform10", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform10;

    @MessageField(id = "MonthName11", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName11;

    @MessageField(id = "SOverPerform11", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform11;

    @MessageField(id = "OverPerform11", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform11;

    @MessageField(id = "SDivPerform11", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform11;

    @MessageField(id = "DivPerform11", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform11;

    @MessageField(id = "SImePerform11", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform11;

    @MessageField(id = "ImePerform11", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform11;

    @MessageField(id = "SCashPerform11", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform11;

    @MessageField(id = "CashPerform11", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform11;

    @MessageField(id = "SLoanPerform11", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform11;

    @MessageField(id = "LoanPerform11", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform11;

    @MessageField(id = "STotPerform11", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform11;

    @MessageField(id = "TotPerform11", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform11;

    @MessageField(id = "MonthName12", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MonthName12;

    @MessageField(id = "SOverPerform12", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOverPerform12;

    @MessageField(id = "OverPerform12", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OverPerform12;

    @MessageField(id = "SDivPerform12", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SDivPerform12;

    @MessageField(id = "DivPerform12", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DivPerform12;

    @MessageField(id = "SImePerform12", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SImePerform12;

    @MessageField(id = "ImePerform12", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ImePerform12;

    @MessageField(id = "SCashPerform12", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SCashPerform12;

    @MessageField(id = "CashPerform12", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CashPerform12;

    @MessageField(id = "SLoanPerform12", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SLoanPerform12;

    @MessageField(id = "LoanPerform12", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoanPerform12;

    @MessageField(id = "STotPerform12", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String STotPerform12;

    @MessageField(id = "TotPerform12", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotPerform12;

    @MessageField(id = "DrawAcctNumListRecord", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DrawAcctNumListRecord;

    @MessageField(id = "REC_01", name = "")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk03H03400Res/DrawAcctNumListRecord")
    private List<REC_01> REC_01;

    @Getter
    @Setter
    public static class REC_01 implements IMessageObject {
        @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctNum;

        @MessageField(id = "CardType", name = "카드종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CardType;

        @MessageField(id = "FamilyType", name = "본인가족구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FamilyType;

        @MessageField(id = "PaymentDate", name = "결제일자", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String PaymentDate;

        @MessageField(id = "OpenDate", name = "개설일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String OpenDate;

        @MessageField(id = "Period", name = "유효기간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String Period;

        @MessageField(id = "CheckCardType", name = "체크카드구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CheckCardType;

        @MessageField(id = "CoOperCardCode", name = "제휴카드코드", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CoOperCardCode;

        @MessageField(id = "DrawAcctName", name = "계좌명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctName;

    }
}