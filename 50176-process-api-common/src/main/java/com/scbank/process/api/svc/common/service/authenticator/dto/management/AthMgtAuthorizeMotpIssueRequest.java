package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 모바일OTP 발급 본거래
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeMotpIssueRequest", type = Type.REQUEST)
public class AthMgtAuthorizeMotpIssueRequest implements IMessageObject {

	@MessageField(id = "userId", name = "이용자아이디")
	private String userId;

	@MessageField(id = "yiGJNO", name = "출금계좌번호")
	private String yiGJNO;

	@MessageField(id = "yiSCNO", name = "폐기할 안전카드 번호")
	private String yiSCNO;

	@MessageField(id = "yiICHONCEHD", name = "이체1회한도")
	private String yiICHONCEHD;

	@MessageField(id = "yiICHDAYHD", name = "이체1일한도")
	private String yiICHDAYHD;

	@MessageField(id = "yiDVINF", name = "모바일OTP인증 Device Id")
	private String yiDVINF;

	@MessageField(id = "yiDVGBN", name = "모바일OTP인증 Device Cd")
	private String yiDVGBN;

	@MessageField(id = "pinBb01", name = "모바일OTP PIN비밀번호")
	private String pinBb01;

	@MessageField(id = "pinBb02", name = "모바일OTP PIN비밀번호 확인")
	private String pinBb02;

	@MessageField(id = "yiOTPVDGB", name = "OTP벤더구분")
	private String yiOTPVDGB;

	@MessageField(id = "clientRnd", name = "Client랜덤값")
	private String clientRnd;

}