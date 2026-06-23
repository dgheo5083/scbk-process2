package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSendAcctAuthResponse", description = "계좌인증 송금 응답 DTO", type = Type.RESPONSE)
public class IdtOcrSendAcctAuthResponse implements IMessageObject {
	@MessageField(id = "sendYn", name = "전송여부")
	private String sendYn;
}
