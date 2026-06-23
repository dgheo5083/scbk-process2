package com.scbank.process.api.fw.session;

import java.io.Serializable;

import com.scbank.process.api.fw.session.impl.GlobalSessionObject;
import com.scbank.process.api.fw.session.impl.LoginSessionObject;

/**
 * 세션 컨텍스트 인터페이스
 * <p>
 * 전역 세션(Global) 및 로그인 세션(Login) 정보를 관리하는 인터페이스입니다.
 * </p>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public interface ISessionContext extends Serializable {

    /**
     * 기본 세션 속성 키 상수
     */
    String SESSION_ATTR_KEY = "ma30.fw.session";

    /**
     * 세션 ID 반환
     *
     * @return 세션 고유 ID
     */
    String getSessionId();

    /**
     * 클라이언트 IP 주소 반환
     *
     * @return 클라이언트 IP
     */
    String getClientIp();

    /**
     * 전역 세션 객체 반환
     *
     * @return GlobalSessionObject 인스턴스
     */
    GlobalSessionObject getGlobalSession();

    /**
     * 로그인 세션 객체 반환
     *
     * @return LoginSessionObject 인스턴스
     */
    LoginSessionObject getLoginSession();

    /**
     * 전역 세션에서 속성 값 조회
     *
     * @param sessionKey 조회할 속성 키
     * @param <T>        반환 타입
     * @return 해당 속성 값, 없으면 null
     */
    @SuppressWarnings("unchecked")
    default <T extends Serializable> T getGlobalAttribute(String sessionKey) {
        GlobalSessionObject globalSession = getGlobalSession();
        if (globalSession == null) {
            return null;
        }
        return (T) globalSession.get(sessionKey);
    }

    /**
     * 전역 세션에 속성 값 저장
     *
     * @param key   속성 키
     * @param value 저장할 값
     * @param <T>   값 타입
     */
    <T> void setGlobalAttribute(String key, T value);

    /**
     * 전역 세션에서 속성 제거
     *
     * @param sessionKey 제거할 속성 키
     * @param <T>        값 타입
     */
    default <T> void removeGlobalAttribute(String sessionKey) {
        GlobalSessionObject globalSession = getGlobalSession();
        if (globalSession == null) {
            return;
        }
        globalSession.remove(sessionKey);
    }

    /**
     * 전역 세션 초기화 (모든 속성 제거)
     */
    default void clearGlobalSession() {
        GlobalSessionObject globalSession = getGlobalSession();
        if (globalSession == null) {
            return;
        }
        globalSession.clear();
    }

    /**
     * 로그인 세션에서 속성 값 조회
     *
     * @param sessionKey 조회할 속성 키
     * @param <T>        반환 타입
     * @return 해당 속성 값, 없으면 null
     */
    default <T> T getLoginAttribute(String sessionKey) {
        LoginSessionObject loginSession = getLoginSession();
        if (loginSession == null) {
            return null;
        }
        return loginSession.get(sessionKey);
    }

    /**
     * 로그인 세션에 속성 값 저장
     *
     * @param key   속성 키
     * @param value 저장할 값
     * @param <T>   값 타입
     */
    <T> void setLoginAttribute(String key, T value);

    /**
     * 로그인 세션에서 속성 제거
     *
     * @param sessionKey 제거할 속성 키
     * @param <T>        값 타입
     */
    default <T> void removeLoginAttribute(String sessionKey) {
        LoginSessionObject loginSession = getLoginSession();
        if (loginSession == null) {
            return;
        }
        loginSession.remove(sessionKey);
    }

    /**
     * 로그인 세션 초기화 (모든 속성 제거)
     */
    default void clearLoginSession() {
        LoginSessionObject loginSession = getLoginSession();
        if (loginSession == null) {
            return;
        }
        loginSession.clear();
    }

    /**
     * 로그인 여부 확인
     *
     * @return 로그인 상태이면 true, 아니면 false
     */
    default boolean isLogined() {
        LoginSessionObject loginSession = this.getLoginSession();
        if (loginSession == null) {
            return false;
        }
        return loginSession.isLogined();
    }

    /**
     * 로그인 처리 시 초기화 작업 수행
     *
     * @param userId 로그인 사용자 ID
     */
    default void initOnLogin(String userId) {
        LoginSessionObject loginSession = this.getLoginSession();
        if (loginSession != null) {
            try {
                loginSession.initOnLogin(userId);
            } catch (Throwable ignored) {
                // 무시
            }
        }
    }

    /**
     * 로그아웃 처리 (내부적으로 {@link #initOnLogout()} 호출)
     */
    default void logout() {
        this.initOnLogout();
    }

    /**
     * 로그아웃 처리 시 전역/로그인 세션 정리
     */
    default void initOnLogout() {
        GlobalSessionObject globalSession = this.getGlobalSession();
        LoginSessionObject loginSession = this.getLoginSession();
        if (globalSession != null) {
            globalSession.clear();
        }
        if (loginSession != null) {
            try {
                loginSession.initOnLogout();
            } catch (Throwable ignored) {
                // 무시
            }
        }
    }
}
