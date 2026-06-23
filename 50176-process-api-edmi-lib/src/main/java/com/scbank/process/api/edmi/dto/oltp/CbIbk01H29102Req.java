package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H29102Req", type = Type.REQUEST, description = "연락처이체 미수취건 조회(당일) 요청 전문", captureSystem = "OLTP")
public class CbIbk01H29102Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ReferPart", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "ReferStartDate", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "ReceiptNum", name = "접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReceiptNum;

    @MessageField(id = "ReferEndDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

    @MessageField(id = "ReferGubun", name = "조회추출방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferGubun;

    @MessageField(id = "RCgAcctNum", name = "출금계좌", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RCgAcctNum;

    @MessageField(id = "RecDepBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecDepBankCode;

    @MessageField(id = "ReqDetailCnt", name = "요구명세건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ReqDetailCnt;

    @MessageField(id = "KeyTransSpecDate", name = "연속키 - 이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransSpecDate;

    @MessageField(id = "KeyTransSpecTime", name = "연속키 - 이체지정시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransSpecTime;

    @MessageField(id = "KeyTransFinalDate", name = "연속키 - 최종일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransFinalDate;

    @MessageField(id = "KeyTransAmtSum", name = "연속키 - 이체금액합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal KeyTransAmtSum;

    @MessageField(id = "KeyFeeSum", name = "연속키 - 수수료합계", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal KeyFeeSum;

    @MessageField(id = "KeyFundSum", name = "연속키 - 투자합계 (추가)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal KeyFundSum;

    @MessageField(id = "KeyReceiptNum", name = "연속키 - 접수번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyReceiptNum;

    @MessageField(id = "Dummy", name = "dummy", length = 372, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;
}
