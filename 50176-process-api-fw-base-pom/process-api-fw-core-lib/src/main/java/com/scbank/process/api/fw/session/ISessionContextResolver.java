package com.scbank.process.api.fw.session;

import jakarta.servlet.http.HttpServletRequest;

/**
 * HTTP 요청으로부터 {@link ISessionContext}를 추출하거나 생성하는 인터페이스
 * <p>
 * 주로 필터, 인터셉터, 컨트롤러 진입 전 단계에서 세션 컨텍스트를 초기화하거나 획득하는 데 사용됩니다.
 * </p>
 * <p>
 * 세션이 존재하지 않으면 새로운 세션 컨텍스트를 생성하여 등록합니다.
 * </p>
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public interface ISessionContextResolver {

    /**
     * HttpServletRequest로부터 {@link ISessionContext}를 반환
     * <p>
     * 세션이 없으면 새로운 세션을 생성하여 {@code request.setAttribute(...)} 등으로 등록할 수 있습니다.
     * </p>
     *
     * @param request 현재 HTTP 요청 객체
     * @return {@link ISessionContext} 인스턴스 (기존 또는 새로 생성된)
     */
    ISessionContext resolve(HttpServletRequest request);
}
