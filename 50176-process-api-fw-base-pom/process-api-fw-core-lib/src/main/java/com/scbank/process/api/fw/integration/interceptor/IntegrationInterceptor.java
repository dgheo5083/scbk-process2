package com.scbank.process.api.fw.integration.interceptor;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * 외부 시스템 연계 흐름에 삽입되는 인터셉터 인터페이스
 *
 * <p>
 * 요청 전, 응답 후, 오류 발생 시점에 사용자 정의 로직을 삽입할 수 있도록 제공합니다.
 *
 * <ul>
 * <li>{@code before()}: 요청 직전에 호출 (예: 트레이스 ID 생성, 로깅, 마스킹 등)</li>
 * <li>{@code after()}: 정상 응답 이후 호출 (예: SLA 측정, 결과 기록 등)</li>
 * <li>{@code onError()}: 예외 발생 시 호출 (예: 알림 전송, 장애 기록 등)</li>
 * </ul>
 *
 * <p>
 * 모든 메서드는 연계 컨텍스트와 요청 객체를 인자로 받아, 유연한 후처리 로직을 구현할 수 있습니다.
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IntegrationInterceptor {

    /**
     * 연계 요청 전 처리 로직
     *
     * @param context 연계 시스템 컨텍스트
     * @param request 요청 객체 (IMessageObject 또는 IntegrationRequest 등)
     */
    default void before(IntegrationContext context, Object request) {

    }

    /**
     * 연계 응답 후 처리 로직
     *
     * @param context  연계 시스템 컨텍스트
     * @param request  요청 객체
     * @param response 응답 객체
     */
    default void after(IntegrationContext context, Object request, Object response) {

    }

    /**
     * 연계 처리 중 예외 발생 시 처리 로직
     *
     * @param context 연계 시스템 컨텍스트
     * @param request 요청 객체
     * @param ex      예외 객체
     */
    default void onError(IntegrationContext context, Object request, Throwable ex) {

    }
}
