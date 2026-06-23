package com.scbank.process.api.fw.core.encrypt;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkException;

/**
 * @author sungdon.choi
 */
public class EncryptException extends FrameworkException {

    private static final long serialVersionUID = 1L;

    public EncryptException(Throwable cause) {
        super(FrameworkErrorCode.ENCRYPT_ENCRYPTION_FAILED.getCode(), cause);
    }
}
