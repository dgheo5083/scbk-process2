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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciIbBnc01001Res", type = Type.RESPONSE, captureSystem = "MCI", description = "보험 상품 리스트")
public class MciIbBnc01001Res implements IMessageObject {
    @MessageField(id = "FO_ERRCD", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRCD;

    @MessageField(id = "FO_ERRMSG", name = "", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRMSG;

    @MessageField(id = "FO_CONTINUOUS", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONTINUOUS;

    @MessageField(id = "FO_PAGENO", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_PAGENO;

    @MessageField(id = "FO_TOTALCNT", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_TOTALCNT;

    @MessageField(id = "FO_CONT_CNT", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FO_CONT_CNT;

    @MessageField(id = "FO_PAY", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01001Res/FO_CONT_CNT")
    private List<FO_PAY> FO_PAY;

    @Getter
        @Setter
    public static class FO_PAY implements IMessageObject {

        @MessageField(id = "DrawAcctNum", name = "증권번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctNum;

        @MessageField(id = "FO_CONT_NAME", name = "구분", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_NAME;

        @MessageField(id = "FO_CONT_INSUNM", name = "보험사", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_INSUNM;

        @MessageField(id = "DrawAcctName", name = "상품명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctName;

        @MessageField(id = "FO_CONT_DATE", name = "계약일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_DATE;

        @MessageField(id = "FO_CONT_INSF", name = "보험기간-from", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_INSF;

        @MessageField(id = "FO_CONT_INST", name = "보험기간-to", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_INST;

        @MessageField(id = "FO_CONT_CUR", name = "보험료단위", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_CUR;

        @MessageField(id = "FO_CONT_PRMAMT", name = "보험료", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_PRMAMT;

        @MessageField(id = "FO_CONT_STATE", name = "상태", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_CONT_STATE;

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

        @MessageField(id = "FO_PROD_TYPE_CD", name = "상품유형코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PROD_TYPE_CD;

        @MessageField(id = "FO_PROD_TYPE_NM", name = "상품유형명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FO_PROD_TYPE_NM;
    }
}