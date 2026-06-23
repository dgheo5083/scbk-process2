package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 생체인증 정보 저장
 */
@Data
@IntegrationMessage(id = "CrtFctConfirmFidoResultResponse", type = Type.RESPONSE)
public class CrtFctConfirmFidoResultResponse implements IMessageObject {

	@MessageField(id = "updatedAndroidIdYn", name = "updatedAndroidIdYN")
	private String updatedAndroidIdYn;

}