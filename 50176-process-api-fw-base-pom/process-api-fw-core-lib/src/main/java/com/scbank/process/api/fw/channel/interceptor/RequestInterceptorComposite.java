package com.scbank.process.api.fw.channel.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 프레임워크 서비스별 전역 요청 인터셉터 Composite
 * 설정된 모든 {@link IRequestInterceptor} 구현체 중 현재 요청 URL에 매칭되는 인터셉터만 필터링하여,
 * 순차적으로 pre/post/after 처리합니다.
 * 
 * <p>
 * 인터셉터는 {@link IRequestInterceptor#getIndex()} 기준으로 정렬되어 실행됩니다.
 * </p>
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class RequestInterceptorComposite {

    /**
     * 등록된 전체 인터셉터 목록
     */
    private final List<IRequestInterceptor> interceptors;

    /**
     * 현재 요청 URI에 매칭되는 인터셉터만 필터링합니다.
     * 
     * @param request 현재 요청 객체
     * @return 지원되는 인터셉터 목록 (정렬됨)
     */
    private List<IRequestInterceptor> getSupportedServiceInterceptorList(HttpServletRequest request) {
        return new ArrayList<>(
                this.interceptors.stream()
                        .filter(interceptor -> interceptor.isSupported(request))
                        .sorted() // index 순으로 정렬
                        .toList());
    }

    /**
     * preHandle 처리 (요청 전 실행)
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param handler  핸들러 객체
     * @return false 반환 시 요청 중단
     * @throws Exception 처리 중 예외 발생 시
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return true;
        }

        List<IRequestInterceptor> interceptorList = getSupportedServiceInterceptorList(request);
        for (IRequestInterceptor interceptor : interceptorList) {
            if (!interceptor.preHandle(request, response, handler)) {
                return false;
            }
        }
        return true;
    }

    /**
     * postHandle 처리 (컨트롤러 실행 후, View 렌더링 전)
     *
     * @param request      HTTP 요청
     * @param response     HTTP 응답
     * @param handler      핸들러 객체
     * @param modelAndView View 모델 객체
     * @throws Exception 처리 중 예외 발생 시
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }

        List<IRequestInterceptor> interceptorList = getSupportedServiceInterceptorList(request);
        for (IRequestInterceptor interceptor : interceptorList) {
            interceptor.postHandle(request, response, handler, modelAndView);
        }
    }

    /**
     * afterCompletion 처리 (전체 요청 완료 후 실행)
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param handler  핸들러 객체
     * @param ex       예외 객체 (있을 경우)
     * @throws Exception 처리 중 예외 발생 시
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }

        List<IRequestInterceptor> interceptorList = getSupportedServiceInterceptorList(request);
        for (IRequestInterceptor interceptor : interceptorList) {
            interceptor.afterCompletion(request, response, handler, ex);
        }
    }
}
