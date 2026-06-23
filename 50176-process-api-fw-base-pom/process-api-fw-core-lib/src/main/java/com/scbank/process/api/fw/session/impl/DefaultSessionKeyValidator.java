package com.scbank.process.api.fw.session.impl;

import java.util.List;
import java.util.Map;

import com.scbank.process.api.fw.session.ISessionKeyValidator;
import com.scbank.process.api.fw.session.SessionProperties;
import com.scbank.process.api.fw.session.SessionScope;

/**
 * 기본 세션 키 검증기 구현체
 * <p>
 * {@link SessionProperties}에 설정된 허용된 세션 키 목록을 기준으로,
 * 세션에 사용할 수 있는 키인지 여부를 검사합니다.
 * 세션 검증 기능이 비활성화된 경우(enabled=false), 모든 키를 허용합니다.
 * </p>
 *
 * @see ISessionKeyValidator
 * @see SessionScope
 * @see SessionProperties
 * 
 *      <pre>
 * 예시 설정 (application.yml):
 * framework:
 *   session:
 *     enabled: true
 *     allowed-keys:
 *       GLOBAL: [traceId, channelId]
 *       LOGIN: [userId, userRole]
 *      </pre>
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public class DefaultSessionKeyValidator implements ISessionKeyValidator {

    /**
     * 세션 범위별 허용된 세션 키 목록
     */
    private final Map<SessionScope, List<String>> allowedSessionKeys;

    /**
     * 세션 키 검증 기능 사용 여부
     */
    private final boolean enabled;

    /**
     * 생성자 - 설정 프로퍼티 기반 초기화
     *
     * @param properties framework.session 설정 정보
     */
    public DefaultSessionKeyValidator(SessionProperties properties) {
        this.enabled = properties.isEnabled();
        this.allowedSessionKeys = properties.getAllowedKeys();
    }

    /**
     * 로그인 세션 키 유효성 검사
     *
     * @param key 검사할 키
     * @return 유효하면 true, 비활성화 상태여도 true
     */
    @Override
    public boolean isAllowedLoginKey(Object key) {
        if (!enabled) {
            return true;
        }
        return allowedSessionKeys.getOrDefault(SessionScope.LOGIN, List.of())
                .stream()
                .anyMatch(k -> k.equals(key));
    }

    /**
     * 글로벌 세션 키 유효성 검사
     *
     * @param key 검사할 키
     * @return 유효하면 true, 비활성화 상태여도 true
     */
    @Override
    public boolean isAllowedGlobalKey(Object key) {
        if (!enabled) {
            return true;
        }
        return allowedSessionKeys.getOrDefault(SessionScope.GLOBAL, List.of())
                .stream()
                .anyMatch(k -> k.equals(key));
    }
}
