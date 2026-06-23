package com.scbank.process.api.fw.core.encrypt;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkException;

/**
 * 복호화 처리 실패 예외 객체
 * 
 * @author sungdon.choi
 */
public class DecryptException extends FrameworkException {

    private static final long serialVersionUID = 1L;

    public DecryptException(Throwable cause) {
        super(FrameworkErrorCode.ENCRYPT_DECRYPTION_FAILED.getCode(), cause);
    }
}
