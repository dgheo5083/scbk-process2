package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 모바일OTP 발급 고객확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateMotpIssueUserRequest", type = Type.REQUEST)
public class AthMgtValidateMotpIssueUserRequest implements IMessageObject {

	@MessageField(id = "screenFlag", name = "screenFlag")
	private String screenFlag;
	
	@MessageField(id = "custNo", name = "custNo")
	private String custNo;
	
	@MessageField(id = "tradNo", name = "tradNo")
	private String tradNo;
	
	@MessageField(id = "bizType", name = "bizType")
	private String bizType;
	
	@MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
	private String scrnDataInfo;
	
	@MessageField(id = "cnnctnWay", name = "cnnctnWay")
	private String cnnctnWay;
	
	@MessageField(id = "clerkNo", name = "clerkNo")
	private String clerkNo;

}