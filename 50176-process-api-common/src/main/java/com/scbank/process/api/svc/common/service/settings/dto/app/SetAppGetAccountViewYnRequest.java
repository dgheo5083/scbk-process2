package com.scbank.process.api.svc.common.service.settings.dto.app;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetAppGetAccountViewYnRequest", description = "계좌노출여부 요청 DTO", type = Type.REQUEST)
public class SetAppGetAccountViewYnRequest implements IMessageObject {

    @MessageField(id = "acctNumType", name = "계좌유형")
    private String acctNumType;

    @MessageField(id = "cardType", name = "카드종류")
    private String cardType;

    @MessageField(id = "menuType", name = "메뉴유형")
    private String menuType;

    @MessageField(id = "remitType", name = "송금확인증유형")
    private String remitType;

}
