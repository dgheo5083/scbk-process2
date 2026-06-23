
package com.scbank.process.api.svc.common.service.mydata.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetMostUsedThreeMonthResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 최근 3개월 결제금액 조회 API")
public class GetMostUsedThreeMonthResponse implements IMessageObject {

    @MessageField(id = "threeMonthList", name = "three_month_list")
    @RepeatedField
    private List<ThreeMonthInfoDto> threeMonthList;

    @MessageField(id = "eCode", name = "에러 코드")
    private String eCode;

    @MessageField(id = "errMsg", name = "에러 메시지")
    private String errMsg;

}
