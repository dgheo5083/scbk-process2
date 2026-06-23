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

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07D53800Res", type = Type.RESPONSE, description = "지방세통합본거래 응답부")
public class CbTbs07D53800Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "GB034O_TGONG", name = "매체별키정보", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_TGONG;

    @MessageField(id = "GB034O_JONG", name = "전문종류", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_JONG;

    @MessageField(id = "GB034O_ERCD", name = "응답코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_ERCD;

    @MessageField(id = "GB034O_JMNO", name = "전문번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_JMNO;

    @MessageField(id = "GB034O_BNCD", name = "분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_BNCD;

    @MessageField(id = "GB034O_GIRO", name = "지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_GIRO;

    @MessageField(id = "GB034O_GIROH", name = "지로한글번호", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_GIROH;

    @MessageField(id = "GB034O_GIGJNO", name = "징수관계좌번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_GIGJNO;

    @MessageField(id = "GB034O_GMAK", name = "금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB034O_GMAK;

    @MessageField(id = "GB034O_HGMK", name = "현금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB034O_HGMK;

    @MessageField(id = "GB034O_TGMK", name = "자기앞", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB034O_TGMK;

    @MessageField(id = "GB034O_GGMK", name = "가계당좌", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB034O_GGMK;

    @MessageField(id = "GB034O_DGMK", name = "당좌수표", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB034O_DGMK;

    @MessageField(id = "GB034O_GRCD", name = "거래코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_GRCD;

    @MessageField(id = "GB034O_SEQ", name = "처리번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_SEQ;

    @MessageField(id = "GB034O_NAPNO", name = "전자납부번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_NAPNO;

    @MessageField(id = "GB034O_NBILNO", name = "납부순번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_NBILNO;

    @MessageField(id = "GB034O_BGYM", name = "부과년월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_BGYM;

    @MessageField(id = "GB034O_GENO", name = "납부계좌번호", length = 16, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_GENO;

    @MessageField(id = "GB034O_OPNO", name = "조작자번호", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_OPNO;

    @MessageField(id = "GB034O_TONG", name = "처리통번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_TONG;

    @MessageField(id = "GB034O_EJUNG", name = "이중전문", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_EJUNG;

    @MessageField(id = "GB034O_CBR", name = "취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_CBR;

    @MessageField(id = "GB034O_MESSAGE", name = "오류메세지", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_MESSAGE;

    @MessageField(id = "GB034O_UPMK", name = "우편환", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_UPMK;

    @MessageField(id = "GB034O_DCAK", name = "대체금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal GB034O_DCAK;

    @MessageField(id = "GB034O_GRHDCD", name = "현금대체구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_GRHDCD;

    @MessageField(id = "GB034O_KGMK", name = "국공채", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_KGMK;

    @MessageField(id = "GB034O_ILNO", name = "일련번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_ILNO;

    @MessageField(id = "GB034O_JNSNW", name = "부과년월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_JNSNW;

    @MessageField(id = "GB034O_RILJA", name = "예약일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_RILJA;

    @MessageField(id = "YOHUJAN", name = "출금후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHUJAN;

    @MessageField(id = "GB034O_IGGB", name = "일괄구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GB034O_IGGB;

    @MessageField(id = "GB034O_DATAGN", name = "데이터건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GB034O_DATAGN;

    @MessageField(id = "GB034O_IGNAPBU", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "TI1TBS07_D538_RES/GB034O_DATAGN")
    private List<GB034O_IGNAPBU> GB034O_IGNAPBU;

    @Data
    public static class GB034O_IGNAPBU implements IMessageObject {
        @MessageField(id = "GB034O_IG_BNCD", name = "발행기관분류코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String GB034O_IG_BNCD;

        @MessageField(id = "GB034O_IG_GIRO", name = "이용기관지로번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String GB034O_IG_GIRO;

        @MessageField(id = "GB034O_IG_NAPNO", name = "전자납부번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String GB034O_IG_NAPNO;

        @MessageField(id = "GB034O_IG_NBILNO", name = "납부순번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String GB034O_IG_NBILNO;

        @MessageField(id = "GB034O_IG_GMAK", name = "납부금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal GB034O_IG_GMAK;

        @MessageField(id = "GB034O_IG_GYNO", name = "거래고유번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String GB034O_IG_GYNO;

        @MessageField(id = "GB034O_IG_RTCD", name = "처리응답코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String GB034O_IG_RTCD;
    }
}
