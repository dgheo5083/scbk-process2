package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 푸시알림 서비스 가입
 */
@Data
@IntegrationMessage(id = "SupPntApplyPushAlarmServiceResponse", type = Type.RESPONSE)
public class SupPntApplyPushAlarmServiceResponse implements IMessageObject {

}
