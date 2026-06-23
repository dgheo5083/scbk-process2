package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "Ti1ibk01H848Req", type = Type.REQUEST, description = "펀드 통장상세조회 요청부")
public class Ti1ibk01H848Req implements IMessageObject {

	@MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String YIUSID;

	@MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03")
	private String YIPASS;

	@MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02")
	private String YIGJNO;
}