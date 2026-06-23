package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbf01D10600Req", type = Type.REQUEST, captureSystem = "OLTP", description = "환전가능 영업점 조회")
public class CbIbf01D10600Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YITONG", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONG;

    @MessageField(id = "YIGMAK", name = "외화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIGMAK;

    @MessageField(id = "YIGUNSU", name = "입력 영업점 개수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIGUNSU;

    @MessageField(id = "YIBRNO0", name = "점번호0", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO0;

    @MessageField(id = "YIBRNO1", name = "점번호1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO1;

    @MessageField(id = "YIBRNO2", name = "점번호2", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO2;

    @MessageField(id = "YIBRNO3", name = "점번호3", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO3;

    @MessageField(id = "YIBRNO4", name = "점번호4", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO4;

    @MessageField(id = "YIBRNO5", name = "점번호5", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO5;

    @MessageField(id = "YIBRNO6", name = "점번호6", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO6;

    @MessageField(id = "YIBRNO7", name = "점번호7", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO7;

    @MessageField(id = "YIBRNO8", name = "점번호8", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO8;

    @MessageField(id = "YIBRNO9", name = "점번호9", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO9;

    @MessageField(id = "YIBRNO10", name = "점번호10", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO10;

    @MessageField(id = "YIBRNO11", name = "점번호11", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO11;

    @MessageField(id = "YIBRNO12", name = "점번호12", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO12;

    @MessageField(id = "YIBRNO13", name = "점번호13", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO13;

    @MessageField(id = "YIBRNO14", name = "점번호14", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO14;

    @MessageField(id = "YIBRNO15", name = "점번호15", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO15;

    @MessageField(id = "YIBRNO16", name = "점번호16", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO16;

    @MessageField(id = "YIBRNO17", name = "점번호17", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO17;

    @MessageField(id = "YIBRNO18", name = "점번호18", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO18;

    @MessageField(id = "YIBRNO19", name = "점번호19", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO19;

    @MessageField(id = "YIBRNO20", name = "점번호20", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO20;

    @MessageField(id = "YIBRNO21", name = "점번호21", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO21;

    @MessageField(id = "YIBRNO22", name = "점번호22", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO22;

    @MessageField(id = "YIBRNO23", name = "점번호23", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO23;

    @MessageField(id = "YIBRNO24", name = "점번호24", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO24;

    @MessageField(id = "YIBRNO25", name = "점번호25", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO25;

    @MessageField(id = "YIBRNO26", name = "점번호26", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO26;

    @MessageField(id = "YIBRNO27", name = "점번호27", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO27;

    @MessageField(id = "YIBRNO28", name = "점번호28", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO28;

    @MessageField(id = "YIBRNO29", name = "점번호29", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO29;

}
