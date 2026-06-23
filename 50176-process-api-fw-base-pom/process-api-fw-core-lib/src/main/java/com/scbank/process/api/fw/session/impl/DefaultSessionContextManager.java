package com.scbank.process.api.fw.session.impl;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 프레임워크 세션 매니저 기본 구현체
 * <p>
 * {@link HttpSession}을 기반으로 프레임워크의 세션 컨텍스트 {@link ISessionContext}를 관리합니다.
 * 전역 세션 및 로그인 세션에 대한 저장/조회/삭제 등의 기능을 제공합니다.
 * </p>
 *
 * <ul>
 * <li>세션 내 {@link ISessionContext} 객체를 중심으로 동작</li>
 * <li>로그인 상태 확인 및 세션 초기화 수행</li>
 * <li>로그인/글로벌 세션의 키 기반 데이터 관리</li>
 * </ul>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@RequiredArgsConstructor
public class DefaultSessionContextManager implements ISessionContextManager {

    /**
     * 로그인 상태 확인
     */
    @Override
    public boolean isLogin() {
        ISessionContext sessionContext = this.getSessionContext();
        return sessionContext != null ? sessionContext.isLogined() : false;
    }

    /**
     * 로그인 처리
     *
     * @param userId 로그인 사용자 ID
     */
    @Override
    public void login(String userId) {
        ISessionContext sessionContext = this.getSessionContext();
        sessionContext.initOnLogin(userId);
        getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
    }

    /**
     * 로그아웃 처리
     */
    @Override
    public void logout() {
        ISessionContext sessionContext = this.getSessionContext();
        sessionContext.logout();
        getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
    }

    /**
     * 로그인 세션에서 값 조회
     */
    @Override
    public <T> T getLoginValue(String sessionKey, Class<T> requiredType) {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext.isLogined()) {
            return sessionContext.getLoginAttribute(sessionKey);
        }
        return null;
    }

    /**
     * 로그인 세션에 값 저장
     */
    @Override
    public <T> void setLoginValue(String sessionKey, T value) {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext.isLogined()) {
            sessionContext.setLoginAttribute(sessionKey, value);
            getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
        }
    }

    /**
     * 로그인 세션에서 값 제거
     */
    @Override
    public void removeLoginValue(String sessionKey) {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext.isLogined()) {
            sessionContext.removeLoginAttribute(sessionKey);
            getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
        }
    }

    /**
     * 로그인 세션 초기화 (전체 제거)
     */
    @Override
    public void clearLoginSession() {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext.isLogined()) {
            sessionContext.clearLoginSession();
            getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
        }
    }

    /**
     * 전역 세션에서 값 조회
     */
    @Override
    public <T> T getGlobalValue(String sessionKey, Class<T> requiredType) {
        ISessionContext sessionContext = this.getSessionContext();
        return sessionContext != null ? sessionContext.getGlobalAttribute(sessionKey) : null;
    }

    /**
     * 전역 세션에 값 저장
     */
    @Override
    public <T> void setGlobalValue(String sessionKey, T value) {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext != null) {
            sessionContext.setGlobalAttribute(sessionKey, value);
            getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
        }
    }

    /**
     * 전역 세션에서 값 제거
     */
    @Override
    public void removeGlobalValue(String sessionKey) {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext != null) {
            sessionContext.removeGlobalAttribute(sessionKey);
            getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
        }
    }

    /**
     * 전역 세션 전체 초기화
     */
    @Override
    public void clearGlobalSession() {
        ISessionContext sessionContext = this.getSessionContext();
        if (sessionContext != null) {
            sessionContext.clearGlobalSession();
            getHttpSession().setAttribute(ISessionContext.SESSION_ATTR_KEY, sessionContext);
        }
    }

    /**
     * HttpSession에서 프레임워크 세션 컨텍스트 추출
     *
     * @return ISessionContext 인스턴스
     */
    private ISessionContext getSessionContext() {
        //IServiceContext sc = ServiceContextHolder.getContext();
        HttpSession httpSession = this.getHttpSession();
        // ISessionContext sessionContext = (ISessionContext)
        // httpSession.getAttribute(ISessionContext.SESSION_ATTR_KEY);
        ISessionContext sessionContext = (ISessionContext)httpSession.getAttribute(ISessionContext.SESSION_ATTR_KEY);
        return sessionContext;
    }

    private HttpSession getHttpSession() {
        IServiceContext sc = ServiceContextHolder.getContext();
        HttpServletRequest request = sc.request();
        HttpSession httpSession = request.getSession();
        return httpSession;
    }
}
