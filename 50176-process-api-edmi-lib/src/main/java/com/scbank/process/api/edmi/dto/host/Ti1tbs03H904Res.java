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
@IntegrationMessage(id = "Ti1tbs03H904Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "모바일OTP 재발급 예비/본처리 응답 전문")
public class Ti1tbs03H904Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "UserID2", name = "사용자ID2", length = 10, masking = true, maskingType = "01")
	private String UserID2;

	@MessageField(id = "YOSCUSE", name = "보안카드 사용방법 1 : 동시사용 2 : 개별사용", length = 1)
	private String YOSCUSE; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "YOSCNO", name = "폐기할 안전카드 번호", length = 8, masking = true, maskingType = "01")
	private String YOSCNO;

	@MessageField(id = "YOSCGB", name = "보안매체 종류(1:안전카드 2:구OTP 3:통합OTP 4:모바일OTP", length = 1)
	private String YOSCGB;

	@MessageField(id = "YOOTPNO", name = "OTP일련번호", length = 12, masking = true, maskingType = "01")
	private String YOOTPNO;

	@MessageField(id = "YOSEKEY", name = "OTP 비밀키 B", length = 64)
	private String YOSEKEY;

	@MessageField(id = "YOICHONCEHD", name = "이체1회한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOICHONCEHD;

	@MessageField(id = "YOICHDAYHD", name = "이체1일한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOICHDAYHD;

}
