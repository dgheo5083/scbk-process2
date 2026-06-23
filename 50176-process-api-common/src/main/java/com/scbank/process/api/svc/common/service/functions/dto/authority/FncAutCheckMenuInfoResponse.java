package com.scbank.process.api.svc.common.service.functions.dto.authority;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncAutCheckMenuInfoResponse", description = "메뉴권한체크 응답 DTO", type = Type.RESPONSE)
public class FncAutCheckMenuInfoResponse implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MessageField(id = "resultCd", name = "응답코드", defaultValue = "")
	private String resultCd;
}
