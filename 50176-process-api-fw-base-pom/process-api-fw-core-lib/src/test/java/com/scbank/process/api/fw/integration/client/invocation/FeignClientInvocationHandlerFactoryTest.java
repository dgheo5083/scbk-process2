package com.scbank.process.api.fw.integration.client.invocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;

import feign.InvocationHandlerFactory;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;

/**
 * FeignClientInvocationHandlerFactory Test Class
 */
@ExtendWith(MockitoExtension.class)
class FeignClientInvocationHandlerFactoryTest {

    @Mock
    private Target<?> mockTarget;

    @Mock
    private MethodHandler mockMethodHandler;

    private FeignClientInvocationHandlerFactory factory;

    @BeforeEach
    void setUp() {
        factory = new FeignClientInvocationHandlerFactory();
    }

    @AfterEach
    void tearDown() {
        IntegrationContextHolder.clear();
        FeignFilterContextHolder.clear();
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {

        @Test
        @DisplayName("Should create InvocationHandler")
        void shouldCreateInvocationHandler() throws NoSuchMethodException {
            Method method = Object.class.getMethod("toString");
            Map<Method, MethodHandler> dispatch = new HashMap<>();
            dispatch.put(method, mockMethodHandler);

            InvocationHandler handler = factory.create(mockTarget, dispatch);

            assertNotNull(handler);
        }

        @Test
        @DisplayName("Should invoke method handler and return result")
        void shouldInvokeMethodHandlerAndReturnResult() throws Throwable {
            IntegrationContext ctx = IntegrationContext.builder()
                    .systemId("MCI")
                    .interfaceId("IF001")
                    .build();
            IntegrationContextHolder.set(ctx);

            Method method = Object.class.getMethod("toString");
            Map<Method, MethodHandler> dispatch = new HashMap<>();
            dispatch.put(method, mockMethodHandler);

            when(mockMethodHandler.invoke(any())).thenReturn("test result");

            InvocationHandler handler = factory.create(mockTarget, dispatch);
            Object result = handler.invoke(null, method, new Object[]{});

            assertEquals("test result", result);
        }

        @Test
        @DisplayName("Should clear FeignFilterContext after invocation")
        void shouldClearFeignFilterContextAfterInvocation() throws Throwable {
            IntegrationContext ctx = IntegrationContext.builder()
                    .systemId("MCI")
                    .build();
            IntegrationContextHolder.set(ctx);

            Method method = Object.class.getMethod("toString");
            Map<Method, MethodHandler> dispatch = new HashMap<>();
            dispatch.put(method, mockMethodHandler);

            when(mockMethodHandler.invoke(any())).thenReturn("result");

            InvocationHandler handler = factory.create(mockTarget, dispatch);
            handler.invoke(null, method, new Object[]{});

            assertNull(FeignFilterContextHolder.get());
        }

        @Test
        @DisplayName("Should clear FeignFilterContext even on exception")
        void shouldClearFeignFilterContextEvenOnException() throws Throwable {
            IntegrationContext ctx = IntegrationContext.builder()
                    .systemId("FEP")
                    .build();
            IntegrationContextHolder.set(ctx);

            Method method = Object.class.getMethod("toString");
            Map<Method, MethodHandler> dispatch = new HashMap<>();
            dispatch.put(method, mockMethodHandler);

            when(mockMethodHandler.invoke(any())).thenThrow(new RuntimeException("Test error"));

            InvocationHandler handler = factory.create(mockTarget, dispatch);

            assertThrows(RuntimeException.class, () ->
                    handler.invoke(null, method, new Object[]{}));

            assertNull(FeignFilterContextHolder.get());
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement InvocationHandlerFactory interface")
        void shouldImplementInvocationHandlerFactoryInterface() {
            assertTrue(factory instanceof InvocationHandlerFactory);
        }
    }
}
