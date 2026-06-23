package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "LongTranInfoDto", type = Type.RESPONSE, description = "마이데이터 > 현대카드 장기카드대출 내역 LongTranInfoDto")
public class LongTranInfoDto implements IMessageObject {

    @MessageField(id = "loanNo", name = "loanNo")
    private String loanNo;

    @MessageField(id = "loanCnt", name = "대출갯수")
    private String loanCnt;

    @MessageField(id = "prodNm", name = "대출상품명")
    private String prodNm;

    @MessageField(id = "loanAmt", name = "대출금액")
    private String loanAmt;

    @MessageField(id = "intRate", name = "이자율")
    private String intRate;

    @MessageField(id = "expireDt", name = "만기일")
    private String expireDt;

    @MessageField(id = "balanceAmt", name = "대출잔액")
    private String balanceAmt;

    @MessageField(id = "rdmptnMthdCd", name = "상환방법")
    private String rdmptnMthdCd;

    @MessageField(id = "loanDt", name = "대출일자")
    private String loanDt;

    @MessageField(id = "rdmptnOrgCd", name = "상환기관코드")
    private String rdmptnOrgCd;

    @MessageField(id = "rdmptnOrgNm", name = "상환기관명")
    private String rdmptnOrgNm;

    @MessageField(id = "rdmptnAcctNo", name = "상환게좌")
    private String rdmptnAcctNo;

}
