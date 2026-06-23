package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "Ti1tbs03Zz92Res", type = Type.RESPONSE, description = "악성앱수집 응답부")
public class Ti1tbs03Zz92Res implements IMessageObject {

    @MessageField(id = "result", name = "처리결과", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String result;

    @MessageField(id = "result_message", name = "결과메시지", length = 200, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String result_message;

}