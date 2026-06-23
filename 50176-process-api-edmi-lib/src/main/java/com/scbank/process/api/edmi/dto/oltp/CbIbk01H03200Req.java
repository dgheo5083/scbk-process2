package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H03200Req", type = Type.REQUEST, description = "펀드계좌조회 요청부", captureSystem = "OLTP")
public class CbIbk01H03200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ContinueInfo", name = "", length = 90, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueInfo;

    @MessageField(id = "PrintCount", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer PrintCount;

}