package com.scbank.process.api.fw.integration.response;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * 외부 시스템 연계 응답 해석기 인터페이스
 *
 * <p>
 * 응답 헤더를 기준으로 정상/오류 여부를 판단하고,
 * 오류 발생 시 예외를 발생시킬 수 있는 전략 인터페이스입니다.
 *
 * <p>
 * 각 연계 시스템별로 응답 코드 해석 방식이 다르므로,
 * 해당 인터페이스의 구현체에서 시스템별 로직을 분리하여 제공합니다.
 *
 * @param <H> 응답 헤더 메시지 타입 (예: MCIResHeader, FEPResHeader 등)
 * @param <E> 응답 에러 메시지 타입 (예: MCIError, HostError 등)
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 20.
 */
public interface IntegrationResponseHandler<REQH, RESH, E> {

    /**
     * 현재 응답 헤더가 오류인지를 판단합니다.
     * <p>
     * 기본값은 false이며, 시스템별로 override하여 사용합니다.
     * </p>
     *
     * @param header 응답 헤더 객체
     * @return true: 오류 응답, false: 정상 응답
     */
    default boolean isError(RESH header) {
        return false;
    }

    /**
     * 응답 헤더에서 응답 코드를 추출합니다.
     * <p>
     * 예: "0000", "E001", "9999" 등 시스템 고유의 응답 코드
     * </p>
     *
     * @param header 응답 헤더 객체
     * @return 응답 코드 문자열
     */
    String getResponseCode(RESH header);

    /**
     * 오류 응답 헤더에 대해 예외를 발생시킵니다.
     * <p>
     * 응답 코드, 오류 메시지 등을 기반으로 사용자 정의 예외를 던질 수 있습니다.
     * </p>
     *
     * @param context 연계시스템 컨텍스트 객체
     * @param header  응답 헤더 객체
     * @param error   응답 에러 객체
     * @throws RuntimeException 또는 IntegrationException 등 커스텀 예외
     */
    void checkErrorAndThrowable(IntegrationContext context, REQH requestHeader, RESH responseHeader, E error);
}