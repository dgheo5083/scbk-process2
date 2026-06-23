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
@IntegrationMessage(id = "MciIbBnc01007Res", type = Type.RESPONSE, captureSystem = "MCI", description = "계약자별수익률조회 응답부")
public class MciIbBnc01007Res implements IMessageObject {
    @MessageField(id = "WOMSGCODE", name = "결과코드", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOMSGCODE;

    @MessageField(id = "WOMSGCONT", name = "결과메세지", length = 300, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOMSGCONT;

    @MessageField(id = "WOCONTNM", name = "계약자명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOCONTNM;

    @MessageField(id = "WOCONTSSN", name = "계약자실명번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String WOCONTSSN;

    @MessageField(id = "WOPRODNMSC", name = "상품명", length = 60, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOPRODNMSC;

    @MessageField(id = "WOPAYPREM", name = "납입보험료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String WOPAYPREM;

    @MessageField(id = "WOPAYNUM", name = "납입회차", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOPAYNUM;

    @MessageField(id = "WOCNTRTSTNM", name = "계약상태명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOCNTRTSTNM;

    @MessageField(id = "WOCONIL", name = "계약일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String WOCONIL;

    @MessageField(id = "WOMANIL", name = "만기일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOMANIL;

    @MessageField(id = "WOPAYMTHDNAME", name = "납입주기명", length = 60, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOPAYMTHDNAME;

    @MessageField(id = "WOTOTNAPPREM", name = "총납입보험료", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String WOTOTNAPPREM;

    @MessageField(id = "WOLCPYMT", name = "최종납입회차", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOLCPYMT;

    @MessageField(id = "WORTNAMT", name = "해약환급금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String WORTNAMT;

    @MessageField(id = "WORTNRATE", name = "해약환급율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String WORTNRATE;

    @MessageField(id = "WOOPTINFO", name = "옵션정보", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOOPTINFO;

    @MessageField(id = "WOSPACCTDESC2", name = "특별계정투입누계설명2", length = 500, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOSPACCTDESC2;

    @MessageField(id = "WOPGAAKDESC2", name = "평가금액설명2", length = 500, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOPGAAKDESC2;

    @MessageField(id = "WOETCDESC1", name = "기타설명1", length = 200, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOETCDESC1;

    @MessageField(id = "WOETCDESC2", name = "기타설명2", length = 200, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOETCDESC2;

    @MessageField(id = "WOETCDESC3", name = "기타설명3", length = 200, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOETCDESC3;

    @MessageField(id = "WOMODGB", name = "모드구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOMODGB;

    @MessageField(id = "WOJRPGSU1", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOJRPGSU1;

    @MessageField(id = "WOGRINF1", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01007Res/WOJRPGSU1")
    private List<WOGRINF1> WOGRINF1;

    @Data
    public static class WOGRINF1 implements IMessageObject {

        @MessageField(id = "WOFNDNA", name = "펀드명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOFNDNA;

        @MessageField(id = "WOFNDRATE", name = "펀드분담율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer WOFNDRATE;

        @MessageField(id = "WOSPACCTAMT", name = "특별계정투입금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOSPACCTAMT;

        @MessageField(id = "WOGIJGGKJYIL", name = "기준가격적용일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
        private String WOGIJGGKJYIL;

        @MessageField(id = "WOGIJUNGA", name = "기준가", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOGIJUNGA;

        @MessageField(id = "WONUJACCT", name = "누적구좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WONUJACCT;

        @MessageField(id = "WOPGAAK", name = "평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOPGAAK;

    }

    @MessageField(id = "WOJRPGSU2", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOJRPGSU2;

    @MessageField(id = "WOGRINF2", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01007Res/WOJRPGSU2")
    private List<WOGRINF2> WOGRINF2;

    @Data
    public static class WOGRINF2 implements IMessageObject {

        @MessageField(id = "WOGIJGGKJYIL1", name = "기준가격적용일자1", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
        private String WOGIJGGKJYIL1;

        @MessageField(id = "WOGIJGCHL", name = "기준가격산출일", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
        private String WOGIJGCHL;

        @MessageField(id = "WODRVDDS", name = "운용일수", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WODRVDDS;

        @MessageField(id = "WOGIJUNGA2", name = "기준가2", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOGIJUNGA2;

        @MessageField(id = "WOPRFTRATE", name = "기간수익률", length = 18, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOPRFTRATE;

        @MessageField(id = "WOW110", name = "1주일수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOW110;

        @MessageField(id = "WOM110", name = "1개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOM110;

        @MessageField(id = "WOM310", name = "3개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOM310;

        @MessageField(id = "WOM610", name = "6개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOM610;

        @MessageField(id = "WOM910", name = "9개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOM910;

        @MessageField(id = "WOY110", name = "1년수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOY110;

        @MessageField(id = "WORLPRRATE", name = "설정이후수익률", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WORLPRRATE;

        @MessageField(id = "WOWECO1", name = "1주일수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOWECO1;

        @MessageField(id = "WOMECO1", name = "1개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOMECO1;

        @MessageField(id = "WOMECO3", name = "3개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOMECO3;

        @MessageField(id = "WOMECO6", name = "6개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOMECO6;

        @MessageField(id = "WOMEC09", name = "9개월수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOMEC09;

        @MessageField(id = "WOYECO1", name = "1년수익률", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOYECO1;

    }

    @MessageField(id = "WOJRPGSU3", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOJRPGSU3;

    @MessageField(id = "WOGRINF3", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01007Res/WOJRPGSU3")
    private List<WOGRINF3> WOGRINF3;

    @Data
    public static class WOGRINF3 implements IMessageObject {

        @MessageField(id = "WOOPTGBNA", name = "옵션구분명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOOPTGBNA;

        @MessageField(id = "WOOPTAK1", name = "옵션금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOOPTAK1;

        @MessageField(id = "WOOPTAK3", name = "옵션금액3", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOOPTAK3;

    }

    @MessageField(id = "WOJRPGSU4", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOJRPGSU4;

    @MessageField(id = "WOGRINF4", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01007Res/WOJRPGSU4")
    private List<WOGRINF4> WOGRINF4;

    @Data
    public static class WOGRINF4 implements IMessageObject {

        @MessageField(id = "WOFNDBKIL", name = "펀드변경일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
        private String WOFNDBKIL;

        @MessageField(id = "WOFNDBKNY", name = "펀드변경내역", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOFNDBKNY;

    }

    @MessageField(id = "WOJRPGSU5", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOJRPGSU5;

    @MessageField(id = "WOGRINF5", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01007Res/WOJRPGSU5")
    private List<WOGRINF5> WOGRINF5;

    @Data
    public static class WOGRINF5 implements IMessageObject {

        @MessageField(id = "WOBKJFNDNA", name = "변경전펀드명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOBKJFNDNA;

        @MessageField(id = "WOBKJFNDRATE", name = "변경전비중", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOBKJFNDRATE;

        @MessageField(id = "WOBKHFNDNM", name = "변경후펀드명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOBKHFNDNM;

        @MessageField(id = "WOBKHFNDRATE", name = "변경후비중", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOBKHFNDRATE;

    }

    @MessageField(id = "WOJRPGSU6", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer WOJRPGSU6;

    @MessageField(id = "WOGRINF6", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbBnc01007Res/WOJRPGSU6")
    private List<WOGRINF6> WOGRINF6;

    @Data
    public static class WOGRINF6 implements IMessageObject {

        @MessageField(id = "WOBKJFNDNA1", name = "변경전펀드명1", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOBKJFNDNA1;

        @MessageField(id = "WOBKJFNDRATE1", name = "변경전비중1", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOBKJFNDRATE1;

        @MessageField(id = "WOBKHFNDNM1", name = "변경후펀드명1", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String WOBKHFNDNM1;

        @MessageField(id = "WOBKHFNDRATE1", name = "변경후비중1", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String WOBKHFNDRATE1;

    }

    @MessageField(id = "WOSMNO", name = "실명번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String WOSMNO;

    @MessageField(id = "WOCMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String WOCMFNA;

    @MessageField(id = "WOSENO", name = "증권번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOSENO;

    @MessageField(id = "WOINSUCODE", name = "보험사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOINSUCODE;

    @MessageField(id = "WOGIJGGKJYIL2", name = "기준가격적용일자2", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String WOGIJGGKJYIL2;

    @MessageField(id = "WOCHNNLGB", name = "채널구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WOCHNNLGB;

}
