package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrValidateAcctAuthNumberRequest", description = "계좌인증 송금 요청 DTO", type = Type.REQUEST)
public class IdtOcrValidateAcctAuthNumberRequest implements IMessageObject {
	
	@MessageField(id = "bizType", name = "업무구분")
	private String bizType;
	
	@MessageField(id = "authNumber", name = "인증번호")
	private String authNumber;
}
