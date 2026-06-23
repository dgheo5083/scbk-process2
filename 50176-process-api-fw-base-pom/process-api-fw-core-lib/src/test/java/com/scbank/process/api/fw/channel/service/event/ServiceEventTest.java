package com.scbank.process.api.fw.channel.service.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;

/**
 * ServiceEvent Test Class
 */
class ServiceEventTest {

    private ServiceEvent serviceEvent;
    private Object source;

    @BeforeEach
    void setUp() {
        source = new Object();
        serviceEvent = new ServiceEvent(source);
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create event with source")
        void shouldCreateEventWithSource() {
            assertNotNull(serviceEvent);
            assertSame(source, serviceEvent.getSource());
        }

        @Test
        @DisplayName("Should throw when source is null")
        void shouldThrowWhenSourceIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new ServiceEvent(null));
        }

        @Test
        @DisplayName("Should create event with String source")
        void shouldCreateEventWithStringSource() {
            ServiceEvent event = new ServiceEvent("testSource");

            assertEquals("testSource", event.getSource());
        }
    }

    @Nested
    @DisplayName("getSource tests")
    class GetSourceTests {

        @Test
        @DisplayName("Should return the source object")
        void shouldReturnTheSourceObject() {
            assertEquals(source, serviceEvent.getSource());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend ApplicationEvent")
        void shouldExtendApplicationEvent() {
            assertTrue(serviceEvent instanceof ApplicationEvent);
        }
    }

    @Nested
    @DisplayName("Timestamp tests")
    class TimestampTests {

        @Test
        @DisplayName("Should have timestamp set")
        void shouldHaveTimestampSet() {
            long timestamp = serviceEvent.getTimestamp();

            assertTrue(timestamp > 0);
            assertTrue(timestamp <= System.currentTimeMillis());
        }
    }

    @Nested
    @DisplayName("Different source types tests")
    class DifferentSourceTypesTests {

        @Test
        @DisplayName("Should accept Integer source")
        void shouldAcceptIntegerSource() {
            ServiceEvent event = new ServiceEvent(Integer.valueOf(123));

            assertEquals(123, event.getSource());
        }

        @Test
        @DisplayName("Should accept custom object source")
        void shouldAcceptCustomObjectSource() {
            CustomSource customSource = new CustomSource("test");
            ServiceEvent event = new ServiceEvent(customSource);

            assertSame(customSource, event.getSource());
        }
    }

    // Helper class for testing
    private static class CustomSource {
        private final String value;

        CustomSource(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }
}
