package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 생체인증 거래결과 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFidoResultResponse", type = Type.RESPONSE)
public class CrtFctGetFidoResultResponse implements IMessageObject {

	@MessageField(id = "resultCode", name = "resultCode")
	private String resultCode;

	@MessageField(id = "resultMsg", name = "resultMsg")
	private String resultMsg;

	@MessageField(id = "resultData", name = "resultData")
	private String resultData;

	@MessageField(id = "trResultConfirmYn", name = "trResultConfirmYN")
	private String trResultConfirmYn;

}