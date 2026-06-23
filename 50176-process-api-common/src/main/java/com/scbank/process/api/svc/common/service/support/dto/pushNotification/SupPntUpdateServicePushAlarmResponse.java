package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 서비스 알림 동의 등록/수정
 */
@Data
@IntegrationMessage(id = "SupPntUpdateServicePushAlarmResponse", type = Type.RESPONSE)
public class SupPntUpdateServicePushAlarmResponse implements IMessageObject {

}
