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
@IntegrationMessage(id = "CbTbs03H29300Req", type = Type.REQUEST, description = "이체결과조회 요청 전문")
public class CbTbs03H29300Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ReferPart", name = "ReferPart", length = 1)
    private String ReferPart;

    @MessageField(id = "ReferStartDate", name = "ReferStartDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferStartDate;

    @MessageField(id = "ReceiptNum", name = "ReceiptNum", length = 4, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReceiptNum;

    @MessageField(id = "ReferEndDate", name = "ReferEndDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferEndDate;

    @MessageField(id = "ReferGubun", name = "ReferGubun", length = 1)
    private String ReferGubun;

    @MessageField(id = "RCgAcctNum", name = "RCgAcctNum", length = 16, masking = true, maskingType = "02")
    private String RCgAcctNum;

    @MessageField(id = "RecDepBankCode", name = "RecDepBankCode", length = 3)
    private String RecDepBankCode;

    @MessageField(id = "ReqDetailCnt", name = "ReqDetailCnt", length = 2, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private Integer ReqDetailCnt;

    @MessageField(id = "YIPAGE", name = "YIPAGE", length = 15, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String YIPAGE;

    @MessageField(id = "YITPAGE", name = "YITPAGE", length = 15, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String YITPAGE;

    @MessageField(id = "KeyTransSpecDate", name = "KeyTransSpecDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String KeyTransSpecDate;

    @MessageField(id = "KeyTransSpecTime", name = "KeyTransSpecTime", length = 2, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String KeyTransSpecTime;

    @MessageField(id = "KeyTransFinalDate", name = "KeyTransFinalDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String KeyTransFinalDate;

    @MessageField(id = "KeyTransAmtSum", name = "KeyTransAmtSum", length = 13)
    private BigDecimal KeyTransAmtSum;

    @MessageField(id = "KeyFeeSum", name = "KeyFeeSum", length = 9)
    private BigDecimal KeyFeeSum;

    @MessageField(id = "KeyFundSum", name = "KeyFundSum", length = 13)
    private BigDecimal KeyFundSum;

    @MessageField(id = "KeyReceiptNum", name = "KeyReceiptNum", length = 4)
    private Integer KeyReceiptNum;

    @MessageField(id = "YIGJICYB", name = "YIGJICYB", length = 1)
    private String YIGJICYB;
}
