package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 휴폐업사실조회
 */
@Data
@IntegrationMessage(id = "AthMgtGetSaupNumOdcloudResponse", type = Type.RESPONSE)
public class AthMgtGetSaupNumOdcloudResponse implements IMessageObject {
	@MessageField(id = "list", name = "응답데이터")
	private String list;

}