package com.scbank.process.api.fw.batch.job.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;

import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.job.IBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;

/**
 * DefaultBatchJobDetailFactory Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultBatchJobDetailFactoryTest {

    private DefaultBatchJobDetailFactory factory;

    @Mock
    private IBatchMetadata batchMetadata;

    @Mock
    private IBatchJobMetadata batchJobMetadata;

    @BeforeEach
    void setUp() {
        factory = new DefaultBatchJobDetailFactory(TestJob.class);
    }

    // Test Job class
    public static class TestJob implements Job {
        @Override
        public void execute(org.quartz.JobExecutionContext context) {
            // Test implementation
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create factory with job class")
        void shouldCreateFactoryWithJobClass() {
            assertNotNull(factory);
        }

        @Test
        @DisplayName("Should accept any Job implementation class")
        void shouldAcceptAnyJobImplementationClass() {
            DefaultBatchJobDetailFactory customFactory = new DefaultBatchJobDetailFactory(DefaultBatchJobExecutor.class);
            assertNotNull(customFactory);
        }
    }

    @Nested
    @DisplayName("createJobDetail tests")
    class CreateJobDetailTests {

        @Test
        @DisplayName("Should create JobDetail with correct properties")
        void shouldCreateJobDetailWithCorrectProperties() {
            String batchId = "test-batch";
            String description = "Test batch description";
            String componentId = "test-component";

            when(batchMetadata.getId()).thenReturn(batchId);
            when(batchMetadata.getDescription()).thenReturn(description);
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn(componentId);

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);

            assertNotNull(jobDetail);
            assertEquals(batchId + "-job", jobDetail.getKey().getName());
            assertEquals(Scheduler.DEFAULT_GROUP, jobDetail.getKey().getGroup());
            assertEquals(description, jobDetail.getDescription());
        }

        @Test
        @DisplayName("Should include component ID in job data map")
        void shouldIncludeComponentIdInJobDataMap() {
            String componentId = "batch-component-001";

            when(batchMetadata.getId()).thenReturn("batch-id");
            when(batchMetadata.getDescription()).thenReturn("desc");
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn(componentId);

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);
            JobDataMap dataMap = jobDetail.getJobDataMap();

            assertEquals(componentId, dataMap.getString(BatchConstants.BATCH_COMPONENT_ID));
        }

        @Test
        @DisplayName("Should include batch metadata in job data map")
        void shouldIncludeBatchMetadataInJobDataMap() {
            when(batchMetadata.getId()).thenReturn("batch-id");
            when(batchMetadata.getDescription()).thenReturn("desc");
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn("comp-id");

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);
            JobDataMap dataMap = jobDetail.getJobDataMap();

            assertNotNull(dataMap.get(BatchConstants.BATCH_METADATA_JOB_KEY));
            assertEquals(batchMetadata, dataMap.get(BatchConstants.BATCH_METADATA_JOB_KEY));
        }

        @Test
        @DisplayName("Should set correct job class")
        void shouldSetCorrectJobClass() {
            when(batchMetadata.getId()).thenReturn("batch-id");
            when(batchMetadata.getDescription()).thenReturn("desc");
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn("comp-id");

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);

            assertEquals(TestJob.class, jobDetail.getJobClass());
        }

        @Test
        @DisplayName("Should handle empty description")
        void shouldHandleEmptyDescription() {
            when(batchMetadata.getId()).thenReturn("batch-id");
            when(batchMetadata.getDescription()).thenReturn("");
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn("comp-id");

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);

            assertEquals("", jobDetail.getDescription());
        }

        @Test
        @DisplayName("Should handle null description")
        void shouldHandleNullDescription() {
            when(batchMetadata.getId()).thenReturn("batch-id");
            when(batchMetadata.getDescription()).thenReturn(null);
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn("comp-id");

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);

            assertNull(jobDetail.getDescription());
        }
    }

    @Nested
    @DisplayName("getJobName tests")
    class GetJobNameTests {

        @Test
        @DisplayName("Should append -job suffix to name")
        void shouldAppendJobSuffixToName() {
            when(batchMetadata.getId()).thenReturn("my-batch");
            when(batchMetadata.getDescription()).thenReturn("desc");
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn("comp-id");

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);

            assertEquals("my-batch-job", jobDetail.getKey().getName());
        }

        @Test
        @DisplayName("Should handle special characters in name")
        void shouldHandleSpecialCharactersInName() {
            when(batchMetadata.getId()).thenReturn("batch_with_underscore");
            when(batchMetadata.getDescription()).thenReturn("desc");
            when(batchMetadata.getBatchJobMetadata()).thenReturn(batchJobMetadata);
            when(batchJobMetadata.getComponentId()).thenReturn("comp-id");

            JobDetail jobDetail = factory.createJobDetail(batchMetadata);

            assertEquals("batch_with_underscore-job", jobDetail.getKey().getName());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchJobDetailFactory")
        void shouldImplementIBatchJobDetailFactory() {
            assertTrue(factory instanceof IBatchJobDetailFactory);
        }
    }
}
