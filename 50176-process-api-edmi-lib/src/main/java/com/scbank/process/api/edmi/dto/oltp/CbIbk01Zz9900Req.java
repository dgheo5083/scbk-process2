package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@IntegrationMessage(id = "CbIbk01Zz9900Req", type = Type.REQUEST, captureSystem = "OLTP", description = "고객별 자산정보 조회")
public class CbIbk01Zz9900Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "CIFNO", name = "고객식별번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CIFNO;
}
