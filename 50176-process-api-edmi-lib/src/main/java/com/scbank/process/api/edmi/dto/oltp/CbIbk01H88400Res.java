package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H88400Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "연금계좌 수익률 조회")
public class CbIbk01H88400Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YONUJSU_SIGN", name = "누적수익률부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONUJSU_SIGN;

    @MessageField(id = "YONUJSU", name = "누적수익률", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YONUJSU;

    @MessageField(id = "YOYRUYL_SIGN", name = "연환산수익률부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYRUYL_SIGN;

    @MessageField(id = "YOYRUYL", name = "연환산수익률", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOYRUYL;
}
