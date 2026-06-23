package com.scbank.process.api.fw.integration.exception;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * <pre>
 * 외부 시스템 연동 시 타임아웃이 발생한 경우 사용되는 예외 클래스입니다.
 *
 * {@link IntegrationSystemException}을 상속하며,
 * 타임아웃이 발생한 시스템 식별자(systemId)와 함께 오류 코드를 전달합니다.
 *
 * 기본 오류 코드는 {@link FrameworkErrorCode#INTEG_TIMEOUT}을 사용하지만,
 * 필요 시 사용자 정의 오류 코드도 전달할 수 있습니다.
 *
 * 주 사용처:
 * - HTTP/TCP 요청의 연결 또는 응답 타임아웃
 * - 비동기 연동에서 SLA 초과
 * </pre>
 *
 * @author sungdon.choi
 * @since 2025.04
 */
public class IntegrationTimeoutException extends IntegrationSystemException {

	private static final long serialVersionUID = 1L;

	/**
	 * 시스템 ID 기반 기본 타임아웃 예외 생성자.
	 * 내부적으로 {@link FrameworkErrorCode#INTEG_TIMEOUT}을 사용합니다.
	 *
	 * @param systemId 연동 대상 시스템 식별자
	 */
	public IntegrationTimeoutException(String systemId) {
		super(systemId, FrameworkErrorCode.INTEG_TIMEOUT.getCode());
	}

	/**
	 * 시스템 ID, 오류 코드, 원인 예외 포함 생성자.
	 *
	 * @param systemId  연동 대상 시스템 식별자
	 * @param errorCode 오류 코드
	 * @param cause     원인 예외
	 */
	public IntegrationTimeoutException(String systemId, Throwable cause) {
		super(systemId, FrameworkErrorCode.INTEG_TIMEOUT.getCode(), cause);
	}
}
