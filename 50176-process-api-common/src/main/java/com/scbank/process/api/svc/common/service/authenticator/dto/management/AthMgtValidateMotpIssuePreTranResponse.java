package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 모바일OTP 발급 예비거래
 */
@Data
@IntegrationMessage(id = "AthMgtValidateMotpIssuePreTranResponse", type = Type.RESPONSE)
public class AthMgtValidateMotpIssuePreTranResponse implements IMessageObject {

	@MessageField(id = "userId2", name = "사용자ID2")
	private String userId2;

	@MessageField(id = "yoICHONCEHD", name = "이체1회한도")
	private String yoICHONCEHD;

	@MessageField(id = "yoICHDAYHD", name = "이체1일한도")
	private String yoICHDAYHD;

	@MessageField(id = "yoSCNO", name = "폐기할 안전카드 번호")
	private String yoSCNO;

	@MessageField(id = "yoSCGB", name = "보안매체 종류")
	private String yoSCGB;
	
	@MessageField(id = "job", name = "직업")
	private String job;
	
	@MessageField(id = "tradNo", name = "tradNo")
	private String tradNo;
	
	@MessageField(id = "custNo", name = "custNo")
	private String custNo;

}