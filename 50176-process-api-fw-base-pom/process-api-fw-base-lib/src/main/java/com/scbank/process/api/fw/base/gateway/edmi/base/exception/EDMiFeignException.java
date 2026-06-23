package com.scbank.process.api.fw.base.gateway.edmi.base.exception;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EDMiFeignException extends FrameworkRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EDMiFeignException() {
        super();
    }

    /**
     * 
     * @param errorCode
     * @param message
     */
    public EDMiFeignException(FrameworkErrorCode errorCode, String message) {
        super(errorCode.getCode(), message);
    }

    /**
     * 
     * @param errorCode
     * @param message
     */
    public EDMiFeignException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * 
     * @param cause
     */
    public EDMiFeignException(Throwable cause) {
        super(cause);
    }
}
