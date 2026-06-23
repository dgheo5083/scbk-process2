package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 인증서 효력정지 계좌 검증
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertSuspensionAcctResponse", type = Type.RESPONSE)
public class CrtJctValidateCertSuspensionAcctResponse implements IMessageObject {

	@MessageField(id = "safeCardKind", name = "보안매체 종류")
	private String safeCardKind;

	@MessageField(id = "securityIndex", name = "안전카드 INDEX1 ")
	private String securityIndex;

	@MessageField(id = "securityIndex2", name = "안전카드 INDEX2")
	private String securityIndex2;

	@MessageField(id = "smartOTP", name = "스마트OTP ")
	private String smartOTP;
	
	@MessageField(id = "inforGubun", name = "정보구분")
	private String inforGubun;

}