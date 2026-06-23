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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H03300Res", type = Type.RESPONSE, description = "대출계좌조회 응답부")
public class CbIbk01H03300Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ContinueInfo", name = "연속정보", length = 90, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueInfo;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "DrawAcctNumListRecord", name = "출력계좌수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DrawAcctNumListRecord;

    @MessageField(id = "Dummy1", name = "더미", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "REC_01", name = "계좌반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H03300Res/DrawAcctNumListRecord")
    private List<REC_01> REC_01;

    @Getter
    @Setter
    public static class REC_01 implements IMessageObject {
        @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 14, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctNum;

        @MessageField(id = "Assort", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Assort;

        @MessageField(id = "DepKind", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DepKind;

        @MessageField(id = "BalSign", name = "잔액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BalSign;

        @MessageField(id = "Balance", name = "잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal Balance;

        @MessageField(id = "Curcy", name = "통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Curcy;

        @MessageField(id = "LoanStartDate", name = "대출신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String LoanStartDate;

        @MessageField(id = "LoanEndDate", name = "대출만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String LoanEndDate;

        @MessageField(id = "LoanRepayPrinAmt", name = "승인한도", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal LoanRepayPrinAmt;

        @MessageField(id = "ExpectedDate", name = "이자예정일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String ExpectedDate;

        @MessageField(id = "LoanRate", name = "이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String LoanRate;
    }
}