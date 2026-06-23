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
@IntegrationMessage(id = "CbIbk01H29104Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "이체결과조회 특정기간집계결과 조회 응답부")
public class CbIbk01H29104Res implements IMessageObject {
    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ReferPart", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "ReferStartDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "ReferEndDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

    @MessageField(id = "CustName", name = "", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "Rcount", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer Rcount;

    @MessageField(id = "KeyTransSpecDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransSpecDate;

    @MessageField(id = "KeyTransFinalDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransFinalDate;

    @MessageField(id = "KeyReceiptNum", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyReceiptNum;

    @MessageField(id = "REC_01", name = "이체명세부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H29104Res/Rcount")
    private List<REC_01> REC_01;

    @Data
    public static class REC_01 implements IMessageObject {
        @MessageField(id = "RecTransDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RecTransDate;

        @MessageField(id = "RecKFBNormalCnt", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer RecKFBNormalCnt;

        @MessageField(id = "RecKFBNormalAmt", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RecKFBNormalAmt;

        @MessageField(id = "RecOtherBankNormalCnt", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer RecOtherBankNormalCnt;

        @MessageField(id = "RecOtherBankNormalAmt", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RecOtherBankNormalAmt;

        @MessageField(id = "RecNormalSumCnt", name = "", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private Integer RecNormalSumCnt;

        @MessageField(id = "RecNormalSumAmt", name = "", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RecNormalSumAmt;
    }
}