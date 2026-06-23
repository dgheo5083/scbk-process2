package com.scbank.process.api.fw.channel.message;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * @author sungdon.choi
 */
public interface IResponseMessage<H, T> extends IMessageObject {

    default H getHeader() {
        return null;
    }

    default T getBody() {
        return null;
    }
}
