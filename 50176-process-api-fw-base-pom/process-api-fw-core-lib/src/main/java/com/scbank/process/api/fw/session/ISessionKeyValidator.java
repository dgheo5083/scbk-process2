package com.scbank.process.api.fw.session;

/**
 * 세션 키 유효성 검증 인터페이스
 * <p>
 * 사전에 정의된 허용 가능한 세션 키 목록에 대해 유효성 검사를 수행합니다.
 * <br>
 * 등록되지 않은 세션 키의 사용을 방지하여, 보안 및 관리 일관성을 유지하기 위해 사용됩니다.
 * </p>
 * <p>
 * 로그인 세션 키와 글로벌 세션 키를 구분하여 검증할 수 있습니다.
 * </p>
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public interface ISessionKeyValidator {

    /**
     * 주어진 키가 허용된 로그인 세션 키인지 확인
     *
     * @param key 확인할 키
     * @return 허용되면 true, 아니면 false
     */
    boolean isAllowedLoginKey(Object key);

    /**
     * 주어진 키가 허용된 글로벌 세션 키인지 확인
     *
     * @param key 확인할 키
     * @return 허용되면 true, 아니면 false
     */
    boolean isAllowedGlobalKey(Object key);

    /**
     * 로그인 세션 키 유효성 검증 (예외 발생)
     *
     * @param key 확인할 키
     * @throws IllegalArgumentException 허용되지 않은 키일 경우
     */
    default void assertLoginKey(Object key) {
        if (!isAllowedLoginKey(key)) {
            throw new IllegalArgumentException("허용되지 않은 로그인 세션 키: " + key);
        }
    }

    /**
     * 글로벌 세션 키 유효성 검증 (예외 발생)
     *
     * @param key 확인할 키
     * @throws IllegalArgumentException 허용되지 않은 키일 경우
     */
    default void assertGlobalKey(Object key) {
        if (!isAllowedGlobalKey(key)) {
            throw new IllegalArgumentException("허용되지 않은 글로벌 세션 키: " + key);
        }
    }
}
