package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H892Res", type = Type.RESPONSE, description = "아이디찾기 응답 전문")
public class CbIbk01H892Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10, multiBytes = true)
	private String UserID;

	@MessageField(id = "UserID2", name = "실제이용자번호", length = 10, masking = true, maskingType = "01", multiBytes = true)
	private String UserID2;

	@MessageField(id = "TSPassword", name = "이용자비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "UserType", name = "고객구분.1:인터넷뱅킹가입여부, 2/4:웹회원, 3/5:미고객", length = 1)
	private String UserType;

	@MessageField(id = "CustName", name = "고객명", length = 32, masking = true, maskingType = "04", multiBytes = true)
	private String CustName;

	@MessageField(id = "SafeCardKind", name = "보안카드종류.1:안전카드,2:구OTP,3통합OTP 이외:미사용(계좌검증)", length = 1)
	private String SafeCardKind;

	@MessageField(id = "SafeCardINDEX", name = "안전카드 위치", length = 6)
	private String SafeCardINDEX;

	@MessageField(id = "SafeCardINDEX2", name = "안전카드 위치2", length = 6)
	private String SafeCardINDEX2;

	@MessageField(id = "YODAECH", name = "대출조회회원고객", length = 1)
	private String YODAECH;

	@MessageField(id = "YOSINYO", name = "신용카드조회고객", length = 1)
	private String YOSINYO;

	@MessageField(id = "SmartOTP", name = "스마트OTP", length = 1)
	private String SmartOTP;

	@MessageField(id = "YOCFJMGB", name = "실명증표구분( 1:주민  2:사업자  3:여권  4:외국인등록 )", length = 1)
	private String YOCFJMGB;

	@MessageField(id = "DUMMY", name = "더미", length = 29)
	private String DUMMY;

}
