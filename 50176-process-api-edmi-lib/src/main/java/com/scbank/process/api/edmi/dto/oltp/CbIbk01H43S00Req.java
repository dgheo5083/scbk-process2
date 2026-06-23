package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H43S00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "청년도약계좌 계좌신규 가능여부 조회")
public class CbIbk01H43S00Req implements IMessageObject {
	
	@MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT)
	private String UserID;
	
	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, encoding = "cp500")
	private String TSPassword;
	
	@MessageField(id = "YIGRGB", name = "조회구분", length = 1, align = AlignType.LEFT)
	private String YIGRGB;
	
	@MessageField(id = "YIJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, masking = true, maskingType = "01")
	private String YIJUMIN;
}
