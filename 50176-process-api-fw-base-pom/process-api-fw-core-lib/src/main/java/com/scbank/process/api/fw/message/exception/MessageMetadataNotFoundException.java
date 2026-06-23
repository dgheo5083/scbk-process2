package com.scbank.process.api.fw.message.exception;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * @author sungdon.choi
 */
public class MessageMetadataNotFoundException extends FrameworkRuntimeException {

    private static final long serialVersionUID = 1L;

    public MessageMetadataNotFoundException(String errorCode) {
        super(errorCode);
    }

    public MessageMetadataNotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public MessageMetadataNotFoundException(String errorCode, String errorMessage, Throwable t) {
        super(errorCode, t);
    }

    public MessageMetadataNotFoundException(Throwable cause) {
        super(FrameworkErrorCode.MSG_METADATA_NOT_FOUND.getCode(), cause);
    }
}
