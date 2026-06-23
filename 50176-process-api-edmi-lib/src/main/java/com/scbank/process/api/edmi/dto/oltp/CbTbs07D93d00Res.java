package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07D93d00Res", type = Type.RESPONSE, description = "직불현금카드사고신고 응답부")
public class CbTbs07D93d00Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOSTATSCODE", name = "응답코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSTATSCODE;

    @MessageField(id = "YOSTATSMessage", name = "응답코드메세지", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSTATSMessage;
}