package com.scbank.process.api.fw.batch.job.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.scbank.process.api.fw.batch.component.IBatchComponent;
import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.context.IBatchContext;
import com.scbank.process.api.fw.batch.context.IBatchContextFactory;
import com.scbank.process.api.fw.batch.job.IBatchJob;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

/**
 * DefaultBatchJobExecutor Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultBatchJobExecutorTest {

    private DefaultBatchJobExecutor executor;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private JobDetail jobDetail;

    @Mock
    private JobDataMap jobDataMap;

    @Mock
    private IBatchContextFactory batchContextFactory;

    @Mock
    private IBatchContext batchContext;

    @Mock
    private IBatchComponent batchComponent;

    private MockedStatic<RuntimeContext> runtimeContextMock;
    private MockedStatic<TraceContextHolder> traceContextHolderMock;

    @BeforeEach
    void setUp() {
        executor = new DefaultBatchJobExecutor();
        runtimeContextMock = mockStatic(RuntimeContext.class);
        traceContextHolderMock = mockStatic(TraceContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        if (runtimeContextMock != null) {
            runtimeContextMock.close();
        }
        if (traceContextHolderMock != null) {
            traceContextHolderMock.close();
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create executor")
        void shouldCreateExecutor() {
            assertNotNull(executor);
        }
    }

    @Nested
    @DisplayName("execute tests")
    class ExecuteTests {

        @Test
        @DisplayName("Should execute batch component successfully")
        void shouldExecuteBatchComponentSuccessfully() throws JobExecutionException {
            String componentId = "test-component";

            runtimeContextMock.when(() -> RuntimeContext.getBean(IBatchContextFactory.class))
                    .thenReturn(batchContextFactory);
            runtimeContextMock.when(() -> RuntimeContext.getBean(componentId, IBatchComponent.class))
                    .thenReturn(batchComponent);

            when(batchContextFactory.create(jobExecutionContext)).thenReturn(batchContext);
            when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);
            when(jobDataMap.getString(BatchConstants.BATCH_COMPONENT_ID)).thenReturn(componentId);

            traceContextHolderMock.when(TraceContextHolder::get).thenReturn(Optional.empty());
            traceContextHolderMock.when(TraceContextHolder::clear).then(invocation -> null);

            // Should not throw
            assertDoesNotThrow(() -> executor.execute(jobExecutionContext));

            verify(batchComponent).execute(batchContext);
        }

        @Test
        @DisplayName("Should handle null batch component")
        void shouldHandleNullBatchComponent() throws JobExecutionException {
            String componentId = "non-existent-component";

            runtimeContextMock.when(() -> RuntimeContext.getBean(IBatchContextFactory.class))
                    .thenReturn(batchContextFactory);
            runtimeContextMock.when(() -> RuntimeContext.getBean(componentId, IBatchComponent.class))
                    .thenReturn(null);

            when(batchContextFactory.create(jobExecutionContext)).thenReturn(batchContext);
            when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);
            when(jobDataMap.getString(BatchConstants.BATCH_COMPONENT_ID)).thenReturn(componentId);

            traceContextHolderMock.when(TraceContextHolder::get).thenReturn(Optional.empty());
            traceContextHolderMock.when(TraceContextHolder::clear).then(invocation -> null);

            // Should not throw even with null component
            assertDoesNotThrow(() -> executor.execute(jobExecutionContext));
        }

        @Test
        @DisplayName("Should set error on context when exception occurs")
        void shouldSetErrorOnContextWhenExceptionOccurs() throws JobExecutionException {
            String componentId = "error-component";
            RuntimeException testException = new RuntimeException("Test error");

            runtimeContextMock.when(() -> RuntimeContext.getBean(IBatchContextFactory.class))
                    .thenReturn(batchContextFactory);
            runtimeContextMock.when(() -> RuntimeContext.getBean(componentId, IBatchComponent.class))
                    .thenReturn(batchComponent);

            when(batchContextFactory.create(jobExecutionContext)).thenReturn(batchContext);
            when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);
            when(jobDataMap.getString(BatchConstants.BATCH_COMPONENT_ID)).thenReturn(componentId);
            doThrow(testException).when(batchComponent).execute(batchContext);

            traceContextHolderMock.when(TraceContextHolder::get).thenReturn(Optional.empty());
            traceContextHolderMock.when(TraceContextHolder::clear).then(invocation -> null);

            // Should not throw, but set error on context
            assertDoesNotThrow(() -> executor.execute(jobExecutionContext));

            verify(batchContext).setError(testException);
        }

        @Test
        @DisplayName("Should clear trace context in finally block")
        void shouldClearTraceContextInFinallyBlock() throws JobExecutionException {
            String componentId = "test-component";

            runtimeContextMock.when(() -> RuntimeContext.getBean(IBatchContextFactory.class))
                    .thenReturn(batchContextFactory);
            runtimeContextMock.when(() -> RuntimeContext.getBean(componentId, IBatchComponent.class))
                    .thenReturn(batchComponent);

            when(batchContextFactory.create(jobExecutionContext)).thenReturn(batchContext);
            when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);
            when(jobDataMap.getString(BatchConstants.BATCH_COMPONENT_ID)).thenReturn(componentId);

            traceContextHolderMock.when(TraceContextHolder::get).thenReturn(Optional.empty());
            traceContextHolderMock.when(TraceContextHolder::clear).then(invocation -> null);

            executor.execute(jobExecutionContext);

            traceContextHolderMock.verify(TraceContextHolder::clear);
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchJob")
        void shouldImplementIBatchJob() {
            assertTrue(executor instanceof IBatchJob);
        }
    }
}
