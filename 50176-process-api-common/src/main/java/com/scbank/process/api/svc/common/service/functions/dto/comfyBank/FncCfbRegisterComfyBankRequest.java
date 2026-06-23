package com.scbank.process.api.svc.common.service.functions.dto.comfyBank;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncCfbRegisterComfyBankRequest", description = "편한뱅킹 정보 등록 요청 DTO", type = Type.REQUEST)
public class FncCfbRegisterComfyBankRequest implements IMessageObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@MessageField(id = "userId", name = "USER ID")
//	private String userId;

//	@MessageField(id = "othr1Chk", name = "")
//	private String othr1Chk;
	
	@MessageField(id = "seniorMode", name = "")
	private String seniorMode;
	
	@MessageField(id = "dbChkYN", name = "DB로직 호출여부")
	private String dbChkYN;
	
	@MessageField(id = "menuId", name = "메뉴 ID")
	private String menuId;
}
