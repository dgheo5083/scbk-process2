package com.scbank.process.api.fw.integration.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * IntegrationInterceptorChain Test Class
 */
@ExtendWith(MockitoExtension.class)
class IntegrationInterceptorChainTest {

    @Mock
    private IntegrationInterceptor mockInterceptor1;

    @Mock
    private IntegrationInterceptor mockInterceptor2;

    @Mock
    private IntegrationInterceptor mockInterceptor3;

    @Mock
    private IntegrationContext mockContext;

    private Object mockRequest;
    private Object mockResponse;

    @BeforeEach
    void setUp() {
        mockRequest = new Object();
        mockResponse = new Object();
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create chain with interceptor list")
        void shouldCreateChainWithInterceptorList() {
            List<IntegrationInterceptor> interceptors = List.of(mockInterceptor1, mockInterceptor2);

            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(interceptors);

            assertNotNull(chain);
        }

        @Test
        @DisplayName("Should create chain with empty list")
        void shouldCreateChainWithEmptyList() {
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(List.of());

            assertNotNull(chain);
        }
    }

    @Nested
    @DisplayName("before tests")
    class BeforeTests {

        @Test
        @DisplayName("Should call before on all interceptors in order")
        void shouldCallBeforeOnAllInterceptorsInOrder() {
            List<IntegrationInterceptor> interceptors = List.of(mockInterceptor1, mockInterceptor2, mockInterceptor3);
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(interceptors);

            chain.before(mockContext, mockRequest);

            InOrder inOrder = inOrder(mockInterceptor1, mockInterceptor2, mockInterceptor3);
            inOrder.verify(mockInterceptor1).before(mockContext, mockRequest);
            inOrder.verify(mockInterceptor2).before(mockContext, mockRequest);
            inOrder.verify(mockInterceptor3).before(mockContext, mockRequest);
        }

        @Test
        @DisplayName("Should handle empty interceptor list")
        void shouldHandleEmptyInterceptorList() {
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(List.of());

            assertDoesNotThrow(() -> chain.before(mockContext, mockRequest));
        }

        @Test
        @DisplayName("Should call before on single interceptor")
        void shouldCallBeforeOnSingleInterceptor() {
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(List.of(mockInterceptor1));

            chain.before(mockContext, mockRequest);

            verify(mockInterceptor1, times(1)).before(mockContext, mockRequest);
        }
    }

    @Nested
    @DisplayName("after tests")
    class AfterTests {

        @Test
        @DisplayName("Should call after on all interceptors in order")
        void shouldCallAfterOnAllInterceptorsInOrder() {
            List<IntegrationInterceptor> interceptors = List.of(mockInterceptor1, mockInterceptor2);
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(interceptors);

            chain.after(mockContext, mockRequest, mockResponse);

            InOrder inOrder = inOrder(mockInterceptor1, mockInterceptor2);
            inOrder.verify(mockInterceptor1).after(mockContext, mockRequest, mockResponse);
            inOrder.verify(mockInterceptor2).after(mockContext, mockRequest, mockResponse);
        }

        @Test
        @DisplayName("Should handle empty interceptor list")
        void shouldHandleEmptyInterceptorList() {
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(List.of());

            assertDoesNotThrow(() -> chain.after(mockContext, mockRequest, mockResponse));
        }
    }

    @Nested
    @DisplayName("onError tests")
    class OnErrorTests {

        @Test
        @DisplayName("Should call onError on all interceptors in order")
        void shouldCallOnErrorOnAllInterceptorsInOrder() {
            List<IntegrationInterceptor> interceptors = List.of(mockInterceptor1, mockInterceptor2);
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(interceptors);
            Throwable error = new RuntimeException("Test error");

            chain.onError(mockContext, mockRequest, error);

            InOrder inOrder = inOrder(mockInterceptor1, mockInterceptor2);
            inOrder.verify(mockInterceptor1).onError(mockContext, mockRequest, error);
            inOrder.verify(mockInterceptor2).onError(mockContext, mockRequest, error);
        }

        @Test
        @DisplayName("Should handle empty interceptor list")
        void shouldHandleEmptyInterceptorList() {
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(List.of());
            Throwable error = new RuntimeException("Test error");

            assertDoesNotThrow(() -> chain.onError(mockContext, mockRequest, error));
        }

        @Test
        @DisplayName("Should pass exception to all interceptors")
        void shouldPassExceptionToAllInterceptors() {
            List<IntegrationInterceptor> interceptors = List.of(mockInterceptor1, mockInterceptor2, mockInterceptor3);
            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(interceptors);
            RuntimeException error = new RuntimeException("Specific error");

            chain.onError(mockContext, mockRequest, error);

            verify(mockInterceptor1).onError(mockContext, mockRequest, error);
            verify(mockInterceptor2).onError(mockContext, mockRequest, error);
            verify(mockInterceptor3).onError(mockContext, mockRequest, error);
        }
    }

    @Nested
    @DisplayName("interceptor order tests")
    class InterceptorOrderTests {

        @Test
        @DisplayName("Should maintain interceptor order")
        void shouldMaintainInterceptorOrder() {
            List<String> executionOrder = new ArrayList<>();

            IntegrationInterceptor first = new IntegrationInterceptor() {
                @Override
                public void before(IntegrationContext context, Object request) {
                    executionOrder.add("first");
                }
            };

            IntegrationInterceptor second = new IntegrationInterceptor() {
                @Override
                public void before(IntegrationContext context, Object request) {
                    executionOrder.add("second");
                }
            };

            IntegrationInterceptorChain chain = new IntegrationInterceptorChain(List.of(first, second));
            chain.before(mockContext, mockRequest);

            assertEquals(2, executionOrder.size());
            assertEquals("first", executionOrder.get(0));
            assertEquals("second", executionOrder.get(1));
        }
    }
}
