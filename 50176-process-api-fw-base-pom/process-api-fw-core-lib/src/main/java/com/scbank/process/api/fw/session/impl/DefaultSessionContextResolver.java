package com.scbank.process.api.fw.session.impl;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextAdapter;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 기본 세션 컨텍스트 리졸버 구현체
 * <p>
 * HTTP 요청으로부터 {@link ISessionContext}를 조회하거나, 존재하지 않을 경우 새로 초기화하여 세션에 등록합니다.
 * 내부적으로 {@link HttpSession}을 사용하여 세션을 관리하며,
 * 세션 객체의 타입이 일치하지 않는 경우 {@link ISessionContextAdapter}를 통해 변환합니다.
 * </p>
 *
 * <ul>
 * <li>세션 미존재 시 {@link DefaultSessionContext}로 초기화</li>
 * <li>기존 세션은 어댑터를 통해 {@link ISessionContext}로 복원</li>
 * </ul>
 *
 * @see ISessionContext
 * @see DefaultSessionContext
 * @see ISessionContextAdapter
 * @see HttpSession
 * 
 * @version 1.0
 * @since 2025.04.17
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultSessionContextResolver implements ISessionContextResolver {

    /**
     * 요청으로부터 {@link ISessionContext}를 조회 또는 생성 후 반환
     *
     * @param request HTTP 요청 객체
     * @return {@link ISessionContext} 인스턴스
     */
    @Override
    public ISessionContext resolve(HttpServletRequest request) {
        if (this.isNewSession(request)) {
            this.initSession(request);
        } else {
            this.checkSession(request);
        }

        HttpSession session = request.getSession();
        return (ISessionContext) session.getAttribute(ISessionContext.SESSION_ATTR_KEY);
    }

    /**
     * 현재 요청에 대해 신규 세션이 필요한지 여부 확인
     *
     * @param request HTTP 요청 객체
     * @return 세션 컨텍스트가 비어있으면 true
     */
    private boolean isNewSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean isNewSession = session == null || session.getAttribute(ISessionContext.SESSION_ATTR_KEY) == null;

        if (log.isDebugEnabled()) {
            log.debug("# 프레임워크 신규 세션 여부: {}", isNewSession);
        }

        return isNewSession;
    }

    /**
     * 새로운 세션 컨텍스트를 생성하여 세션에 등록
     *
     * @param request HTTP 요청 객체
     */
    private void initSession(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();

        if (httpSession == null) {
            if (log.isDebugEnabled()) {
                log.debug("# http session is null ");
            }
            return;
        }

        DefaultSessionContext frameworkSession = new DefaultSessionContext();
        frameworkSession.setSessionId(httpSession.getId());

        // 전역 세션, 로그인 세션 초기화
        frameworkSession.setGlobalSession(new GlobalSessionObject());
        frameworkSession.setLoginSession(new LoginSessionObject());

        httpSession.setAttribute(ISessionContext.SESSION_ATTR_KEY, frameworkSession);

        if (log.isDebugEnabled()) {
            log.debug("# 프레임워크 신규 세션 초기화: {}", httpSession.getId());
        }
    }

    /**
     * 기존 세션 객체가 올바른 형태인지 검증하고, 필요 시 어댑터로 복원
     *
     * @param request HTTP 요청 객체
     */
    private void checkSession(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();

        if (httpSession == null) {
            if (log.isDebugEnabled()) {
                log.debug("# http session is null ");
            }
            return;
        }

        Object raw = httpSession.getAttribute(ISessionContext.SESSION_ATTR_KEY);

        ISessionContextAdapter sessionContextAdapter = RuntimeContext.getBean(ISessionContextAdapter.class);
        if (sessionContextAdapter == null) {
        	if (log.isDebugEnabled()) {
                log.debug("# 프레임워크 sessionContextAdapter bean is null");
            }
        	return;
        }
        
        ISessionContext frameworkSession = sessionContextAdapter.adapt(raw);
        httpSession.setAttribute(ISessionContext.SESSION_ATTR_KEY, frameworkSession);

        if (log.isDebugEnabled()) {
            log.debug("# 프레임워크 체크 세션 : {}", httpSession.getId());
        }
    }
}
