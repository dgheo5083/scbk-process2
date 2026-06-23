package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciIbBnc01004Res", type = Type.RESPONSE, captureSystem = "MCI", description = "보험납입내역조회 응답부")
public class MciIbBnc01004Res implements IMessageObject {

    @MessageField(id = "FO_ERRCD", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRCD;

    @MessageField(id = "FO_ERRMSG", name = "", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRMSG;

    @MessageField(id = "FO_INSUNAME", name = "", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_INSUNAME;

    @MessageField(id = "FO_PRODTNAME", name = "", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_PRODTNAME;

    @MessageField(id = "FO_CONTNAME", name = "", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONTNAME;

    @MessageField(id = "FO_CURRENCY", name = "", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CURRENCY;

    @MessageField(id = "FO_PAY_CNT", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_PAY_CNT;

    @MessageField(id = "FO_PAY", name = "")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01004Res/FO_PAY_CNT")
    private List<FO_PAY> FO_PAY;

    @Data
    public static class FO_PAY implements IMessageObject {
        @MessageField(id = "FO_PAY_NUM", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_NUM;

        @MessageField(id = "FO_PAY_CYL", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_CYL;

        @MessageField(id = "FO_PAY_YM", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_YM;

        @MessageField(id = "FO_PAY_YMD", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_YMD;

        @MessageField(id = "FO_PAY_TOTPRM", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_TOTPRM;

        @MessageField(id = "FO_PAY_EPAY", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_EPAY;

        @MessageField(id = "FO_PAY_FTMONC", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_FTMONC;

        @MessageField(id = "FO_PAY_RLPM", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_RLPM;

        @MessageField(id = "FO_PAY_DLYINT", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_DLYINT;

        @MessageField(id = "FO_PAY_DISCTP", name = "", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PAY_DISCTP;

    }
}