package com.scbank.process.api.svc.shared.integration.interceptor;

import java.time.Duration;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 전문 중복거래 체크 인터셉터
 */
@Profile("redis-session")
@Slf4j
@RequiredArgsConstructor
@Component("duplicateTranCheckInterceptor")
public class DuplicateTranCheckInterceptor implements IntegrationInterceptor {

	private static final String TX_KEY_NAME = "txKey";
	private static final String KEY_PREFIX = "tx:dup:";
	private static final long PROCESSING_TTL = 130L;
	
	/**
	 * redis template bean
	 */
	private final StringRedisTemplate redisTemplate;
	
	@Override
	public void before(IntegrationContext context, Object request) {
		String tranCd = StringUtils.defaultIfEmpty(context.getAttribute("tranCd"), "");
		if (!StringUtils.hasText(tranCd)) {
			log.warn("# 거래코드가 존재하지  않음. interfaceId::{}", context.getInterfaceId());
			return;
		}
		
		//중복거래 체크 용 거래키 생성
		String txKey = this.generatedTxKey(tranCd);
		context.setAttribute(TX_KEY_NAME, txKey);
		
		log.debug("# 중복거래 체크용 거래키 생성: {}", txKey);
		
		Boolean locked = this.redisTemplate.opsForValue().setIfAbsent(txKey, "Y", Duration.ofSeconds(PROCESSING_TTL));
		if (!Boolean.TRUE.equals(locked)) {
			//TODO
			log.debug("# 중복거래 발생함 txKey: {}, tranCd: {}", txKey, tranCd);
		}
	}

	@Override
	public void after(IntegrationContext context, Object request, Object response) {
		this.release(context);
	}
	
	@Override
	public void onError(IntegrationContext context, Object request, Throwable ex) {
		this.release(context);
	}
	
	/**
	 * 중복거래키 삭제
	 * @param context {@link IntegrationContext}
	 */
	private void release(IntegrationContext context) {
		try {
			String txKey = context.getAttribute(TX_KEY_NAME);
			if (StringUtils.hasText(txKey)) {
				Boolean result = this.redisTemplate.delete(txKey);
				log.debug("# 중복거래 체크용 거래키 삭제: {}, result: {}", txKey, result);
			}
		} finally {
			context.removeAttribute(TX_KEY_NAME);
		}
	}
	
	/**
	 * 중복거래 체크용 거래 키 생성
	 * TX:DUP:{sessionId}:{tranCd}
	 * @param tranCd 거래코드
	 * @return 중복거래 체크용 거래키 문자열
	 */
	private String generatedTxKey(String tranCd) {
		String sessionId = this.getSessionId();
		return KEY_PREFIX + sessionId + ":" + tranCd;
	}
	
	/**
	 * 
	 * @return {@link HttpServletRequest}
	 */
	private HttpServletRequest getHttpRequest() {
		HttpServletRequest httpRequest = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return httpRequest;
	}
	
	/**
	 * 세션ID 문자열을 획득한다.
	 * @return 세션ID 문자열
	 */
	private String getSessionId() {
		HttpServletRequest httpRequest = this.getHttpRequest();
		if (httpRequest == null) {
			return StringUtils.EMPTY;
		}
		
		String sessionId = httpRequest.getSession().getId();
		return sessionId;
	}
}
