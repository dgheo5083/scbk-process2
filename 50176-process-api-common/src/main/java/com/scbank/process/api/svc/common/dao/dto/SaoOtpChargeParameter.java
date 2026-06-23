package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class SaoOtpChargeParameter implements IMessageObject {

	@MessageField(id = "userId", name = "userId")
    private String userId;

	@MessageField(id = "deviceId", name = "deviceId")
    private String deviceId;

	@MessageField(id = "deviceType", name = "deviceType")
    private String deviceType;
	
	@MessageField(id = "tokenId", name = "tokenId")
	private String tokenId;
	
	@MessageField(id = "issueType", name = "issueType")
	private String issueType;
	
	@MessageField(id = "regDeviceUuid", name = "regDeviceUuid")
	private String regDeviceUuid;

}
