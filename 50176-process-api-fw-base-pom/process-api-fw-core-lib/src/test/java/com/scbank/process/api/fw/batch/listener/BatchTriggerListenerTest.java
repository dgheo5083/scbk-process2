package com.scbank.process.api.fw.batch.listener;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quartz.listeners.TriggerListenerSupport;

/**
 * BatchTriggerListener Test Class
 */
class BatchTriggerListenerTest {

    private BatchTriggerListener listener;

    @BeforeEach
    void setUp() {
        listener = new BatchTriggerListener();
    }

    @Nested
    @DisplayName("getName tests")
    class GetNameTests {

        @Test
        @DisplayName("Should return class simple name")
        void shouldReturnClassSimpleName() {
            assertEquals("BatchTriggerListener", listener.getName());
        }

        @Test
        @DisplayName("Should return consistent name on multiple calls")
        void shouldReturnConsistentName() {
            String name1 = listener.getName();
            String name2 = listener.getName();
            assertEquals(name1, name2);
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend TriggerListenerSupport")
        void shouldExtendTriggerListenerSupport() {
            assertTrue(listener instanceof TriggerListenerSupport);
        }
    }
}
