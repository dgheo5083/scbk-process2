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
@IntegrationMessage(id = "CbTbs03H211ObsRes", type = Type.RESPONSE, captureSystem = "OLTP", description = "오픈뱅킹 이체 FDS로그용")
public class CbTbs03H211ObsRes implements IMessageObject {
    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ProcessDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ProcessDate;

    @MessageField(id = "CgAcctNum", name = "", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctNum;

    @MessageField(id = "HgSName", name = "", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSName;

    @MessageField(id = "HgSHName", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSHName;

    @MessageField(id = "JgCode", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JgCode;

    @MessageField(id = "JYCode", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JYCode;

    @MessageField(id = "IgBankCode", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgBankName", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankName;

    @MessageField(id = "IgAcctNum", name = "", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgAcctNum;

    @MessageField(id = "TotSgAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotSgAmt;

    @MessageField(id = "TotFeeAmt", name = "", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotFeeAmt;

    @MessageField(id = "FeeCode", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FeeCode;

    @MessageField(id = "FeeText", name = "", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FeeText;

    @MessageField(id = "TransCode", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransCode;

    @MessageField(id = "JbCode", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JbCode;

    @MessageField(id = "CMSCode", name = "", length = 24, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCode;

    @MessageField(id = "CMSCodeInYN", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCodeInYN;

    @MessageField(id = "CMSCodeYN", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCodeYN;

    @MessageField(id = "CMSCodeText1", name = "", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCodeText1;

    @MessageField(id = "CMSCodeText2", name = "", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCodeText2;

    @MessageField(id = "RNameInYN", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RNameInYN;

    @MessageField(id = "HgRName", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRName;

    @MessageField(id = "HgRNam1", name = "", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRNam1;

    @MessageField(id = "HgSNam1", name = "", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSNam1;

    @MessageField(id = "YyDate", name = "", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YyDate;

    @MessageField(id = "YyTime", name = "", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YyTime;

    @MessageField(id = "AmtSign", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign;

    @MessageField(id = "AfterBal", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AfterBal;

    @MessageField(id = "TrustAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TrustAmt;

    @MessageField(id = "TrustTax", name = "", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TrustTax;

    @MessageField(id = "TrustAgainAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TrustAgainAmt;

    @MessageField(id = "StartJsNum", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String StartJsNum;

    @MessageField(id = "HgCode", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgCode;

    @MessageField(id = "LastJsNum", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LastJsNum;

    @MessageField(id = "Rcount", name = "", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Rcount;

    @MessageField(id = "TR211Rec1", name = "record")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03H211ObsRes/Rcount")
    private List<TR211Rec1> TR211Rec1;

    @Data
    public static class TR211Rec1 implements IMessageObject {
        @MessageField(id = "RJsNum", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RJsNum;

        @MessageField(id = "RSgAmt", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RSgAmt;

        @MessageField(id = "RFeeAmt", name = "", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RFeeAmt;

        @MessageField(id = "RJmNum", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RJmNum;

        @MessageField(id = "RError", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RError;

        @MessageField(id = "RResult", name = "", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RResult;

        @MessageField(id = "RState", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RState;

        @MessageField(id = "RYebi", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RYebi;
    }
}
