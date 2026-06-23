package com.scbank.process.api.fw.batch.job.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.scbank.process.api.fw.batch.constants.BatchConstants;
import com.scbank.process.api.fw.batch.exception.BatchJobException;
import com.scbank.process.api.fw.batch.job.IBatchTriggerFactory;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata.TriggerType;

/**
 * DefaultBatchTriggerFactory Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultBatchTriggerFactoryTest {

    private DefaultBatchTriggerFactory factory;

    @Mock
    private IBatchMetadata metadata;

    @Mock
    private IBatchTriggerMetadata triggerMetadata;

    @BeforeEach
    void setUp() {
        factory = new DefaultBatchTriggerFactory();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create factory")
        void shouldCreateFactory() {
            assertNotNull(factory);
        }
    }

    @Nested
    @DisplayName("createTrigger tests - null trigger type")
    class NullTriggerTypeTests {

        @Test
        @DisplayName("Should throw exception when trigger type is null")
        void shouldThrowExceptionWhenTriggerTypeIsNull() {
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(null);

            assertThrows(BatchJobException.class, () -> factory.createTrigger(metadata));
        }
    }

    @Nested
    @DisplayName("createTrigger tests - SIMPLE trigger")
    class SimpleTriggerTests {

        @Test
        @DisplayName("Should create simple trigger with valid parameters")
        void shouldCreateSimpleTriggerWithValidParameters() throws BatchJobException {
            Map<String, String> props = new HashMap<>();
            props.put(BatchConstants.TRIGGER_PROP_START_DELAY, "0");
            props.put(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL, "1000");
            props.put(BatchConstants.TRIGGER_PROP_REPEAT_COUNT, "10");

            when(metadata.getId()).thenReturn("test-batch");
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.SIMPLE);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("0");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL)).thenReturn("1000");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_COUNT)).thenReturn("10");

            Trigger trigger = factory.createTrigger(metadata);

            assertNotNull(trigger);
            assertTrue(trigger instanceof SimpleTrigger);
            assertEquals("test-batch-trigger", trigger.getKey().getName());
            assertEquals(Scheduler.DEFAULT_GROUP, trigger.getKey().getGroup());
        }

        @Test
        @DisplayName("Should create simple trigger with start delay")
        void shouldCreateSimpleTriggerWithStartDelay() throws BatchJobException {
            when(metadata.getId()).thenReturn("test-batch");
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.SIMPLE);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("5000");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL)).thenReturn("1000");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_COUNT)).thenReturn("5");

            Trigger trigger = factory.createTrigger(metadata);

            assertNotNull(trigger);
        }

        @Test
        @DisplayName("Should throw exception when repeat interval is invalid")
        void shouldThrowExceptionWhenRepeatIntervalIsInvalid() {
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.SIMPLE);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("0");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL)).thenReturn("-1");

            assertThrows(BatchJobException.class, () -> factory.createTrigger(metadata));
        }

        @Test
        @DisplayName("Should throw exception when repeat count is invalid")
        void shouldThrowExceptionWhenRepeatCountIsInvalid() {
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.SIMPLE);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("0");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL)).thenReturn("1000");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_COUNT)).thenReturn("0");

            assertThrows(BatchJobException.class, () -> factory.createTrigger(metadata));
        }

        @Test
        @DisplayName("Should throw exception when repeat interval is zero")
        void shouldThrowExceptionWhenRepeatIntervalIsZero() {
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.SIMPLE);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("0");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL)).thenReturn("0");

            assertThrows(BatchJobException.class, () -> factory.createTrigger(metadata));
        }
    }

    @Nested
    @DisplayName("createTrigger tests - CRON trigger")
    class CronTriggerTests {

        @Test
        @DisplayName("Should create cron trigger with valid expression")
        void shouldCreateCronTriggerWithValidExpression() throws BatchJobException {
            when(metadata.getId()).thenReturn("cron-batch");
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.CRON);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("0");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_CRON_EXPRESSION)).thenReturn("0 0 12 * * ?");

            Trigger trigger = factory.createTrigger(metadata);

            assertNotNull(trigger);
            assertTrue(trigger instanceof CronTrigger);
            assertEquals("cron-batch-trigger", trigger.getKey().getName());
        }

        @Test
        @DisplayName("Should throw exception when cron expression is empty")
        void shouldThrowExceptionWhenCronExpressionIsEmpty() {
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.CRON);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("0");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_CRON_EXPRESSION)).thenReturn("");

            assertThrows(BatchJobException.class, () -> factory.createTrigger(metadata));
        }

        @Test
        @DisplayName("Should create cron trigger with start delay")
        void shouldCreateCronTriggerWithStartDelay() throws BatchJobException {
            when(metadata.getId()).thenReturn("cron-batch");
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.CRON);
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_START_DELAY)).thenReturn("5000");
            when(triggerMetadata.getProperty(BatchConstants.TRIGGER_PROP_CRON_EXPRESSION)).thenReturn("0 0 * * * ?");

            Trigger trigger = factory.createTrigger(metadata);

            assertNotNull(trigger);
        }
    }

    @Nested
    @DisplayName("createTrigger tests - NONE trigger type")
    class NoneTriggerTypeTests {

        @Test
        @DisplayName("Should throw exception for NONE trigger type")
        void shouldThrowExceptionForNoneTriggerType() {
            when(metadata.getId()).thenReturn("none-batch");
            when(metadata.getBatchTriggerMetadata()).thenReturn(triggerMetadata);
            when(triggerMetadata.getTriggerType()).thenReturn(TriggerType.NONE);

            assertThrows(BatchJobException.class, () -> factory.createTrigger(metadata));
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchTriggerFactory")
        void shouldImplementIBatchTriggerFactory() {
            assertTrue(factory instanceof IBatchTriggerFactory);
        }
    }
}
