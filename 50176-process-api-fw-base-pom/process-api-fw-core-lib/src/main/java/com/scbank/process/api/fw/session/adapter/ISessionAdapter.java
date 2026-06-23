package com.scbank.process.api.fw.session.adapter;

/**
 * 세션 속성 값의 포맷/타입을 상호 변환하기 위한 어댑터 인터페이스.
 *
 * <p>
 * 주요 역할:
 * <ul>
 * <li>저장 시 {@link #wrap(String, Object)}로 세션값에 메타 삽입/타입 변환 수행</li>
 * <li>조회 시 {@link #unwrap(String, Object)}로 메타 제거/원래 타입 복원 수행</li>
 * <li>{@link #supports(String, Object)}로 특정 세션 키/값에 대한 처리 가능 여부 판단</li>
 * </ul>
 *
 * <p>
 * 사용 흐름(예):
 * 
 * <pre>
 * if (adapter.supports(key, value)) {
 *     Object toSave = adapter.wrap(key, value); // setAttribute 전에 호출
 *     // ... delegate.setAttribute(key, toSave);
 * }
 *
 * Object raw = delegate.getAttribute(key);
 * Object view = adapter.supports(key, raw) ? adapter.unwrap(key, raw) : raw; // getAttribute 후 처리
 * </pre>
 *
 * <p>
 * 구현 시 유의사항:
 * <ul>
 * <li>null 입력 허용 정책을 문서화(필요 시 null-safe 처리)</li>
 * <li>멀티 스레드 환경에서 상태를 가지지 않는(stateless) 구현 권장</li>
 * <li>wrap/unwrap은 반드시 역변환 가능하도록(가능한 경우) 설계</li>
 * </ul>
 */
public interface ISessionAdapter {

    /**
     * 주어진 세션 키/값 쌍에 대해 본 어댑터가 처리 대상인지 여부를 반환한다.
     *
     * @param sessionKey 세션 속성 키(예: "ma30.session")
     * @param value      현재 값(저장 전/조회 후의 원본 또는 래핑된 값). null일 수 있음.
     * @return 처리 가능하면 true, 아니면 false
     */
    boolean supports(String sessionKey, Object value);

    /**
     * 세션에 저장하기 전에 값에 대한 전처리를 수행한다.
     * <p>
     * 예: 메타데이터 삽입, 타입 호환 포맷으로의 변환, 암호화 등.
     *
     * @param sessionKey 세션 속성 키
     * @param value      저장 예정인 원본 값(애플리케이션 관점의 값). null일 수 있음.
     * @return 세션에 실제로 저장할 변환된 값(래핑된 값). 구현에 따라 value 그대로 반환할 수 있음.
     */
    Object wrap(String sessionKey, Object value);

    /**
     * 세션에서 조회한 값에 대한 후처리를 수행한다.
     * <p>
     * 예: 메타데이터 제거, 원래 도메인 타입으로 복원, 복호화 등.
     *
     * @param sessionKey 세션 속성 키
     * @param value      세션에 저장되어 있던 래핑된 값(원본이 아님). null일 수 있음.
     * @return 애플리케이션에서 사용 가능한 복원/변환된 값. 구현에 따라 value 그대로 반환할 수 있음.
     */
    Object unwrap(String sessionKey, Object value);
}
