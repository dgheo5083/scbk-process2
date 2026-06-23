package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrCheckDipositorRequest", description = "예금주 조회 요청 DTO", type = Type.REQUEST)
public class IdtOcrCheckDepositorRequest implements IMessageObject {
	@MessageField(id = "bnkCd", name = "은행코드")
	private String bnkCd;
	
	@MessageField(id = "acctNo", name = "계좌번호")
	private String acctNo;
}
