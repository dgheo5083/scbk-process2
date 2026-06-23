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
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.listeners.SchedulerListenerSupport;

/**
 * BatchSchedulerListener Test Class
 */
@ExtendWith(MockitoExtension.class)
class BatchSchedulerListenerTest {

    private BatchSchedulerListener listener;

    @Mock
    private Trigger trigger;

    @Mock
    private TriggerKey triggerKey;

    @BeforeEach
    void setUp() {
        listener = new BatchSchedulerListener();
    }

    @Nested
    @DisplayName("schedulerStarted tests")
    class SchedulerStartedTests {

        @Test
        @DisplayName("Should log scheduler started info")
        void shouldLogSchedulerStartedInfo() {
            // Should not throw exception
            assertDoesNotThrow(() -> listener.schedulerStarted());
        }
    }

    @Nested
    @DisplayName("schedulerShutdown tests")
    class SchedulerShutdownTests {

        @Test
        @DisplayName("Should log scheduler shutdown info")
        void shouldLogSchedulerShutdownInfo() {
            // Should not throw exception
            assertDoesNotThrow(() -> listener.schedulerShutdown());
        }
    }

    @Nested
    @DisplayName("jobScheduled tests")
    class JobScheduledTests {

        @Test
        @DisplayName("Should log job scheduled info")
        void shouldLogJobScheduledInfo() {
            when(trigger.getKey()).thenReturn(triggerKey);

            // Should not throw exception
            assertDoesNotThrow(() -> listener.jobScheduled(trigger));

            verify(trigger).getKey();
        }
    }

    @Nested
    @DisplayName("jobUnscheduled tests")
    class JobUnscheduledTests {

        @Test
        @DisplayName("Should log job unscheduled info")
        void shouldLogJobUnscheduledInfo() {
            // Should not throw exception
            assertDoesNotThrow(() -> listener.jobUnscheduled(triggerKey));
        }
    }

    @Nested
    @DisplayName("schedulerError tests")
    class SchedulerErrorTests {

        @Test
        @DisplayName("Should log scheduler error")
        void shouldLogSchedulerError() {
            SchedulerException cause = new SchedulerException("Test scheduler error");
            String errorMessage = "Error message";

            // Should not throw exception
            assertDoesNotThrow(() -> listener.schedulerError(errorMessage, cause));
        }

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            SchedulerException cause = new SchedulerException("Test error");

            // Should not throw exception
            assertDoesNotThrow(() -> listener.schedulerError(null, cause));
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend SchedulerListenerSupport")
        void shouldExtendSchedulerListenerSupport() {
            assertTrue(listener instanceof SchedulerListenerSupport);
        }
    }
}
