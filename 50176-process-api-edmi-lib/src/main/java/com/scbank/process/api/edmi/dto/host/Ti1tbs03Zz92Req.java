package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "Ti1tbs03Zz92Req", type = Type.REQUEST, description = "악성앱수집 요청부")
public class Ti1tbs03Zz92Req implements IMessageObject {

    @MessageField(id = "usid", name = "usid", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String usid;

    @MessageField(id = "password", name = "password", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String password;

    @MessageField(id = "device_type", name = "디바이스Type", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String device_type;

    @MessageField(id = "wdata", name = "wdata", length = 2048, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String wdata;

    @MessageField(id = "ndata", name = "ndata", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ndata;

    @MessageField(id = "af_data", name = "af_data", length = 4000, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String af_data;

    @MessageField(id = "uuidKey", name = "uuidKey", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String uuidKey;

    @MessageField(id = "Spyware", name = "Spyware", length = 13364, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Spyware;

}