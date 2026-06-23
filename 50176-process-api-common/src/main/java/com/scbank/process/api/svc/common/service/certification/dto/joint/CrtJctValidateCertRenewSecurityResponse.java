package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 갱신 보안매체 검증
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRenewSecurityResponse", type = Type.RESPONSE)
public class CrtJctValidateCertRenewSecurityResponse implements IMessageObject {

	@MessageField(id = "ssrGubun", name = "수수료징구구분(1:징구,2:미징구)")
	private String ssrGubun;

	@MessageField(id = "serial", name = "인증서 serialNumber")
	private String serial;

	@MessageField(id = "certIndex", name = "인증서 index")
	private String certIndex;

	@MessageField(id = "encData", name = "인증서 비밀번호")
	private String encData;

}