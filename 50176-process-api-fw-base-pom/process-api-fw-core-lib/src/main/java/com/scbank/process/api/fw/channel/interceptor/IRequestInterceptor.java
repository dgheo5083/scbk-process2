package com.scbank.process.api.fw.channel.interceptor;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Dispatcher 흐름에서 실행되는 요청 인터셉터 인터페이스입니다.
 * 실행 순서(index)와 URL 패턴 조건을 함께 정의할 수 있습니다.
 * 
 * <p>
 * Spring MVC의 {@link org.springframework.web.servlet.HandlerInterceptor}를 기반으로
 * 합니다.
 * 
 * @author sungdon.choi
 */
public interface IRequestInterceptor extends Comparable<IRequestInterceptor> {

    /**
     * 인터셉터의 실행 우선순위를 반환합니다.
     * 값이 낮을수록 먼저 실행됩니다.
     *
     * @return 인터셉터 실행 순서
     */
    default int getIndex() {
        return 0;
    }

    /**
     * 인터셉터가 적용될 URL 패턴 목록을 반환합니다.
     * 예: {@code "/api/**"}
     *
     * @return URL 패턴 목록
     */
    default List<String> getUrlPatterns() {
        return List.of();
    }

    /**
     * 해당 요청에 대해 이 인터셉터가 동작해야 하는지를 판단합니다.
     *
     * @param request 현재 요청
     * @return true일 경우 인터셉터가 적용됨
     */
    default boolean isSupported(HttpServletRequest request) {
        return false;
    }

    /**
     * 컨트롤러 실행 전에 호출됩니다.
     * false를 반환하면 요청 처리가 중단되며, 이후 흐름도 실행되지 않습니다.
     *
     * @param request  요청
     * @param response 응답
     * @param handler  실행 대상 핸들러 객체
     * @return 계속 진행할지 여부
     * @throws Exception 예외 발생 시
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }

    /**
     * 컨트롤러 실행 후, View 렌더링 전 호출됩니다.
     *
     * @param request      요청
     * @param response     응답
     * @param handler      실행 대상 핸들러 객체
     * @param modelAndView 렌더링될 뷰 정보
     * @throws Exception 예외 발생 시
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     * 요청 처리 완료 후, 예외 발생 여부와 상관없이 항상 호출됩니다.
     *
     * @param request  요청
     * @param response 응답
     * @param handler  실행 대상 핸들러 객체
     * @param ex       처리 중 발생한 예외 (없을 수도 있음)
     * @throws Exception 예외 발생 시
     */
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
    }

    /**
     * 인터셉터 정렬을 위한 비교 메서드입니다.
     * {@code getIndex()} 값을 기준으로 정렬됩니다.
     *
     * @param o 비교 대상 인터셉터
     * @return 정렬 결과
     */
    @Override
    default int compareTo(IRequestInterceptor o) {
        return Integer.compare(this.getIndex(), o.getIndex());
    }
}
