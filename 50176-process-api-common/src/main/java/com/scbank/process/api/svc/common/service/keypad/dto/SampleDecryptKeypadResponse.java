package com.scbank.process.api.svc.common.service.keypad.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SampleDecryptKeypadResponse", description = "복호화 결과 DTO", type = Type.RESPONSE)
public class SampleDecryptKeypadResponse implements IMessageObject {

    @MessageField(id = "decStr1", name = "복호화1")
    private String decStr1;
    
    @MessageField(id = "decStr2", name = "복호화2")
    private String decStr2;
    
    @MessageField(id = "decStr3", name = "복호화3")
    private String decStr3;

    
}
