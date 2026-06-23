package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H84900Req", type = Type.REQUEST, captureSystem = "OLTP", description = "이체 이상계좌 조회 요청 전문")
public class CbIbk01H84900Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIIBKCD", name = "입금계좌은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBKCD;

    @MessageField(id = "YIIGJNO", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String YIIGJNO;
}
