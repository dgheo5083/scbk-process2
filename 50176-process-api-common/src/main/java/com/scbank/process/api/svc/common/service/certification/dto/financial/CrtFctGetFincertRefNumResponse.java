package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 금융인증서 생체인증 등록허용 장치 목록 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFincertRefNumResponse", type = Type.RESPONSE)
public class CrtFctGetFincertRefNumResponse implements IMessageObject {

	@MessageField(id = "refNum", name = "참조번호")
	private String refNum;

	@MessageField(id = "appCode", name = "인가코드")
	private String appCode;

}