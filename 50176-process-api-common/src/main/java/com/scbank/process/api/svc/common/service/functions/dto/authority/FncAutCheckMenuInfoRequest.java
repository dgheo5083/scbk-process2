package com.scbank.process.api.svc.common.service.functions.dto.authority;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncAutCheckMenuInfoRequest", description = "메뉴권한 체크 요청 DTO", type = Type.REQUEST)
public class FncAutCheckMenuInfoRequest implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "menuId", name = "메뉴식별자", defaultValue = "")
	private String menuId;
	
	@MessageField(id = "acType", name = "접근제어타입", defaultValue = "")
	private String acType;
	
	@MessageField(id = "forceCheckCode", name = "이용시간 체크 코드", defaultValue = "")
	private String forceCheckCode;
}
