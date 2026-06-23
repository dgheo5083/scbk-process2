package com.scbank.process.api.fw.channel.logging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.message.IResponseMessage;

/**
 * RequestResponseBodyLoggingProcessor Test Class
 */
@ExtendWith(MockitoExtension.class)
class RequestResponseBodyLoggingProcessorTest {

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private HttpInputMessage inputMessage;

    @Mock
    private ServerHttpRequest serverRequest;

    @Mock
    private ServerHttpResponse serverResponse;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IResponseMessage<?, ?> responseMessage;

    private RequestResponseBodyLoggingProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new RequestResponseBodyLoggingProcessor();
    }

    @AfterEach
    void tearDown() {
        ServiceContextHolder.clear();
    }

    @Nested
    @DisplayName("supports (RequestBody) tests")
    class SupportsRequestBodyTests {

        @Test
        @DisplayName("Should return true when parameter has RequestBody annotation")
        void shouldReturnTrueWhenHasRequestBodyAnnotation() {
            when(methodParameter.hasParameterAnnotation(RequestBody.class)).thenReturn(true);

            boolean result = processor.supports(methodParameter, String.class, null);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when parameter does not have RequestBody annotation")
        void shouldReturnFalseWhenNoRequestBodyAnnotation() {
            when(methodParameter.hasParameterAnnotation(RequestBody.class)).thenReturn(false);

            boolean result = processor.supports(methodParameter, String.class, null);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("supports (ResponseBody) tests")
    class SupportsResponseBodyTests {

        @Test
        @DisplayName("Should always return true for response body advice")
        void shouldAlwaysReturnTrueForResponseBodyAdvice() {
            boolean result = processor.supports(methodParameter, null);

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("handleEmptyBody tests")
    class HandleEmptyBodyTests {

        @Test
        @DisplayName("Should handle empty body gracefully")
        void shouldHandleEmptyBodyGracefully() {
            ServiceContextHolder.setContext(serviceContext);

            Object result = processor.handleEmptyBody(null, inputMessage, methodParameter, String.class, null);

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("afterBodyRead tests")
    class AfterBodyReadTests {

        @Test
        @DisplayName("Should return body as-is when not byte array")
        void shouldReturnBodyAsIsWhenNotByteArray() {
            String body = "test body";

            Object result = processor.afterBodyRead(body, inputMessage, methodParameter, String.class, null);

            assertEquals(body, result);
        }

        @Test
        @DisplayName("Should process byte array body")
        void shouldProcessByteArrayBody() {
            ServiceContextHolder.setContext(serviceContext);
            byte[] body = "{}".getBytes(StandardCharsets.UTF_8);

            Object result = processor.afterBodyRead(body, inputMessage, methodParameter, byte[].class, null);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("beforeBodyWrite tests")
    class BeforeBodyWriteTests {

        @Test
        @DisplayName("Should return body as-is when not IResponseMessage")
        void shouldReturnBodyAsIsWhenNotIResponseMessage() {
            String body = "test response";

            Object result = processor.beforeBodyWrite(body, methodParameter, MediaType.APPLICATION_JSON,
                    null, serverRequest, serverResponse);

            assertEquals(body, result);
        }

        @Test
        @DisplayName("Should process IResponseMessage body")
        void shouldProcessIResponseMessageBody() {
            ServiceContextHolder.setContext(serviceContext);

            Object result = processor.beforeBodyWrite(responseMessage, methodParameter, MediaType.APPLICATION_JSON,
                    null, serverRequest, serverResponse);

            assertEquals(responseMessage, result);
        }
    }
}
