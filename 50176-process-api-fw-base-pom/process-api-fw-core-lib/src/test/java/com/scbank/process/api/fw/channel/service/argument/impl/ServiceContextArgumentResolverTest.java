package com.scbank.process.api.fw.channel.service.argument.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.context.DefaultServiceContext;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * ServiceContextArgumentResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceContextArgumentResolverTest {

    @Mock
    private ParameterMetadata parameterMetadata;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IMessageObject inputMessage;

    private ServiceContextArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new ServiceContextArgumentResolver();
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true when parameter type is IServiceContext")
        void shouldReturnTrueWhenParameterTypeIsIServiceContext() {
            when(parameterMetadata.getType()).thenReturn((Class) IServiceContext.class);

            boolean result = resolver.supports(parameterMetadata);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when parameter type is subclass of IServiceContext")
        void shouldReturnTrueWhenParameterTypeIsSubclassOfIServiceContext() {
            when(parameterMetadata.getType()).thenReturn((Class) DefaultServiceContext.class);

            boolean result = resolver.supports(parameterMetadata);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when parameter type is not IServiceContext")
        void shouldReturnFalseWhenParameterTypeIsNotIServiceContext() {
            when(parameterMetadata.getType()).thenReturn((Class) String.class);

            boolean result = resolver.supports(parameterMetadata);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for Object type")
        void shouldReturnFalseForObjectType() {
            when(parameterMetadata.getType()).thenReturn((Class) Object.class);

            boolean result = resolver.supports(parameterMetadata);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("resolveArgument tests")
    class ResolveArgumentTests {

        @Test
        @DisplayName("Should return the service context as-is")
        void shouldReturnServiceContextAsIs() {
            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, inputMessage);

            assertSame(serviceContext, result);
        }

        @Test
        @DisplayName("Should return context regardless of input value")
        void shouldReturnContextRegardlessOfInputValue() {
            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertSame(serviceContext, result);
        }

        @Test
        @DisplayName("Should return null when context is null")
        void shouldReturnNullWhenContextIsNull() {
            Object result = resolver.resolveArgument(parameterMetadata, null, inputMessage);

            assertNull(result);
        }
    }
}
