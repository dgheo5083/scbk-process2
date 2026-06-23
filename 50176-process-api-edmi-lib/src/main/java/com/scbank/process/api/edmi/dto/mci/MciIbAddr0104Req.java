package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciIbAddr0104Req", type = Type.REQUEST, description = "주소찾기 요청부")
public class MciIbAddr0104Req implements IMessageObject {

    @MessageField(id = "TISIDONA", name = "시도명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TISIDONA;

    @MessageField(id = "TISIGGNA", name = "시군구명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TISIGGNA;

    @MessageField(id = "TIJSOJH", name = "신주소조회", length = 60, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TIJSOJH;

}