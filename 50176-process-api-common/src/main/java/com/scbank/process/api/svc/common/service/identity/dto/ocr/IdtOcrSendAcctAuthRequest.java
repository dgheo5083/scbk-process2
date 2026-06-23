package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSendAcctAuthRequest", description = "계좌인증 송금 요청 DTO", type = Type.REQUEST)
public class IdtOcrSendAcctAuthRequest implements IMessageObject {
	@MessageField(id = "bnkCd", name = "은행코드")
	private String bnkCd;
	
	@MessageField(id = "acctNo", name = "계좌번호")
	private String acctNo;
	
	@MessageField(id = "extantFlag", name = "난수생성 여부")
	private String extantFlag;
	
	@MessageField(id = "changeAcctFlag", name = "계좌변경 여부")
	private String changeAcctFlag;
	
	@MessageField(id = "casaFlag", name = "계좌개설 여부")
	private String casaFlag;
}
