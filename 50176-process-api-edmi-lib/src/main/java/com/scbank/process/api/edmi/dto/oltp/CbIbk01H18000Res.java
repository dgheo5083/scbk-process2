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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H18000Res", type = Type.RESPONSE, description = "영업점 입금계좌등록 조회 응답 전문")
public class CbIbk01H18000Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGRGB", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRGB;

    @MessageField(id = "YOYCONT", name = "연속거래여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYCONT;

    @MessageField(id = "YOYMSVCD", name = "서비스코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYMSVCD;

    @MessageField(id = "YOYMIPJI", name = "계좌부입지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYMIPJI;

    @MessageField(id = "YOYMGBNK", name = "계좌부은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYMGBNK;

    @MessageField(id = "YOYMGGJN", name = "계좌부계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYMGGJN;

    @MessageField(id = "YOYMIBNK", name = "입금계좌은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYMIBNK;

    @MessageField(id = "YOYMIGJN", name = "입금계좌계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYMIGJN;

    @MessageField(id = "YOYDUMY", name = "DUMMY", length = 31, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYDUMY;

    @MessageField(id = "RCount", name = "출력명세수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "DetailRecord", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H18000Res/RCount")
    private List<DetailRecord> DetailRecord;

    @Data
    public static class DetailRecord implements IMessageObject {
        @MessageField(id = "YOAGUBUN", name = "구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOAGUBUN;

        @MessageField(id = "YOPCJR", name = "PARENT CHILD GUBUN", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPCJR;

        @MessageField(id = "YOPGEJA", name = "계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPGEJA;

        @MessageField(id = "YOPDRIL", name = "등록일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOPDRIL;

        @MessageField(id = "YOPDRBR", name = "등록점－조작자", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPDRBR;

        @MessageField(id = "YOPHJIL", name = "해지일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOPHJIL;

        @MessageField(id = "YOPHJBR", name = "해지점－조작자", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPHJBR;

        @MessageField(id = "YOPIPGB", name = "입급계좌지정여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPIPGB;

        @MessageField(id = "YOPSNGB", name = "송금인명지정방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPSNGB;

        @MessageField(id = "YOPHNDO", name = "자기앞수표결제전한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YOPHNDO;

        @MessageField(id = "YOPGIIL", name = "약정기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOPGIIL;

        @MessageField(id = "YOPDUMMY", name = "PARENT DUMMY", length = 41, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPDUMMY;

        @MessageField(id = "YOCBKCD", name = "은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCBKCD;

        @MessageField(id = "YOCGEJA", name = "계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCGEJA;

        @MessageField(id = "YOCDRIL", name = "등록일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOCDRIL;

        @MessageField(id = "YOCDRBR", name = "등록점 - 조작자", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCDRBR;

        @MessageField(id = "YOCHJIL", name = "해지일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOCHJIL;

        @MessageField(id = "YOCHJBR", name = "해지점 - 조작자", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCHJBR;

        @MessageField(id = "YOCDUMMY", name = "CHILD DUMMY", length = 45, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCDUMMY;

    }

    @MessageField(id = "YODUMMY", name = "DUMMY", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
