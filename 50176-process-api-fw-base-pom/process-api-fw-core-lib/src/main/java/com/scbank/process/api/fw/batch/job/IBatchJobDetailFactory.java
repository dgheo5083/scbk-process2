package com.scbank.process.api.fw.batch.job;

import org.quartz.JobDetail;

import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;

/**
 * 
 */
@FunctionalInterface
public interface IBatchJobDetailFactory {

	/**
	 * 
	 * @return
	 */
	JobDetail createJobDetail(IBatchMetadata metadata);
}
