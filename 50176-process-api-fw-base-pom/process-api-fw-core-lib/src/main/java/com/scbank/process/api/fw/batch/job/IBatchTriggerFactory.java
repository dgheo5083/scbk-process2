package com.scbank.process.api.fw.batch.job;

import org.quartz.Trigger;

import com.scbank.process.api.fw.batch.exception.BatchJobException;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;

/**
 * 
 */
@FunctionalInterface
public interface IBatchTriggerFactory {

	/**
	 * 
	 * @param metadata
	 * @return
	 * @throws BatchJobException
	 */
	Trigger createTrigger(IBatchMetadata metadata) throws BatchJobException;
}
