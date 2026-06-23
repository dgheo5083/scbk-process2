package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetIdCardShotAblListResponse", description = "신분증촬영 가능 목록조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetIdCardShotAblListResponse implements IMessageObject {
	
	@MessageField(id = "viewIdType", name = "신분증촬영가능목록")
	private String viewIdType;
	
	@MessageField(id = "isOldAppVer", name = "구앱버전여부")
	private String isOldAppVer;
	
}
