package com.scbank.process.api.fw.batch.scheduler;

import org.springframework.beans.factory.DisposableBean;

/**
 * 프레임워크 배치 스케줄러 인터페이스
 */
public interface IBatchSchedulerManager extends DisposableBean {

	// @Override
	// default void afterPropertiesSet() throws Exception {
	// this.init();
	// }

	/**
	 * 배치 스케줄러 초기화를 수행
	 */
	void init();
}