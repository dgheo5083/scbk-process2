package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * OTP 이용등록/해제 계좌확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateOtpAcctRequest", type = Type.REQUEST)
public class AthMgtValidateOtpAcctRequest implements IMessageObject {

	@MessageField(id = "accountNum", name = "계좌번호")
	private String accountNum;

	@MessageField(id = "bbKeypadNum", name = "계좌비밀번호")
	private String bbKeypadNum;

}