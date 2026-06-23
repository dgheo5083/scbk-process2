package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1sms01S002Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "완료시 SMS발송 응답 전문")
public class Ti1sms01S002Res implements IMessageObject {

	// 개별부 필드 없음

}
