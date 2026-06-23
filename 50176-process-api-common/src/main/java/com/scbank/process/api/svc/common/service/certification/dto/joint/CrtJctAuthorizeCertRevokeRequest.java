package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 폐기 완료
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeCertRevokeRequest", type = Type.REQUEST)
public class CrtJctAuthorizeCertRevokeRequest implements IMessageObject {

	@MessageField(id = "certPolicy", name = "인증서 구분")
	private String certPolicy;

	@MessageField(id = "certIssueDate", name = "발급일")
	private String certIssueDate;

	@MessageField(id = "certExpireDate", name = "만료일")
	private String certExpireDate;

}