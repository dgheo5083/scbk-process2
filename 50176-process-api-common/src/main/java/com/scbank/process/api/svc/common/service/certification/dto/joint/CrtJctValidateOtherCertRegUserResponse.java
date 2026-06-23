package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 타기관 등록 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateOtherCertRegUserResponse", type = Type.RESPONSE)
public class CrtJctValidateOtherCertRegUserResponse implements IMessageObject {

	@MessageField(id = "yoLRSOTGB", name = "개인정보노출구분 값 = 1 차단")
	private String yoLRSOTGB;

}