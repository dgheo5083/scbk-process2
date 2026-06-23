package com.scbank.process.api.fw.core.error;

/**
 * 프레임워크 오류코드 열거형 공통 인터페이스
 * <p>
 * 모든 에러 코드 Enum은 이 인터페이스를 구현하여
 * 코드 값과 메시지를 표준화된 방식으로 제공합니다.
 * </p>
 *
 * <p>
 * 주로 {@code ExceptionHandler}, {@code ResponseMessageFactory},
 * {@code ErrorResponse} 구성 시 사용됩니다.
 * </p>
 *
 * @see FrameworkErrorCode
 * @see co.kr.scbank.framework.core.exception.FrameworkRuntimeException
 */
public interface IErrorCode {

    /**
     * 에러 코드 (예: "E40001", "AUTH-001")
     *
     * @return 에러 코드 문자열
     */
    String getCode();

    /**
     * 에러 메시지 (기본 메시지 또는 메시지 키)
     *
     * @return 에러 메시지 또는 키
     */
    String getMessage();
}
