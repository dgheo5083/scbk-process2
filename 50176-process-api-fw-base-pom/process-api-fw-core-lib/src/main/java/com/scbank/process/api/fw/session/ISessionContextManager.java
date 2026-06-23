package com.scbank.process.api.fw.session;

/**
 * 프레임워크 공통 세션 매니저 인터페이스
 * <p>
 * 로그인 세션과 글로벌 세션의 데이터를 통합적으로 관리하는 API를 제공합니다.
 * 내부적으로 {@link ISessionContext}를 참조하여 세션 값을 저장/조회/삭제할 수 있도록 추상화된 인터페이스입니다.
 * </p>
 * 
 * <ul>
 * <li>로그인 여부 판단 및 로그인/로그아웃 처리</li>
 * <li>로그인/전역 세션 값 저장, 조회, 삭제, 초기화</li>
 * </ul>
 *
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public interface ISessionContextManager {

    /**
     * Spring Bean ID 상수
     */
    String BEAN_ID = "csl.SessionManager";

    /**
     * 현재 사용자가 로그인 상태인지 여부 반환
     *
     * @return 로그인 상태면 true, 아니면 false
     */
    default boolean isLogin() {
        return false;
    }

    /**
     * 로그인 처리
     *
     * @param userId 로그인할 사용자 ID
     */
    default void login(String userId) {
        // 구현체에서 로그인 상태 초기화 수행
    }

    /**
     * 사용자 로그아웃 처리
     */
    default void logout() {
        // 구현체에서 세션 초기화 수행
    }

    /**
     * 로그인 세션에서 값 조회
     *
     * @param sessionKey   조회할 키
     * @param requiredType 반환 받을 타입
     * @param <T>          반환 타입 (Serializable)
     * @return 저장된 값, 없으면 null
     */
    <T> T getLoginValue(String sessionKey, Class<T> requiredType);

    /**
     * 로그인 세션에 값 저장
     *
     * @param sessionKey 저장할 키
     * @param value      저장할 값
     * @param <T>        값 타입 (Serializable)
     */
    <T> void setLoginValue(String sessionKey, T value);

    /**
     * 로그인 세션에서 값 제거
     *
     * @param sessionKey 제거할 키
     */
    void removeLoginValue(String sessionKey);

    /**
     * 로그인 세션 전체 초기화
     */
    void clearLoginSession();

    /**
     * 글로벌 세션에서 값 조회
     *
     * @param sessionKey   조회할 키
     * @param requiredType 반환 받을 타입
     * @param <T>          반환 타입 (Serializable)
     * @return 저장된 값, 없으면 null
     */
    <T> T getGlobalValue(String sessionKey, Class<T> requiredType);

    /**
     * 글로벌 세션에 값 저장
     *
     * @param sessionKey 저장할 키
     * @param value      저장할 값
     * @param <T>        값 타입 (Serializable)
     */
    <T> void setGlobalValue(String sessionKey, T value);

    /**
     * 글로벌 세션에서 값 제거
     *
     * @param sessionKey 제거할 키
     */
    void removeGlobalValue(String sessionKey);

    /**
     * 글로벌 세션 전체 초기화
     */
    void clearGlobalSession();
}
