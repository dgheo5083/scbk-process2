package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 편한뱅킹 적재
 */
@Data
@IntegrationMessage(id = "RegisterComfyBankRequest", type = Type.REQUEST)
public class RegisterComfyBankRequest implements IMessageObject {    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "userId", name = "")
    private String userId;

    @MessageField(id = "orgSeniorYn", name = "")
    private String orgSeniorYn;
    
    @MessageField(id = "chnSeniorYn", name = "")
    private String chnSeniorYn;
    
    @MessageField(id = "othr1Chk", name = "")
    private String othr1Chk;

   
}
