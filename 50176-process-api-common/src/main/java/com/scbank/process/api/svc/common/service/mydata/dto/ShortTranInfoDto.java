package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ShortTranInfoDto", type = Type.RESPONSE, description = "마이데이터 > 현대카드 단기대출서비스 ShortTranInfoDto")
public class ShortTranInfoDto implements IMessageObject {

    @MessageField(id = "loanAmt", name = "대출금액")
    private String loanAmt;

    @MessageField(id = "balanceAmt", name = "잔여금액")
    private String balanceAmt;

    @MessageField(id = "intRate", name = "이자율")
    private String intRate;

    @MessageField(id = "stlmtExpctdDt", name = "결제예정일")
    private String stlmtExpctdDt;

    @MessageField(id = "loanDt", name = "대출일")
    private String loanDt;

    @MessageField(id = "rdmptnOrgCd", name = "상환기관코드")
    private String rdmptnOrgCd;

    @MessageField(id = "rdmptnOrgNm", name = "상환기관명")
    private String rdmptnOrgNm;

    @MessageField(id = "rdmptnAcctNo", name = "상환계좌번호")
    private String rdmptnAcctNo;

}
