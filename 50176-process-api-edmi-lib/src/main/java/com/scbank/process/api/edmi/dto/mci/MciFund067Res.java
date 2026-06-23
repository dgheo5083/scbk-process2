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
@IntegrationMessage(id = "MciFund067Res", type = Type.RESPONSE, captureSystem = "MCI", description = "펀드 실질투자수익률보고서조회 응답부")
public class MciFund067Res implements IMessageObject {
    @MessageField(id = "AOOUTMACNA", name = "출력마크로명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOOUTMACNA;

    @MessageField(id = "AOPRODCOD", name = "상품코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOPRODCOD;

    @MessageField(id = "AOJPNO", name = "전표번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOJPNO;

    @MessageField(id = "AOJHIL", name = "조회일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHIL;

    @MessageField(id = "AOJHTM", name = "조회시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHTM;

    @MessageField(id = "AOCMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOCMFNA;

    @MessageField(id = "AOJHGIJNW", name = "조회기준년월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHGIJNW;

    @MessageField(id = "AOGBCOD", name = "구분코드(1:역내, 2:역외-적립, 3:역외-임의식)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOGBCOD;

    @MessageField(id = "AOSMLTL1", name = "소제목 1", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL1;

    @MessageField(id = "AOFNDNA", name = "펀드명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOFNDNA;

    @MessageField(id = "AOSINIL", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOSINIL;

    @MessageField(id = "AOACCTNO13", name = "계좌번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOACCTNO13;

    @MessageField(id = "AOCONGG1", name = "계약기간", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOCONGG1;

    @MessageField(id = "AOFXJJNJASU", name = "외화잔존좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXJJNJASU;

    @MessageField(id = "AOGIJIL", name = "기준일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOGIJIL;

    @MessageField(id = "AOFXWJJAN", name = "외화원장잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXWJJAN;

    @MessageField(id = "AOMIDHMEAK1", name = "중간환매금액 1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOMIDHMEAK1;

    @MessageField(id = "AOMIDHMEAK2", name = "중간환매금액 2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOMIDHMEAK2;

    @MessageField(id = "AOFXSSRAK", name = "외화수수료금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXSSRAK;

    @MessageField(id = "AOSALESSRRT52", name = "판매수수료율 52", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOSALESSRRT52;

    @MessageField(id = "AOMGHY1", name = "마진환율 1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOMGHY1;

    @MessageField(id = "AOFXPGAAK", name = "외화평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXPGAAK;

    @MessageField(id = "AOCGAK1", name = "차감금액 1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOCGAK1;

    @MessageField(id = "AOCGAK2", name = "차감금액 2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOCGAK2;

    @MessageField(id = "AONUJSURT1", name = "누적수익율 1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AONUJSURT1;

    @MessageField(id = "AONUJSURT2", name = "누적수익율 2", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AONUJSURT2;

    @MessageField(id = "AOHWSSURT1", name = "환산수익율 1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOHWSSURT1;

    @MessageField(id = "AOHWSSURT2", name = "환산수익율 2", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOHWSSURT2;

    @MessageField(id = "AOJRPGSU1", name = "조립건수 1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJRPGSU1;

    @MessageField(id = "AOFXPMSSINF", name = "외화판매수수료")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciFund067Res/AOJRPGSU1")
    private List<AOFXPMSSINF> AOFXPMSSINF;

    @Data
    public static class AOFXPMSSINF implements IMessageObject {

        @MessageField(id = "AOOUTTL11", name = "출력제목 11", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOOUTTL11;

        @MessageField(id = "AOCURYKN", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOCURYKN;

        @MessageField(id = "AOFXSTAGJAK", name = "외화시작기준금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXSTAGJAK;

        @MessageField(id = "AOGIJSTAHCH", name = "기준시작회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOGIJSTAHCH;

        @MessageField(id = "AOFXENDGJAK", name = "외화종료기준금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXENDGJAK;

        @MessageField(id = "AOGIJENDHCH", name = "기준종료회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOGIJENDHCH;

        @MessageField(id = "AOISMMANGBN", name = "이상미만구분명", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOISMMANGBN;

        @MessageField(id = "AOSSRRT52", name = "수수료율 52", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOSSRRT52;

    }

    @MessageField(id = "AOJHSTAIL", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHSTAIL;

    @MessageField(id = "AOJHENDIL", name = "조회종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHENDIL;

    @MessageField(id = "AOCURYKN1", name = "통화약명 1", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOCURYKN1;

    @MessageField(id = "AOJRPGSU2", name = "조립건수 2", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJRPGSU2;

    @MessageField(id = "AOGJWINF", name = "계좌정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciFund067Res/AOJRPGSU2")
    private List<AOGJWINF> AOGJWINF;

    @Data
    public static class AOGJWINF implements IMessageObject {

        @MessageField(id = "AOGRIL", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOGRIL;

        @MessageField(id = "AOGRGBN4", name = "거래구분명 4", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOGRGBN4;

        @MessageField(id = "AOFXGRJASU", name = "외화거래좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXGRJASU;

        @MessageField(id = "AOFXGRAK", name = "외화거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXGRAK;

        @MessageField(id = "AODRGJG", name = "등록기준가", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AODRGJG;

    }

    @MessageField(id = "AOHAPJASU1", name = "합계좌수 1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPJASU1;

    @MessageField(id = "AOHAPAK1", name = "합계금액 1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPAK1;

    @MessageField(id = "AOHAPJASU2", name = "합계좌수 2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPJASU2;

    @MessageField(id = "AOHAPAK2", name = "합계금액 2", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPAK2;

    @MessageField(id = "AOHAPJASU3", name = "합계좌수 3", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPJASU3;

    @MessageField(id = "AOHAPAK3", name = "합계금액 3", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPAK3;

    @MessageField(id = "AOHAPJASU4", name = "합계좌수 4", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPJASU4;

    @MessageField(id = "AOHAPAK4", name = "합계금액 4", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOHAPAK4;
}
