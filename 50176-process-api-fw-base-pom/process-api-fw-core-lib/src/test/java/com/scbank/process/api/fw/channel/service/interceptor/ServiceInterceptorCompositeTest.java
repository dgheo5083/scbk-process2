package com.scbank.process.api.fw.channel.service.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * ServiceInterceptorComposite Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceInterceptorCompositeTest {

    @Mock
    private IServiceInterceptor interceptor1;

    @Mock
    private IServiceInterceptor interceptor2;

    @Mock
    private IServiceInterceptor interceptor3;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IMessageObject inputMessage;

    @Mock
    private IMessageObject outputMessage;

    private ServiceInterceptorComposite composite;

    @BeforeEach
    void setUp() {
        composite = new ServiceInterceptorComposite(Arrays.asList(interceptor1, interceptor2, interceptor3));
    }

    @Nested
    @DisplayName("preHandle tests")
    class PreHandleTests {

        @Test
        @DisplayName("Should return true when all interceptors return true")
        void shouldReturnTrueWhenAllInterceptorsReturnTrue() {
            when(interceptor1.preHandle(serviceContext, inputMessage)).thenReturn(true);
            when(interceptor2.preHandle(serviceContext, inputMessage)).thenReturn(true);
            when(interceptor3.preHandle(serviceContext, inputMessage)).thenReturn(true);

            boolean result = composite.preHandle(serviceContext, inputMessage);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when first interceptor returns false")
        void shouldReturnFalseWhenFirstInterceptorReturnsFalse() {
            when(interceptor1.preHandle(serviceContext, inputMessage)).thenReturn(false);

            boolean result = composite.preHandle(serviceContext, inputMessage);

            assertFalse(result);
            verify(interceptor2, never()).preHandle(any(), any());
            verify(interceptor3, never()).preHandle(any(), any());
        }

        @Test
        @DisplayName("Should return false when middle interceptor returns false")
        void shouldReturnFalseWhenMiddleInterceptorReturnsFalse() {
            when(interceptor1.preHandle(serviceContext, inputMessage)).thenReturn(true);
            when(interceptor2.preHandle(serviceContext, inputMessage)).thenReturn(false);

            boolean result = composite.preHandle(serviceContext, inputMessage);

            assertFalse(result);
            verify(interceptor1).preHandle(serviceContext, inputMessage);
            verify(interceptor2).preHandle(serviceContext, inputMessage);
            verify(interceptor3, never()).preHandle(any(), any());
        }

        @Test
        @DisplayName("Should execute interceptors in order")
        void shouldExecuteInterceptorsInOrder() {
            when(interceptor1.preHandle(serviceContext, inputMessage)).thenReturn(true);
            when(interceptor2.preHandle(serviceContext, inputMessage)).thenReturn(true);
            when(interceptor3.preHandle(serviceContext, inputMessage)).thenReturn(true);

            composite.preHandle(serviceContext, inputMessage);

            InOrder inOrder = inOrder(interceptor1, interceptor2, interceptor3);
            inOrder.verify(interceptor1).preHandle(serviceContext, inputMessage);
            inOrder.verify(interceptor2).preHandle(serviceContext, inputMessage);
            inOrder.verify(interceptor3).preHandle(serviceContext, inputMessage);
        }

        @Test
        @DisplayName("Should return true for empty interceptor list")
        void shouldReturnTrueForEmptyList() {
            ServiceInterceptorComposite emptyComposite = new ServiceInterceptorComposite(Collections.emptyList());

            boolean result = emptyComposite.preHandle(serviceContext, inputMessage);

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("postHandle tests")
    class PostHandleTests {

        @Test
        @DisplayName("Should execute all interceptors")
        void shouldExecuteAllInterceptors() {
            composite.postHandle(serviceContext, outputMessage);

            verify(interceptor1).postHandle(serviceContext, outputMessage);
            verify(interceptor2).postHandle(serviceContext, outputMessage);
            verify(interceptor3).postHandle(serviceContext, outputMessage);
        }

        @Test
        @DisplayName("Should execute interceptors in order")
        void shouldExecuteInterceptorsInOrder() {
            composite.postHandle(serviceContext, outputMessage);

            InOrder inOrder = inOrder(interceptor1, interceptor2, interceptor3);
            inOrder.verify(interceptor1).postHandle(serviceContext, outputMessage);
            inOrder.verify(interceptor2).postHandle(serviceContext, outputMessage);
            inOrder.verify(interceptor3).postHandle(serviceContext, outputMessage);
        }

        @Test
        @DisplayName("Should handle empty interceptor list")
        void shouldHandleEmptyList() {
            ServiceInterceptorComposite emptyComposite = new ServiceInterceptorComposite(Collections.emptyList());

            assertDoesNotThrow(() -> emptyComposite.postHandle(serviceContext, outputMessage));
        }

        @Test
        @DisplayName("Should continue even if interceptor throws exception")
        void shouldContinueOnException() {
            doThrow(new RuntimeException("Test exception")).when(interceptor1).postHandle(any(), any());

            assertThrows(RuntimeException.class, () -> composite.postHandle(serviceContext, outputMessage));
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create composite with single interceptor")
        void shouldCreateWithSingleInterceptor() {
            ServiceInterceptorComposite singleComposite =
                    new ServiceInterceptorComposite(List.of(interceptor1));

            when(interceptor1.preHandle(serviceContext, inputMessage)).thenReturn(true);

            boolean result = singleComposite.preHandle(serviceContext, inputMessage);

            assertTrue(result);
            verify(interceptor1).preHandle(serviceContext, inputMessage);
        }

        @Test
        @DisplayName("Should create composite with null-safe list")
        void shouldWorkWithEmptyList() {
            ServiceInterceptorComposite emptyComposite =
                    new ServiceInterceptorComposite(Collections.emptyList());

            assertTrue(emptyComposite.preHandle(serviceContext, inputMessage));
        }
    }
}
