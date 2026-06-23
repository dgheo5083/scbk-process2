package com.scbank.process.api.fw.batch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.scbank.process.api.fw.batch.context.IBatchContextFactory;
import com.scbank.process.api.fw.batch.job.IBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.job.IBatchTriggerFactory;
import com.scbank.process.api.fw.batch.job.impl.DefaultBatchJobDetailFactory;
import com.scbank.process.api.fw.batch.job.impl.DefaultBatchTriggerFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.metadata.impl.DefaultBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.scheduler.IBatchSchedulerManager;
import com.scbank.process.api.fw.batch.scheduler.impl.DefaultBatchSchedularManager;

/**
 * BatchConfiguration Test Class
 */
@ExtendWith(MockitoExtension.class)
class BatchConfigurationTest {

    private BatchConfiguration configuration;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private BatchProperties batchProperties;

    @Mock
    private SchedulerFactoryBean schedulerFactoryBean;

    @Mock
    private Scheduler scheduler;

    @Mock
    private IBatchMetadataRegistry batchMetadataRegistry;

    @Mock
    private IBatchJobDetailFactory batchJobDetailFactory;

    @Mock
    private IBatchTriggerFactory batchTriggerFactory;

    @BeforeEach
    void setUp() {
        configuration = new BatchConfiguration();
        configuration.setApplicationContext(applicationContext);
    }

    @Nested
    @DisplayName("setApplicationContext tests")
    class SetApplicationContextTests {

        @Test
        @DisplayName("Should set application context")
        void shouldSetApplicationContext() {
            BatchConfiguration config = new BatchConfiguration();
            assertDoesNotThrow(() -> config.setApplicationContext(applicationContext));
        }
    }

    @Nested
    @DisplayName("schedulerFactoryBean tests")
    class SchedulerFactoryBeanTests {

        @Test
        @DisplayName("Should create SchedulerFactoryBean with properties")
        void shouldCreateSchedulerFactoryBeanWithProperties() {
            when(batchProperties.isAutoStartup()).thenReturn(false);

            SchedulerFactoryBean factoryBean = configuration.schedulerFactoryBean(batchProperties);

            assertNotNull(factoryBean);
            verify(batchProperties).isAutoStartup();
        }

        @Test
        @DisplayName("Should create SchedulerFactoryBean with autoStartup true")
        void shouldCreateSchedulerFactoryBeanWithAutoStartupTrue() {
            when(batchProperties.isAutoStartup()).thenReturn(true);

            SchedulerFactoryBean factoryBean = configuration.schedulerFactoryBean(batchProperties);

            assertNotNull(factoryBean);
        }
    }

    @Nested
    @DisplayName("scheduler tests")
    class SchedulerTests {

        @Test
        @DisplayName("Should get scheduler from factory bean")
        void shouldGetSchedulerFromFactoryBean() {
            when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);

            Scheduler result = configuration.scheduler(schedulerFactoryBean);

            assertEquals(scheduler, result);
            verify(schedulerFactoryBean).getScheduler();
        }
    }

    @Nested
    @DisplayName("batchMetadataRegistry tests")
    class BatchMetadataRegistryTests {

        @Test
        @DisplayName("Should create DefaultBatchMetadataRegistry")
        void shouldCreateDefaultBatchMetadataRegistry() {
            IBatchMetadataRegistry registry = configuration.batchMetadataRegisty(batchProperties);

            assertNotNull(registry);
            assertTrue(registry instanceof DefaultBatchMetadataRegistry);
        }
    }

    @Nested
    @DisplayName("batchJobDetailFactory tests")
    class BatchJobDetailFactoryTests {

        @Test
        @DisplayName("Should create DefaultBatchJobDetailFactory")
        void shouldCreateDefaultBatchJobDetailFactory() {
            IBatchJobDetailFactory factory = configuration.batchJobDetailFactory();

            assertNotNull(factory);
            assertTrue(factory instanceof DefaultBatchJobDetailFactory);
        }
    }

    @Nested
    @DisplayName("batchTriggerFactory tests")
    class BatchTriggerFactoryTests {

        @Test
        @DisplayName("Should create DefaultBatchTriggerFactory")
        void shouldCreateDefaultBatchTriggerFactory() {
            IBatchTriggerFactory factory = configuration.batchTriggerFactory();

            assertNotNull(factory);
            assertTrue(factory instanceof DefaultBatchTriggerFactory);
        }
    }

    @Nested
    @DisplayName("batchSchedulerManager tests")
    class BatchSchedulerManagerTests {

        @Test
        @DisplayName("Should create DefaultBatchSchedularManager")
        void shouldCreateDefaultBatchSchedularManager() {
            IBatchSchedulerManager manager = configuration.batchSchedulerManager(
                    scheduler,
                    batchMetadataRegistry,
                    batchJobDetailFactory,
                    batchTriggerFactory);

            assertNotNull(manager);
            assertTrue(manager instanceof DefaultBatchSchedularManager);
        }
    }

    @Nested
    @DisplayName("batchContextFactory tests")
    class BatchContextFactoryTests {

        @Test
        @DisplayName("Should create IBatchContextFactory")
        void shouldCreateIBatchContextFactory() {
            IBatchContextFactory factory = configuration.batchContextFactory();

            assertNotNull(factory);
        }
    }

    @Nested
    @DisplayName("ApplicationContextAware implementation tests")
    class ApplicationContextAwareTests {

        @Test
        @DisplayName("Should implement ApplicationContextAware")
        void shouldImplementApplicationContextAware() {
            assertTrue(configuration instanceof org.springframework.context.ApplicationContextAware);
        }
    }
}
