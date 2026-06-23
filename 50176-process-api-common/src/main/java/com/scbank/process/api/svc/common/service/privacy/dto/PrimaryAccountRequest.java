package com.scbank.process.api.svc.common.service.privacy.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "PrimaryAccountRequest", type = Type.REQUEST)
public class PrimaryAccountRequest implements IMessageObject {

	@MessageField(id = "serviceId", name = "serviceId")
    private String serviceId;
	
	@MessageField(id = "noLogin", name = "noLogin")
	private String noLogin;
    
	@MessageField(id = "custNo", name = "custNo")
    private String custNo;
    
	@MessageField(id = "yocdcok", name = "yocdcok")
    private String yocdcok;
    
	@MessageField(id = "userId", name = "userId")
    private String userId;
    
	@MessageField(id = "selAcctNum", name = "selAcctNum")
    private String selAcctNum;
    
	@MessageField(id = "acctBb", name = "acctBb")
    private String acctBb;
    
	@MessageField(id = "subFAcctNum", name = "subFAcctNum")
    private String subFAcctNum;
    
	@MessageField(id = "subSAcctNum", name = "subSAcctNum")
    private String subSAcctNum;
    
	@MessageField(id = "subTAcctNum", name = "subTAcctNum")
    private String subTAcctNum;
}