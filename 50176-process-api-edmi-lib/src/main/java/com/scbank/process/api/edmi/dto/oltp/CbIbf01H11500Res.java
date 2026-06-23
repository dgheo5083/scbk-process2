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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbf01H11500Res", type = Type.RESPONSE, description = "외환-입출금거래명세 응답부")

public class CbIbf01H11500Res implements IMessageObject {

    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "CustName", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "BalSign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalSign;

    @MessageField(id = "Balance", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal Balance;

    @MessageField(id = "DetailTotCnt", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DetailTotCnt;

    @MessageField(id = "RCount", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "StartDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String StartDate;

    @MessageField(id = "EndDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String EndDate;

    @MessageField(id = "DepItemName", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemName;

    @MessageField(id = "CurrencyName", name = "", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CurrencyName;

    @MessageField(id = "K_Date", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer K_TrxNum;

    @MessageField(id = "ForcurTrxDetailRecord", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbf01H11500Res/RCount")
    private List<ForcurTrxDetailRecord> ForcurTrxDetailRecord;

    @Data
    public static class ForcurTrxDetailRecord implements IMessageObject {
        @MessageField(id = "TrxDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxDate;

        @MessageField(id = "TrxNum", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxNum;

        @MessageField(id = "TrxPart", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxPart;

        @MessageField(id = "TrxAmtSign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxAmtSign;

        @MessageField(id = "TrxAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAmt;

        @MessageField(id = "ExchRate", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String ExchRate;

        @MessageField(id = "PositionSign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String PositionSign;

        @MessageField(id = "PositionAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal PositionAmt;

        @MessageField(id = "TrxBranch", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxBranch;

        @MessageField(id = "BackDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String BackDate;

        @MessageField(id = "Summary", name = "", length = 24, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary;

        @MessageField(id = "BranchName", name = "", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BranchName;

        @MessageField(id = "Dummy", name = "", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Dummy;

    }

}
