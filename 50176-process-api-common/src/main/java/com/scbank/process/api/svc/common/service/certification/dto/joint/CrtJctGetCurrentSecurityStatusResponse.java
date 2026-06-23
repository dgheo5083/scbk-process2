package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 인증/보안 현황 조회
 */
@Data
@IntegrationMessage(id = "CrtJctGetCurrentSecurityStatusResponse", type = Type.RESPONSE)
public class CrtJctGetCurrentSecurityStatusResponse implements IMessageObject {

	@MessageField(id = "loginType", name = "로그인 타입")
	private String loginType;

	@MessageField(id = "safeCardKind", name = "보안매체종류")
	private String safeCardKind;

	@MessageField(id = "smartOTP", name = "모바일OTP 여부값")
	private String smartOTP;

	@MessageField(id = "timeDepLimt", name = "1회 이체한도")
	private String timeDepLimt;

	@MessageField(id = "dayDepLimt", name = "1일 이체한도")
	private String dayDepLimt;

	@MessageField(id = "deviceCount", name = "지정단말사용개수")
	private String deviceCount;

	@MessageField(id = "yoDELA", name = "지연이체여부")
	private String yoDELA;
}