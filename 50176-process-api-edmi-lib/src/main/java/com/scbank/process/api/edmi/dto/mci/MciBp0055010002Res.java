package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciBp0055010002Res", type = Type.RESPONSE, xmlRootName = "TRANDATA")
public class MciBp0055010002Res implements IMessageObject {

    @MessageField(id = "TISIDONA", name = "시도명", length = 20)
    private String TISIDONA;

    @MessageField(id = "TISIGGNA", name = "시군구명", length = 20)
    private String TISIGGNA;

    @MessageField(id = "TIJSOJH", name = "신주소조회", length = 60)
    private String TIJSOJH;

    @MessageField(id = "FLDdelimiter1", name = "FLDdelimiter1", length = 1, defaultValue = "0x1F")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "SEGdelimiter1", length = 1, defaultValue = "0x1E")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "ENDdelimiter1", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "ENDdelimiter2", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter2;
}
