package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetMostUsedThreeMonthRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 최근 3개월 결제금액 조회 API")
public class GetMostUsedThreeMonthRequest implements IMessageObject {

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

}
