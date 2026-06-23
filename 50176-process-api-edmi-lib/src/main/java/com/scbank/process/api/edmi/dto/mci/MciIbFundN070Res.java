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

import lombok.Builder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciIbFundN070Res", type = Type.RESPONSE, captureSystem = "MCI", description = "외화펀드 선취수수료 조회")
public class MciIbFundN070Res implements IMessageObject {
    @MessageField(id = "AOOUTMACNA", name = "출력마크로명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOOUTMACNA;

    @MessageField(id = "AOPRODCOD", name = "상품코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOPRODCOD;

    @MessageField(id = "AOJPNO", name = "전표번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOJPNO;

    @MessageField(id = "AOOPIL", name = "조작자일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOOPIL;

    @MessageField(id = "AOOPTM", name = "조작자시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOOPTM;

    @MessageField(id = "AOFNDCOD", name = "펀드코드", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOFNDCOD;

    @MessageField(id = "AOFNDNA", name = "펀드명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOFNDNA;

    @MessageField(id = "AOGJWJN", name = "계좌점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOGJWJN;

    @MessageField(id = "AOKMCOD", name = "과목코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOKMCOD;

    @MessageField(id = "AOGJWNO", name = "계좌번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "02")
    private Integer AOGJWNO;

    @MessageField(id = "AOCMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String AOCMFNA;

    @MessageField(id = "AOFXIPAK", name = "외화입금금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXIPAK;

    @MessageField(id = "AOGUIMSG1", name = "안내메시지1", length = 68, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOGUIMSG1;

    @MessageField(id = "AOGUIMSG2", name = "안내메시지2", length = 68, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOGUIMSG2;

    @MessageField(id = "AOJRPGSU1", name = "조립건수1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJRPGSU1;

    @MessageField(id = "AOFXPMSSINF", name = "외화판매수수료")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbFundN070Res/AOJRPGSU1")
    private List<AOFXPMSSINF> AOFXPMSSINF;

    @Data
    public static class AOFXPMSSINF implements IMessageObject {

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

        @MessageField(id = "AOSALESSRRT52", name = "판매수수료율52", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOSALESSRRT52;

        @MessageField(id = "AOFXSALESSR", name = "외화판매수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOFXSALESSR;

    }

    @MessageField(id = "AOSMLTL1", name = "소제목1", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL1;

    @MessageField(id = "AOSMLTL2", name = "소제목2", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL2;

    @MessageField(id = "AOSMLTL3", name = "소제목3", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL3;

    @MessageField(id = "AOSMLTL4", name = "소제목4", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL4;

    @MessageField(id = "AOSMLTL5", name = "소제목5", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL5;

    @MessageField(id = "AOGRAK", name = "거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOGRAK;

    @MessageField(id = "AOCYAK", name = "청약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOCYAK;

    @MessageField(id = "AOSALESSR", name = "판매수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOSALESSR;

    @MessageField(id = "AOSSRRT52", name = "수수료율52", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOSSRRT52;

    @MessageField(id = "AOFXSSRAK", name = "외화수수료금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXSSRAK;

    @MessageField(id = "AOJHNGJWJN", name = "전환계좌점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHNGJWJN;

    @MessageField(id = "AOJHNKMCOD", name = "전환과목코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOJHNKMCOD;

    @MessageField(id = "AOJHNGJWNO", name = "전환계좌번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "02")
    private Integer AOJHNGJWNO;

    @MessageField(id = "AOFXJHNJUSU", name = "외화전환주식수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXJHNJUSU;

    @MessageField(id = "AOCURYKN1", name = "통화약명1", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOCURYKN1;

    @MessageField(id = "AOJHNSSRRT52", name = "전환수수료율52", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHNSSRRT52;

    @MessageField(id = "AOFXJHNSSR", name = "외화전환수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOFXJHNSSR;

    @MessageField(id = "AOJHNSSR", name = "전환수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AOJHNSSR;

    @MessageField(id = "AOJDSJYHY", name = "전환대상적용환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJDSJYHY;

}