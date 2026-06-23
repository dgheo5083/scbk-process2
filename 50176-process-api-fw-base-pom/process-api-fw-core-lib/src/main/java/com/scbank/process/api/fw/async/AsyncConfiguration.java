package com.scbank.process.api.fw.async;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;

import com.scbank.process.api.fw.async.executor.AsyncExecutorRegistrar;
import com.scbank.process.api.fw.async.task.DefaultContextCopyingTaskDecorator;

/**
 * Async 처리 Java Configuration
 */
@Configuration
@ConditionalOnProperty(name = "csl.async.enabled", havingValue = "true")
@EnableAsync
public class AsyncConfiguration {

	@Bean
	@ConditionalOnMissingBean(TaskDecorator.class)
	TaskDecorator taskDecorator() {
		return new DefaultContextCopyingTaskDecorator();
	}
	
	@Bean
	AsyncExecutorRegistrar asyncExecutorRegistrar () {
		return new AsyncExecutorRegistrar();
	}
}
