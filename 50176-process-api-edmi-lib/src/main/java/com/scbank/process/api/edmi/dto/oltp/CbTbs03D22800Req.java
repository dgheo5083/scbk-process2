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
@IntegrationMessage(id = "CbTbs03D22800Req", type = Type.REQUEST, description = "예약이체 변경 본처리 요청 전문", captureSystem = "OLTP")
public class CbTbs03D22800Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPassword; // _DNFE2E_ _/DNFE2E_

    @MessageField(id = "ModifyAmt", name = "수정금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ModifyAmt;

    @MessageField(id = "TransJJDate", name = "이체일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransJJDate;

    @MessageField(id = "TransJJTime", name = "이체시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransJJTime;

    @MessageField(id = "SerialNum", name = "접수일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SerialNum;

    @MessageField(id = "YgMsno", name = "요구전문수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YgMsno;

    @MessageField(id = "KTransJJDate", name = "연속키-이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransJJDate;

    @MessageField(id = "KTransJJTime", name = "연속키-이체지정시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransJJTime;

    @MessageField(id = "KSerialNum", name = "연속키-일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KSerialNum;

    @MessageField(id = "Dummy", name = "dummy", length = 328, align = AlignType.LEFT, padding = StringUtils.SPACE)
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

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "dummy1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "dummy2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;
}
