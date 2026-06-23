package com.scbank.process.api.svc.shared.service.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class SecuritySharedRequest implements IMessageObject {

    @MessageField(id = "index1", name = "")
    private String index1;

    @MessageField(id = "index2", name = "")
    private String index2;
}
