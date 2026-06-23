package com.scbank.process.api.fw.session.impl;

import com.scbank.process.api.fw.session.SessionScope;

/**
 * 프레임워크 공통 세션 - 글로벌 세션 객체
 * <p>
 * 로그인 여부와 관계없이 모든 사용자에게 공통으로 적용되는 세션 저장소입니다.
 * 내부적으로 {@link AbstractSessionObject}를 상속하며, 세션 범위를
 * {@link SessionScope#GLOBAL}로 지정합니다.
 * </p>
 *
 * <p>
 * 주로 사용자 트래킹, 채널 정보, 다국어 설정 등 로그인과 무관한 공통 데이터를 저장하는 데 사용됩니다.
 * </p>
 *
 * @see AbstractSessionObject
 * @see SessionScope#GLOBAL
 * @see LoginSessionObject
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public class GlobalSessionObject extends AbstractSessionObject {

    private static final long serialVersionUID = 1L;

    /**
     * 기본 생성자 - GLOBAL 세션 범위로 초기화
     */
    public GlobalSessionObject() {
        super(SessionScope.GLOBAL);
    }
}
