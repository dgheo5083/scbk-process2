package com.scbank.process.api.fw.session;

/**
 * 프레임워크 세션 컨텍스트 복구 어댑터 인터페이스
 * <p>
 * 외부 시스템 또는 다른 세션 저장소에서 전달된 원시 세션 데이터를
 * 프레임워크의 {@link ISessionContext} 형태로 변환할 때 사용됩니다.
 * </p>
 * <p>
 * 구현체 예: Redis 세션, JWT 페이로드, HTTP 요청 속성 등 다양한 세션 원천에서 사용 가능
 * </p>
 * 
 * @author gasigol
 * @version 1.0
 * @since 2025.04.17
 */
public interface ISessionContextAdapter {

    /**
     * 외부 세션 데이터를 {@link ISessionContext}로 변환
     *
     * @param raw 외부 또는 비표준 세션 데이터 객체
     * @return 변환된 프레임워크 세션 컨텍스트, null 반환 가능성 있음
     */
    ISessionContext adapt(Object raw);
}
