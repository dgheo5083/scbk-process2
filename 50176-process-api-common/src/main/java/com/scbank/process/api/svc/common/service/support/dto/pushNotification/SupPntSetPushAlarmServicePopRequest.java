package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 푸시 알림 서비스 설정 팝업
 */
@Data
@IntegrationMessage(id = "SupPntSetPushAlarmServicePopRequest", type = Type.REQUEST)
public class SupPntSetPushAlarmServicePopRequest implements IMessageObject {

}
