package com.scbank.process.api.svc.common.service.mydata.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetPaymentLastFiveDaysResponse", type = Type.RESPONSE, description = "마이데이터 > 최근 5일 결제금액 조회 API")
public class GetPaymentLastFiveDaysResponse implements IMessageObject {

    @MessageField(id = "fivedaysList", name = "fivedays_list")
    @RepeatedField
    private List<FiveDaysInfoDto> fivedaysList;

    @MessageField(id = "inTimestamp", name = "inTimestamp")
    private String inTimestamp;

    @MessageField(id = "eCode", name = "에러 코드")
    private String eCode;

    @MessageField(id = "errMsg", name = "에러 메시지")
    private String errMsg;

    @MessageField(id = "currDate", name = "현재년월")
    private String currDate;

    @MessageField(id = "fiveDays", name = "fiveDays")
    private String fiveDays;

    @MessageField(id = "dayArr", name = "dayArr")
    @RepeatedField
    private List<String> dayArr;

    @MessageField(id = "strArray", name = "strArray")
    @RepeatedField
    private List<String> strArray;

}
