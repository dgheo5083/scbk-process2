package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbTbs07H42200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "자동이체거래내역조회 응답부")
public class CbTbs07H42200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "RCount", name = "명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RCount;

    @MessageField(id = "KBranchNum1", name = "연속-점번호1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KBranchNum1;

    @MessageField(id = "KCustNum1", name = "연속-고객번호1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KCustNum1;

    @MessageField(id = "KGmCode1", name = "연속-과목코드1", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KGmCode1;

    @MessageField(id = "KAcctNum1", name = "연속-계좌번호1", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KAcctNum1;

    @MessageField(id = "KTransDate1", name = "연속-이체일자1", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransDate1;

    @MessageField(id = "KBankCode1", name = "연속-은행코드1", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KBankCode1;

    @MessageField(id = "KTransAcctNum1", name = "연속-이체계좌번호1", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KTransAcctNum1;

    @MessageField(id = "KBranchNum2", name = "연속-점번호2", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KBranchNum2;

    @MessageField(id = "KCustNum2", name = "연속-고객번호2", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KCustNum2;

    @MessageField(id = "KCgAcctNum2", name = "연속-출금계좌번호2", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KCgAcctNum2;

    @MessageField(id = "KGiroNum2", name = "연속-지로번호2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KGiroNum2;

    @MessageField(id = "KNapNum2", name = "연속-납부자번호2", length = 25, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KNapNum2;

    @MessageField(id = "TR422Rec1", name = "이체명세부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs07H42200Res/RCount")
    private List<TR422Rec1> TR422Rec1;

    @Data
    public static class TR422Rec1 implements IMessageObject {
        @MessageField(id = "RCgAcctNum", name = "출금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RCgAcctNum;

        @MessageField(id = "RIgBankCode", name = "입금은행코드", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgBankCode;

        @MessageField(id = "RIgBankName", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgBankName;

        @MessageField(id = "RIgAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RIgAcctNum;

        @MessageField(id = "RTransDate", name = "이체일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransDate;

        @MessageField(id = "RTransAmt", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransAmt;

        @MessageField(id = "RDrBranchName", name = "등록점명", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RDrBranchName;

        @MessageField(id = "RTransKind", name = "이체종류", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RTransKind;

        @MessageField(id = "RTransEndDate", name = "이체종료월", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RTransEndDate;

        @MessageField(id = "YOJUKYO", name = "출금통장표시내역", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOJUKYO;

        @MessageField(id = "YOJUKIP", name = "입금통장표시내역", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOJUKIP;

        @MessageField(id = "YOSTART", name = "이체시작월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOSTART;
    }
}
