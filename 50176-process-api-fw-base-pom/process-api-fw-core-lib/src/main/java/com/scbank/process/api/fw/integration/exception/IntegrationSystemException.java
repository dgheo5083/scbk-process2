package com.scbank.process.api.fw.integration.exception;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 외부 시스템 연동 시 발생한 시스템 수준의 예외를 표현하는 클래스입니다.
 *
 * 이 클래스는 {@link IntegrationException}을 상속하며,
 * 추가적으로 연동 대상 시스템의 식별자(systemId)를 오류 위치(errorLocation)로 지정할 수 있습니다.
 *
 * 주로 다음과 같은 상황에서 사용됩니다:
 * - 특정 연동 시스템(예: MCI, FEP, EDMI 등)에서 예외 발생 시 원인 추적
 * - 시스템별 장애 구분 및 로깅 시 명확한 위치 정보 제공
 *
 * Lombok의 {@code @Getter}, {@code @Setter}를 사용해 오류 위치 필드를 관리합니다.
 * </pre>
 *
 * @author sungdon.choi
 * @since 2025.04
 */
@Getter
@Setter
public class IntegrationSystemException extends IntegrationException {

    private static final long serialVersionUID = 1L;

    /**
     * 시스템 ID 기반 예외 생성자 (원인 예외 포함).
     *
     * @param systemId 연동 대상 시스템 식별자
     * @param cause    원인 예외
     */
    public IntegrationSystemException(String systemId, Throwable cause) {
        super(FrameworkErrorCode.INTEG_FAILED.getCode(), cause);
        this.setErrorLocation(systemId);
    }

    /**
     * 시스템 ID + 오류 코드 + 원인 예외 포함 생성자.
     *
     * @param systemId  연동 대상 시스템 식별자
     * @param errorCode 오류 코드
     * @param cause     원인 예외
     */
    public IntegrationSystemException(String systemId, String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.setErrorLocation(systemId);
    }

    /**
     * 시스템 ID + 오류 코드 기반 예외 생성자.
     *
     * @param systemId  연동 대상 시스템 식별자
     * @param errorCode 오류 코드
     */
    public IntegrationSystemException(String systemId, String errorCode) {
        super(errorCode);
        this.setErrorLocation(systemId);
    }
}
