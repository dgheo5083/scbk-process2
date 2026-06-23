package com.scbank.process.api.fw.channel.logging.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * HttpBaseLog Test Class
 */
class HttpBaseLogTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create log with log type")
        void shouldCreateLogWithLogType() {
            TestHttpLog log = new TestHttpLog("REQUEST");

            assertEquals("REQUEST", log.getLogType());
        }
    }

    @Nested
    @DisplayName("Getter and setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get tracking ID")
        void shouldSetAndGetTrackingId() {
            TestHttpLog log = new TestHttpLog("REQUEST");
            log.setTrackingId("TRACK-123");

            assertEquals("TRACK-123", log.getTrackingId());
        }

        @Test
        @DisplayName("Should set and get method")
        void shouldSetAndGetMethod() {
            TestHttpLog log = new TestHttpLog("REQUEST");
            log.setMethod("POST");

            assertEquals("POST", log.getMethod());
        }

        @Test
        @DisplayName("Should set and get URI")
        void shouldSetAndGetUri() {
            TestHttpLog log = new TestHttpLog("REQUEST");
            log.setUri("/api/test");

            assertEquals("/api/test", log.getUri());
        }

        @Test
        @DisplayName("Should set and get headers")
        void shouldSetAndGetHeaders() {
            TestHttpLog log = new TestHttpLog("REQUEST");
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            log.setHeaders(headers);

            assertEquals(headers, log.getHeaders());
            assertEquals("application/json", log.getHeaders().get("Content-Type"));
        }

        @Test
        @DisplayName("Should have timestamp initialized")
        void shouldHaveTimestampInitialized() {
            TestHttpLog log = new TestHttpLog("REQUEST");

            assertNotNull(log.getTimestamp());
        }

        @Test
        @DisplayName("Should set and get timestamp")
        void shouldSetAndGetTimestamp() {
            TestHttpLog log = new TestHttpLog("REQUEST");
            log.setTimestamp("2025-01-01 12:00:00");

            assertEquals("2025-01-01 12:00:00", log.getTimestamp());
        }
    }

    /**
     * Concrete test implementation of HttpBaseLog
     */
    private static class TestHttpLog extends HttpBaseLog {
        public TestHttpLog(String logType) {
            super(logType);
        }
    }
}
