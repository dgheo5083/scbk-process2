package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03H90300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "모바일OTP 비밀키, 랜덤질의값 조회 요청 전문")
public class CbTbs03H90300Req implements IMessageObject {

	@MessageField(id = "YIUSERID", name = "이용자번호(빈값세팅)", length = 10, masking = true, maskingType = "01")
	private String YIUSERID;

	@MessageField(id = "YIOTPNO", name = "OTP일련번호", length = 12, masking = true, maskingType = "01")
	private String YIOTPNO;

	@MessageField(id = "YIPINNO", name = "서버PIN번호", length = 6, masking = true, maskingType = "03")
	private String YIPINNO; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "YIDVINF", name = "디바이스식별자", length = 64)
	private String YIDVINF;

	@MessageField(id = "YIDVGBN", name = "디바이스구분", length = 2)
	private String YIDVGBN;

	@MessageField(id = "YIOTPVDGB", name = "OTP벤더구분", length = 3)
	private String YIOTPVDGB;

	@MessageField(id = "YIPRECHK", name = "MOTP사전검증", length = 1)
	private String YIPRECHK;

}
