package com.scbank.process.api.svc.common.service.keypad.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SampleDecryptKeypadRequest", description = "복호화 테스트 요청 DTO", type = Type.REQUEST)
public class SampleDecryptKeypadRequest implements IMessageObject {

    @MessageField(id = "businessStr", name = "업무정보")
    private String businessStr;
    
    @MessageField(id = "accntPw", name = "계봐비밀번호")
    private String accntPw;
    
    @MessageField(id = "ssn", name = "주민번호")
    private String ssn;

}
