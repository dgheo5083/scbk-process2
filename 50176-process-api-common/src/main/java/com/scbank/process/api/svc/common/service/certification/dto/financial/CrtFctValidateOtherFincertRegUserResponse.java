package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 타행금융인증서 등록 본인확인
 */
@Data
@IntegrationMessage(id = "CrtFctValidateOtherFincertRegUserResponse", type = Type.RESPONSE)
public class CrtFctValidateOtherFincertRegUserResponse implements IMessageObject {

	@MessageField(id = "yoLRSOTGB", name = "개인정보 노출 여부 = 1 차단")
	private String yoLRSOTGB;

}