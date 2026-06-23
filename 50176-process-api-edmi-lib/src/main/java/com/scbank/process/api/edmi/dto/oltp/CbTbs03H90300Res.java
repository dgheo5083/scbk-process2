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
@IntegrationMessage(id = "CbTbs03H90300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "모바일OTP 비밀키, 랜덤질의값 조회 응답 전문")
public class CbTbs03H90300Res implements IMessageObject {

	@MessageField(id = "YOUSERID", name = "이용자번호(빈값세팅)", length = 10, masking = true, maskingType = "01")
	private String YOUSERID;

	@MessageField(id = "YOSEKEY", name = "OTP 비밀키 B", length = 64)
	private String YOSEKEY;

	@MessageField(id = "YOMRAND", name = "랜덤질의값(OTP챌린지값)", length = 64)
	private String YOMRAND;

	@MessageField(id = "YOERCNT", name = "PIN 오류횟수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOERCNT;

	@MessageField(id = "YOMOTP", name = "MOTP서비스유무", length = 1)
	private String YOMOTP;

}
