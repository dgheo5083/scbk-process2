package com.scbank.process.api.edmi.dto.host;

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
@IntegrationMessage(id = "Ti1tbs03H904Req", type = Type.REQUEST, captureSystem = "OLTP", description = "모바일OTP 재발급 예비/본처리 요청 전문")
public class Ti1tbs03H904Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "PerBusNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String PerBusNo;

	@MessageField(id = "YIPINNO", name = "서버PIN번호", length = 16, masking = true, maskingType = "03")
	private String YIPINNO; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "YIDVINF", name = "디바이스식별자", length = 64)
	private String YIDVINF;

	@MessageField(id = "YIDVGBN", name = "디바이스구분", length = 2)
	private String YIDVGBN;

	@MessageField(id = "YIOTPVDGB", name = "OTP벤더구분", length = 3)
	private String YIOTPVDGB;

	@MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02")
	private String YIGJNO;

	@MessageField(id = "YISCNO", name = "폐기할안전카드번호", length = 8, masking = true, maskingType = "01")
	private String YISCNO;

	@MessageField(id = "YIICHONCEHD", name = "이체1회한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIICHONCEHD;

	@MessageField(id = "YIICHDAYHD", name = "이체1일한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIICHDAYHD;
}
