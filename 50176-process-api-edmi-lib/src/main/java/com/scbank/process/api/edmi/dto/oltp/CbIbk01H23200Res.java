package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H23200Res", type = Type.REQUEST, captureSystem = "OLTP", description = "대출원금/원리금 상환 응답부")
public class CbIbk01H23200Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "JsNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JsNum;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "HgSName", name = "의뢰인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String HgSName;

    @MessageField(id = "JgCode", name = "지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JgCode;

    @MessageField(id = "LoanAcctNum", name = "대출계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String LoanAcctNum;

    @MessageField(id = "CjName", name = "수취인명(차주명)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CjName;

    @MessageField(id = "LoanPart", name = "대출구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LoanPart;

    @MessageField(id = "ShWAmt", name = "상환원금(상환월)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ShWAmt;

    @MessageField(id = "AmtSign1", name = "출금후잔액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign1;

    @MessageField(id = "AfterBal", name = "출금후잔액", length = 13, align = AlignType.RIGHT, padding = "0x30")
    private BigDecimal AfterBal;

    @MessageField(id = "ShAmtTot", name = "상환금액합계", length = 13, align = AlignType.RIGHT, padding = "0x30")
    private BigDecimal ShAmtTot;

    @MessageField(id = "MonthAmt", name = "월부금", length = 13, align = AlignType.RIGHT, padding = "0x30")
    private BigDecimal MonthAmt;

    @MessageField(id = "LoanBal", name = "대출잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LoanBal;

    @MessageField(id = "IntSign1", name = "약정이자 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IntSign1;

    @MessageField(id = "YjInt", name = "약정이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YjInt;

    @MessageField(id = "YcInt", name = "연체이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YcInt;

    @MessageField(id = "TotInt", name = "이자합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotInt;

    @MessageField(id = "IntSDate", name = "이자시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer IntSDate;

    @MessageField(id = "IntEDate", name = "이자종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer IntEDate;

    @MessageField(id = "NextIntDate", name = "다음이자일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NextIntDate;

    @MessageField(id = "JdShSsyo", name = "중도상환 수수료", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal JdShSsyo;

    @MessageField(id = "YjDate", name = "약정기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YjDate;

    @MessageField(id = "LastMonth", name = "최종납부월", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer LastMonth;

    @MessageField(id = "AmtSign2", name = "상환후잔액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign2;

    @MessageField(id = "ShAfterBal", name = "상환후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ShAfterBal;

    @MessageField(id = "ShAfterHC", name = "상환후회차", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ShAfterHC;

    @MessageField(id = "Rate", name = "이율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer Rate;

    @MessageField(id = "AmtSign3", name = "환출이자 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign3;

    @MessageField(id = "HcInt", name = "환출이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal HcInt;

    @MessageField(id = "ShYjHC", name = "상환예정회차", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ShYjHC;

    @MessageField(id = "ShYrHC", name = "상환완료회차", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ShYrHC;

    @MessageField(id = "YcYN", name = "연체유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YcYN;

    @MessageField(id = "RealLoanRepayAmt", name = "실상환금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal RealLoanRepayAmt;

    @MessageField(id = "PointLoanRepayAmy", name = "포인트상환금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal PointLoanRepayAmy;

    @MessageField(id = "YORVKGB", name = "대출계약철회대상 Y(73아님), 1:전액상환(73), 2:일부상환(73)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORVKGB;

    @MessageField(id = "YOLSSYN", name = "강제/당연기한상실Y", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOLSSYN;

    @MessageField(id = "HsamtSign", name = "원금제외일반상환액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HsamtSign;

    @MessageField(id = "YOHSAMT", name = "원금제외일반상환액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHSAMT;

    @MessageField(id = "RvkakSign", name = "철회상환액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RvkakSign;

    @MessageField(id = "YORVKAK", name = "철회상환액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YORVKAK;

    @MessageField(id = "YOCNCCD", name = "저당권유지말소동의(Y:대상)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCNCCD;

    @MessageField(id = "YODUMMY", name = "dummy", length = 28, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
