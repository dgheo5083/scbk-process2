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
@IntegrationMessage(id = "CbIbf01H10200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "First환전예약서비스등록")
public class CbIbf01H10200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIREFNO", name = "예약번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIREFNO;

    @MessageField(id = "YIGUBUN", name = "흐름분류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBUN;

    @MessageField(id = "YIGOGAK", name = "고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGOGAK;

    @MessageField(id = "YIDRIL", name = "등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIDRIL;

    @MessageField(id = "YITIME", name = "등록시간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YITIME;

    @MessageField(id = "YIBRNO", name = "영업점코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIBRNO;

    @MessageField(id = "YIYYJR", name = "예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYYJR;

    @MessageField(id = "YITONG", name = "예약통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONG;

    @MessageField(id = "YIGMAK", name = "예약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIGMAK;

    @MessageField(id = "YIRATE", name = "예약환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIRATE;

    @MessageField(id = "YIEXDAY", name = "예약만기일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIEXDAY;

    @MessageField(id = "YIWGJNO", name = "원화지급계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIWGJNO;

    @MessageField(id = "YIFGJNO", name = "외화지급계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIFGJNO;

    @MessageField(id = "YIPASS", name = "지급계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private Integer YIPASS;

    @MessageField(id = "EMAIL", name = "E-mail", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EMAIL;

    @MessageField(id = "YICGRATE", name = "체결된환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICGRATE;

}
