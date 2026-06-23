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

@IntegrationMessage(id = "CbIbf01H10300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "목표환율매매(First환전예약) 조회업무")
public class CbIbf01H10300Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YISTGBN", name = "상태구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISTGBN;

    @MessageField(id = "YISTAIL", name = "조회시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YISTAIL;

    @MessageField(id = "YIENDIL", name = "조회끝일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIENDIL;

    @MessageField(id = "YIJHGBN", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGBN;

    @MessageField(id = "YIREFNO", name = "환전예약번호", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIREFNO;

    @MessageField(id = "YIYCIFNO", name = "CIF-NO", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIYCIFNO;

    @MessageField(id = "YIYBRNO", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIYBRNO;

    @MessageField(id = "YIYGOGAK", name = "고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIYGOGAK;

    @MessageField(id = "YIYREFNO", name = "환전예약번호", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIYREFNO;

}
