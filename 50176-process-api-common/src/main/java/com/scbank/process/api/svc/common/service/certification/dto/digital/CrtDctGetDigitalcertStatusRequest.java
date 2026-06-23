package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 디지털인증서 조회
 */
@Data
@IntegrationMessage(id = "CrtDctGetDigitalcertStatusRequest", type = Type.REQUEST)
public class CrtDctGetDigitalcertStatusRequest implements IMessageObject {
	
	@MessageField(id = "fidoAppId", name = "fidoAppId")
	private String fidoAppId;
	
	@MessageField(id = "fidoDeviceid", name = "fidoDeviceid")
	private String fidoDeviceid;
	
	@MessageField(id = "faceDevice", name = "faceDevice", defaultValue = "N")
	private String faceDevice;
	
	@MessageField(id = "issueBio", name = "issueBio")
	private String issueBio;
	@MessageField(id = "issueFaceid", name = "issueFaceid")
	private String issueFaceid;
	@MessageField(id = "issuePin", name = "issuePin")
	private String issuePin;
}