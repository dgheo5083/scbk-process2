package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindAddressRefineRequest", description = "주소정제 요청", type = Type.REQUEST)
public class FncSchFindAddressRefineRequest implements IMessageObject {

    @MessageField(id = "tinewjsogb2", name = "신주소구분2", example = "2")
    private String tinewjsogb2;

    @MessageField(id = "tiupno", name = "우편번호", example = "03187")
    private String tiupno;

    @MessageField(id = "tinewjso1", name = "신주소1", example = "서울 종로구 광화문우체국사서함 860")
    private String tinewjso1;

    @MessageField(id = "tinewjso4", name = "신주소4", example = "")
    private String tinewjso4;
}
