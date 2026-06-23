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
@IntegrationMessage(id = "CbIbk01H29104Req", type = Type.REQUEST, captureSystem = "OLTP", description = "이체결과조회 특정기간집계결과 조회 요청부")
public class CbIbk01H29104Req implements IMessageObject {
    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ReferPart", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "ReferStartDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "ReceiptNum", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReceiptNum;

    @MessageField(id = "ReferEndDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

    @MessageField(id = "ReferGubun", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferGubun;

    @MessageField(id = "RCgAcctNum", name = "", length = 16, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RCgAcctNum;

    @MessageField(id = "RecDepBankCode", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecDepBankCode;

    @MessageField(id = "ReqDetailCnt", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReqDetailCnt;

    @MessageField(id = "KeyTransSpecDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransSpecDate;

    @MessageField(id = "KeyTransSpecTime", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransSpecTime;

    @MessageField(id = "KeyTransFinalDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransFinalDate;

    @MessageField(id = "KeyTransAmtSum", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyTransAmtSum;

    @MessageField(id = "KeyFeeSum", name = "", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyFeeSum;

    @MessageField(id = "KeyFundSum", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyFundSum;

    @MessageField(id = "KeyReceiptNum", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyReceiptNum;

    @MessageField(id = "Dummy", name = "", length = 372, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;
}
