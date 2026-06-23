package com.scbank.process.api.fw.channel.logging.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.context.IServiceContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HttpLogFactory Test Class
 */
@ExtendWith(MockitoExtension.class)
class HttpLogFactoryTest {

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private HttpLogFactory logFactory;

    @BeforeEach
    void setUp() {
        logFactory = new HttpLogFactory();
        when(serviceContext.request()).thenReturn(request);
    }

    @Nested
    @DisplayName("buildRequestLog tests")
    class BuildRequestLogTests {

        @Test
        @DisplayName("Should create request log with all fields")
        void shouldCreateRequestLogWithAllFields() {
            // Given
            String trackingId = "tracking-123";
            String uri = "/api/test";
            String method = "POST";
            Map<String, Object> body = Map.of("key", "value");

            when(serviceContext.requestUId()).thenReturn(trackingId);
            when(request.getRequestURI()).thenReturn(uri);
            when(request.getMethod()).thenReturn(method);
            when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());

            // When
            HttpRequestLog result = logFactory.buildRequestLog(serviceContext, body);

            // Then
            assertNotNull(result);
            assertEquals(trackingId, result.getTrackingId());
            assertEquals(uri, result.getUri());
            assertEquals(method, result.getMethod());
            assertEquals(body, result.getBody());
        }

        @Test
        @DisplayName("Should extract request headers")
        void shouldExtractRequestHeaders() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-123");
            when(request.getRequestURI()).thenReturn("/api/test");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeaderNames()).thenReturn(
                    Collections.enumeration(java.util.List.of("Content-Type", "Authorization")));
            when(request.getHeader("Content-Type")).thenReturn("application/json");
            when(request.getHeader("Authorization")).thenReturn("Bearer token");

            // When
            HttpRequestLog result = logFactory.buildRequestLog(serviceContext, null);

            // Then
            assertNotNull(result.getHeaders());
            assertEquals("application/json", result.getHeaders().get("Content-Type"));
            assertEquals("Bearer token", result.getHeaders().get("Authorization"));
        }

        @Test
        @DisplayName("Should handle null body")
        void shouldHandleNullBody() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-123");
            when(request.getRequestURI()).thenReturn("/api/test");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());

            // When
            HttpRequestLog result = logFactory.buildRequestLog(serviceContext, null);

            // Then
            assertNotNull(result);
            assertNull(result.getBody());
        }

        @Test
        @DisplayName("Should handle empty headers")
        void shouldHandleEmptyHeaders() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-123");
            when(request.getRequestURI()).thenReturn("/api/test");
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());

            // When
            HttpRequestLog result = logFactory.buildRequestLog(serviceContext, "body");

            // Then
            assertNotNull(result.getHeaders());
            assertTrue(result.getHeaders().isEmpty());
        }
    }

    @Nested
    @DisplayName("buildResponseLog tests")
    class BuildResponseLogTests {

        @BeforeEach
        void setUpResponse() {
            when(serviceContext.response()).thenReturn(response);
        }

        @Test
        @DisplayName("Should create response log with all fields")
        void shouldCreateResponseLogWithAllFields() {
            // Given
            String trackingId = "tracking-456";
            String uri = "/api/test";
            String method = "POST";
            int status = 200;
            Map<String, Object> body = Map.of("result", "success");

            when(serviceContext.requestUId()).thenReturn(trackingId);
            when(request.getRequestURI()).thenReturn(uri);
            when(request.getMethod()).thenReturn(method);
            when(response.getStatus()).thenReturn(status);
            when(response.getHeaderNames()).thenReturn(Collections.emptyList());

            // When
            HttpResponseLog result = logFactory.buildResponseLog(serviceContext, body);

            // Then
            assertNotNull(result);
            assertEquals(trackingId, result.getTrackingId());
            assertEquals(uri, result.getUri());
            assertEquals(method, result.getMethod());
            assertEquals(status, result.getStatus());
            assertEquals(body, result.getBody());
        }

        @Test
        @DisplayName("Should extract response headers")
        void shouldExtractResponseHeaders() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-456");
            when(request.getRequestURI()).thenReturn("/api/test");
            when(request.getMethod()).thenReturn("POST");
            when(response.getStatus()).thenReturn(200);
            when(response.getHeaderNames()).thenReturn(java.util.List.of("Content-Type", "X-Custom-Header"));
            when(response.getHeader("Content-Type")).thenReturn("application/json");
            when(response.getHeader("X-Custom-Header")).thenReturn("custom-value");

            // When
            HttpResponseLog result = logFactory.buildResponseLog(serviceContext, null);

            // Then
            assertNotNull(result.getHeaders());
            assertEquals("application/json", result.getHeaders().get("Content-Type"));
            assertEquals("custom-value", result.getHeaders().get("X-Custom-Header"));
        }

        @Test
        @DisplayName("Should handle different HTTP status codes")
        void shouldHandleDifferentStatusCodes() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-789");
            when(request.getRequestURI()).thenReturn("/api/error");
            when(request.getMethod()).thenReturn("GET");
            when(response.getStatus()).thenReturn(500);
            when(response.getHeaderNames()).thenReturn(Collections.emptyList());

            // When
            HttpResponseLog result = logFactory.buildResponseLog(serviceContext, "error");

            // Then
            assertEquals(500, result.getStatus());
        }

        @Test
        @DisplayName("Should handle null body")
        void shouldHandleNullBody() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-000");
            when(request.getRequestURI()).thenReturn("/api/empty");
            when(request.getMethod()).thenReturn("DELETE");
            when(response.getStatus()).thenReturn(204);
            when(response.getHeaderNames()).thenReturn(Collections.emptyList());

            // When
            HttpResponseLog result = logFactory.buildResponseLog(serviceContext, null);

            // Then
            assertNull(result.getBody());
        }

        @Test
        @DisplayName("Should handle empty response headers")
        void shouldHandleEmptyResponseHeaders() {
            // Given
            when(serviceContext.requestUId()).thenReturn("uuid-empty");
            when(request.getRequestURI()).thenReturn("/api/test");
            when(request.getMethod()).thenReturn("GET");
            when(response.getStatus()).thenReturn(200);
            when(response.getHeaderNames()).thenReturn(Collections.emptyList());

            // When
            HttpResponseLog result = logFactory.buildResponseLog(serviceContext, "body");

            // Then
            assertNotNull(result.getHeaders());
            assertTrue(result.getHeaders().isEmpty());
        }
    }
}
