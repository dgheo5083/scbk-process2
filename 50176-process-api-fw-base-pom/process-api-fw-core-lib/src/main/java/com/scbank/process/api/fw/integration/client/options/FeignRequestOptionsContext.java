package com.scbank.process.api.fw.integration.client.options;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * OpenFeign RequestOptions 컨텍스트 클래스
 * <pre>
 * - 업무에서 설정한 OpenFeign RequestOptions을 스레드로컬에 저장 후, 
 *   Feign Client 실행 시 컨텍스트에 저장된 RequestOptions을 꺼내어 설정하도록 한다.
 * </pre>
 * 
 * @author sungdon.choi
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeignRequestOptionsContext {

	private static final ThreadLocal<feign.Request.Options> local = new ThreadLocal<>();
	
	/**
	 * Options 정보를 스레드로컬에 저장한다.
	 * @param options OpenFeign RequestOptions
	 */
	public static void set(feign.Request.Options options) {
		local.set(options);
	}
	
	/**
	 * 스레드로컬에 저장된 Options 정보를 가져온다.
	 * @return 스레드로컬에 저장된 Options 정보
	 */
	public static feign.Request.Options get() {
		return local.get();
	}
	
	/**
	 * 스레드로컬 clear
	 */
	public static void clear() {
		local.remove();
	}
}
