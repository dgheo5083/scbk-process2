package com.scbank.process.api.fw.base.exception;

import java.util.List;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import lombok.ToString;

/**
 * 프로세스 API 업무 서비스 예외 클래스
 * 프로세스 API 업무 서비스에서 예외를 발생시키는 경우 사용한다.
 * 
 * @author sungdon.choi
 */
@ToString(callSuper = true)
public class PRCServiceException extends FrameworkRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param cause
     */
    public PRCServiceException(Throwable cause) {
        super(cause);
    }

    public PRCServiceException(FrameworkErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 오류 코드만 전달하는 기본 생성자
     * 
     * @param errorCode 오류 코드
     */
    public PRCServiceException(String errorCode) {
        super(errorCode);
    }

    /**
     * 오류 코드와 사용자 정의 메시지 전달
     * 
     * @param errorCode 오류 코드
     * @param message   메시지 (로그 확인용)
     */
    public PRCServiceException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * 오류 코드 + cause 전달
     * 
     * @param errorCode 오류 코드
     * @param cause     원인 예외
     */
    public PRCServiceException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * 오류코드 + 오류 메시지 + cause 전달
     * 
     * @param errorCode 업무정의 오류코드
     * @param message   오류 메시지
     * @param cause     원인 예외
     */
    public PRCServiceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * 오류 코드 + 메시지 치환 인자 전달
     * 
     * @param errorCode   오류 코드
     * @param messageArgs 메시지 치환 인자
     */
    public PRCServiceException(String errorCode, List<Object> messageArgs) {
        super(errorCode, messageArgs);
    }

    /**
     * 오류 코드 + 메시지 인자 + cause 전달
     * 
     * @param errorCode   오류 코드
     * @param messageArgs 메시지 치환 인자
     * @param cause       원인 예외
     */
    public PRCServiceException(String errorCode, List<Object> messageArgs, Throwable cause) {
        super(errorCode, messageArgs, cause);
    }
}
