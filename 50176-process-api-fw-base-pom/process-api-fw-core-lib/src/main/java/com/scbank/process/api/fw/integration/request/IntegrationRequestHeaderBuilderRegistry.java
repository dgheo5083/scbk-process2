package com.scbank.process.api.fw.integration.request;

/**
 * 연계 시스템별 요청 헤더 빌더를 제공하는 레지스트리 인터페이스
 *
 * <p>
 * 각 시스템 ID(MCI, FEP, HOST 등)에 매핑된 {@link IntegrationRequestHeaderBuilder} 인스턴스를
 * 반환합니다.
 * <ul>
 * <li>프레임워크에서 통합 연계 처리를 위해 사용됩니다.</li>
 * <li>내부적으로 Map&lt;String, IntegrationRequestHeaderBuilder&gt; 형식으로 관리하는 구현체를
 * 통해 동작합니다.</li>
 * </ul>
 *
 * <p>
 * ex) systemId가 "MCI"일 경우 MCIRequestHeaderBuilder를 반환
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IntegrationRequestHeaderBuilderRegistry {

    /**
     * 지정된 시스템 ID에 해당하는 요청 헤더 빌더를 반환합니다.
     *
     * @param systemId 연계 시스템 식별자 (예: "MCI", "FEP")
     * @return 해당 시스템에 매핑된 IntegrationRequestHeaderBuilder
     */
    @SuppressWarnings("rawtypes")
    IntegrationRequestHeaderBuilder get(String systemId);
}
