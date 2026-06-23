package com.scbank.process.api.svc.shared.components.mydata;

/**
 * 마이데이터 클라이언트 인터셉터 인터페이스
 */
public interface IMyDataClientInterceptor {

	/**
	 * 
	 * @param logValues
	 */
	void before(Object... logValues);

	/**
	 * 
	 * @param logValues
	 */
	void after(Object... logValues);
}
