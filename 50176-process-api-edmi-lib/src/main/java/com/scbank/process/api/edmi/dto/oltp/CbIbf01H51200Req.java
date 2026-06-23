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
@IntegrationMessage(id = "CbIbf01H51200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "인터넷환전 내역조회")
public class CbIbf01H51200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIJHGB", name = "지정구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "YISTIL", name = "조회시작일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISTIL;

    @MessageField(id = "YIEDIL", name = "조회종료일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIEDIL;

    @MessageField(id = "YIREFNO", name = "REF-NO", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIREFNO;

    @MessageField(id = "YIMSSU", name = "요구명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIMSSU;

    @MessageField(id = "YIYILJA", name = "연속거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIYILJA;

    @MessageField(id = "YIYJSNO", name = "연속접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIYJSNO;

    @MessageField(id = "YIGUBUN", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBUN;

}
