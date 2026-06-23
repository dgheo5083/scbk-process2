package com.scbank.process.api.fw.batch.job.impl;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;

import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.job.IBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultBatchJobDetailFactory implements IBatchJobDetailFactory {

	private final Class<? extends Job> jobClass;

	@Override
	public JobDetail createJobDetail(IBatchMetadata batchMetadata) {
		String jobName = this.getJobName(batchMetadata.getId());
		String description = batchMetadata.getDescription();

		IBatchJobMetadata batchJobMetadata = batchMetadata.getBatchJobMetadata();
		String batchComponentId = batchJobMetadata.getComponentId();

		JobDataMap dataMap = new JobDataMap();
		dataMap.put(BatchConstants.BATCH_COMPONENT_ID, batchComponentId);
		dataMap.put(BatchConstants.BATCH_METADATA_JOB_KEY, batchMetadata);

		JobDetail jobDetail = JobBuilder.newJob(jobClass)
				.withIdentity(jobName, Scheduler.DEFAULT_GROUP)
				.withDescription(description)
				.usingJobData(dataMap)
				.build();
		return jobDetail;
	}

	protected String getJobName(String name) {
		return name + "-job";
	}
}
