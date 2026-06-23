package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 갱신 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtFctValidateFincertRenewRequest", type = Type.REQUEST)
public class CrtFctValidateFincertRenewRequest implements IMessageObject {

	@MessageField(id = "pkcs7SignedData", name = "전자서명값")
	private String pkcs7SignedData;

	@MessageField(id = "renewCertSerial", name = "인증서 일련번호")
	private String renewCertSerial;

	@MessageField(id = "renewSimpleKeyToken", name = "간편인증등록토큰")
	private String renewSimpleKeyToken;

	@MessageField(id = "connectType", name = "B 고정", defaultValue = "B")
	private String connectType;

}