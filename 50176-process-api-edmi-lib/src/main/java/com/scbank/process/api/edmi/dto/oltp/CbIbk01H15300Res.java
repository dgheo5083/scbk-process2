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

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H15300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "대출거래내역조회 응답부")
public class CbIbk01H15300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String AcctNum;

    @MessageField(id = "CustName", name = "고객명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "TrxBrnchName", name = "거래점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TrxBrnchName;

    @MessageField(id = "GamokName", name = "과목명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GamokName;

    @MessageField(id = "LoanBal", name = "대출(보증)잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LoanBal;

    @MessageField(id = "RCount", name = "레코드명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "TotalPageCnt", name = "총페이지수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer TotalPageCnt;

    @MessageField(id = "NowPageCnt", name = "현체페이지", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NowPageCnt;

    @MessageField(id = "LoanTrxDetailReqRlt", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H15300Res/RCount")
    private List<LoanTrxDetailReqRlt> LoanTrxDetailReqRlt;

    @Data
    public static class LoanTrxDetailReqRlt implements IMessageObject {

        @MessageField(id = "TrxDate", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer TrxDate;

        @MessageField(id = "TrxCode", name = "거래구분", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String TrxCode;

        @MessageField(id = "TrxAmt", name = "거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal TrxAmt;

        @MessageField(id = "Rate", name = "이율", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String Rate;

        @MessageField(id = "RateStartDate", name = "이자시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer RateStartDate;

        @MessageField(id = "RateEndDate", name = "이자종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer RateEndDate;

        @MessageField(id = "DayCnt", name = "일수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer DayCnt;

        @MessageField(id = "InterAmt", name = "이자금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal InterAmt;

        @MessageField(id = "LoanBal", name = "대출잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal LoanBal;

    }
}