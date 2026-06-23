package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 생체인증 인증서 등록상태 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFidoCertificateResponse", type = Type.RESPONSE)
public class CrtFctGetFidoCertificateResponse implements IMessageObject {

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

	@MessageField(id = "regYn", name = "등록여부")
	private String regYn;

	@MessageField(id = "certSeqNum", name = "인증서 일련번호")
	private String certSeqNum;

	@MessageField(id = "simpleKeyToken", name = "간편인증등록토큰")
	private String simpleKeyToken;

	@MessageField(id = "appId", name = "appId")
	private String appId;

	@MessageField(id = "deviceId", name = "deviceId")
	private String deviceId;

	@MessageField(id = "renewBio", name = "갱신여부")
	private String renewBio;

}