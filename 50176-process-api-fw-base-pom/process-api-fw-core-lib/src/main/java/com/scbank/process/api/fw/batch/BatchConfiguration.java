package com.scbank.process.api.fw.batch;

import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.context.IBatchContextFactory;
import com.scbank.process.api.fw.batch.context.impl.DefaultBatchContext;
import com.scbank.process.api.fw.batch.job.IBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.job.IBatchTriggerFactory;
import com.scbank.process.api.fw.batch.job.impl.DefaultBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.job.impl.DefaultBatchJobExecutor;
import com.scbank.process.api.fw.batch.job.impl.DefaultBatchTriggerFactory;
import com.scbank.process.api.fw.batch.listener.BatchJobListener;
import com.scbank.process.api.fw.batch.listener.BatchSchedulerListener;
import com.scbank.process.api.fw.batch.listener.BatchTriggerListener;
import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.metadata.impl.DefaultBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.scheduler.IBatchSchedulerManager;
import com.scbank.process.api.fw.batch.scheduler.impl.DefaultBatchSchedularManager;

/**
 * 프레임워크 배치 Spring Configuration
 */
@Configuration
@EnableConfigurationProperties({ BatchProperties.class })
@ConditionalOnProperty(prefix = "batch", name = "enabled", havingValue = "true")
public class BatchConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnMissingBean(SchedulerFactoryBean.class)
	SchedulerFactoryBean schedulerFactoryBean(
			BatchProperties properties) {
		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setOverwriteExistingJobs(true);
		factoryBean.setAutoStartup(properties.isAutoStartup());
		factoryBean.setSchedulerListeners(new BatchSchedulerListener());
		factoryBean.setGlobalJobListeners(new BatchJobListener());
		factoryBean.setGlobalTriggerListeners(new BatchTriggerListener());
		return factoryBean;
	}

	/**
	 * 쿼츠 스케줄러 빈
	 * 
	 * @param schedulerFactoryBean
	 * @return 쿼츠 스케줄러 빈
	 */
	@Bean
	Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
		return schedulerFactoryBean.getScheduler();
	}

	/**
	 * 프레임워크 배치 메타데이터 레지스트리 빈
	 * 
	 * @param properties 프레임워크 배치 설정
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(IBatchMetadataRegistry.class)
	IBatchMetadataRegistry batchMetadataRegisty(BatchProperties properties) {
		return new DefaultBatchMetadataRegistry(properties);
	}

	/**
	 * 프레임워크 배치 JobDetail 생성 팩토리 빈
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(IBatchJobDetailFactory.class)
	IBatchJobDetailFactory batchJobDetailFactory() {
		return new DefaultBatchJobDetailFactory(DefaultBatchJobExecutor.class);
	}

	/**
	 * 프레임워크 배치 트리거 생성 픽토리 빈
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(IBatchTriggerFactory.class)
	IBatchTriggerFactory batchTriggerFactory() {
		return new DefaultBatchTriggerFactory();
	}

	/**
	 * 프레임워크 배치 스케줄러 매니저 빈
	 * 
	 * @param scheduler             쿼츠 스케줄러
	 * @param batchMetadataRegisty  프레임워크 배치 메타데이터 레지스트리
	 * @param batchJobDetailFactory 프레임워크 배치 JobDetail 팩토리
	 * @param batchTriggerFactory   프레임워크 배치 트리거 팩토리
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(IBatchSchedulerManager.class)
	IBatchSchedulerManager batchSchedulerManager(
			Scheduler scheduler,
			IBatchMetadataRegistry batchMetadataRegisty,
			IBatchJobDetailFactory batchJobDetailFactory,
			IBatchTriggerFactory batchTriggerFactory) {
		return new DefaultBatchSchedularManager(
				scheduler,
				batchMetadataRegisty,
				batchJobDetailFactory,
				batchTriggerFactory);
	}

	/**
	 * 프레임워크 배치 실행 컨텍스트 팩토리 빈
	 * 
	 * @return 배치 실행 컨텍스트 팩토리 빈
	 */
	@Bean
	@ConditionalOnMissingBean(IBatchContextFactory.class)
	IBatchContextFactory batchContextFactory() {
		return (context) -> {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();

			IBatchMetadata metadata = (IBatchMetadata) dataMap.get(BatchConstants.BATCH_METADATA_JOB_KEY);
			IBatchJobMetadata jobMetadata = metadata.getBatchJobMetadata();

			String jobName = context.getJobDetail().getKey().toString();
			String triggerName = context.getTrigger().getKey().toString();
			String instanceId = context.getFireInstanceId();

			DefaultBatchContext batchContext = new DefaultBatchContext(jobName, triggerName, instanceId,
					jobMetadata.getInitParameters());
			return batchContext;
		};
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
