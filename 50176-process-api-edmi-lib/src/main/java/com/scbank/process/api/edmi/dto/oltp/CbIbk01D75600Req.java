package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01D75600Req", type = Type.REQUEST, captureSystem = "OLTP", description = "기본계좌변경 요청")
public class CbIbk01D75600Req implements IMessageObject {

	@MessageField(id = "YIUSID1", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String YIUSID1;

	@MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03")
	private String YIPASS;

	@MessageField(id = "YIGJWJN", name = "계좌점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIGJWJN;

	@MessageField(id = "YIGJWKM", name = "계좌과목", length = 2)
	private String YIGJWKM;

	@MessageField(id = "YIGJWNO", name = "계좌번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIGJWNO;
	
}