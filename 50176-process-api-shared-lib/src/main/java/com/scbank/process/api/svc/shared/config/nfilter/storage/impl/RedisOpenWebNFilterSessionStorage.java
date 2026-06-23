package com.scbank.process.api.svc.shared.config.nfilter.storage.impl;

import java.time.Duration;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 세션에 담긴 openweb nfilter 데이터를 꺼내와 redis에 저장/읽기 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class RedisOpenWebNFilterSessionStorage implements IOpenWebNFilterSessionStorage {

	private final RedisTemplate<String, Object> redisTemplate;
	
	public void save(String key, Map<String, Object> sessionData) {
		this.redisTemplate.opsForValue().set(key, sessionData, Duration.ofMinutes(10));
		
		Long exipre = this.redisTemplate.getExpire(key);
		log.debug("# Redis save nfilter session info key={}, ttl={}", key, exipre);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> read(String key) {
		Long exipre = this.redisTemplate.getExpire(key);
		log.debug("# Redis read nfilter session info key={}, ttl={}", key, exipre);
		
		return (Map<String, Object>) this.redisTemplate.opsForValue().get(key);
	}
}
