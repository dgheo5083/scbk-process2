package com.scbank.process.api.fw.session;

/**
 * 세션 저장소 범위 구분 enum
 * <p>
 * 프레임워크에서 관리하는 세션 키의 범위를 구분하기 위한 열거형입니다.
 * {@link co.kr.scbank.framework.core.session.SessionProperties} 및
 * {@link co.kr.scbank.framework.core.session.ISessionKeyValidator} 등에서 사용됩니다.
 * </p>
 *
 * <ul>
 * <li>{@code GLOBAL} - 로그인 여부와 관계없이 모든 사용자에게 공통으로 적용되는 세션 범위</li>
 * <li>{@code LOGIN} - 로그인한 사용자에게만 적용되는 개인화 세션 범위</li>
 * </ul>
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public enum SessionScope {
    /**
     * 전역 세션 범위 (비로그인 사용자 포함)
     */
    GLOBAL,

    /**
     * 로그인 세션 범위 (로그인한 사용자 한정)
     */
    LOGIN
}
