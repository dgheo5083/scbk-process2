package com.scbank.process.api.fw.batch.exception;

import java.util.List;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

public class BatchJobExecutionException extends FrameworkRuntimeException {

	private static final long serialVersionUID = 1L;

	public BatchJobExecutionException(String errorCode) {
		super(errorCode);
	}

	public BatchJobExecutionException(String errorCode, List<Object> messageArgs, Throwable cause) {
		super(errorCode, messageArgs, cause);
	}

	public BatchJobExecutionException(String errorCode, List<Object> messageArgs) {
		super(errorCode, messageArgs);
	}

	public BatchJobExecutionException(String errorCode, String message) {
		super(errorCode, message);
	}

	public BatchJobExecutionException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
