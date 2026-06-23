package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 생체인증 인증서 등록상태 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFidoCertificateRequest", type = Type.REQUEST)
public class CrtFctGetFidoCertificateRequest implements IMessageObject {

	@MessageField(id = "certSeqNum", name = "인증서 일련번호", defaultValue = "")
	private String certSeqNum;

	@MessageField(id = "simpleKeyToken", name = "간편인증등록토큰", defaultValue = "")
	private String simpleKeyToken;

	@MessageField(id = "appId", name = "appId", defaultValue = "")
	private String appId;

	@MessageField(id = "deviceId", name = "deviceId", defaultValue = "")
	private String deviceId;

	@MessageField(id = "renewBio", name = "갱신여부", defaultValue = "")
	private String renewBio;
	
	@MessageField(id = "os", name = "os", defaultValue = "")
	private String os;
	
	@MessageField(id = "spass", name = "spass", defaultValue = "")
	private String spass;
	
	@MessageField(id = "fingerprintDevice", name = "fingerprintDevice", defaultValue = "")
	private String fingerprintDevice;
	
	@MessageField(id = "fingerprintEnrolled", name = "fingerprintEnrolled", defaultValue = "")
	private String fingerprintEnrolled;
	
	@MessageField(id = "faceDevice", name = "faceDevice", defaultValue = "")
	private String faceDevice;
	
	@MessageField(id = "faceEnrolled", name = "faceEnrolled", defaultValue = "")
	private String faceEnrolled;
	
	@MessageField(id = "pinEnabled", name = "pinEnabled", defaultValue = "")
	private String pinEnabled;
	
	@MessageField(id = "lockYN", name = "lockYN", defaultValue = "")
	private String lockYN;
	
	@MessageField(id = "irisDevice", name = "irisDevice", defaultValue = "")
	private String irisDevice;
	
	@MessageField(id = "irisEnrolled", name = "irisEnrolled", defaultValue = "")
	private String irisEnrolled;

}