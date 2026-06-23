package com.scbank.process.api.fw.channel.exception;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * 서비스 이용 가능 시간이 아닐 때 발생하는 예외.
 */
public class ServiceTimeException extends FrameworkRuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceTimeException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ServiceTimeException(String errorCode) {
        super(errorCode);
    }
}
