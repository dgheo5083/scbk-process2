package com.scbank.process.api.fw.core.cache;

import java.util.Map;

/**
 * <pre>
 * 프레임워크 전용 캐시 매니저 인터페이스입니다.
 * 
 * 다양한 캐시 스토리지(e.g., Local Cache, Redis 등)에 대해 
 * 통합된 캐시 접근/조작 기능을 제공합니다.
 * 
 * 주요 책임:
 * - 캐시에서 데이터 조회
 * - 캐시 저장 및 삭제
 * - 캐시 존재 여부 확인
 * - 캐시 비우기
 * </pre>
 *
 * <p>
 * <b>주요 특징:</b>
 * </p>
 * <ul>
 * <li>캐시 이름(cacheName) 단위로 데이터 구분</li>
 * <li>Generic 타입 지원을 통한 타입 안전성 제공</li>
 * </ul>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 */
public interface ICacheManager {

    /**
     * 기본 Bean ID 상수
     */
    String BEAN_ID = "csl.cache.manager";

    /**
     * 캐시에서 지정된 키의 값을 조회합니다.
     *
     * @param cacheName 캐시 이름
     * @param key       조회할 키
     * @param type      반환 타입
     * @param <T>       반환 객체 타입
     * @return 캐시된 객체 (없으면 {@code null})
     */
    <T> T get(String cacheName, Object key, Class<T> type);

    /**
     * 캐시에 키-값을 저장합니다.
     *
     * @param cacheName 캐시 이름
     * @param key       저장할 키
     * @param value     저장할 값
     */
    void put(String cacheName, Object key, Object value);

    /**
     * 캐시에서 지정된 키를 삭제합니다.
     *
     * @param cacheName 캐시 이름
     * @param key       삭제할 키
     */
    void evict(String cacheName, Object key);

    /**
     * 지정된 캐시를 완전히 비웁니다.
     *
     * @param cacheName 비울 캐시 이름
     */
    void clear(String cacheName);

    /**
     * 지정된 키가 캐시에 존재하는지 확인합니다.
     *
     * @param cacheName 캐시 이름
     * @param key       확인할 키
     * @return 존재하면 {@code true}, 없으면 {@code false}
     */
    boolean contains(String cacheName, Object key);

    /**
     * 주어진 캐시 이름에 해당하는 모든 캐시 데이터를 반환
     *
     * @param cacheName 캐시 이름
     * @param type      값 타입
     * @return 캐시 키-값 맵
     */
    <T> Map<ICacheKey, T> getAll(String cacheName, Class<T> type);
}
