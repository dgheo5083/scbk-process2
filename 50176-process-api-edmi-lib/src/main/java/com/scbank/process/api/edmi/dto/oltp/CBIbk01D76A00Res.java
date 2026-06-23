package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CBIbk01D76A00Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "모바일OTP 가입 및 해지 조회 응답 전문")
public class CBIbk01D76A00Res implements IMessageObject {

	@MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String YOUSID;

	@MessageField(id = "YIMOTPNO", name = "MOTP번호", length = 4)
	private String YIMOTPNO;

	@MessageField(id = "YOCVMYN", name = "가입여부", length = 1)
	private String YOCVMYN;

	@MessageField(id = "YOCVMHP", name = "등록일해지일", length = 12, padding = StringUtils.ZERO)
	private String YOCVMHP;

}
