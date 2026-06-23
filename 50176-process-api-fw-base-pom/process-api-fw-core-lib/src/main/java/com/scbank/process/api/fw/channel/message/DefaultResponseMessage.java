package com.scbank.process.api.fw.channel.message;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * @author sungdon.choi
 */
@Data
@IntegrationMessage(id = "defaultResponseMessage", type = Type.RESPONSE, description = "기본 채널 응답 메시지")
public class DefaultResponseMessage<H extends IMessageObject, T extends IMessageObject> implements
        IResponseMessage<H, T> {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "header", name = "채널 헤더부")
    private H header;

    @MessageField(id = "body", name = "채널 바디부")
    private T body;
}
