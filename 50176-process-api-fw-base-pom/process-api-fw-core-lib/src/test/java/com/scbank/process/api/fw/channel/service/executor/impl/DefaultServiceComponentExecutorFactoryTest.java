package com.scbank.process.api.fw.channel.service.executor.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.service.argument.ServiceMethodArgumentResolverComposite;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutor;
import com.scbank.process.api.fw.channel.service.interceptor.ServiceInterceptorComposite;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * DefaultServiceComponentExecutorFactory Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultServiceComponentExecutorFactoryTest {

    @Mock
    private ServiceMethodArgumentResolverComposite argumentResolverComposite;

    @Mock
    private ServiceInterceptorComposite serviceInterceptorComposite;

    @Mock
    private ServiceMethodMetadata serviceMethodMetadata;

    private DefaultServiceComponentExecutorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DefaultServiceComponentExecutorFactory(argumentResolverComposite, serviceInterceptorComposite);
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {

        @Test
        @DisplayName("Should create executor from service method metadata")
        void shouldCreateExecutorFromServiceMethodMetadata() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestServiceComponent testBean = new TestServiceComponent();
                Method testMethod = TestServiceComponent.class.getMethod("execute", IMessageObject.class);

                when(serviceMethodMetadata.getDeclaringClass()).thenReturn((Class) TestServiceComponent.class);
                when(serviceMethodMetadata.getMethod()).thenReturn(testMethod);
                runtimeContextMock.when(() -> RuntimeContext.getBean(TestServiceComponent.class))
                        .thenReturn(testBean);

                // When
                IServiceComponentExecutor executor = factory.create(serviceMethodMetadata);

                // Then
                assertNotNull(executor);
                assertTrue(executor instanceof DefaultServiceComponentExecutor);
            }
        }

        @Test
        @DisplayName("Should throw exception when bean not found")
        void shouldThrowExceptionWhenBeanNotFound() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                Method testMethod = TestServiceComponent.class.getMethod("execute", IMessageObject.class);

                when(serviceMethodMetadata.getDeclaringClass()).thenReturn((Class) TestServiceComponent.class);
                when(serviceMethodMetadata.getMethod()).thenReturn(testMethod);
                runtimeContextMock.when(() -> RuntimeContext.getBean(TestServiceComponent.class))
                        .thenThrow(new RuntimeException("Bean not found"));

                // When/Then
                assertThrows(RuntimeException.class, () -> factory.create(serviceMethodMetadata));
            }
        }

        @Test
        @DisplayName("Should handle method with multiple parameters")
        void shouldHandleMethodWithMultipleParameters() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestServiceComponent testBean = new TestServiceComponent();
                Method testMethod = TestServiceComponent.class.getMethod("multiParamMethod", String.class, Integer.class);

                when(serviceMethodMetadata.getDeclaringClass()).thenReturn((Class) TestServiceComponent.class);
                when(serviceMethodMetadata.getMethod()).thenReturn(testMethod);
                runtimeContextMock.when(() -> RuntimeContext.getBean(TestServiceComponent.class))
                        .thenReturn(testBean);

                // When
                IServiceComponentExecutor executor = factory.create(serviceMethodMetadata);

                // Then
                assertNotNull(executor);
            }
        }
    }

    /**
     * Test service component class for testing purposes
     */
    public static class TestServiceComponent {

        public IMessageObject execute(IMessageObject input) {
            return mock(IMessageObject.class);
        }

        public void multiParamMethod(String param1, Integer param2) {
            // Test method with multiple parameters
        }
    }
}
