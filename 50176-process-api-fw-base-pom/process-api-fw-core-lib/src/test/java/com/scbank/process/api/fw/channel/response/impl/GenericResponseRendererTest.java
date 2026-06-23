package com.scbank.process.api.fw.channel.response.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.IResponseMessage;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GenericResponseRenderer Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenericResponseRendererTest {

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private IResponseMessage<?, ?> responseMessage;

    private GenericResponseRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new GenericResponseRenderer();
        when(serviceContext.request()).thenReturn(request);
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for JSON accept header")
        void shouldReturnTrueForJsonAccept() {
            when(request.getHeader("Accept")).thenReturn("application/json");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for XML accept header")
        void shouldReturnTrueForXmlAccept() {
            when(request.getHeader("Accept")).thenReturn("application/xml");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for text/plain accept header")
        void shouldReturnTrueForTextPlainAccept() {
            when(request.getHeader("Accept")).thenReturn("text/plain");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when Accept header is empty")
        void shouldReturnTrueWhenAcceptEmpty() {
            when(request.getHeader("Accept")).thenReturn("");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when Accept header is null")
        void shouldReturnTrueWhenAcceptNull() {
            when(request.getHeader("Accept")).thenReturn(null);

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for wildcard accept")
        void shouldReturnTrueForWildcardAccept() {
            when(request.getHeader("Accept")).thenReturn("*/*");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when data is null")
        void shouldReturnFalseWhenDataNull() {
            boolean result = renderer.supports(null, serviceContext);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for unsupported accept type")
        void shouldReturnFalseForUnsupportedAccept() {
            when(request.getHeader("Accept")).thenReturn("image/png");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true for multiple accept types with one supported")
        void shouldReturnTrueForMultipleAcceptWithSupported() {
            when(request.getHeader("Accept")).thenReturn("image/png, application/json");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("render tests")
    class RenderTests {

        @Test
        @DisplayName("Should render with JSON content type for application/json accept")
        void shouldRenderWithJsonContentType() {
            when(request.getHeader("Accept")).thenReturn("application/json");

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            assertNotNull(result);
            assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
            assertEquals(responseMessage, result.getBody());
        }

        @Test
        @DisplayName("Should render with XML content type for application/xml accept")
        void shouldRenderWithXmlContentType() {
            when(request.getHeader("Accept")).thenReturn("application/xml");

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            assertNotNull(result);
            assertEquals(MediaType.APPLICATION_XML, result.getHeaders().getContentType());
        }

        @Test
        @DisplayName("Should default to JSON when accept is empty")
        void shouldDefaultToJsonWhenAcceptEmpty() {
            when(request.getHeader("Accept")).thenReturn("");

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
        }

        @Test
        @DisplayName("Should default to JSON when accept is null")
        void shouldDefaultToJsonWhenAcceptNull() {
            when(request.getHeader("Accept")).thenReturn(null);

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
        }

        @Test
        @DisplayName("Should default to JSON for wildcard accept")
        void shouldDefaultToJsonForWildcard() {
            when(request.getHeader("Accept")).thenReturn("*/*");

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
        }

        @Test
        @DisplayName("Should return 200 OK status")
        void shouldReturnOkStatus() {
            when(request.getHeader("Accept")).thenReturn("application/json");

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            assertEquals(200, result.getStatusCode().value());
        }

        @Test
        @DisplayName("Should use first matching supported type from multiple accepts")
        void shouldUseFirstMatchingSupportedType() {
            when(request.getHeader("Accept")).thenReturn("text/plain, application/json");

            ResponseEntity<Object> result = renderer.render(responseMessage, serviceContext);

            // Should use text/plain as it appears first and is supported
            assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
        }
    }
}
