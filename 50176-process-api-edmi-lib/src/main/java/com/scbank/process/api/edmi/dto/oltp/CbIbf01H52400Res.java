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

@IntegrationMessage(id = "CbIbf01H52400Res", type = Type.RESPONSE, description = "외화보통예금/당좌예금입금")
public class CbIbf01H52400Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "acceptNo", name = "접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String acceptNo;

    @MessageField(id = "custName", name = "예금주(고객명)", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String custName;

    @MessageField(id = "wonPayAcctNo", name = "원화출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String wonPayAcctNo;

    @MessageField(id = "payWonAmt", name = "출금원화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String payWonAmt;

    @MessageField(id = "payAfterWonAmt", name = "출금후원화잔액", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String payAfterWonAmt;

    @MessageField(id = "applyCurr", name = "적용환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String applyCurr;

    @MessageField(id = "frRcvAcctNo", name = "외화입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String frRcvAcctNo;

    @MessageField(id = "currCode", name = "해약계좌통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String currCode;

    @MessageField(id = "rcvFrAmt", name = "입금외화금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String rcvFrAmt;

    @MessageField(id = "rcvAfterFrAmt", name = "입금후외화금액", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String rcvAfterFrAmt;

    @MessageField(id = "specialAmt", name = "우대금액", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String specialAmt;

    @MessageField(id = "YOUSAK", name = "대미환산입금금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSAK;

    @MessageField(id = "YOHYUDRT", name = "환율우대율", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHYUDRT;

}
