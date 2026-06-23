package com.scbank.process.api.fw.batch.exception;

import java.util.List;

import com.scbank.process.api.fw.core.exception.FrameworkException;

public class BatchJobException extends FrameworkException {

	private static final long serialVersionUID = 1L;

	public BatchJobException(String errorCode) {
		super(errorCode);
	}

	public BatchJobException(String errorCode, List<Object> messageArgs, Throwable cause) {
		super(errorCode, messageArgs, cause);
	}

	public BatchJobException(String errorCode, List<Object> messageArgs) {
		super(errorCode, messageArgs);
	}

	public BatchJobException(String errorCode, String message) {
		super(errorCode, message);
	}

	public BatchJobException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public BatchJobException(Throwable cause) {
		super(cause);
	}
}
