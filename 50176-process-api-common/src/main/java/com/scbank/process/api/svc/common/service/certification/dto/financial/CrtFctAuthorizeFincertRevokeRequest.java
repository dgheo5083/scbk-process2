package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 폐기
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeFincertRevokeRequest", type = Type.REQUEST)
public class CrtFctAuthorizeFincertRevokeRequest implements IMessageObject {

	@MessageField(id = "certPolicy", name = "CertPolicy")
	private String certPolicy;

	@MessageField(id = "certIssueDate", name = "발급일")
	private String certIssueDate;

	@MessageField(id = "certExpireDate", name = "만료일")
	private String certExpireDate;

	@MessageField(id = "raGubun", name = "RAGubun")
	private String raGubun;

}