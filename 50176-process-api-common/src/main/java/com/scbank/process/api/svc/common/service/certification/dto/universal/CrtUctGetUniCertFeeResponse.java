package com.scbank.process.api.svc.common.service.certification.dto.universal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 범용인증서 발급 수수료 징수 여부 조회
 */
@Data
@IntegrationMessage(id = "CrtUctGetUniCertFeeResponse", type = Type.RESPONSE)
public class CrtUctGetUniCertFeeResponse implements IMessageObject {

	@MessageField(id = "sgRefNo", name = "참조번호")
	private String sgRefNo;

	@MessageField(id = "sgAuthCode", name = "인가코드")
	private String sgAuthCode;

	@MessageField(id = "sgSsrGubun", name = "수수료 징구 구분")
	private String sgSsrGubun;

	@MessageField(id = "ssrFee", name = "수수료")
	private String ssrFee;

	@MessageField(id = "ssrVat", name = "부가가치세")
	private String ssrVat;

	@MessageField(id = "telNo", name = "전화번호")
	private String telNo;

}