package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H41100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "현대카드 결제예상금액 전문")
public class CbTbs07H41100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YORTCD", name = "결과코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORTCD;

    @MessageField(id = "YORTNM", name = "결과명", length = 80, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORTNM;

    @MessageField(id = "YOGJIL", name = "결제일", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJIL;

    @MessageField(id = "YOGJAK", name = "당월결제금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGJAK;

    @MessageField(id = "YOYHWOL", name = "카드유효기간", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYHWOL;

}
