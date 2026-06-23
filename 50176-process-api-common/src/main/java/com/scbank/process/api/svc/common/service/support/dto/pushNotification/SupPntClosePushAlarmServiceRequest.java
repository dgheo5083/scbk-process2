package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 알림 서비스 초기화
 */
@Data
@IntegrationMessage(id = "SupPntClosePushAlarmServiceRequest", type = Type.REQUEST)
public class SupPntClosePushAlarmServiceRequest implements IMessageObject {

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

}
