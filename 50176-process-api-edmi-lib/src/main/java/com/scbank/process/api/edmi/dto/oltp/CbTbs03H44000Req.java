package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs03H44000Req", type = Type.REQUEST, description = "세금우대한도변경 요청부", captureSystem = "OLTP")
public class CbTbs03H44000Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIBKHAND", name = "변경세금우대한도", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIBKHAND;

    @MessageField(id = "YIBKGJNO", name = "변경계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIBKGJNO;

}
