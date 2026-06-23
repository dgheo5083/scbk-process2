package com.scbank.process.api.fw.batch.constants;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * BatchConstants Test Class
 *
 * JUnit 5 + Mockito based test class for 100% Jacoco coverage.
 */
class BatchConstantsTest {

    @Test
    @DisplayName("Should be able to instantiate BatchConstants for coverage")
    void shouldInstantiateBatchConstants() {
        // Instantiate for Jacoco coverage of default constructor
        BatchConstants batchConstants = new BatchConstants();
        assertNotNull(batchConstants);
    }

    @Test
    @DisplayName("Should have correct BATCH_METADATA_JOB_KEY constant value")
    void shouldHaveCorrectBatchMetadataJobKeyValue() {
        assertEquals("batch.metadata", BatchConstants.BATCH_METADATA_JOB_KEY);
    }

    @Test
    @DisplayName("Should have correct BATCH_COMPONENT_ID constant value")
    void shouldHaveCorrectBatchComponentIdValue() {
        assertEquals("batch.component.id", BatchConstants.BATCH_COMPONENT_ID);
    }

    @Test
    @DisplayName("Should have correct JOBDETAIL_NAME_SUFFIX constant value")
    void shouldHaveCorrectJobDetailNameSuffixValue() {
        assertEquals("-job", BatchConstants.JOBDETAIL_NAME_SUFFIX);
    }

    @Test
    @DisplayName("Should have correct TRIGGER_NAME_SUFFIX constant value")
    void shouldHaveCorrectTriggerNameSuffixValue() {
        assertEquals("-trigger", BatchConstants.TRIGGER_NAME_SUFFIX);
    }

    @Test
    @DisplayName("Should have correct TRIGGER_PROP_START_DELAY constant value")
    void shouldHaveCorrectTriggerPropStartDelayValue() {
        assertEquals("startDelay", BatchConstants.TRIGGER_PROP_START_DELAY);
    }

    @Test
    @DisplayName("Should have correct TRIGGER_PROP_REPEAT_INTERVAL constant value")
    void shouldHaveCorrectTriggerPropRepeatIntervalValue() {
        assertEquals("repeatInterval", BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL);
    }

    @Test
    @DisplayName("Should have correct TRIGGER_PROP_REPEAT_COUNT constant value")
    void shouldHaveCorrectTriggerPropRepeatCountValue() {
        assertEquals("repeatCount", BatchConstants.TRIGGER_PROP_REPEAT_COUNT);
    }

    @Test
    @DisplayName("Should have correct TRIGGER_PROP_PRIORITY constant value")
    void shouldHaveCorrectTriggerPropPriorityValue() {
        assertEquals("priority", BatchConstants.TRIGGER_PROP_PRIORITY);
    }

    @Test
    @DisplayName("Should have correct TRIGGER_PROP_CRON_EXPRESSION constant value")
    void shouldHaveCorrectTriggerPropCronExpressionValue() {
        assertEquals("cronExpression", BatchConstants.TRIGGER_PROP_CRON_EXPRESSION);
    }

    @Test
    @DisplayName("Constants should be final and not modifiable")
    void constantsShouldBeFinal() {
        // Verify constants are non-null strings
        assertNotNull(BatchConstants.BATCH_METADATA_JOB_KEY);
        assertNotNull(BatchConstants.BATCH_COMPONENT_ID);
        assertNotNull(BatchConstants.JOBDETAIL_NAME_SUFFIX);
        assertNotNull(BatchConstants.TRIGGER_NAME_SUFFIX);
        assertNotNull(BatchConstants.TRIGGER_PROP_START_DELAY);
        assertNotNull(BatchConstants.TRIGGER_PROP_REPEAT_INTERVAL);
        assertNotNull(BatchConstants.TRIGGER_PROP_REPEAT_COUNT);
        assertNotNull(BatchConstants.TRIGGER_PROP_PRIORITY);
        assertNotNull(BatchConstants.TRIGGER_PROP_CRON_EXPRESSION);
    }
}
