package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "NfTradPrgrssCustInfoRequest", type = Type.REQUEST)
public class NfTradPrgrssCustInfoRequest {
	
	@MessageField(id = "custNo", name = "고객번호")
	private String custNo;
	
	@MessageField(id = "bizType", name = "업무구분")
	private String bizType;
	
	@MessageField(id = "integratedConselingYn", name = "통화상담여부")
	private String integratedConselingYn;
	
}
