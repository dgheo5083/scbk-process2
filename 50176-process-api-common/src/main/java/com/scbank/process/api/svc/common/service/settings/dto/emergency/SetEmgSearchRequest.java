package com.scbank.process.api.svc.common.service.settings.dto.emergency;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetEmgSearchRequest", description = "PMS 긴급공지 요청 DTO", type = Type.REQUEST)
public class SetEmgSearchRequest implements IMessageObject {

    @MessageField(id = "ctgryCd", name = "카테고리 코드", example = "Z04001")
    private String ctgryCd;

}
