package com.scbank.process.api.fw.batch.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 쿼츠 잡 리스너 구현 클래스
 */
public class BatchJobListener extends JobListenerSupport {

    private final Logger log = LoggerFactory.getLogger("batch");

    @Override
    public String getName() {
        return BatchJobListener.class.getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().toString();
        log.info("배치 시작: {}", jobName);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().toString();
        log.warn("배치 실행 취소됨: {}", jobName);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String jobName = context.getJobDetail().getKey().toString();
        if (jobException != null) {
            log.error("배치 실패: {}", jobName, jobException);
        } else {
            log.info("배치 완료: {}", jobName);
        }
    }
}
