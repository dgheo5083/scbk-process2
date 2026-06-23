package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 환율 푸쉬알림 등록/수정/삭제
 */
@Data
@IntegrationMessage(id = "SupPntUpdateRatePushAlarmResponse", type = Type.RESPONSE)
public class SupPntUpdateRatePushAlarmResponse implements IMessageObject {

}
