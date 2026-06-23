package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H92200Req", type = Type.REQUEST, description = "이체한도 조회/변경")
public class CbIbk01H92200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ReferPart", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferPart;

    @MessageField(id = "TimeDepLimt", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TimeDepLimt;

    @MessageField(id = "DayDepLimt", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal DayDepLimt;

    @MessageField(id = "change1TimeDepLimt", name = "변경1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal change1TimeDepLimt;

    @MessageField(id = "change1DayDepLimt", name = "변경1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal change1DayDepLimt;

    @MessageField(id = "MSList", name = "출력요구건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer MSList;

    @MessageField(id = "K_ServiceCode", name = "서비스코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String K_ServiceCode;

    @MessageField(id = "K_DepPayPart", name = "입지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String K_DepPayPart;

    @MessageField(id = "K_DrawBankCode", name = "출금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String K_DrawBankCode;

    @MessageField(id = "K_DrawAcctNum", name = "출금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String K_DrawAcctNum;

    @MessageField(id = "K_DepBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String K_DepBankCode;

    @MessageField(id = "K_DepAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String K_DepAcctNum;
}
