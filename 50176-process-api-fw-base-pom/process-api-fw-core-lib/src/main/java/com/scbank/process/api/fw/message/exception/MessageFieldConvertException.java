package com.scbank.process.api.fw.message.exception;

import com.scbank.process.api.fw.core.exception.FrameworkException;

/**
 * 전문 필드 변환 처리 예외
 *
 * @author sungdon.choi
 */
public class MessageFieldConvertException extends FrameworkException {

    private static final long serialVersionUID = 1L;

    public MessageFieldConvertException(Throwable cause) {
        super(cause);
    }

    public MessageFieldConvertException(String errorCode) {
        super(errorCode);
    }

    public MessageFieldConvertException(String errorCode, String message) {
        super(errorCode, message);
    }
}
