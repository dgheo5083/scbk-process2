package com.scbank.process.api.fw.async.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.scbank.process.api.fw.async.AsyncProperties;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.Pool;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.RejectionPolicy;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties.Shutdown;

/**
 * 프레임워크 ThreadPoolTaskExecutor 팩토리 클래스 
 */
public final class AsyncExecutorFactory {

	/**
	 * ThreadPoolTaskExecutor 생성
	 * @param properties {@link ThreadPoolProperties}
	 * @return
	 */
	public static ThreadPoolTaskExecutor create(AsyncProperties.ThreadPoolProperties properties, TaskDecorator decorator) {
		Pool p = properties.getPool();
		
		ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
		ex.setCorePoolSize(p.getCoreSize());
		ex.setMaxPoolSize(p.getMaxSize());
		ex.setQueueCapacity(p.getQueueCapacity());
		ex.setKeepAliveSeconds(p.getKeepAliveSeconds());
		ex.setAllowCoreThreadTimeOut(p.isAllowCoreThreadTimeOut());
		ex.setThreadNamePrefix(p.getThreadNamePrefix());
		
		if (decorator != null) {
			ex.setTaskDecorator(decorator);
		}
		
		ex.setRejectedExecutionHandler(toHandler(properties));
		
		Shutdown shutdown = properties.getShutdown();
		ex.setWaitForTasksToCompleteOnShutdown(shutdown.isWaitForTasksCompleteOnShutdown());
		ex.setAwaitTerminationSeconds(shutdown.getAwaitTerminationSeconds());
		
		ex.initialize();
		return ex;
	}
	
	private static RejectedExecutionHandler toHandler(AsyncProperties.ThreadPoolProperties properties) {
		RejectionPolicy policy = properties.getRejectionPolicy();
		switch (policy) {
		case DISCARD: return new ThreadPoolExecutor.DiscardPolicy();
		case DISCARD_OLDEST: return new ThreadPoolExecutor.DiscardOldestPolicy();
		case CALLER_RUNS: return new ThreadPoolExecutor.CallerRunsPolicy();
		case ABORT: return new ThreadPoolExecutor.AbortPolicy();
		default: return new ThreadPoolExecutor.DiscardPolicy();
		}
	}
}
