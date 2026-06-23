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
@IntegrationMessage(id = "CbIbk01H29100Res", type = Type.RESPONSE, description = "이체결과조회 상세정보 조회 응답부", captureSystem = "OLTP")
public class CbIbk01H29100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ReferPart", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "CustName", name = "", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "OutReceiptNum", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OutReceiptNum;

    @MessageField(id = "DrawAcctNum", name = "", length = 16, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DrawAcctNum;

    @MessageField(id = "TransAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TransAmt;

    @MessageField(id = "FeeAmt", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FeeAmt;

    @MessageField(id = "Sign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Sign;

    @MessageField(id = "PostTrxBal", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal PostTrxBal;

    @MessageField(id = "ProcessState", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ProcessState;

    @MessageField(id = "DepBankName", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankName;

    @MessageField(id = "DepAcctNum", name = "", length = 16, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepAcctNum;

    @MessageField(id = "RecipientName", name = "", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecipientName;

    @MessageField(id = "TextMgtNum", name = "", length = 24, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TextMgtNum;

    @MessageField(id = "TransDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransDate;

    @MessageField(id = "ProcessTime", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ProcessTime;

    @MessageField(id = "TransPart", name = "", length = 18, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPart;

    @MessageField(id = "ErrorCode", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ErrorCode;

    @MessageField(id = "ErrorMsg", name = "", length = 74, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ErrorMsg;

    @MessageField(id = "CMSCode", name = "", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCode;

    @MessageField(id = "ProcessCode", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ProcessCode;

    @MessageField(id = "DepBankCode", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode;

    @MessageField(id = "YOIHGB", name = "", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIHGB;

    @MessageField(id = "HgRNam1", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRNam1;

    @MessageField(id = "YOSRGB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSRGB;
}
