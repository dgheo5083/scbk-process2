package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 등록상태 조회
 */
@Data
@IntegrationMessage(id = "CrtDctGetDigitalcertRegStatusResponse", type = Type.RESPONSE)
public class CrtDctGetDigitalcertRegStatusResponse implements IMessageObject {

	@MessageField(id = "acceptedSize", name = "허용인증장치수")
	private int acceptedSize;

	@MessageField(id = "acceptedString", name = "허용인증장치목록")
	private String acceptedString;

	@MessageField(id = "acceptedJsonString", name = "허용인증장치목록")
	private String acceptedJsonString;

	@MessageField(id = "registeredSize", name = "등록인증장치수")
	private int registeredSize;

	@MessageField(id = "registeredString", name = "등록인증장치목록")
	private String registeredString;

	@MessageField(id = "registeredJsonString", name = "등록인증장치목록")
	private String registeredJsonString;

	@MessageField(id = "issuedCertListSize", name = "발급인증장치수")
	private int issuedCertListSize;

	@MessageField(id = "issuedCertListString", name = "발급인증장치목록")
	private String issuedCertListString;

	@MessageField(id = "issuedCertListJsonString", name = "발급인증장치목록")
	private String issuedCertListJsonString;

	@MessageField(id = "birth", name = "생년월일")
	private String birth;

	@MessageField(id = "teleOne", name = "전화번호1")
	private String teleOne;

	@MessageField(id = "teleTwo", name = "전화번호2")
	private String teleTwo;

	@MessageField(id = "teleThree", name = "전화번호3")
	private String teleThree;

}