package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CBIbk01D75600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "기본계좌변경 응답")
public class CBIbk01D75600Res implements IMessageObject {

	@MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String YOUSID;

	@MessageField(id = "YODUMY", name = "더미", length = 90)
	private String YODUMY;

	
}