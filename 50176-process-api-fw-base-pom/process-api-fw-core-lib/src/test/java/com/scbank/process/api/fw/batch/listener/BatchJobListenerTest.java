package com.scbank.process.api.fw.batch.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.listeners.JobListenerSupport;

/**
 * BatchJobListener Test Class
 */
@ExtendWith(MockitoExtension.class)
class BatchJobListenerTest {

    private BatchJobListener listener;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private JobDetail jobDetail;

    @Mock
    private JobKey jobKey;

    @BeforeEach
    void setUp() {
        listener = new BatchJobListener();
    }

    @Nested
    @DisplayName("getName tests")
    class GetNameTests {

        @Test
        @DisplayName("Should return class simple name")
        void shouldReturnClassSimpleName() {
            assertEquals("BatchJobListener", listener.getName());
        }
    }

    @Nested
    @DisplayName("jobToBeExecuted tests")
    class JobToBeExecutedTests {

        @Test
        @DisplayName("Should log job start info")
        void shouldLogJobStartInfo() {
            when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(jobKey.toString()).thenReturn("DEFAULT.testJob");

            // Should not throw exception
            assertDoesNotThrow(() -> listener.jobToBeExecuted(jobExecutionContext));

            verify(jobExecutionContext).getJobDetail();
            verify(jobDetail).getKey();
        }
    }

    @Nested
    @DisplayName("jobExecutionVetoed tests")
    class JobExecutionVetoedTests {

        @Test
        @DisplayName("Should log job vetoed warning")
        void shouldLogJobVetoedWarning() {
            when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(jobKey.toString()).thenReturn("DEFAULT.testJob");

            // Should not throw exception
            assertDoesNotThrow(() -> listener.jobExecutionVetoed(jobExecutionContext));

            verify(jobExecutionContext).getJobDetail();
            verify(jobDetail).getKey();
        }
    }

    @Nested
    @DisplayName("jobWasExecuted tests")
    class JobWasExecutedTests {

        @Test
        @DisplayName("Should log job completion when no exception")
        void shouldLogJobCompletionWhenNoException() {
            when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(jobKey.toString()).thenReturn("DEFAULT.testJob");

            // Should not throw exception
            assertDoesNotThrow(() -> listener.jobWasExecuted(jobExecutionContext, null));

            verify(jobExecutionContext).getJobDetail();
            verify(jobDetail).getKey();
        }

        @Test
        @DisplayName("Should log job failure when exception occurred")
        void shouldLogJobFailureWhenExceptionOccurred() {
            when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
            when(jobDetail.getKey()).thenReturn(jobKey);
            when(jobKey.toString()).thenReturn("DEFAULT.testJob");

            JobExecutionException jobException = new JobExecutionException("Job failed");

            // Should not throw exception
            assertDoesNotThrow(() -> listener.jobWasExecuted(jobExecutionContext, jobException));

            verify(jobExecutionContext).getJobDetail();
            verify(jobDetail).getKey();
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend JobListenerSupport")
        void shouldExtendJobListenerSupport() {
            assertTrue(listener instanceof JobListenerSupport);
        }
    }
}
