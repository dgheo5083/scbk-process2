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
@IntegrationMessage(id = "CbIbk01H22900Res", type = Type.RESPONSE, description = "등록된 예약이체 조회 응답 전문")
public class CbIbk01H22900Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "UserName", name = "이용자성명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserName;

    @MessageField(id = "KTransJJDate", name = "연속키-이체지정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransJJDate;

    @MessageField(id = "KTransJJTime", name = "연속키-이체지정시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransJJTime;

    @MessageField(id = "KSerialNum", name = "연속키-일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KSerialNum;

    @MessageField(id = "RCount", name = "건수", length = 2)
    private Integer RCount;

    @MessageField(id = "TR229Rec1", name = "이체명세부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H22900Res/RCount")
    private List<TR229Rec1> TR229Rec1;

    @Data
    public static class TR229Rec1 implements IMessageObject {
        @MessageField(id = "RTransJJDate1", name = "이체지정일자1", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransJJDate1;

        @MessageField(id = "RTransJJDate2", name = "이체지정일자2", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransJJDate2;

        @MessageField(id = "RTransDate", name = "이체등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransDate;

        @MessageField(id = "RTransJJTime", name = "이체지정시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransJJTime;

        @MessageField(id = "RSerialNum", name = "일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RSerialNum;

        @MessageField(id = "RCgAcctNum", name = "출금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RCgAcctNum;

        @MessageField(id = "RHgSName", name = "의뢰인명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RHgSName;

        @MessageField(id = "RJgCode", name = "지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RJgCode;

        @MessageField(id = "RIgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgBankCode;

        @MessageField(id = "RIgBankName", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgBankName;

        @MessageField(id = "RIgAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgAcctNum;

        @MessageField(id = "RSgAmt", name = "송금금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RSgAmt;

        @MessageField(id = "RFeeAmt", name = "수수료금액", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RFeeAmt;

        @MessageField(id = "RHgRName", name = "수취인이름", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RHgRName;
    }

    @MessageField(id = "YIJHGB", name = "조회구분(연락처이체 : KBD)", length = 3, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YIJHGB;

}
