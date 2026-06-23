package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CBIbk01D76A00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "모바일OTP 가입 및 해지 조회 요청 전문")
public class CBIbk01D76A00Req implements IMessageObject {

	@MessageField(id = "YIUSERID", name = "이용자번호(빈값세팅)", length = 10, masking = true, maskingType = "01")
	private String YIUSERID;

	@MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "YIMOTPGB", name = "MOTP입력여부", length = 1)
	private String YIMOTPGB; // MOTPNO 입력시 Y SET

	@MessageField(id = "YIMOTPNO", name = "MOTP번호", length = 4)
	private String YIMOTPNO;

	@MessageField(id = "YIIDGB", name = "로그아웃여부", length = 1)
	private String YIIDGB;

	@MessageField(id = "YIJMNO", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String YIJMNO;

}
