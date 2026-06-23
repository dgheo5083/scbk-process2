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
@IntegrationMessage(id = "CbIbk01H29100Req", type = Type.RESPONSE, description = "이체결과조회 상세정보 조회 요청부", captureSystem = "OLTP")
public class CbIbk01H29100Req implements IMessageObject {
    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ReferPart", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "ReferStartDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "InReceiptNum", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InReceiptNum;

    @MessageField(id = "ReferEndDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

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

    @MessageField(id = "KeyReceiptNum", name = "", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KeyReceiptNum;
}
