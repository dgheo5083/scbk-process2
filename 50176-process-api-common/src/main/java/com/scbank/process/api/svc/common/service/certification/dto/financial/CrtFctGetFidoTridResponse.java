package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 생체인증 TRID 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFidoTridResponse", type = Type.RESPONSE)
public class CrtFctGetFidoTridResponse implements IMessageObject {

	@MessageField(id = "resultCode", name = "응답코드")
	private String resultCode;

	@MessageField(id = "resultMsg", name = "응답메시지")
	private String resultMsg;

	@MessageField(id = "trId", name = "trId")
	private String trId;

	@MessageField(id = "svcTrId", name = "svcTrId")
	private String svcTrId;

	@MessageField(id = "userBankingId", name = "USER_BANKING_ID")
	private String userBankingId;

}