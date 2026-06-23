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
@IntegrationMessage(id = "CbTbs03H21100Res", type = Type.RESPONSE, description = "즉시이체 응답 전문")
public class CbTbs03H21100Res implements IMessageObject {

    @MessageField(id = "UserID", length = 10)
    private String UserID;

    @MessageField(id = "ProcessDate", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ProcessDate;

    @MessageField(id = "CgAcctNum", length = 14)
    private String CgAcctNum;

    @MessageField(id = "HgSName", length = 26)
    private String HgSName;

    @MessageField(id = "HgSHName", length = 22)
    private String HgSHName;

    @MessageField(id = "JgCode", length = 1)
    private String JgCode;

    @MessageField(id = "JYCode", length = 1)
    private String JYCode;

    @MessageField(id = "IgBankCode", length = 3)
    private String IgBankCode;

    @MessageField(id = "IgBankName", length = 6)
    private String IgBankName;

    @MessageField(id = "IgAcctNum", length = 16)
    private String IgAcctNum;

    @MessageField(id = "TotSgAmt", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotSgAmt;

    @MessageField(id = "TotFeeAmt", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotFeeAmt;

    @MessageField(id = "FeeCode", length = 1)
    private String FeeCode;

    @MessageField(id = "FeeText", length = 62)
    private String FeeText;

    @MessageField(id = "TransCode", length = 1)
    private String TransCode;

    @MessageField(id = "JbCode", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JbCode;

    @MessageField(id = "CMSCode", length = 24)
    private String CMSCode;

    @MessageField(id = "CMSCodeInYN", length = 1)
    private String CMSCodeInYN;

    @MessageField(id = "CMSCodeYN", length = 1)
    private String CMSCodeYN;

    @MessageField(id = "CMSCodeText1", length = 62)
    private String CMSCodeText1;

    @MessageField(id = "CMSCodeText2", length = 62)
    private String CMSCodeText2;

    @MessageField(id = "RNameInYN", length = 1)
    private String RNameInYN;

    @MessageField(id = "HgRName", length = 22)
    private String HgRName;

    @MessageField(id = "HgRNam1", length = 22)
    private String HgRNam1;

    @MessageField(id = "HgSNam1", length = 26)
    private String HgSNam1;

    @MessageField(id = "YyDate", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YyDate;

    @MessageField(id = "YyTime", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YyTime;

    @MessageField(id = "AmtSign", length = 1)
    private String AmtSign;

    @MessageField(id = "AfterBal", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AfterBal;

    @MessageField(id = "TrustAmt", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TrustAmt;

    @MessageField(id = "TrustTax", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TrustTax;

    @MessageField(id = "TrustAgainAmt", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TrustAgainAmt;

    @MessageField(id = "StartJsNum", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String StartJsNum;

    @MessageField(id = "HgCode", length = 4)
    private String HgCode;

    @MessageField(id = "LastJsNum", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LastJsNum;

    @MessageField(id = "Rcount", length = 2)
    private int Rcount;

    @MessageField(id = "TR211Rec1")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03H21100Res/Rcount")
    private List<TR211Rec1Grid> TR211Rec1;

    @Getter
    @Setter
    public static class TR211Rec1Grid implements IMessageObject {
        @MessageField(id = "RJsNum", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RJsNum;

        @MessageField(id = "RSgAmt", length = 13)
        private BigDecimal RSgAmt;

        @MessageField(id = "RFeeAmt", length = 5)
        private BigDecimal RFeeAmt;

        @MessageField(id = "RJmNum", length = 13)
        private String RJmNum;

        @MessageField(id = "RError", length = 4)
        private String RError;

        @MessageField(id = "RResult", length = 12)
        private String RResult;

        @MessageField(id = "RState", length = 1)
        private String RState;

        @MessageField(id = "RYebi", length = 1)
        private String RYebi;
    }
}