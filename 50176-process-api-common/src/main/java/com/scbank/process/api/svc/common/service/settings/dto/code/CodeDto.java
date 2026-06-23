package com.scbank.process.api.svc.common.service.settings.dto.code;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "CodeDto", description = "코드목록")
public class CodeDto implements IMessageObject {

    @MessageField(id = "code", name = "코드")
    private String code;

    @MessageField(id = "name", name = "코드명")
    private String name;

    @MessageField(id = "url", name = "이미지 url")
    private String url;

    @MessageField(id = "fcode", name = "fcode")
    private String fcode;

}
