package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 디지털인증서 등록상태 조회
 */
@Data
@IntegrationMessage(id = "CrtDctGetDigitalcertRegStatusRequest", type = Type.REQUEST)
public class CrtDctGetDigitalcertRegStatusRequest implements IMessageObject {

	@MessageField(id = "telNo1", name = "전화번호1", defaultValue = "")
	private String telNo1;

	@MessageField(id = "telNo2", name = "전화번호2", defaultValue = "")
	private String telNo2;

	@MessageField(id = "telNo3", name = "전화번호3", defaultValue = "")
	private String telNo3;

	@MessageField(id = "email1", name = "이메일1", defaultValue = "")
	private String email1;

	@MessageField(id = "email2", name = "이메일2", defaultValue = "")
	private String email2;

	@MessageField(id = "first", defaultValue = "")
	private String first;
	
	@MessageField(id = "refresh", defaultValue = "")
	private String refresh;
	
	@MessageField(id = "certCode", defaultValue = "")
	private String certCode;
	
	@MessageField(id = "issueBio")
	private String issueBio;
	@MessageField(id = "issueFaceid")
	private String issueFaceid;
	@MessageField(id = "issuePin")
	private String issuePin;
}