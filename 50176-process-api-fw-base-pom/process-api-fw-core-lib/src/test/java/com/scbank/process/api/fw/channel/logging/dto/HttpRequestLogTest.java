package com.scbank.process.api.fw.channel.logging.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * HttpRequestLog Test Class
 */
class HttpRequestLogTest {

    private HttpRequestLog requestLog;

    @BeforeEach
    void setUp() {
        requestLog = new HttpRequestLog();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with request log type")
        void shouldCreateWithRequestLogType() {
            assertEquals("request", requestLog.getLogType());
        }
    }

    @Nested
    @DisplayName("Body tests")
    class BodyTests {

        @Test
        @DisplayName("Should set and get body as Map")
        void shouldSetAndGetBodyAsMap() {
            Map<String, Object> body = Map.of("key", "value", "count", 100);
            requestLog.setBody(body);

            assertEquals(body, requestLog.getBody());
        }

        @Test
        @DisplayName("Should set and get body as String")
        void shouldSetAndGetBodyAsString() {
            String body = "{\"key\":\"value\"}";
            requestLog.setBody(body);

            assertEquals(body, requestLog.getBody());
        }

        @Test
        @DisplayName("Should handle null body")
        void shouldHandleNullBody() {
            requestLog.setBody(null);

            assertNull(requestLog.getBody());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should inherit tracking ID from HttpBaseLog")
        void shouldInheritTrackingIdFromHttpBaseLog() {
            requestLog.setTrackingId("REQ-123");

            assertEquals("REQ-123", requestLog.getTrackingId());
        }

        @Test
        @DisplayName("Should inherit method from HttpBaseLog")
        void shouldInheritMethodFromHttpBaseLog() {
            requestLog.setMethod("POST");

            assertEquals("POST", requestLog.getMethod());
        }

        @Test
        @DisplayName("Should inherit URI from HttpBaseLog")
        void shouldInheritUriFromHttpBaseLog() {
            requestLog.setUri("/api/test");

            assertEquals("/api/test", requestLog.getUri());
        }

        @Test
        @DisplayName("Should inherit headers from HttpBaseLog")
        void shouldInheritHeadersFromHttpBaseLog() {
            Map<String, String> headers = Map.of("Content-Type", "application/json");
            requestLog.setHeaders(headers);

            assertEquals(headers, requestLog.getHeaders());
        }
    }
}
