package com.scbank.process.api.svc.common.service.support.dto.event;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupEvtApplyEventRequest", type = Type.REQUEST)
public class SupEvtApplyEventRequest implements IMessageObject {

    @MessageField(id = "hpNum", name = "hpNum")
    String hpNum;

    @MessageField(id = "email", name = "email")
    String email;

    @MessageField(id = "evntId", name = "evntId")
    String evntId;

    @MessageField(id = "bbsMkCd", name = "bbsMkCd")
    String bbsMkCd;

    @MessageField(id = "custName", name = "custName")
    String custName;

    @MessageField(id = "zipCode", name = "zipCode")
    String zipCode;

    @MessageField(id = "address1", name = "address1")
    String address1;

    @MessageField(id = "address2", name = "address2")
    String address2;

}
