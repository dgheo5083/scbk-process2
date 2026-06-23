package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbf01H52500Res", type = Type.RESPONSE, description = "외화보통예금/당좌예금지급")
public class CbIbf01H52500Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "acceptNo", name = "접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String acceptNo;

    @MessageField(id = "custName", name = "예금주(고객명)", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String custName;

    @MessageField(id = "frPayAcctNo", name = "외화출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String frPayAcctNo;

    @MessageField(id = "payFrAmt", name = "출금외화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String payFrAmt;

    @MessageField(id = "payAfterFrAmt", name = "출금후외화잔액", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String payAfterFrAmt;

    @MessageField(id = "applyCurr", name = "적용환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String applyCurr;

    @MessageField(id = "wonCurrRcvAcctNo", name = "원화입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String wonCurrRcvAcctNo;

    @MessageField(id = "payFrCurrCode", name = "해약계좌통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String payFrCurrCode;

    @MessageField(id = "rcvWonAmt", name = "입금원화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String rcvWonAmt;

    @MessageField(id = "rcvAfterWonAmt", name = "입금후원화잔액", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String rcvAfterWonAmt;

    @MessageField(id = "specialAmt", name = "우대금액", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String specialAmt;

    @MessageField(id = "YOUSAK", name = "대미환산출금금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSAK;

    @MessageField(id = "TTbuyAmt", name = "전신환매입율해당액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TTbuyAmt;

    @MessageField(id = "cashBuyRate", name = "현찰매입율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String cashBuyRate;

    @MessageField(id = "frCashAmt", name = "외화현찰해당액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String frCashAmt;

    @MessageField(id = "YOINAME", name = "입금계좌예금주(고객명)", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOINAME;

    @MessageField(id = "YOHYUDRT", name = "환율우대율", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHYUDRT;
}
