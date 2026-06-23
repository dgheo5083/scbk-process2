package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetMostSpentLastMonthRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 지난달 가장 많이 쓴 가맹점 및 금액 조회 API")
public class GetMostSpentLastMonthRequest implements IMessageObject {

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

}
