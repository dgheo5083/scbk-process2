package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterEdocRecoveryDataRequest", type = Type.REQUEST)
public class RegisterEdocRecoveryDataRequest implements IMessageObject {

	@MessageField(id = "custNo", name = "고갹번호")
    private String custNo;
    
    @MessageField(id = "tradNo", name = "거래번호")
    private String tradNo;
    
	@MessageField(id = "rcvryMk", name = "복구구분")
    private String rcvryMk;

    @MessageField(id = "edocCd", name = "전자문서코드")
    private String edocCd;

    @MessageField(id = "rcvryData", name = "복구자료")
    private String rcvryData;
}
