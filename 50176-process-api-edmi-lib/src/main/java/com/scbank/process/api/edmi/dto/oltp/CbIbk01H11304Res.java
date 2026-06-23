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
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H11304Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "입출금명세조회_적립식 응답부")
public class CbIbk01H11304Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "AcctNum", name = "", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "NewBranchName", name = "", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NewBranchName;

    @MessageField(id = "DepItemName", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemName;

    @MessageField(id = "CustName", name = "", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "FixSavGb", name = "", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FixSavGb;

    @MessageField(id = "ContractAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ContractAmt;

    @MessageField(id = "MonthAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal MonthAmt;

    @MessageField(id = "TotInAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotInAmt;

    @MessageField(id = "NewDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NewDate;

    @MessageField(id = "DueDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DueDate;

    @MessageField(id = "ContractCnt", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ContractCnt;

    @MessageField(id = "K_TrxNum", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer K_TrxNum;

    @MessageField(id = "Rcount", name = "", length = 3)
    private Integer Rcount;

    @MessageField(id = "DepTrxDetailRecord", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H11304Res/Rcount")
    private List<DepTrxDetailRecord> DepTrxDetailRecord;

    @Data
    public static class DepTrxDetailRecord implements IMessageObject {
        @MessageField(id = "TrxDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxDate;

        @MessageField(id = "Hoicha", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String Hoicha;

        @MessageField(id = "MonthHoicha", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String MonthHoicha;

        @MessageField(id = "TrxAmt", name = "", length = 13, align = AlignType.RIGHT)
        private BigDecimal TrxAmt;

        @MessageField(id = "TrxAfterBalance", name = "", length = 13, align = AlignType.RIGHT)
        private BigDecimal TrxAfterBalance;

        @MessageField(id = "Summary", name = "", length = 24, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Summary;

        @MessageField(id = "BranchName", name = "", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BranchName;

    }
}