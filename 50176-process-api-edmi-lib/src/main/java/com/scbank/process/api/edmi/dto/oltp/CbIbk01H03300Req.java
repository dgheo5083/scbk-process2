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
@IntegrationMessage(id = "CbIbk01H03300Req", type = Type.REQUEST, description = "대출계좌조회 요청부", captureSystem = "OLTP")
public class CbIbk01H03300Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "ContinueInfo", name = "연속정보", length = 90, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueInfo;

    @MessageField(id = "PrintCount", name = "명세요구건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer PrintCount;

}