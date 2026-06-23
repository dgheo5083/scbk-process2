package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "AthMgtCustInfoLeakerTranBlockResponse", type = Type.RESPONSE)
public class AthMgtCustInfoLeakerTranBlockResponse implements IMessageObject {

	@MessageField(id = "yoLRSOTGB", name = "개인정보노출등록구분 1:등록 3:미등록")
	private String yoLRSOTGB;

}