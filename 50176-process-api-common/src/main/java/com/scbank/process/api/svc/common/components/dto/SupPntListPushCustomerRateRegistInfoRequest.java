package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PUSH 고객 환율 등록 정보 조회
 */
@Data
@IntegrationMessage(id = "SupPntListPushCustomerRateRegistInfoRequest", type = Type.REQUEST)
public class SupPntListPushCustomerRateRegistInfoRequest implements IMessageObject {

    @MessageField(id = "serno", name = "")
    private String serno;

}
