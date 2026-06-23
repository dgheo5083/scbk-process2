package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "CustInfoInquiryRequest", type = Type.REQUEST)
public class CustCddInfoInquiryRequest implements IMessageObject {
	@MessageField(id = "isFinal", name = "최종처리여부")
	private String isFinal;
	
	@MessageField(id = "yijhgb", name = "거래구분")
	private String yijhgb;
	
	@MessageField(id = "yifirst", name = "첫거래여부")
	private String yifirst;
}
