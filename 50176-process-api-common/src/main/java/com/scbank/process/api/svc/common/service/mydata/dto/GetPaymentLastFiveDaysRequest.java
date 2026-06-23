package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetPaymentLastFiveDaysRequest", type = Type.REQUEST, description = "마이데이터 > 최근 5일 결제금액 조회 API")
public class GetPaymentLastFiveDaysRequest implements IMessageObject {

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    // @MessageField(id = "signTime", name = "")
    // private String signTime;

}
