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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01H1A300Res", type = Type.RESPONSE, description = "펀드 입출금명세조회")
public class CbIbk01H1A300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "CustName", name = "성명", length = 32, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "NomiBalance", name = "명목잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NomiBalance;

    @MessageField(id = "AvailBalance", name = "지불가능잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AvailBalance;

    @MessageField(id = "Rcount", name = "명세건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer Rcount;

    @MessageField(id = "ReferStartDay", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDay;

    @MessageField(id = "ReferEndDay", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDay;

    @MessageField(id = "DepItemName", name = "예금과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemName;

    @MessageField(id = "FundCode", name = "펀드번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundCode;

    @MessageField(id = "FundName", name = "펀드명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundName;

    @MessageField(id = "DepItemZong", name = "예금종별", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemZong;

    @MessageField(id = "CurrencyCode", name = "통화코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CurrencyCode;

    @MessageField(id = "CurrencyName", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CurrencyName;

    @MessageField(id = "HungBalance", name = "평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal HungBalance;

    @MessageField(id = "K_Date", name = "일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxNum;

    @MessageField(id = "K_TrxBranch", name = "취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxBranch;

    @MessageField(id = "K_AddCode", name = "증가코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_AddCode;

    @MessageField(id = "DepDetailRecordlim", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H1A300Res/Rcount")
    private List<DepDetailRecordlim> DepDetailRecordlim;

    @Data
    public static class DepDetailRecordlim implements IMessageObject {
        @MessageField(id = "TrxDate", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxDate;

        @MessageField(id = "CriterAmt", name = "기준가", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal CriterAmt;

        @MessageField(id = "TrxCount1", name = "거래좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer TrxCount1;

        @MessageField(id = "DepPayPart", name = "입지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DepPayPart;

        @MessageField(id = "CancelPart", name = "취소구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CancelPart;

        @MessageField(id = "TrxAmtSign", name = "거래금액(입금)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAmtSign;

        @MessageField(id = "TrxAfterBalance", name = "거래후잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAfterBalance;

        @MessageField(id = "Summary", name = "적요(거래내용)", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary;

        @MessageField(id = "TrxBranchNum", name = "취급점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxBranchNum;

        @MessageField(id = "NoneBankbookTrxYN", name = "무통장거래여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String NoneBankbookTrxYN;

        @MessageField(id = "BranchName", name = "지점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BranchName;
    }
}
