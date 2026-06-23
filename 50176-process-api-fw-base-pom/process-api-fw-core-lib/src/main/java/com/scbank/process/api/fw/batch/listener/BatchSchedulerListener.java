package com.scbank.process.api.fw.batch.listener;

import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 쿼츠 스케줄러 리스너 구현 클래스
 */
public class BatchSchedulerListener extends SchedulerListenerSupport {

    private static final Logger log = LoggerFactory.getLogger("batch");

    @Override
    public void schedulerStarted() {
        log.info("Quartz 스케줄러가 시작되었습니다.");
    }

    @Override
    public void schedulerShutdown() {
        log.info("Quartz 스케줄러가 종료되었습니다.");
    }

    @Override
    public void jobScheduled(Trigger trigger) {
        log.info("Quartz 잡 등록됨: {}", trigger.getKey());
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        log.info("Quartz 잡 해제됨: {}", triggerKey);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        log.error("Quartz 스케줄러 오류 발생: {}, 원인: {}", msg, cause.getMessage(), cause);
    }
}
