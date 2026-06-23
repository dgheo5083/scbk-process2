
package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListCardLoanRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 장기카드대출 거래내역조회 API")
public class ListCardLoanRequest implements IMessageObject {

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    @MessageField(id = "orgName", name = "기관명")
    private String orgName;

    @MessageField(id = "reqNo", name = "")
    private String reqNo;

    @MessageField(id = "nextKey", name = "다음페이지 KEY")
    private String nextKey;

    @MessageField(id = "limitCnt", name = "페이지건수")
    private String limitCnt;

    @MessageField(id = "hdFlag", name = "현대카드 플래그")
    private String hdFlag;

    @MessageField(id = "loanLongAmt", name = "장기카드대출 결제예정금액 ")
    private String loanLongAmt;

    @MessageField(id = "cardId", name = "카드 id")
    private String cardId;

    @MessageField(id = "searchMonth", name = "조회년월")
    private String searchMonth;

    @MessageField(id = "loanNo", name = "대출번호")
    private String loanNo;

}
