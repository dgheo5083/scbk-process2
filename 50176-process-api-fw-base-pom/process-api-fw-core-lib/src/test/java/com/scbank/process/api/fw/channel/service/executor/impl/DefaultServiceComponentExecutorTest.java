package com.scbank.process.api.fw.channel.service.executor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.argument.ServiceMethodArgumentResolverComposite;
import com.scbank.process.api.fw.channel.service.interceptor.ServiceInterceptorComposite;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * DefaultServiceComponentExecutor Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultServiceComponentExecutorTest {

    @Mock
    private ServiceMethodMetadata methodMetadata;

    @Mock
    private ServiceMethodArgumentResolverComposite argumentResolver;

    @Mock
    private ServiceInterceptorComposite serviceInterceptorComposite;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IMessageObject inputMessage;

    @Mock
    private IMessageObject outputMessage;

    @Mock
    private List<ParameterMetadata> parameters;

    private DefaultServiceComponentExecutor executor;

    @Nested
    @DisplayName("execute tests")
    class ExecuteTests {

        @Test
        @DisplayName("Should execute service method successfully")
        void shouldExecuteServiceMethodSuccessfully() throws Throwable {
            // Given
            TestService testService = new TestService();
            Method testMethod = TestService.class.getMethod("process", IMessageObject.class);
            Object[] args = new Object[]{inputMessage};

            when(methodMetadata.getParameters()).thenReturn(parameters);
            when(argumentResolver.resolveArguments(parameters, serviceContext, inputMessage)).thenReturn(args);

            executor = new DefaultServiceComponentExecutor(
                    methodMetadata,
                    argumentResolver,
                    serviceInterceptorComposite,
                    testService,
                    testMethod);

            // When
            IMessageObject result = executor.execute(serviceContext, inputMessage);

            // Then
            assertNotNull(result);
            verify(serviceInterceptorComposite).preHandle(serviceContext, inputMessage);
            verify(serviceInterceptorComposite).postHandle(eq(serviceContext), any());
        }

        @Test
        @DisplayName("Should execute without interceptor when null")
        void shouldExecuteWithoutInterceptorWhenNull() throws Throwable {
            // Given
            TestService testService = new TestService();
            Method testMethod = TestService.class.getMethod("process", IMessageObject.class);
            Object[] args = new Object[]{inputMessage};

            when(methodMetadata.getParameters()).thenReturn(parameters);
            when(argumentResolver.resolveArguments(parameters, serviceContext, inputMessage)).thenReturn(args);

            executor = new DefaultServiceComponentExecutor(
                    methodMetadata,
                    argumentResolver,
                    null, // null interceptor
                    testService,
                    testMethod);

            // When
            IMessageObject result = executor.execute(serviceContext, inputMessage);

            // Then
            assertNotNull(result);
        }

        @Test
        @DisplayName("Should propagate target exception from InvocationTargetException")
        void shouldPropagateTargetException() throws Exception {
            // Given
            TestService testService = new TestService();
            Method testMethod = TestService.class.getMethod("throwException");
            Object[] args = new Object[]{};

            when(methodMetadata.getParameters()).thenReturn(parameters);
            when(argumentResolver.resolveArguments(parameters, serviceContext, inputMessage)).thenReturn(args);

            executor = new DefaultServiceComponentExecutor(
                    methodMetadata,
                    argumentResolver,
                    null,
                    testService,
                    testMethod);

            // When/Then
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> executor.execute(serviceContext, inputMessage));
            assertEquals("Test exception", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle method with no parameters")
        void shouldHandleMethodWithNoParameters() throws Throwable {
            // Given
            TestService testService = new TestService();
            Method testMethod = TestService.class.getMethod("noParamMethod");
            Object[] args = new Object[]{};

            when(methodMetadata.getParameters()).thenReturn(parameters);
            when(argumentResolver.resolveArguments(parameters, serviceContext, inputMessage)).thenReturn(args);

            executor = new DefaultServiceComponentExecutor(
                    methodMetadata,
                    argumentResolver,
                    null,
                    testService,
                    testMethod);

            // When
            IMessageObject result = executor.execute(serviceContext, inputMessage);

            // Then
            assertNull(result);
        }
    }

    /**
     * Test service class for testing purposes
     */
    public static class TestService {

        public IMessageObject process(IMessageObject input) {
            return mock(IMessageObject.class);
        }

        public void throwException() {
            throw new RuntimeException("Test exception");
        }

        public IMessageObject noParamMethod() {
            return null;
        }
    }
}
