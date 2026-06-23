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
@IntegrationMessage(id = "CbIbk01H29102Res", type = Type.RESPONSE, description = "연락처이체 미수취건 조회(당일) 응답 전문")
public class CbIbk01H29102Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ReferPart", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "CustName", name = "고객명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "ReferStartDate", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "ReferEndDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

    @MessageField(id = "TransAmtSum", name = "이체금액합계", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TransAmtSum;

    @MessageField(id = "FeeSum", name = "수수료합계", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FeeSum;

    @MessageField(id = "ReferGubun", name = "조회추출방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferGubun;

    @MessageField(id = "RCgAcctNum", name = "출금계좌", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RCgAcctNum;

    @MessageField(id = "RecDepBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecDepBankCode;

    @MessageField(id = "RCount", name = "명세건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "KeyTransSpecDate", name = "연속키 - 이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransSpecDate;

    @MessageField(id = "KeyTransFinalDate", name = "연속키 - 최종일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransFinalDate;

    @MessageField(id = "KeyTransAmtSum", name = "연속키 - 이체금액합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal KeyTransAmtSum;

    @MessageField(id = "KeyFeeSum", name = "연속키 - 수수료합계", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal KeyFeeSum;

    @MessageField(id = "KeyReceiptNum", name = "연속키 - 접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyReceiptNum;

    @MessageField(id = "TR291Rec1", name = "이체명세부_")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H29102Res/RCount")
    private List<TR291Rec1> TR291Rec1;

    @Data
    public static class TR291Rec1 implements IMessageObject {
        @MessageField(id = "RecTransDate", name = "이체일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RecTransDate;

        @MessageField(id = "RecReceiptNum", name = "접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RecReceiptNum;

        @MessageField(id = "RecTransTime", name = "이체시간", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecTransTime;

        @MessageField(id = "RecDrawAcctNum", name = "출금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecDrawAcctNum;

        @MessageField(id = "RecDepBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecDepBankCode;

        @MessageField(id = "RecDepBankName", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecDepBankName;

        @MessageField(id = "RecDepAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecDepAcctNum;

        @MessageField(id = "RecTransAmt", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RecTransAmt;

        @MessageField(id = "RecFeeAmt", name = "수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RecFeeAmt;

        @MessageField(id = "RecRecipientName", name = "수취인명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecRecipientName;

        @MessageField(id = "RecProcessState", name = "처리상태", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecProcessState;

        @MessageField(id = "RecProcessCode", name = "영문처리상태", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecProcessCode;

        @MessageField(id = "HgRNam1", name = "입금인이름", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String HgRNam1;

        @MessageField(id = "FeeCode", name = "수수료구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FeeCode;

        @MessageField(id = "YOMBIGO", name = "입금통장표기내용", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMBIGO;

        @MessageField(id = "YOSERVICETYPE", name = "서비스코드(스페이스 : 키보드뱅킹 1:전화번호이체 2:카카오톡이체)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSERVICETYPE;

        @MessageField(id = "YOCHANNELCODE", name = "서비스 채널코드(요청한 채널타입(Android : 49 IOS : 50)", length = 19, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCHANNELCODE;

        @MessageField(id = "YOMICJR", name = "이체구분", length = 18, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMICJR;
    }
}
