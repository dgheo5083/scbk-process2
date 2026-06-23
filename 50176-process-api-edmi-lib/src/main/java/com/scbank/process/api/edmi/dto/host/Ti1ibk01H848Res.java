package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "Ti1ibk01H848Res", type = Type.RESPONSE, description = "펀드 통장상세조회 응답부")
public class Ti1ibk01H848Res implements IMessageObject {

	@MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String YOUSID;

	@MessageField(id = "YOHYGMAK", name = "해약금액", length = 15)
	private String YOHYGMAK;

	@MessageField(id = "YOSEGUM", name = "세금합계", length = 13)
	private String YOSEGUM;

	@MessageField(id = "YOBFIJA", name = "세전이자", length = 13)
	private String YOBFIJA;

	@MessageField(id = "YOAFIJA", name = "세후이자", length = 13)
	private String YOAFIJA;

	@MessageField(id = "YOSBXJIGUN", name = "SMARTBOX 지급건수", length = 3)
	private String YOSBXJIGUN;

	@MessageField(id = "YODUMMY", name = "YODUMMY", length = 33)
	private String YODUMMY;
}