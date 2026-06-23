package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 휴폐업사실조회
 */
@Data
@IntegrationMessage(id = "AthMgtGetSaupNumOdcloudRequest", type = Type.REQUEST)
public class AthMgtGetSaupNumOdcloudRequest implements IMessageObject {
	
	@MessageField(id = "saupNumList", name = "사업자등록번호 목록")
	private String saupNumList;
	
}