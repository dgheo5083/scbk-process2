package com.scbank.process.api.svc.common.service.identity.dto.mid;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrConfirmMobileAuthUserInfoResponse", description = "SP서버인증결과 비교 응답 DTO", type = Type.RESPONSE)
public class IdtMidConfirmMobileAuthUserInfoResponse implements IMessageObject {
	@MessageField(id = "compareResult", name = "비교결과")
	private String compareResult;
	
	@MessageField(id = "calsBusinessDay", name = "영업일")
	private String calsBusinessDay;
	
	@MessageField(id = "productName", name = "상품명")
	private String productName;
	
	@MessageField(id = "acctNo", name = "계좌번호")
	private String acctNo;
	
	@MessageField(id = "bankName", name = "은행명")
	private String bankName;
	
	@MessageField(id = "anotherAcctNo", name = "타행계좌번호")
	private String anotherAcctNo;
	
	@MessageField(id = "seqNo", name = "seqNo")
	private String seqNo;
}
