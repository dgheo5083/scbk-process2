package com.scbank.process.api.fw.channel.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.context.handler.IServiceContextHandler;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ServiceRequestInterceptor Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceRequestInterceptorTest {

    @Mock
    private IServiceContextHandler serviceContextHandler;

    @Mock
    private ObjectProvider<IRequestInterceptor> interceptorsProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IDevice device;

    private ServiceRequestInterceptor interceptor;

    @BeforeEach
    void setUp() {
        when(interceptorsProvider.stream()).thenReturn(Collections.<IRequestInterceptor>emptyList().stream());
        interceptor = new ServiceRequestInterceptor(serviceContextHandler, interceptorsProvider);
        interceptor.init();
    }

    @AfterEach
    void tearDown() {
        ServiceContextHolder.clear();
        TraceContextHolder.clear();
        MDC.clear();
    }

    @Nested
    @DisplayName("preHandle tests")
    class PreHandleTests {

        @Test
        @DisplayName("Should create service context and store in ThreadLocal")
        void shouldCreateAndStoreServiceContext() throws Exception {
            // Given
            when(serviceContextHandler.createServiceContext(request, response)).thenReturn(serviceContext);
            when(serviceContext.request()).thenReturn(request);
            when(serviceContext.requestUId()).thenReturn("test-uuid");
            //when(serviceContext.device()).thenReturn(device);
            //when(device.getId()).thenReturn("PC");
            when(request.getRequestURI()).thenReturn("/api/test");

            // When
            boolean result = interceptor.preHandle(request, response, new Object());

            // Then
            assertTrue(result);
            assertEquals(serviceContext, ServiceContextHolder.getContext());
        }

        @Test
        @DisplayName("Should set MDC values")
        void shouldSetMdcValues() throws Exception {
            // Given
            when(serviceContextHandler.createServiceContext(request, response)).thenReturn(serviceContext);
            when(serviceContext.request()).thenReturn(request);
            when(serviceContext.requestUId()).thenReturn("test-uuid-123");
            //when(serviceContext.device()).thenReturn(device);
            //when(device.getId()).thenReturn("MOBILE");
            when(request.getRequestURI()).thenReturn("/api/test");

            // When
            interceptor.preHandle(request, response, new Object());

            // Then
            assertEquals("test-uuid-123", MDC.get("request-uuid"));
            //assertEquals("MOBILE", MDC.get("device"));
        }

        @Test
        @DisplayName("Should initialize TraceContext")
        void shouldInitializeTraceContext() throws Exception {
            // Given
            when(serviceContextHandler.createServiceContext(request, response)).thenReturn(serviceContext);
            when(serviceContext.request()).thenReturn(request);
            when(serviceContext.requestUId()).thenReturn("test-uuid");
            //when(serviceContext.device()).thenReturn(device);
            //when(device.getId()).thenReturn("PC");
            when(request.getRequestURI()).thenReturn("/api/v1/users");

            // When
            interceptor.preHandle(request, response, new Object());

            // Then
            Optional<?> traceContext = TraceContextHolder.get();
            assertTrue(traceContext.isPresent());
        }
    }

    @Nested
    @DisplayName("postHandle tests")
    class PostHandleTests {

        @Test
        @DisplayName("Should call composite postHandle")
        void shouldCallCompositePostHandle() throws Exception {
            // When
            interceptor.postHandle(request, response, new Object(), null);

            // Then - no exception thrown
        }
    }

    @Nested
    @DisplayName("afterCompletion tests")
    class AfterCompletionTests {

        @Test
        @DisplayName("Should clear ThreadLocal resources")
        void shouldClearThreadLocalResources() throws Exception {
            // Given - set up context first
            when(serviceContextHandler.createServiceContext(request, response)).thenReturn(serviceContext);
            when(serviceContext.request()).thenReturn(request);
            when(serviceContext.requestUId()).thenReturn("test-uuid");
            //when(serviceContext.device()).thenReturn(device);
            //when(device.getId()).thenReturn("PC");
            when(request.getRequestURI()).thenReturn("/api/test");
            interceptor.preHandle(request, response, new Object());

            // When
            interceptor.afterCompletion(request, response, new Object(), null);

            // Then
            assertNull(ServiceContextHolder.getContext());
            assertTrue(TraceContextHolder.get().isEmpty());
            assertNull(MDC.get("request-uuid"));
        }

        @Test
        @DisplayName("Should clear resources even when exception is provided")
        void shouldClearResourcesOnException() throws Exception {
            // Given - set up context first
            when(serviceContextHandler.createServiceContext(request, response)).thenReturn(serviceContext);
            when(serviceContext.request()).thenReturn(request);
            when(serviceContext.requestUId()).thenReturn("test-uuid");
            //when(serviceContext.device()).thenReturn(device);
           // when(device.getId()).thenReturn("PC");
            when(request.getRequestURI()).thenReturn("/api/test");
            interceptor.preHandle(request, response, new Object());

            // When
            interceptor.afterCompletion(request, response, new Object(), new RuntimeException("Test error"));

            // Then
            assertNull(ServiceContextHolder.getContext());
        }
    }

    @Nested
    @DisplayName("init tests")
    class InitTests {

        @Test
        @DisplayName("Should initialize RequestInterceptorComposite")
        void shouldInitializeComposite() {
            // Given
            when(interceptorsProvider.stream()).thenReturn(Collections.<IRequestInterceptor>emptyList().stream());

            // When
            interceptor.init();

            // Then - no exception thrown, interceptor is usable
            assertDoesNotThrow(() -> interceptor.postHandle(request, response, new Object(), null));
        }
    }
}
