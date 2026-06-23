
package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetMostSpentLastMonthResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 지난달 가장 많이 쓴 가맹점 및 금액 조회 API")
public class GetMostSpentLastMonthResponse implements IMessageObject {

    @MessageField(id = "useAmt", name = "이용금액")
    private String useAmt;

    @MessageField(id = "mrchntNm", name = "가맹점명")
    private String mrchntNm;

    @MessageField(id = "prevMonth", name = "prevMonth")
    private String prevMonth;

}
