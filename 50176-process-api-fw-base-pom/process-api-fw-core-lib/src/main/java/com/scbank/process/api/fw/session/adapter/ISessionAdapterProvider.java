package com.scbank.process.api.fw.session.adapter;

/**
 * 세션 값 변환을 총괄하는 어댑터 프로바이더.
 *
 * <p>
 * 역할:
 * <ul>
 * <li>하나 이상의 {@link ISessionAdapter} 구현을 관리/조합한다.</li>
 * <li>{@code sessionKey}, {@code value}를 기준으로 적합한 어댑터를 선택해
 * {@link #wrap(String, Object)}, {@link #unwrap(String, Object)} 호출을 위임한다.</li>
 * <li>해당하는 어댑터가 없을 경우 기본 전략(그대로 반환 등)을 적용한다.</li>
 * </ul>
 *
 * <p>
 * 구현 가이드:
 * <ul>
 * <li>스레드 안전성을 고려해 불변 리스트/등록 방식 권장</li>
 * <li>어댑터 선택 기준: {@link ISessionAdapter#supports(String, Object)}</li>
 * <li>다중 매칭 시 우선순위 정책을 명확히(첫 매칭/가장 구체적 매칭 등)</li>
 * <li>null 인자 처리 정책을 문서화(가능하면 null-safe)</li>
 * </ul>
 */
public interface ISessionAdapterProvider {

    /**
     * 세션 저장 전, 값에 대한 전처리를 수행한다.
     * <p>
     * 예: 메타데이터 삽입, 타입 호환 포맷 변환, 암호화 등.
     * </p>
     *
     * @param sessionKey 세션 속성 키(예: "ma30.session")
     * @param value      저장 예정 값(애플리케이션 관점의 원본). null 가능.
     * @return 세션에 실제 저장할 변환된 값(어댑터가 없거나 불필요하면 원본 반환 가능)
     */
    Object wrap(String sessionKey, Object value);

    /**
     * 세션 조회 후, 값에 대한 후처리를 수행한다.
     * <p>
     * 예: 메타데이터 제거, 원래 도메인 타입 복원, 복호화 등.
     * </p>
     *
     * @param sessionKey 세션 속성 키
     * @param value      세션에 저장되어 있던 래핑된 값. null 가능.
     * @return 애플리케이션에서 사용 가능한 복원/변환된 값(어댑터가 없거나 불필요하면 원본 반환 가능)
     */
    Object unwrap(String sessionKey, Object value);
}
