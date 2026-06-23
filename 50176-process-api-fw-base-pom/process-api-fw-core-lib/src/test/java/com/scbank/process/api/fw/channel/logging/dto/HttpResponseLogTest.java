package com.scbank.process.api.fw.channel.logging.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * HttpResponseLog Test Class
 */
class HttpResponseLogTest {

    private HttpResponseLog responseLog;

    @BeforeEach
    void setUp() {
        responseLog = new HttpResponseLog();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with response log type")
        void shouldCreateWithResponseLogType() {
            assertEquals("response", responseLog.getLogType());
        }
    }

    @Nested
    @DisplayName("Status tests")
    class StatusTests {

        @Test
        @DisplayName("Should set and get status")
        void shouldSetAndGetStatus() {
            responseLog.setStatus(200);

            assertEquals(200, responseLog.getStatus());
        }

        @Test
        @DisplayName("Should handle error status codes")
        void shouldHandleErrorStatusCodes() {
            responseLog.setStatus(500);

            assertEquals(500, responseLog.getStatus());
        }
    }

    @Nested
    @DisplayName("Duration tests")
    class DurationTests {

        @Test
        @DisplayName("Should set and get duration")
        void shouldSetAndGetDuration() {
            responseLog.setDuration(1500L);

            assertEquals(1500L, responseLog.getDuration());
        }

        @Test
        @DisplayName("Should handle zero duration")
        void shouldHandleZeroDuration() {
            responseLog.setDuration(0L);

            assertEquals(0L, responseLog.getDuration());
        }
    }

    @Nested
    @DisplayName("Body tests")
    class BodyTests {

        @Test
        @DisplayName("Should set and get body as Map")
        void shouldSetAndGetBodyAsMap() {
            Map<String, Object> body = Map.of("status", "success", "data", "result");
            responseLog.setBody(body);

            assertEquals(body, responseLog.getBody());
        }

        @Test
        @DisplayName("Should set and get body as String")
        void shouldSetAndGetBodyAsString() {
            String body = "{\"status\":\"success\"}";
            responseLog.setBody(body);

            assertEquals(body, responseLog.getBody());
        }

        @Test
        @DisplayName("Should handle null body")
        void shouldHandleNullBody() {
            responseLog.setBody(null);

            assertNull(responseLog.getBody());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should inherit tracking ID from HttpBaseLog")
        void shouldInheritTrackingIdFromHttpBaseLog() {
            responseLog.setTrackingId("RES-456");

            assertEquals("RES-456", responseLog.getTrackingId());
        }

        @Test
        @DisplayName("Should inherit method from HttpBaseLog")
        void shouldInheritMethodFromHttpBaseLog() {
            responseLog.setMethod("GET");

            assertEquals("GET", responseLog.getMethod());
        }

        @Test
        @DisplayName("Should inherit URI from HttpBaseLog")
        void shouldInheritUriFromHttpBaseLog() {
            responseLog.setUri("/api/response");

            assertEquals("/api/response", responseLog.getUri());
        }

        @Test
        @DisplayName("Should inherit headers from HttpBaseLog")
        void shouldInheritHeadersFromHttpBaseLog() {
            Map<String, String> headers = Map.of("Content-Type", "application/json");
            responseLog.setHeaders(headers);

            assertEquals(headers, responseLog.getHeaders());
        }
    }
}
