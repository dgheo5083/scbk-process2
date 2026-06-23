package com.scbank.process.api.fw.core.error;

import java.io.Serializable;
import java.util.List;

/**
 * 프레임워크 공통 에러 메시지 마커 인터페이스
 * <p>
 * 예외 처리 시 전달될 에러 메시지 객체의 타입을 명확하게 지정하기 위한 마커입니다. 주로 {@code ErrorResponse},
 * {@code BaseErrorMessage}, 국제화 메시지 포맷 등에서 활용됩니다.
 * </p>
 *
 * <p>
 * 이 인터페이스는 {@link Serializable}을 상속하며, 분산 환경 또는 응답 직렬화를 지원합니다.
 * </p>
 *
 * <p>
 * <b>주의:</b> 구체적인 필드는 하위 구현 클래스에 정의합니다.
 * </p>
 *
 * @see co.kr.scbank.framework.core.exception.FrameworkRuntimeException
 */
public interface IErrorMessage extends Serializable {

    /**
     * 에러코드 획득
     * 
     * @return
     */
    String getErrorCode();

    /**
     * 에러메시지 획득
     * 
     * @return
     */
    String getErrorMessage();

    /**
     * 에러 가이드 메시지 목록 획득
     * 
     * @return
     */
    List<String> getErrorGuideMessages();
}
