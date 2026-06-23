package com.scbank.process.api.fw.session.adapter;

import java.util.Optional;

/**
 * 세션 키별 메타데이터(타입/요소/자식 필드)를 조회하는 레지스트리.
 *
 * <p>
 * 역할
 * </p>
 * <ul>
 * <li>세션 저장/조회 시 사용할 {@link SessionMetadataInfo}를 제공</li>
 * <li>간편 조회를 위한 v3/v4 타입, 요소 타입(ElementType) 단건 조회 API 제공</li>
 * </ul>
 *
 * <p>
 * 반환 규약
 * </p>
 * <ul>
 * <li>요청한 세션 키에 대한 메타가 없거나 비활성화된 경우: {@code Optional.empty()}</li>
 * <li>존재하나 일부 필드만 정의된 경우: 정의된 필드에 한해 {@code Optional}이 채워짐</li>
 * </ul>
 *
 * <p>
 * 스레드 안전성
 * </p>
 * <ul>
 * <li>애플리케이션 전역에서 공유되는 경우가 많으므로, 구현체는 불변/캐시 기반으로 설계 권장</li>
 * </ul>
 */
public interface ISessionMetadataRegistry {

    /**
     * 세션 키에 매핑된 전체 메타 정보 조회.
     *
     * @param sessionKey 세션 속성 키(예: "ma30.session" 또는 도메인별 세션 키)
     * @return 메타 정보가 있으면 {@code Optional.of(info)}, 없으면 {@code Optional.empty()}
     */
    Optional<SessionMetadataInfo> getMetadata(String sessionKey);

    /**
     * 세션 키에 매핑된 v3 타입(클래스명 등) 조회.
     *
     * @param sessionKey 세션 속성 키
     * @return v3 타입이 정의된 경우 해당 문자열, 없으면 {@code Optional.empty()}
     */
    Optional<String> getV3Type(String sessionKey);

    /**
     * 세션 키에 매핑된 v4 타입(클래스명 등) 조회.
     *
     * @param sessionKey 세션 속성 키
     * @return v4 타입이 정의된 경우 해당 문자열, 없으면 {@code Optional.empty()}
     */
    Optional<String> getV4Type(String sessionKey);

    /**
     * 세션 키에 매핑된 리스트/배열 요소 타입(ElementType) 메타 정보 조회.
     * <p>
     * 요소 타입이 없는 단순 객체 키의 경우 빈 Optional을 반환한다.
     * </p>
     *
     * @param sessionKey 세션 속성 키
     * @return 요소 타입 메타가 있으면 {@code Optional.of(mapping)}, 없으면
     *         {@code Optional.empty()}
     */
    Optional<SessionMetadataInfo.ElementTypeMapping> getElementType(String sessionKey);
}
