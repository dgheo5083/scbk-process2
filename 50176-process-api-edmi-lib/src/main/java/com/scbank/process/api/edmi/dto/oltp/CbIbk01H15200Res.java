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
@IntegrationMessage(id = "CbIbk01H15200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "대출이자납부 응답부")
public class CbIbk01H15200Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "CustName", name = "고객명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "TrxBrnchNum", name = "거래점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer TrxBrnchNum;

    @MessageField(id = "TrxBrnchName", name = "거래점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TrxBrnchName;

    @MessageField(id = "AcctGamok", name = "계정과목", length = 34, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctGamok;

    @MessageField(id = "RepayType", name = "상환방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RepayType;

    @MessageField(id = "RepayTypeH", name = "상환방법2", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RepayTypeH;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String AcctNum;

    @MessageField(id = "LoanPrinBal", name = "대출잔액(원금)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LoanPrinBal;

    @MessageField(id = "LoanRate", name = "대출이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer LoanRate;

    @MessageField(id = "YJRateSign", name = "약정이자부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YJRateSign;

    @MessageField(id = "YJRate", name = "약정이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YJRate;

    @MessageField(id = "YCRate", name = "연체이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YCRate;

    @MessageField(id = "RateTotalAmtSign", name = "이자합계부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RateTotalAmtSign;

    @MessageField(id = "RateTotalAmt", name = "이자합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal RateTotalAmt;

    @MessageField(id = "RateStartDate", name = "이자시작일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RateStartDate;

    @MessageField(id = "RateEndDate", name = "이자종료일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RateEndDate;

    @MessageField(id = "NextInterDate", name = "다음이자일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NextInterDate;

    @MessageField(id = "YJDueDate", name = "약정기일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YJDueDate;

    @MessageField(id = "DelayYN", name = "연체여부", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DelayYN;

    @MessageField(id = "AutoDrawYN", name = "자동이체신청여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AutoDrawYN;

    @MessageField(id = "AutoDrawDate", name = "자동이체출금일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AutoDrawDate;

    @MessageField(id = "AutoDrawAcctNum", name = "자동이체계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String AutoDrawAcctNum;

    @MessageField(id = "LoanExecDate", name = "대출일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LoanExecDate;

    @MessageField(id = "InstallAmt", name = "원리균등상환액", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InstallAmt;

    @MessageField(id = "TotalRepayFreq", name = "상환총예정횟수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer TotalRepayFreq;

    @MessageField(id = "RepayedFreq", name = "상환완료횟수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RepayedFreq;

    @MessageField(id = "OverDue", name = "연체금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal OverDue;

    @MessageField(id = "FullPay", name = "완제대상금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FullPay;

    @MessageField(id = "FullPayKind", name = "완제여부", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FullPayKind;

    @MessageField(id = "YeiJungIlIJa", name = "납부예정일자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YeiJungIlIJa;

    @MessageField(id = "DelayDt", name = "연체일수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DelayDt;

    @MessageField(id = "YOPIYUL", name = "기준금리", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOPIYUL;

    @MessageField(id = "YOGAGAMSign", name = "가산금리부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGAGAMSign;

    @MessageField(id = "YOGAGAM", name = "가산금리", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGAGAM;

    @MessageField(id = "YOIYGNM", name = "이율구분명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIYGNM;

    @MessageField(id = "YOOUTGB", name = "이율출력선택", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOUTGB;

}