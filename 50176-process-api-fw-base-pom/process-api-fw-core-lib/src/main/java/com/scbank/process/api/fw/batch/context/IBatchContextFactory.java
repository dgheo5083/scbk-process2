package com.scbank.process.api.fw.batch.context;

import org.quartz.JobExecutionContext;

/**
 * 프레임워크 배치 컨텍스트 생성 팩토리 인터페이스
 */
@FunctionalInterface
public interface IBatchContextFactory {

	/**
	 * 프레임워크 배치 컨텍스트를 생성한다.
	 * 
	 * @param executionContext 쿼츠 작업 실행 컨텍스트 {@link JobExecutionContext}
	 * @return 프레임워크 배치 컨텍스트 구현 클래스
	 */
	IBatchContext create(JobExecutionContext executionContext);
}
