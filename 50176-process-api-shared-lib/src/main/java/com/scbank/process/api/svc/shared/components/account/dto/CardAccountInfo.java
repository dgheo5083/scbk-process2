package com.scbank.process.api.svc.shared.components.account.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "CardAccountInfo", type = Type.RESPONSE, description = "카드계좌목록조회 응답")
public class CardAccountInfo implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "cardType", name = "카드종류")
    private String cardType;

    @MessageField(id = "familyType", name = "본인가족구분")
    private String familyType;

    @MessageField(id = "paymentDate", name = "결제일자")
    private String paymentDate;

    @MessageField(id = "openDate", name = "개설일자")
    private String openDate;

    @MessageField(id = "period", name = "유효기간")
    private String period;

    @MessageField(id = "checkCardType", name = "체크카드구분")
    private String checkCardType;

    @MessageField(id = "coOperCardCode", name = "제휴카드코드")
    private String coOperCardCode;

    @MessageField(id = "drawAcctName", name = "계좌명")
    private String drawAcctName;
}
