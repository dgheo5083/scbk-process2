package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbAddr0101Req", type = Type.REQUEST, description = "주소정제 요청부")
public class MciIbAddr0101Req implements IMessageObject {

    @MessageField(id = "TINEWJSOGB2", name = "신주소구분2", length = 1, multiBytes = true)
    private String TINEWJSOGB2;

    @MessageField(id = "TIUPNO", name = "우편번호", length = 6, multiBytes = true)
    private String TIUPNO;

    @MessageField(id = "TINEWJSO1", name = "신주소1", length = 70, multiBytes = true)
    private String TINEWJSO1;

    @MessageField(id = "TINEWJSO4", name = "신주소4", length = 100, multiBytes = true)
    private String TINEWJSO4;

}