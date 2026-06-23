package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03D24700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "오픈뱅킹 자동이체 실행결과 등록")
public class CbTbs03D24700Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String YIPASS;

    @MessageField(id = "YIJMNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String YIJMNO;

    @MessageField(id = "YIGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIGRIL;

    @MessageField(id = "YISEQ", name = "일자별일련번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISEQ;

    @MessageField(id = "YIDRGB", name = "등록구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDRGB;

    @MessageField(id = "YIICYN", name = "이체성공여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICYN;

    @MessageField(id = "YIICJR", name = "자동이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICJR;

    @MessageField(id = "YIIGJNO", name = "입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGJNO;

    @MessageField(id = "YICGJNO", name = "출금계좌번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICGJNO;

    @MessageField(id = "YIICGM", name = "이체금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIICGM;

    @MessageField(id = "YIC", name = "출금결과정보")
    private YIC YIC;

    @MessageField(id = "YII", name = "입금결과정보")
    private YII YII;

    @MessageField(id = "YIB", name = "출금반환결과정보")
    private YIB YIB;

    @Data
    public static class YIC implements IMessageObject {

        @MessageField(id = "YICST", name = "출금상태", length = 1)
        private String YICST;

        @MessageField(id = "YICGRNO", name = "출금거래고유번호", length = 20)
        private String YICGRNO;

        @MessageField(id = "YICGECD", name = "출금거래응답코드", length = 5)
        private String YICGECD;

        @MessageField(id = "YICBECD", name = "출금은행거래응답코드", length = 3)
        private String YICBECD;
    }

    @Data
    public static class YII implements IMessageObject {

        @MessageField(id = "YIIST", name = "입금상태", length = 1)
        private String YIIST;

        @MessageField(id = "YIIGRNO", name = "입금거래고유번호", length = 20)
        private String YIIGRNO;

        @MessageField(id = "YIIGECD", name = "입금거래응답코드", length = 5)
        private String YIIGECD;

        @MessageField(id = "YIIBECD", name = "입금은행거래응답코드", length = 3)
        private String YIIBECD;
    }

    @Data
    public static class YIB implements IMessageObject {

        @MessageField(id = "YIBST", name = "출금반환상태", length = 1)
        private String YIBST;

        @MessageField(id = "YIBGRNO", name = "출금반환거래고유번호", length = 20)
        private String YIBGRNO;

        @MessageField(id = "YIBGECD", name = "출금반환거래응답코드", length = 5)
        private String YIBGECD;

        @MessageField(id = "YIBBECD", name = "출금반환은행거래응답코드", length = 3)
        private String YIBBECD;
    }

}
