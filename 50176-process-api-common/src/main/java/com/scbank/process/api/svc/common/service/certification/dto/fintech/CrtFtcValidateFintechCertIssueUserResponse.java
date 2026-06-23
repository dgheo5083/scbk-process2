package com.scbank.process.api.svc.common.service.certification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 핀테크인증서 이용등록 본인확인
 */
@Data
@IntegrationMessage(id = "CrtFtcValidateFintechCertIssueUserResponse", type = Type.RESPONSE)
public class CrtFtcValidateFintechCertIssueUserResponse implements IMessageObject {

	@MessageField(id = "yoLRSOTGB", name = "개인정보노출구분 값 = 1 차단")
	private String yoLRSOTGB;

	@MessageField(id = "etbYn", name = "당행고객여부")
	private String etbYn;

	@MessageField(id = "casaYn", name = "카사 유무")
	private String casaYn;

	@MessageField(id = "ibUserYn", name = "인뱅고객여부")
	private String ibUserYn;

}