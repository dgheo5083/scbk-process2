package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * OTP 이용등록/해제
 */
@Data
@IntegrationMessage(id = "AthMgtAuthorizeOtpRequest", type = Type.REQUEST)
public class AthMgtAuthorizeOtpRequest implements IMessageObject {

	@MessageField(id = "yiGUBUN", name = "거래구분")
	private String yiGUBUN;

	@MessageField(id = "inClassCode", name = "등록해제구분용")
	private String inClassCode;

	@MessageField(id = "yiVDCD", name = "벤더코드")
	private String yiVDCD;

	@MessageField(id = "yiOTPNO", name = "OTP일련번호")
	private String yiOTPNO;

	@MessageField(id = "cgAcctNum", name = "계좌번호")
	private String cgAcctNum;

	@MessageField(id = "signData", name = "전자서명값")
	private String signData;
	
	@MessageField(id = "bbKeypadNum", name = "bbKeypadNum")
	private String bbKeypadNum;

}