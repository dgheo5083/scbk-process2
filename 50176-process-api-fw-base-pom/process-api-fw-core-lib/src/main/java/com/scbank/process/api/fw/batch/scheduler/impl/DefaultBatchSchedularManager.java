package com.scbank.process.api.fw.batch.scheduler.impl;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.batch.job.IBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.job.IBatchTriggerFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.scheduler.IBatchSchedulerManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 배치 스케줄러 매니저 구현 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultBatchSchedularManager implements IBatchSchedulerManager {

    /**
     * 쿼츠 스케줄러
     */
    private final Scheduler scheduler;

    /**
     * 프레임워크 배치 메타데이터 레지스트리
     */
    private final IBatchMetadataRegistry batchMetadataRegistry;

    /**
     * 프레임워크 배치 JobDetail 팩토리 인터페이스
     */
    private final IBatchJobDetailFactory batchJobDetailFactory;

    /**
     * 프레임워크 배치 트리거 팩토리 인터페이스
     */
    private final IBatchTriggerFactory batchTriggerFactory;

    /**
     * 어플리케이션 구동 완료 후 배치 Job/Trigger 등록 및 스케줄러 시작
     */
    @EventListener(classes = { ApplicationReadyEvent.class })
    @Override
    public void init() {
        log.info("# 프레임워크 배치 스케줄러 매니저 초기화");

        try {
            if (scheduler.isStarted()) {
                log.info("# 프레임워크 배치 스케줄러가 이미 시작됨");
                return;
            }

            List<IBatchMetadata> metadatas = batchMetadataRegistry.getMetadatas();
            if (CollectionUtils.isEmpty(metadatas)) {
                log.info("# 등록된 배치 메타데이터가 없음");
                return;
            }

            for (IBatchMetadata metadata : metadatas) {
                try {
                    Trigger trigger = batchTriggerFactory.createTrigger(metadata);
                    JobDetail jobDetail = batchJobDetailFactory.createJobDetail(metadata);

                    if (scheduler.checkExists(jobDetail.getKey())) {
                        log.warn("Job 이미 등록됨: {}, 재등록 생략", jobDetail.getKey());
                        continue;
                    }

                    scheduler.scheduleJob(jobDetail, trigger);
                    log.info("# 배치 등록 완료: {}", jobDetail.getKey());

                } catch (Exception e) {
                    log.error("# 배치 등록 실패: {}, Cause: {}", metadata.getId(), e.getMessage(), e);
                }
            }

            scheduler.start();

            if (log.isInfoEnabled()) {
                log.info("# 프레임워크 배치 스케줄러 시작 완료");
            }

        } catch (SchedulerException e) {
            log.error("# 프레임워크 배치 스케줄러 초기화 중 예외 발생", e);
        } catch (Exception e) {
            log.error("# 프레임워크 배치 스케줄러 초기화 중 예외 발생", e);
        }
    }

    /**
     * 애플리케이션 종료 시 스케줄러 정리
     */
    @Override
    public void destroy() throws Exception {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown(false);
            log.info("# 프레임워크 배치 스케줄러 종료 완료");
        }
    }
}
