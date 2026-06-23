package com.scbank.process.api.svc.shared.service.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class AuthSharedRequest implements IMessageObject {

    @MessageField(id = "tpassYn", name = "")
    private String tpassYn;

    @MessageField(id = "tpassCleanYn", name = "")
    private String tpassCleanYn;

}
