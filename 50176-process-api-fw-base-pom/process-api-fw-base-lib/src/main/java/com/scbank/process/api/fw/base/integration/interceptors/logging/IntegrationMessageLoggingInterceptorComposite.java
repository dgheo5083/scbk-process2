package com.scbank.process.api.fw.base.integration.interceptors.logging;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * 연계시스템 전문 요청/응답 로그 인터셉터 Composite 클래스
 * 연계시스템 전문포맷(FIXEDLENGTH/JSON/XML)에 따라 지원하는 로깅인터셉터를 호출하여 요청/응답 로그 출력
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
@Component("integrationMessageLoggingInterceptorComposite")
public class IntegrationMessageLoggingInterceptorComposite implements IntegrationInterceptor {

    private final List<AbstractIntegrationLoggingInterceptor> loggingInterceptors;

    /**
     * 연계 요청 전 처리 로직
     *
     * @param context 연계 시스템 컨텍스트
     * @param request 요청 객체 (IMessageObject 또는 IntegrationRequest 등)
     */
    public void before(IntegrationContext context, Object request) {
        List<AbstractIntegrationLoggingInterceptor> interceptors = Collections.unmodifiableList(loggingInterceptors);
        for (AbstractIntegrationLoggingInterceptor interceptor : interceptors) {
            if (interceptor == null) {
                continue;
            }

            if (!interceptor.isSupported(context)) {
                continue;
            }

            interceptor.before(context, request);
        }
    }

    /**
     * 연계 응답 후 처리 로직
     *
     * @param context  연계 시스템 컨텍스트
     * @param request  요청 객체
     * @param response 응답 객체
     */
    public void after(IntegrationContext context, Object request, Object response) {
        List<AbstractIntegrationLoggingInterceptor> interceptors = Collections.unmodifiableList(loggingInterceptors);
        for (AbstractIntegrationLoggingInterceptor interceptor : interceptors) {
            if (interceptor == null) {
                continue;
            }

            if (!interceptor.isSupported(context)) {
                continue;
            }

            interceptor.after(context, request, response);
        }
    }
}
