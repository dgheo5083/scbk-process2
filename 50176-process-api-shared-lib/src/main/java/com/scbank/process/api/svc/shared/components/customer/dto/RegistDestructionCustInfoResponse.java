package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegistDestructionCustInfoResponse", type = Type.RESPONSE)
public class RegistDestructionCustInfoResponse implements IMessageObject {
	@MessageField(id = "userId", name = "이용자번호")
    private String userId;
	
	@MessageField(id = "yoGrGb", name = "거래구분")
    private String yoGrGb;
	
	@MessageField(id = "yoKuk", name = "국가코드")
    private String yoKuk;
	
	@MessageField(id = "dustYn", name = "파기여부")
    private String dustYn;
	
	@MessageField(id = "newYn", name = "신규여부")
    private String newYn;
}
