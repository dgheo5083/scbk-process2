package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciTm1009010009Res", type = Type.RESPONSE, captureSystem = "MCI", description = "펀드해지계좌 조회 응답부")

public class MciTm1009010009Res implements IMessageObject {
    @MessageField(id = "AOOUTMACNA", name = "출력마크로명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOOUTMACNA;

    @MessageField(id = "AOPRODCOD", name = "상품코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOPRODCOD;

    @MessageField(id = "AOJPNO", name = "전표번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOJPNO;

    @MessageField(id = "AOCMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String AOCMFNA;

    @MessageField(id = "AOJHIL", name = "조회일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHIL;

    @MessageField(id = "AOJHTM", name = "조회시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHTM;

    @MessageField(id = "AOJRPGSU1", name = "조립건수1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJRPGSU1;

    @MessageField(id = "AOGJWINF", name = "계좌정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciTm1009010009Res/AOJRPGSU1")
    private List<AOGJWINF> AOGJWINF;

    @Data
    public static class AOGJWINF implements IMessageObject {
        @MessageField(id = "AOKWANGYE", name = "관계", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOKWANGYE;

        @MessageField(id = "AORLJNA", name = "관계자명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
        private String AORLJNA;

        @MessageField(id = "AOACCTNO13", name = "계좌번호13", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
        private String AOACCTNO13;

        @MessageField(id = "AOSGYUIL", name = "신규일자", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOSGYUIL;

        @MessageField(id = "AOFNDNA", name = "펀드명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOFNDNA;

        @MessageField(id = "AOCURNA", name = "통화명", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOCURNA;

        @MessageField(id = "AOFXSUTAK", name = "외화수탁금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXSUTAK;

        @MessageField(id = "AOFXJJNJASU", name = "외화잔존좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXJJNJASU;

        @MessageField(id = "AODRGJG2", name = "등록기준가2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AODRGJG2;

        @MessageField(id = "AODRGJG", name = "등록기준가", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AODRGJG;

        @MessageField(id = "AOFXPGAAK", name = "외화평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXPGAAK;

        @MessageField(id = "AOPGAPL", name = "평가손익", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOPGAPL;

        @MessageField(id = "AONUJSURT", name = "누적수익율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AONUJSURT;

        @MessageField(id = "AOFXJIGAK", name = "외화지급금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXJIGAK;

        @MessageField(id = "AOMANGIIL", name = "만기일자", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOMANGIIL;

        @MessageField(id = "AOLTTIME", name = "LATE적용시각", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOLTTIME;
    }

    @MessageField(id = "AOJHGJIL", name = "조회기준일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHGJIL;

    @MessageField(id = "AOOPNO", name = "조작(자)번호", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOOPNO;

    @MessageField(id = "AOOPJN", name = "조작(자)점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOOPJN;

}
