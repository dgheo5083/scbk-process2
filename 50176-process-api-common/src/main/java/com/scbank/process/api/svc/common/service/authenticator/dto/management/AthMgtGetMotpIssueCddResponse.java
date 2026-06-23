package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "AthMgtGetMotpIssueCddResponse", type = Type.RESPONSE)
public class AthMgtGetMotpIssueCddResponse implements IMessageObject {
	
	@MessageField(id = "kcddPollingYn", name = "kcddPollingYn")
	private String kcddPollingYn;
	
	@MessageField(id = "yocddilFlag", name = "yocddilFlag")
	private String yocddilFlag;
	
	@MessageField(id = "idCardCd", name = "idCardCd")
	private String idCardCd;
	
	@MessageField(id = "authntIndCd", name = "authntIndCd")
	private String authntIndCd;
	
	@MessageField(id = "cddReqCd", name = "cddReqCd")
	private String cddReqCd;
	
}