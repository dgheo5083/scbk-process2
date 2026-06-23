package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSetAuthInitInfoRequest", description = "인증기본정보세팅 요청 DTO", type = Type.REQUEST)
public class IdtOcrSetAuthInitInfoRequest implements IMessageObject {
	
	@MessageField(id = "custNo", name = "고객번호", example = "1405318")
	private String custNo;
	
	@MessageField(id = "tradNo", name = "거래번호", example = "3504457")
	private String tradNo;
	
	@MessageField(id = "bizType", name = "업무구분", example = "MPLR")
	private String bizType;
	
	@MessageField(id = "cnnctnWay", name = "제휴처", example = "")
	private String cnnctnWay;
	
	@MessageField(id = "cnnctnTradNo", name = "제휴처거래번호", example = "")
	private String cnnctnTradNo;
	
	@MessageField(id = "clerkNo", name = "행번", example = "")
	private String clerkNo;
}
