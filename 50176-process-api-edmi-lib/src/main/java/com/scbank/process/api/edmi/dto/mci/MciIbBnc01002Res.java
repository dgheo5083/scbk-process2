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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciIbBnc01002Res", type = Type.RESPONSE, captureSystem = "MCI", description = "보험 계약상세조회 응답부")
public class MciIbBnc01002Res implements IMessageObject {

    @MessageField(id = "FO_ERRCD", name = "FO_ERRCD", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRCD;

    @MessageField(id = "FO_ERRMSG", name = "FO_ERRMSG", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRMSG;

    @MessageField(id = "FO_INSUNAME", name = "FO_INSUNAME", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_INSUNAME;

    @MessageField(id = "FO_PRODTNAME", name = "FO_PRODTNAME", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_PRODTNAME;

    @MessageField(id = "FO_CONTNAME", name = "FO_CONTNAME", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONTNAME;

    @MessageField(id = "FO_PRODTGB", name = "FO_PRODTGB", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_PRODTGB;

    @MessageField(id = "FO_CONTDATE", name = "FO_CONTDATE", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONTDATE;

    @MessageField(id = "FO_INSFROM", name = "FO_INSFROM", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_INSFROM;

    @MessageField(id = "FO_INSTO", name = "FO_INSTO", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_INSTO;

    @MessageField(id = "FO_CURRENCY", name = "FO_CURRENCY", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CURRENCY;

    @MessageField(id = "FO_PRMAMT", name = "FO_PRMAMT", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_PRMAMT;

    @MessageField(id = "FO_STATE", name = "FO_STATE", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_STATE;

    @MessageField(id = "FO_CYCLE", name = "FO_CYCLE", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CYCLE;

    @MessageField(id = "FO_PERIOD", name = "FO_PERIOD", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_PERIOD;

    @MessageField(id = "FO_LASTPAYMON", name = "FO_LASTPAYMON", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_LASTPAYMON;

    @MessageField(id = "FO_LASTPAYNUM", name = "FO_LASTPAYNUM", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_LASTPAYNUM;

    @MessageField(id = "FO_TRAMOK", name = "FO_TRAMOK", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_TRAMOK;

    @MessageField(id = "FO_TRADEST", name = "FO_TRADEST", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_TRADEST;

    @MessageField(id = "FO_BANKNM", name = "FO_BANKNM", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_BANKNM;

    @MessageField(id = "FO_ACNO", name = "FO_ACNO", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ACNO;

    @MessageField(id = "FO_DEPNM", name = "FO_DEPNM", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_DEPNM;

    @MessageField(id = "FO_HOPDD", name = "FO_HOPDD", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_HOPDD;

    @MessageField(id = "FO_REL_CNT", name = "FO_REL_CNT", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_REL_CNT;

    @MessageField(id = "FO_REL", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01002Res/FO_REL_CNT")
    private List<FO_REL> FO_REL;

    @Data
    public static class FO_REL implements IMessageObject {
        @MessageField(id = "FO_REL_GBNAME", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_REL_GBNAME;

        @MessageField(id = "FO_REL_NAME", name = "", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_REL_NAME;

        @MessageField(id = "FO_REL_JUMIN", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_REL_JUMIN;

        @MessageField(id = "FO_REL_BNFR", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_REL_BNFR;
    }

    @MessageField(id = "FO_ITEM_CNT", name = "FO_ITEM_CNT", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_ITEM_CNT;

    @MessageField(id = "FO_ITEM", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01002Res/FO_ITEM_CNT")
    private List<FO_ITEM> FO_ITEM;

    @Data
    public static class FO_ITEM implements IMessageObject {
        @MessageField(id = "FO_ITEM_NAME", name = "", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_ITEM_NAME;

        @MessageField(id = "FO_ITEM_AMT", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_ITEM_AMT;

        @MessageField(id = "FO_ITEM_PRMAMT", name = "", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_ITEM_PRMAMT;
    }

    @MessageField(id = "FO_LCNCCODE", name = "생손보구분코드", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_LCNCCODE;

    @MessageField(id = "FO_CONT_INSUCD", name = "보험사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONT_INSUCD;

    @MessageField(id = "FO_PRODCD", name = "상품코드", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_PRODCD;

    @MessageField(id = "FO_CONT_STATECD", name = "계약상태코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONT_STATECD;

    @MessageField(id = "FO_CONT_CHNNLGB", name = "가입채널코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONT_CHNNLGB;

    @MessageField(id = "FO_CONT_TEL", name = "보험사콜센터", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONT_TEL;

}