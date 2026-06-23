package com.scbank.process.api.fw.batch.scheduler.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.scbank.process.api.fw.batch.exception.BatchJobException;
import com.scbank.process.api.fw.batch.job.IBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.job.IBatchTriggerFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.scheduler.IBatchSchedulerManager;

/**
 * DefaultBatchSchedularManager Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultBatchSchedularManagerTest {

    private DefaultBatchSchedularManager manager;

    @Mock
    private Scheduler scheduler;

    @Mock
    private IBatchMetadataRegistry batchMetadataRegistry;

    @Mock
    private IBatchJobDetailFactory batchJobDetailFactory;

    @Mock
    private IBatchTriggerFactory batchTriggerFactory;

    @Mock
    private IBatchMetadata metadata;

    @Mock
    private JobDetail jobDetail;

    @Mock
    private Trigger trigger;

    @Mock
    private JobKey jobKey;

    @BeforeEach
    void setUp() {
        manager = new DefaultBatchSchedularManager(
                scheduler,
                batchMetadataRegistry,
                batchJobDetailFactory,
                batchTriggerFactory);
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create manager with all dependencies")
        void shouldCreateManagerWithAllDependencies() {
            assertNotNull(manager);
        }
    }

    @Nested
    @DisplayName("init tests")
    class InitTests {

        @Test
        @DisplayName("Should not initialize when scheduler already started")
        void shouldNotInitializeWhenSchedulerAlreadyStarted() throws SchedulerException {
            when(scheduler.isStarted()).thenReturn(true);

            manager.init();

            verify(scheduler, never()).scheduleJob(any(JobDetail.class), any(Trigger.class));
            verify(scheduler, never()).start();
        }

        @Test
        @DisplayName("Should not initialize when no metadatas available")
        void shouldNotInitializeWhenNoMetadatasAvailable() throws SchedulerException {
            when(scheduler.isStarted()).thenReturn(false);
            when(batchMetadataRegistry.getMetadatas()).thenReturn(new ArrayList<>());

            manager.init();

            verify(scheduler, never()).scheduleJob(any(JobDetail.class), any(Trigger.class));
            verify(scheduler, never()).start();
        }

        @Test
        @DisplayName("Should not initialize when metadatas is null")
        void shouldNotInitializeWhenMetadatasIsNull() throws SchedulerException {
            when(scheduler.isStarted()).thenReturn(false);
            when(batchMetadataRegistry.getMetadatas()).thenReturn(null);

            manager.init();

            verify(scheduler, never()).scheduleJob(any(JobDetail.class), any(Trigger.class));
        }

        @Test
        @DisplayName("Should schedule jobs and start scheduler")
        void shouldScheduleJobsAndStartScheduler() throws SchedulerException, BatchJobException {
            List<IBatchMetadata> metadatas = Arrays.asList(metadata);

            when(scheduler.isStarted()).thenReturn(false);
            when(batchMetadataRegistry.getMetadatas()).thenReturn(metadatas);
            when(batchTriggerFactory.createTrigger(metadata)).thenReturn(trigger);
            when(batchJobDetailFactory.createJobDetail(metadata)).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(scheduler.checkExists(jobKey)).thenReturn(false);

            manager.init();

            verify(scheduler).scheduleJob(jobDetail, trigger);
            verify(scheduler).start();
        }

        @Test
        @DisplayName("Should skip already registered job")
        void shouldSkipAlreadyRegisteredJob() throws SchedulerException, BatchJobException {
            List<IBatchMetadata> metadatas = Arrays.asList(metadata);

            when(scheduler.isStarted()).thenReturn(false);
            when(batchMetadataRegistry.getMetadatas()).thenReturn(metadatas);
            when(batchTriggerFactory.createTrigger(metadata)).thenReturn(trigger);
            when(batchJobDetailFactory.createJobDetail(metadata)).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(scheduler.checkExists(jobKey)).thenReturn(true);

            manager.init();

            verify(scheduler, never()).scheduleJob(any(JobDetail.class), any(Trigger.class));
            verify(scheduler).start();
        }

        @Test
        @DisplayName("Should handle exception during job registration")
        void shouldHandleExceptionDuringJobRegistration() throws SchedulerException, BatchJobException {
            List<IBatchMetadata> metadatas = Arrays.asList(metadata);

            when(scheduler.isStarted()).thenReturn(false);
            when(batchMetadataRegistry.getMetadatas()).thenReturn(metadatas);
            when(batchTriggerFactory.createTrigger(metadata)).thenThrow(new BatchJobException("ERR", "Error"));
            when(metadata.getId()).thenReturn("failed-batch");

            // Should not throw, handled internally
            assertDoesNotThrow(() -> manager.init());

            verify(scheduler).start();
        }

        @Test
        @DisplayName("Should handle scheduler exception")
        void shouldHandleSchedulerException() throws SchedulerException {
            when(scheduler.isStarted()).thenThrow(new SchedulerException("Scheduler error"));

            // Should not throw, handled internally
            assertDoesNotThrow(() -> manager.init());
        }
    }

    @Nested
    @DisplayName("destroy tests")
    class DestroyTests {

        @Test
        @DisplayName("Should shutdown scheduler when not already shutdown")
        void shouldShutdownSchedulerWhenNotAlreadyShutdown() throws Exception {
            when(scheduler.isShutdown()).thenReturn(false);

            manager.destroy();

            verify(scheduler).shutdown(false);
        }

        @Test
        @DisplayName("Should not shutdown when already shutdown")
        void shouldNotShutdownWhenAlreadyShutdown() throws Exception {
            when(scheduler.isShutdown()).thenReturn(true);

            manager.destroy();

            verify(scheduler, never()).shutdown(anyBoolean());
        }

        @Test
        @DisplayName("Should handle null scheduler")
        void shouldHandleNullScheduler() throws Exception {
            DefaultBatchSchedularManager nullManager = new DefaultBatchSchedularManager(
                    null,
                    batchMetadataRegistry,
                    batchJobDetailFactory,
                    batchTriggerFactory);

            // Should not throw
            assertDoesNotThrow(() -> nullManager.destroy());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchSchedulerManager")
        void shouldImplementIBatchSchedulerManager() {
            assertTrue(manager instanceof IBatchSchedulerManager);
        }
    }

    @Nested
    @DisplayName("Multiple metadata tests")
    class MultipleMetadataTests {

        @Mock
        private IBatchMetadata metadata2;

        @Mock
        private JobDetail jobDetail2;

        @Mock
        private Trigger trigger2;

        @Mock
        private JobKey jobKey2;

        @Test
        @DisplayName("Should register multiple jobs")
        void shouldRegisterMultipleJobs() throws SchedulerException, BatchJobException {
            List<IBatchMetadata> metadatas = Arrays.asList(metadata, metadata2);

            when(scheduler.isStarted()).thenReturn(false);
            when(batchMetadataRegistry.getMetadatas()).thenReturn(metadatas);

            when(batchTriggerFactory.createTrigger(metadata)).thenReturn(trigger);
            when(batchJobDetailFactory.createJobDetail(metadata)).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(scheduler.checkExists(jobKey)).thenReturn(false);

            when(batchTriggerFactory.createTrigger(metadata2)).thenReturn(trigger2);
            when(batchJobDetailFactory.createJobDetail(metadata2)).thenReturn(jobDetail2);
            when(jobDetail2.getKey()).thenReturn(jobKey2);
            when(scheduler.checkExists(jobKey2)).thenReturn(false);

            manager.init();

            verify(scheduler).scheduleJob(jobDetail, trigger);
            verify(scheduler).scheduleJob(jobDetail2, trigger2);
            verify(scheduler).start();
        }
    }
}
