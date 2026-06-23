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
@IntegrationMessage(id = "CbIbf01H11300Res", type = Type.RESPONSE, description = "외화명세조회")
public class CbIbf01H11300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "CustName", name = "성명", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "BalSign", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalSign;

    @MessageField(id = "Balance", name = "잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal Balance;

    @MessageField(id = "DetailTotCnt", name = "명세총건수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DetailTotCnt;

    @MessageField(id = "RCount", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "StartDate", name = "시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String StartDate;

    @MessageField(id = "EndDate", name = "종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String EndDate;

    @MessageField(id = "DepItemName", name = "예금과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemName;

    @MessageField(id = "CurrencyName", name = "통화명", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CurrencyName;

    @MessageField(id = "K_Date", name = "연속정보일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "연속정보처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxNum;

    @MessageField(id = "ForcurTrxDetailRecord", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbf01H11300Res/RCount")
    private List<ForcurTrxDetailRecord> ForcurTrxDetailRecord;

    @Data
    public static class ForcurTrxDetailRecord implements IMessageObject {
        @MessageField(id = "TrxDate", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxDate;

        @MessageField(id = "TrxNum", name = "처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String TrxNum;

        @MessageField(id = "TrxPart", name = "거래구분", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxPart;

        @MessageField(id = "TrxAmtSign", name = "거래금액sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxAmtSign;

        @MessageField(id = "TrxAmt", name = "거래금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAmt;

        @MessageField(id = "ExchRate", name = "환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal ExchRate;

        @MessageField(id = "PositionSign", name = "Position금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String PositionSign;

        @MessageField(id = "PositionAmt", name = "Position금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal PositionAmt;

        @MessageField(id = "TrxBranch", name = "취급점", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxBranch;

        @MessageField(id = "BackDate", name = "기산일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BackDate;
    }
}
