package com.scbank.process.api.fw.batch.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 프레임워크 쿼츠 작업 인터페이스
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public interface IBatchJob extends Job {

}
