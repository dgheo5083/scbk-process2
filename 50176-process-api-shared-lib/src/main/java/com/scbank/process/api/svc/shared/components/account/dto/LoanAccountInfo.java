package com.scbank.process.api.svc.shared.components.account.dto;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "LoanAccountInfo", type = Type.RESPONSE, description = "대출계좌목록조회 응답")
public class LoanAccountInfo implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "assort", name = "종별")
    private String assort;

    @MessageField(id = "depKind", name = "계좌종류")
    private String depKind;

    @MessageField(id = "balSign", name = "잔액부호")
    private String balSign;

    @MessageField(id = "balance", name = "잔액")
    private BigDecimal balance;

    @MessageField(id = "curcy", name = "통화")
    private String curcy;

    @MessageField(id = "loanStartDate", name = "대출신규일")
    private String loanStartDate;

    @MessageField(id = "loanEndDate", name = "대출만기일")
    private String loanEndDate;

    @MessageField(id = "loanRepayPrinAmt", name = "승인한도")
    private BigDecimal loanRepayPrinAmt;

    @MessageField(id = "expectedDate", name = "이자예정일")
    private String expectedDate;

    @MessageField(id = "loanRate", name = "이율")
    private String loanRate;

    @MessageField(id = "drawAcctName", name = "계좌명")
    private String drawAcctName;

    @MessageField(id = "loanAcctKmCd", name = "")
    private String loanAcctKmCd;

    @MessageField(id = "loanKind", name = "")
    private String loanKind;

}
