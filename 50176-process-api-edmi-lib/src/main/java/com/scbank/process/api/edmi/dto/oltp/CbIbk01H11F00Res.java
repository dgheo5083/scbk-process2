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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H11F00Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "요구불계좌거래명세조회 응답 전문")
public class CbIbk01H11F00Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "AcctNum", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "CustName", name = "CustName", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "PerNo", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "BlankCnt", name = "BlankCnt", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int BlankCnt;

    @MessageField(id = "NomiBalSign", name = "NomiBalSign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NomiBalSign;

    @MessageField(id = "NomiBalance", name = "NomiBalance", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NomiBalance;

    @MessageField(id = "UnFundBalSign", name = "UnFundBalSign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UnFundBalSign;

    @MessageField(id = "UnFundBalance", name = "UnFundBalance", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal UnFundBalance;

    @MessageField(id = "AvailBalSign", name = "AvailBalSign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AvailBalSign;

    @MessageField(id = "AvailBalance", name = "AvailBalance", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AvailBalance;

    @MessageField(id = "PassbookBalSign", name = "PassbookBalSign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PassbookBalSign;

    @MessageField(id = "PassbookBalance", name = "PassbookBalance", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal PassbookBalance;

    @MessageField(id = "RCount", name = "RCount", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int RCount;

    @MessageField(id = "ReferStartDay", name = "ReferStartDay", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferStartDay;

    @MessageField(id = "ReferEndDay", name = "ReferEndDay", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferEndDay;

    @MessageField(id = "DepItemName", name = "DepItemName", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemName;

    @MessageField(id = "AcctNick", name = "AcctNick", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNick;

    @MessageField(id = "K_Date", name = "K_Date", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private Integer K_Date;

    @MessageField(id = "K_TrxNum", name = "K_TrxNum", length = 7, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private Integer K_TrxNum;

    @MessageField(id = "K_TrxBranch", name = "K_TrxBranch", length = 3, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private Integer K_TrxBranch;

    @MessageField(id = "K_AddCode", name = "K_AddCode", length = 3, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private Integer K_AddCode;

    @MessageField(id = "K_Time", name = "K_Time", length = 6, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private Integer K_Time;

    @MessageField(id = "DepTrxDetailRecord", name = "출력명세반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H11F00Res/RCount")
    private List<DepTrxDetailRecord> DepTrxDetailRecord;

    @Getter
    @Setter
    public static class DepTrxDetailRecord implements IMessageObject {

        @MessageField(id = "TrxDate", name = "TrxDate", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxDate;

        @MessageField(id = "TrxTime", name = "TrxTime", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxTime;

        @MessageField(id = "TrxNum", name = "TrxNum", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private int TrxNum;

        @MessageField(id = "Summary1Part", name = "Summary1Part", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private int Summary1Part;

        @MessageField(id = "Summary1", name = "Summary1", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary1;

        @MessageField(id = "Summary2Part", name = "Summary2Part", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary2Part;

        @MessageField(id = "Summary2", name = "Summary2", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary2;

        @MessageField(id = "DepPayPart", name = "DepPayPart", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DepPayPart;

        @MessageField(id = "CancelPart", name = "CancelPart", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String CancelPart;

        @MessageField(id = "TrxAmtSign", name = "TrxAmtSign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxAmtSign;

        @MessageField(id = "TrxAmt", name = "TrxAmt", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAmt;

        @MessageField(id = "TrxAfterBalSign", name = "TrxAfterBalSign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxAfterBalSign;

        @MessageField(id = "TrxAfterBalance", name = "TrxAfterBalance", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAfterBalance;

        @MessageField(id = "TrxBranchNum", name = "TrxBranchNum", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private int TrxBranchNum;

        @MessageField(id = "NoneBankbookTrxYN", name = "NoneBankbookTrxYN", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String NoneBankbookTrxYN;

        @MessageField(id = "BankName", name = "BankName", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String BankName;

        @MessageField(id = "BranchName", name = "BranchName", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BranchName;

        @MessageField(id = "TotFeeAmt", name = "TotFeeAmt", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TotFeeAmt;
    }

}
