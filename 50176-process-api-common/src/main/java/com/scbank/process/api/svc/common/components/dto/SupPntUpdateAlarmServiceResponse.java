package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * 알림 서비스 등록 및 변경
 */
@Data
@IntegrationMessage(id = "SupPntUpdateAlarmServiceResponse", type = Type.RESPONSE)
public class SupPntUpdateAlarmServiceResponse implements IMessageObject {

}
