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
@IntegrationMessage(id = "CbIbk01H29200Res", type = Type.RESPONSE, description = "등록된 예약이체 조회 응답 전문")
public class CbIbk01H29200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "JhPart", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JhPart;

    @MessageField(id = "CustName", name = "고객명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "JhSDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JhSDate;

    @MessageField(id = "JhEDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JhEDate;

    @MessageField(id = "RCount", name = "출력명세건수", length = 2)
    private Integer RCount;

    @MessageField(id = "KTransDate", name = "연속-이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransDate;

    @MessageField(id = "KTransTime", name = "연속-이체시각", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransTime;

    @MessageField(id = "KJsNum", name = "연속-접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer KJsNum;

    @MessageField(id = "PROCESSTP", name = "PROCESSTP")
    private String PROCESSTP;

    @MessageField(id = "JhSDateFormat", name = "조회시작일")
    private String JhSDateFormat;

    @MessageField(id = "JhEDateFormat", name = "조회종료일")
    private String JhEDateFormat;

    @MessageField(id = "TR292Rec1", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H29200Res/RCount")
    private List<TR292Rec1> TR292Rec1;

    @Data
    public static class TR292Rec1 implements IMessageObject {
        @MessageField(id = "RTransDate", name = "이체일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransDate;

        @MessageField(id = "RJsNum", name = "접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RJsNum;

        @MessageField(id = "RTransTime", name = "이체시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransTime;

        @MessageField(id = "RCgAcctNum", name = "출금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RCgAcctNum;

        @MessageField(id = "RIgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgBankCode;

        @MessageField(id = "RIgBankName", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgBankName;

        @MessageField(id = "RIgAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgAcctNum;

        @MessageField(id = "RTransAmt", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RTransAmt;

        @MessageField(id = "RFeeAmt", name = "수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RFeeAmt;

        @MessageField(id = "RHgRName", name = "수취인명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RHgRName;

        @MessageField(id = "RCrState", name = "처리상태", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RCrState;

        @MessageField(id = "RECrState", name = "영문처리상태", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RECrState;

    }
}
