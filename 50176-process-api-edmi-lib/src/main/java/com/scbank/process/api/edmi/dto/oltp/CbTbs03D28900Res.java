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
@IntegrationMessage(id = "CbTbs03D28900Res", type = Type.RESPONSE, description = "착오송금 본처리 응답부")
public class CbTbs03D28900Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGRIL;

    @MessageField(id = "YOOGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOGJNO;

    @MessageField(id = "YOJINM", name = "지급인명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJINM;

    @MessageField(id = "YOIBKCD", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIBKCD;

    @MessageField(id = "YOBKNM", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBKNM;

    @MessageField(id = "YOTICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTICGM;

    @MessageField(id = "YOTSSR", name = "총수수료", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOTSSR;

    @MessageField(id = "YOSCNM", name = "출금통장표시내용", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSCNM;

    @MessageField(id = "YOGOYU", name = "반환고유번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGOYU;

    @MessageField(id = "YOCGIL", name = "청구일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCGIL;

    @MessageField(id = "YOCGSAYU", name = "청구사유", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCGSAYU;

    @MessageField(id = "YOWGRIL", name = "원거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOWGRIL;

    @MessageField(id = "YOWCGAK", name = "청구금액", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOWCGAK;

    @MessageField(id = "YOJANAK", name = "출금후 잔액", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOJANAK;

    @MessageField(id = "YOBULYN", name = "불능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBULYN;

}
