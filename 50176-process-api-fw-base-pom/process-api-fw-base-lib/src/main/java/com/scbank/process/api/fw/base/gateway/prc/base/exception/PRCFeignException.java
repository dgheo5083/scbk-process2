package com.scbank.process.api.fw.base.gateway.prc.base.exception;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * 프로세스 API Feign Exception 클래스
 */
public class PRCFeignException extends FrameworkRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PRCFeignException() {
        super();
    }

    /**
     * 
     * @param errorCode
     * @param message
     */
    public PRCFeignException(FrameworkErrorCode errorCode, String message) {
        super(errorCode.getCode(), message);
    }

    /**
     * 
     * @param errorCode
     * @param message
     */
    public PRCFeignException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * 
     * @param cause
     */
    public PRCFeignException(Throwable cause) {
        super(cause);
    }
}
