package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrCheckDipositorResponse", description = "예금주 조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrCheckDepositorResponse implements IMessageObject {
	
	@MessageField(id = "extantFlag", name = "난수생성여부")
	private String extantFlag;
	
	@MessageField(id = "errCd", name = "에러코드")
	private String errCd;
	
}
