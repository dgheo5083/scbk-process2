package com.scbank.process.api.fw.batch.job.impl;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scbank.process.api.fw.batch.component.IBatchComponent;
import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.context.IBatchContext;
import com.scbank.process.api.fw.batch.context.IBatchContextFactory;
import com.scbank.process.api.fw.batch.job.IBatchJob;
import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceLogger;
import com.scbank.process.api.fw.core.log.trace.TraceSection;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

/**
 * 프레임워크 배치 실행 작업 구현 클래스
 */
public class DefaultBatchJobExecutor implements IBatchJob {

	private Logger log = LoggerFactory.getLogger("batch");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 배치 컨텍스트 생성
		IBatchContextFactory batchContextFactory = RuntimeContext.getBean(IBatchContextFactory.class);
		IBatchContext batchContext = batchContextFactory.create(context);

		try {
			String componentId = this.getBatchComponentId(context);
			// TraceContext 초기화
			TraceContext traceContext = new TraceContext(componentId, TraceSection.JOB);
			TraceContextHolder.set(traceContext);

			// 배치 컴포넌트 획득
			IBatchComponent batchComponent = this.getBatchComponent(componentId);
			if (batchComponent != null) {
				batchComponent.execute(batchContext);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			batchContext.setError(e);
		} finally {
			// Trace 종료 및 로깅
			TraceContextHolder.get().ifPresent((traceContext) -> {
				traceContext.end();
				TraceLogger.logTraceTree(traceContext.getRoot());
			});

			// ThreadLocal, MDC 정리
			TraceContextHolder.clear();
		}
	}

	private String getBatchComponentId(JobExecutionContext executionContext) {
		JobDataMap dataMap = executionContext.getMergedJobDataMap();
		String componentId = dataMap.getString(BatchConstants.BATCH_COMPONENT_ID);
		return componentId;
	}

	/**
	 * 배치 컴포넌트 빈을 획득한다.
	 * 
	 * @param executionContext 쿼츠 잡 실행 컨텍스트
	 * @return
	 */
	private IBatchComponent getBatchComponent(String componentId) {
		return RuntimeContext.getBean(componentId, IBatchComponent.class);
	}
}
