package com.scbank.process.api.svc.common.service.identity.dto.mid;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrConfirmMobileAuthUserInfoRequest", description = "SP서버인증결과 비교 요청 DTO", type = Type.REQUEST)
public class IdtMidConfirmMobileAuthUserInfoRequest implements IMessageObject {
	@MessageField(id = "seqNo", name = "seqNo", example = "")
	private String seqNo;
}
