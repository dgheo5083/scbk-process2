package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 발급
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeFincertIssueResponse", type = Type.RESPONSE)
public class CrtFctAuthorizeFincertIssueResponse implements IMessageObject {

	@MessageField(id = "refNum", name = "참조번호")
	private String refNum;

	@MessageField(id = "appCode", name = "인가코드")
	private String appCode;

}