package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03H81800Res", type = Type.RESPONSE, description = "타기관 OTP 등록")
public class CbTbs03H81800Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "YOGUBUN", name = "거래구분", length = 1)
	private String YOGUBUN;

	@MessageField(id = "YOVDCD", name = "벤더코드", length = 3)
	private String YOVDCD;

	@MessageField(id = "YOOTPNO", name = "OTP일련번호", length = 12, masking = true, maskingType = "01")
	private String YOOTPNO;

	@MessageField(id = "YOSTAT", name = "상태코드", length = 1)
	private String YOSTAT;

	@MessageField(id = "YOIRCD", name = "발급기관코드", length = 5)
	private String YOIRCD;

	@MessageField(id = "SmartOTP", name = "스마트OTP", length = 1)
	private String SmartOTP;

	@MessageField(id = "YODUMMY", name = "더미", length = 37)
	private String 더미;

}