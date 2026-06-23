package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 디지털인증서 해지 완료
 */
@Data
@IntegrationMessage(id = "CrtDctConfirmDigitalcertRevokeRequest", type = Type.REQUEST)
public class CrtDctConfirmDigitalcertRevokeRequest implements IMessageObject {

	@MessageField(id = "verifyType", name = "인증서 구분")
	private String verifyType;

}