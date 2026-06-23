package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H11F00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "요구불계좌거래명세조회 요청 전문")
public class CbIbk01H11F00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, padding = StringUtils.SPACE, align = AlignType.LEFT)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", encoding = "cp500", length = 8, padding = StringUtils.SPACE, align = AlignType.LEFT)
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "AcctNum", length = 11, padding = StringUtils.SPACE, align = AlignType.LEFT)
    private String AcctNum;

    @MessageField(id = "AcctPassword", name = "AcctPassword", length = 4, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String AcctPassword;

    @MessageField(id = "ReferStartDay", name = "ReferStartDay", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferStartDay;

    @MessageField(id = "DealTime", name = "DealTime", length = 4, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String DealTime;

    @MessageField(id = "ReferEndDay", name = "ReferEndDay", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferEndDay;

    @MessageField(id = "BankbookPart", name = "BankbookPart", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BankbookPart;

    @MessageField(id = "TrxPart", name = "TrxPart", length = 2, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String TrxPart;

    @MessageField(id = "MSList", name = "MSList", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int MSList;

    @MessageField(id = "PrintPart", name = "PrintPart", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int PrintPart;

    @MessageField(id = "K_Date", name = "K_Date", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "K_TrxNum", length = 7, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String K_TrxNum;

    @MessageField(id = "K_TrxBranch", name = "K_TrxBranch", length = 3, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String K_TrxBranch;

    @MessageField(id = "K_AddCode", name = "K_AddCode", length = 3, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String K_AddCode;

    @MessageField(id = "K_Time", name = "K_Time", length = 6, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String K_Time;

    @MessageField(id = "PerNo", name = "PerNo", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String PerNo;

    @MessageField(id = "PwdSkip", name = "PwdSkip", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PwdSkip;

    @MessageField(id = "Dummy", name = "Dummy", length = 376, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "InputJumin", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "ChipNo", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "CardIssueDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "TeleOne", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "TeleTwo", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "TeleThree", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true)
    private String TeleThree;
}
