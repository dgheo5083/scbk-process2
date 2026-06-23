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
@IntegrationMessage(id = "CbIbk01H1A301Res", type = Type.RESPONSE, description = "펀드 입출금명세조회")
public class CbIbk01H1A301Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "CustName", name = "성명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "BlankCnt", name = "미기장건수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer BlankCnt;

    @MessageField(id = "NomiBalSign", name = "명목잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NomiBalSign;

    @MessageField(id = "NomiBalance", name = "명목잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NomiBalance;

    @MessageField(id = "UnFundBalSign", name = "미자금화잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UnFundBalSign;

    @MessageField(id = "UnFundBalance", name = "미자금화잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal UnFundBalance;

    @MessageField(id = "AvailBalSign", name = "지불가능잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AvailBalSign;

    @MessageField(id = "AvailBalance", name = "지불가능잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AvailBalance;

    @MessageField(id = "PassbookBalSign", name = "통장잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PassbookBalSign;

    @MessageField(id = "PassbookBalance", name = "통장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal PassbookBalance;

    @MessageField(id = "RCount", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "ReferStartDay", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDay;

    @MessageField(id = "ReferEndDay", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDay;

    @MessageField(id = "DepItemName", name = "예금과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemName;

    @MessageField(id = "AcctNick", name = "계좌별명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNick;

    @MessageField(id = "K_Date", name = "연속정보일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "연속정보처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxNum;

    @MessageField(id = "K_TrxBranch", name = "연속정보취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxBranch;

    @MessageField(id = "K_AddCode", name = "연속정보증가코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_AddCode;

    @MessageField(id = "K_Time", name = "연속정보시간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Time;

    @MessageField(id = "DepTrxDetailRecord", name = "출력명세반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H1A301Res/RCount")
    private List<DepTrxDetailRecord> DepTrxDetailRecord;

    @Data
    public static class DepTrxDetailRecord implements IMessageObject {
        @MessageField(id = "TrxDate", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxDate;

        @MessageField(id = "TrxTime", name = "거래시간/시분", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxTime;

        @MessageField(id = "TrxNum", name = "처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxNum;

        @MessageField(id = "Summary1Part", name = "적요1구분", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String Summary1Part;

        @MessageField(id = "Summary1", name = "적요1", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary1;

        @MessageField(id = "Summary2Part", name = "적요2구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary2Part;

        @MessageField(id = "Summary2", name = "적요2", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary2;

        @MessageField(id = "DepPayPart", name = "입지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DepPayPart;

        @MessageField(id = "CancelPart", name = "취소구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CancelPart;

        @MessageField(id = "TrxAmtSign", name = "거래금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxAmtSign;

        @MessageField(id = "TrxAmt", name = "거래금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAmt;

        @MessageField(id = "TrxAfterBalSign", name = "거래후잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxAfterBalSign;

        @MessageField(id = "TrxAfterBalance", name = "거래후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAfterBalance;

        @MessageField(id = "TrxBranchNum", name = "취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxBranchNum;

        @MessageField(id = "NoneBankbookTrxYN", name = "무통장거래여부", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String NoneBankbookTrxYN;

        @MessageField(id = "BankName", name = "은행명", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BankName;

        @MessageField(id = "BranchName", name = "지점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BranchName;

        @MessageField(id = "TotFeeAmt", name = "수수료금액", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TotFeeAmt;
    }
}
