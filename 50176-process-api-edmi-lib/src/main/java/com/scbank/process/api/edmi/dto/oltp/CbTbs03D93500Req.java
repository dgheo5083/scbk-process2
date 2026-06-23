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
@IntegrationMessage(id = "CbTbs03D93500Req", type = Type.REQUEST, description = "입금지정계좌")
public class CbTbs03D93500Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YIGUBN", name = "서비스구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBN;

    @MessageField(id = "YIMUSEHAN", name = "미지정이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private BigDecimal YIMUSEHAN;

    @MessageField(id = "DepBankCode0", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode0;

    @MessageField(id = "DepAcctNum0", name = "계좌번호", length = 14, align = AlignType.LEFT)
    private String DepAcctNum0;

    @MessageField(id = "DepBankCode1", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode1;

    @MessageField(id = "DepAcctNum1", name = "계좌번호", length = 14, align = AlignType.LEFT)
    private String DepAcctNum1;

    @MessageField(id = "DepBankCode2", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode2;

    @MessageField(id = "DepAcctNum2", name = "계좌번호", length = 14, align = AlignType.LEFT)
    private String DepAcctNum2;

    @MessageField(id = "DepBankCode3", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode3;

    @MessageField(id = "DepAcctNum3", name = "계좌번호", length = 14, align = AlignType.LEFT)
    private String DepAcctNum3;

    @MessageField(id = "DepBankCode4", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode4;

    @MessageField(id = "DepAcctNum4", name = "계좌번호", length = 14, align = AlignType.LEFT)
    private String DepAcctNum4;

    @MessageField(id = "DepBankCode5", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepBankCode5;

    @MessageField(id = "DepAcctNum5", name = "계좌번호", length = 14, align = AlignType.LEFT)
    private String DepAcctNum5;
}