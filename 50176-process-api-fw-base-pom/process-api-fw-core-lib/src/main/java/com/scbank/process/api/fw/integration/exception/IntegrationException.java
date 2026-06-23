package com.scbank.process.api.fw.integration.exception;

import java.util.List;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * <pre>
 * 통합 시스템 연동(Integration) 중 발생하는 예외를 표현하는 Runtime 예외 클래스입니다.
 *
 * 이 예외는 {@link FrameworkRuntimeException}을 상속하여,
 * 메시지 코드 기반 예외 처리 및 메시지 치환 인자를 함께 전달할 수 있도록 설계되었습니다.
 *
 * 주요 사용 시나리오:
 * - 외부 시스템 연동 실패
 * - 응답 메시지 파싱 오류
 * - SLA 타임아웃, 연결 실패 등 연동 장애
 *
 * 다양한 생성자를 통해 메시지 코드, 치환 인자, 원인 예외(Throwable)를 유연하게 전달할 수 있습니다.
 * </pre>
 *
 * @author sungdon.choi
 * @since 2025.04
 */
public class IntegrationException extends FrameworkRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 오류 코드 기반 예외 생성자.
	 *
	 * @param errorCode 예외 메시지 코드
	 */
	public IntegrationException(String errorCode) {
		super(errorCode);
	}

	/**
	 * 오류 코드 + 메시지 치환 인자 + 원인 예외 포함 생성자.
	 *
	 * @param errorCode   예외 메시지 코드
	 * @param messageArgs 메시지 치환 인자 목록
	 * @param cause       원인 예외
	 */
	public IntegrationException(String errorCode, List<Object> messageArgs, Throwable cause) {
		super(errorCode, messageArgs, cause);
	}

	/**
	 * 오류 코드 + 메시지 치환 인자 포함 생성자.
	 *
	 * @param errorCode   예외 메시지 코드
	 * @param messageArgs 메시지 치환 인자 목록
	 */
	public IntegrationException(String errorCode, List<Object> messageArgs) {
		super(errorCode, messageArgs);
	}

	/**
	 * 오류 코드 + 메시지 직접 지정 생성자.
	 *
	 * @param errorCode 예외 메시지 코드
	 * @param message   상세 메시지
	 */
	public IntegrationException(String errorCode, String message) {
		super(errorCode, message);
	}

	/**
	 * 오류 코드 + 원인 예외 포함 생성자.
	 *
	 * @param errorCode 예외 메시지 코드
	 * @param cause     원인 예외
	 */
	public IntegrationException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
