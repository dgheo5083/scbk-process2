package com.scbank.process.api.fw.base.exception;

import java.util.List;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import lombok.ToString;

/**
 * CSL 업무 서비스 예외 클래스
 * CSL 업무 서비스에서 예외를 발생시키는 경우 사용한다.
 * 
 * @author sungdon.choi
 */
@Deprecated
@ToString(callSuper = true)
public class CSLServiceException extends FrameworkRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param cause
     */
    public CSLServiceException(Throwable cause) {
        super(cause);
    }

    public CSLServiceException(FrameworkErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 오류 코드만 전달하는 기본 생성자
     * 
     * @param errorCode 오류 코드
     */
    public CSLServiceException(String errorCode) {
        super(errorCode);
    }

    /**
     * 오류 코드와 사용자 정의 메시지 전달
     * 
     * @param errorCode 오류 코드
     * @param message   메시지 (로그 확인용)
     */
    public CSLServiceException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * 오류 코드 + cause 전달
     * 
     * @param errorCode 오류 코드
     * @param cause     원인 예외
     */
    public CSLServiceException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * 오류코드 + 오류 메시지 + cause 전달
     * 
     * @param errorCode 업무정의 오류코드
     * @param message   오류 메시지
     * @param cause     원인 예외
     */
    public CSLServiceException(String errorCode, String message, Throwable cause) {
        this.setErrorMessage(message);
    }

    /**
     * 오류 코드 + 메시지 치환 인자 전달
     * 
     * @param errorCode   오류 코드
     * @param messageArgs 메시지 치환 인자
     */
    public CSLServiceException(String errorCode, List<Object> messageArgs) {
        super(errorCode, messageArgs);
    }

    /**
     * 오류 코드 + 메시지 인자 + cause 전달
     * 
     * @param errorCode   오류 코드
     * @param messageArgs 메시지 치환 인자
     * @param cause       원인 예외
     */
    public CSLServiceException(String errorCode, List<Object> messageArgs, Throwable cause) {
        super(errorCode, messageArgs, cause);
    }
}
