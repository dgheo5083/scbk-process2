package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 디지털인증서 발급 본인확인
 */
@Data
@IntegrationMessage(id = "CrtDctValidateDigitalcertIssueUserRequest", type = Type.REQUEST)
public class CrtDctValidateDigitalcertIssueUserRequest implements IMessageObject {

	@MessageField(id = "custJumin1", name = "실명번호")
	private String custJumin1;

	@MessageField(id = "custJumin2", name = "실명번호")
	private String custJumin2;

	@MessageField(id = "acctNum", name = "계좌번호")
	private String acctNum;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;
	
	@MessageField(id = "os", name = "os")
	private String os;
	
	@MessageField(id = "spass", name = "spass")
	private String spass;
	
	@MessageField(id = "fingerprintDevice", name = "fingerprintDevice")
	private String fingerprintDevice;
	
	@MessageField(id = "fingerprintEnrolled", name = "fingerprintEnrolled")
	private String fingerprintEnrolled;
	
	@MessageField(id = "faceDevice", name = "faceDevice")
	private String faceDevice;
	
	@MessageField(id = "faceEnrolled", name = "faceEnrolled")
	private String faceEnrolled;
	
	@MessageField(id = "pinEnabled", name = "pinEnabled")
	private String pinEnabled;
	
	@MessageField(id = "appId", name = "appId")
	private String appId;
	
	@MessageField(id = "deviceId", name = "deviceId")
	private String deviceId;
	
	@MessageField(id = "lockYN", name = "lockYN")
	private String lockYN;
	
	@MessageField(id = "irisDevice", name = "irisDevice")
	private String irisDevice;
	
	@MessageField(id = "irisEnrolled", name = "irisEnrolled")
	private String irisEnrolled;
	
}