package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetCardListDataRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 상세 정보 조회 API")
public class GetCardListDataRequest implements IMessageObject {

    @MessageField(id = "isRtFlg", name = "새로고침 플레그 (새로고침 : Y , 초기 : N)")
    private String isRtFlg;

}
