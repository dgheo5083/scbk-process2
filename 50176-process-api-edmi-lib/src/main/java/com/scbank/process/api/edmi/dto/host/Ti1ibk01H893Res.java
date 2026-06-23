package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H893Res", type = Type.RESPONSE, description = "FATCA등록/조회 응답 전문")
public class Ti1ibk01H893Res implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String UserID;
	
	@MessageField(id = "YOFLAG", name = "거래전평가FLAG", length = 2)
	private String YOFLAG;
	
	@MessageField(id = "YOGEORE", name = "이용자구분", length = 2)
	private String YOGEORE;
	
	@MessageField(id = "YO303YN", name = "국적구분", length = 1)
	private String YO303YN;
	
	@MessageField(id = "YOFATYN", name = "가능여부", length = 1)
	private String YOFATYN;
	
	@MessageField(id = "YOUSPP", name = "거주구분", length = 1)
	private String YOUSPP;
	
	@MessageField(id = "YODUMMY", name = "신규더미추가", length = 50)
	private String YODUMMY;
}
