package com.scbank.process.api.fw.base.gateway.edmi.base.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.scbank.process.api.fw.integration.client.options.FeignRequestOptionsContext;

import feign.Client;
import feign.Request;
import feign.Request.Options;
import feign.Response;
import lombok.RequiredArgsConstructor;

/**
 * EDMI 전문 송/수신 Feign Client 클래스
 */
@RequiredArgsConstructor
public class EDMIFeignClient implements Client {
	
	/**
	 * feign.Client
	 */
	private final Client delegate;

	@Override
	public Response execute(Request request, Options options) throws IOException {
		Request.Options merged = this.merge(options);
		return this.delegate.execute(request, merged);
	}
	
	/**
	 * 업무에서 설정한 타임아웃(연결/수신) 정보와 기본 타임아웃 설정정보를 merge한다.
	 * @param original 기본 설정정보
	 * @return merge 된 설정정보
	 */
	private Request.Options merge (Request.Options original) {
		Request.Options overrided = FeignRequestOptionsContext.get();
		if (overrided == null) {
			return original;
		}
		
		long connectTimeoutMillis = overrided.connectTimeoutMillis() > 0 ? overrided.connectTimeout() : original.connectTimeoutMillis();
		long readTimeoutMillis = overrided.readTimeoutMillis() > 0 ? overrided.readTimeoutMillis() : original.readTimeoutMillis();
		Request.Options merged = new Request.Options(
				connectTimeoutMillis, 
				TimeUnit.MILLISECONDS, 
				readTimeoutMillis, 
				TimeUnit.MILLISECONDS, 
				original.isFollowRedirects());
		return merged;
	}
}
