package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 푸시 알림 서비스 이용 동의 원장 조회 응답
 */
@Data
@IntegrationMessage(id = "GetPushAlarmServiceTermsAgreeResponse", type = Type.RESPONSE)
public class GetPushAlarmServiceTermsAgreeResponse implements IMessageObject {

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

}
