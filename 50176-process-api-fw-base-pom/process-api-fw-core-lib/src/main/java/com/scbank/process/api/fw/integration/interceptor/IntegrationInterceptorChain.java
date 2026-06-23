package com.scbank.process.api.fw.integration.interceptor;

import java.util.List;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * 연계 요청에 적용되는 인터셉터 체인
 *
 * <p>
 * {@link IntegrationInterceptor} 목록을 순차적으로 실행하여,
 * 요청 전/후 처리 및 예외 상황에 대한 공통 흐름을 제공합니다.
 *
 * <ul>
 * <li>before(): 요청 전 사전 처리 (로그, 마스킹, 트레이싱 등)</li>
 * <li>after(): 응답 후 후처리 (SLA 측정, 로그 등)</li>
 * <li>onError(): 예외 발생 시 후처리 (알림, 슬랙 전송 등)</li>
 * </ul>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public class IntegrationInterceptorChain {

    /**
     * 실행 대상 인터셉터 목록
     */
    private final List<IntegrationInterceptor> interceptors;

    /**
     * 인터셉터 체인을 생성합니다.
     *
     * @param interceptors 적용할 인터셉터 목록
     */
    public IntegrationInterceptorChain(List<IntegrationInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * 연계 요청 전 인터셉터를 순차 실행합니다.
     *
     * @param ctx 연계 컨텍스트
     * @param req 요청 객체
     */
    public void before(IntegrationContext ctx, Object req) {
        for (IntegrationInterceptor i : interceptors) {
            i.before(ctx, req);
        }
    }

    /**
     * 연계 응답 후 인터셉터를 순차 실행합니다.
     *
     * @param ctx 연계 컨텍스트
     * @param req 요청 객체
     * @param res 응답 객체
     */
    public void after(IntegrationContext ctx, Object req, Object res) {
        for (IntegrationInterceptor i : interceptors) {
            i.after(ctx, req, res);
        }
    }

    /**
     * 연계 처리 중 예외 발생 시 인터셉터를 순차 실행합니다.
     *
     * @param ctx 연계 컨텍스트
     * @param req 요청 객체
     * @param e   예외 객체
     */
    public void onError(IntegrationContext ctx, Object req, Throwable e) {
        for (IntegrationInterceptor i : interceptors) {
            i.onError(ctx, req, e);
        }
    }
}
