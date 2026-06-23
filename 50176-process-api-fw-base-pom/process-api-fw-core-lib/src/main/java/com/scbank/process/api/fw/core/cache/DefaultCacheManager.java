package com.scbank.process.api.fw.core.cache;

import java.util.Map;
import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;

import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * 프레임워크 표준 캐시 매니저 구현체입니다.
 * 
 * Spring의 {@link SimpleCacheManager} + Caffeine 캐시를 기반으로,
 * {@link ICacheManager} 인터페이스를 구현합니다.
 * 
 * 주요 역할:
 * - 캐시 생성 및 TTL(Time-To-Live), 최대 크기(Max Size) 설정
 * - 기본적인 캐시 저장/조회/삭제/초기화 기능 제공
 * </pre>
 *
 * <p>
 * <b>특징:</b>
 * </p>
 * <ul>
 * <li>Caffeine 캐시를 활용한 메모리 내 캐시</li>
 * <li>{@link CacheProperties}를 기반으로 동적 캐시 설정</li>
 * <li>캐시 미존재 시 예외 발생 처리</li>
 * </ul>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 16.
 * 
 * @see ICacheManager
 * @see SimpleCacheManager
 */
@RequiredArgsConstructor
public class DefaultCacheManager implements ICacheManager {

	private final CacheManager delegate;

	public <T> T get(String cacheName, Object key, Class<T> type) {
		var cache = delegate.getCache(cacheName);
		return (cache == null) ? null : cache.get(key, type);
	}

	public void put(String cacheName, Object key, Object value) {
		var cache = Objects.requireNonNull(delegate.getCache(cacheName), "");
		cache.put(key, value);
	}

	public void evict(String cacheName, Object key) {
		var cache = delegate.getCache(cacheName);
		if (cache != null) {
			cache.evict(key);
		}
	}

	public void clear(String cacheName) {
		var cache = delegate.getCache(cacheName);
		if (cache != null) {
			cache.clear();
		}
	}

	public boolean contains(String cacheName, Object key) {
		var cache = delegate.getCache(cacheName);
		return cache != null && cache.get(key) != null;
	}

	public <T> Map<ICacheKey, T> getAll(String cacheName, Class<T> type) {
		return null;
	}

}
