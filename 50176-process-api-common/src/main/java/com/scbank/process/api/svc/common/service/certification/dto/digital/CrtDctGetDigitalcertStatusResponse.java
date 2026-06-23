package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 조회
 */
@Data
@IntegrationMessage(id = "CrtDctGetDigitalcertStatusResponse", type = Type.RESPONSE)
public class CrtDctGetDigitalcertStatusResponse implements IMessageObject {

	@MessageField(id = "issuedCertListSize", name = "발급인증장치수")
	private int issuedCertListSize;

	@MessageField(id = "issuedCertListJsonArray", name = "발급인증장치목록")
	private String issuedCertListJsonArray;

	@MessageField(id = "issuedCertListJsonString", name = "발급인증장치목록")
	private String issuedCertListJsonString;

	@MessageField(id = "regYn", name = "등록여부")
	private String regYn;

}