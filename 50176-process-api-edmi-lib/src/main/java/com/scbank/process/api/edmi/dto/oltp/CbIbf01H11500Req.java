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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbf01H11500Req", type = Type.REQUEST, description = "외환-입출금거래명세 요청부")
public class CbIbf01H11500Req implements IMessageObject {

    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "AcctPassword", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AcctPassword;

    @MessageField(id = "ReferStartDay", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDay;

    @MessageField(id = "DealTime", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DealTime;

    @MessageField(id = "ReferEndDay", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDay;

    @MessageField(id = "BankbookPart", name = "", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BankbookPart;

    @MessageField(id = "TrxPart", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TrxPart;

    @MessageField(id = "MSList", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String MSList;

    @MessageField(id = "PrintPart", name = "", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PrintPart;

    @MessageField(id = "K_Date", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Date;

    @MessageField(id = "K_TrxNum", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxNum;

    @MessageField(id = "K_TrxBranch", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_TrxBranch;

    @MessageField(id = "K_AddCode", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_AddCode;

    @MessageField(id = "K_Time", name = "", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String K_Time;

    @MessageField(id = "PerNo", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "PwdSkip", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PwdSkip;

    @MessageField(id = "Dummy", name = "", length = 376, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

}
