package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 인증서 효력회복 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRestoreUserRequest", type = Type.REQUEST)
public class CrtJctValidateCertRestoreUserRequest implements IMessageObject {

	@MessageField(id = "userId", name = "사용자 아이디")
	private String userId;

	@MessageField(id = "custJumin1", name = "생년월일")
	private String custJumin1;

	@MessageField(id = "custJumin2", name = "주민번호 뒷자리")
	private String custJumin2;

}