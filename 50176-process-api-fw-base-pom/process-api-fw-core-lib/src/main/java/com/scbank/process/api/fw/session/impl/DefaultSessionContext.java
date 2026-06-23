package com.scbank.process.api.fw.session.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContext;

import lombok.Data;

/**
 * 기본 세션 컨텍스트 구현체
 * <p>
 * {@link ISessionContext}의 기본 구현체로, 전역 세션과 로그인 세션 정보를 함께 관리합니다.
 * 클라이언트 IP 및 세션 ID 정보도 포함되어 있으며, 로그인 상태에 따라 로그인 세션 데이터를 사용할 수 있습니다.
 * </p>
 * 
 * <ul>
 * <li>전역 세션은 항상 존재하며, 로그인 세션은 로그인 시점에 초기화됩니다.</li>
 * <li>Jackson 역직렬화 시 알 수 없는 필드는 무시됩니다.</li>
 * </ul>
 *
 * @see ISessionContext
 * @see GlobalSessionObject
 * @see LoginSessionObject
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultSessionContext implements ISessionContext {

    private static final long serialVersionUID = 1L;

    /**
     * 전역 세션 객체
     */
    private GlobalSessionObject globalSession;

    /**
     * 로그인 세션 객체
     */
    private LoginSessionObject loginSession;

    /**
     * 클라이언트 IP 주소
     */
    private String clientIp;

    /**
     * 세션 ID (고유 식별자)
     */
    private String sessionId;

    /**
     * 기본 생성자 (전역 세션 자동 생성)
     */
    public DefaultSessionContext() {
        this.globalSession = new GlobalSessionObject();
    }

    /**
     * 세션 ID 기반 생성자
     *
     * @param sessionId 세션 ID
     */
    public DefaultSessionContext(String sessionId) {
        if (StringUtils.isNotEmpty(sessionId)) {
            sessionId = sessionId.trim();
        }
        this.sessionId = sessionId;
    }

    /**
     * 전역 세션에 값 저장
     *
     * @param key   세션 키
     * @param value 저장할 값
     */
    @Override
    public <T> void setGlobalAttribute(String key, T value) {
        if (this.globalSession == null) {
            this.globalSession = new GlobalSessionObject();
        }
        this.globalSession.put(key, value);
    }

    /**
     * 로그인 세션에 값 저장
     *
     * @param key   세션 키
     * @param value 저장할 값
     * @throws IllegalStateException 로그인하지 않은 상태에서 호출한 경우
     */
    @Override
    public <T> void setLoginAttribute(String key, T value) {
        if (!isLogined()) {
            throw new IllegalStateException("로그인 상태에서만 로그인 세션에 값을 추가할 수 있습니다.");
        }
        this.loginSession.put(key, value);
    }

    /**
     * 현재 세션이 로그인 상태인지 확인
     *
     * @return 로그인 상태이면 true, 아니면 false
     */
    @Override
    public boolean isLogined() {
        return this.loginSession != null && loginSession.isLogined();
    }

    /**
     * 로그인 처리 (로그인 세션 초기화)
     *
     * @param userId 로그인 사용자 ID
     */
    @Override
    public void initOnLogin(String userId) {
        if (this.loginSession != null) {
            try {
                this.loginSession.initOnLogin(userId);
            } catch (Throwable ignored) {
                // 예외 무시
            }
        }
    }

    /**
     * 로그아웃 처리 (내부적으로 {@link #initOnLogout()} 호출)
     */
    @Override
    public void logout() {
        this.initOnLogout();
    }

    /**
     * 세션 초기화 처리 (전역 + 로그인 세션 정리)
     */
    @Override
    public void initOnLogout() {
        if (this.globalSession != null) {
            this.globalSession.clear();
        }
        if (this.loginSession != null) {
            try {
                this.loginSession.initOnLogout();
            } catch (Throwable ignored) {
                // 예외 무시
            }
        }
    }
}
