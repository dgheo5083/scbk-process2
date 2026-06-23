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

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.message.IMessageObject;

/**
 * InputObjectArgumentResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
class InputObjectArgumentResolverTest {

    @Mock
    private ParameterMetadata parameterMetadata;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IMessageObject inputMessage;

    private InputObjectArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new InputObjectArgumentResolver();
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true when parameter is body")
        void shouldReturnTrueWhenParameterIsBody() {
            when(parameterMetadata.isBody()).thenReturn(true);

            boolean result = resolver.supports(parameterMetadata);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when parameter is not body")
        void shouldReturnFalseWhenParameterIsNotBody() {
            when(parameterMetadata.isBody()).thenReturn(false);

            boolean result = resolver.supports(parameterMetadata);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("resolveArgument tests")
    class ResolveArgumentTests {

        @Test
        @DisplayName("Should return input object as-is")
        void shouldReturnInputObjectAsIs() {
            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, inputMessage);

            assertSame(inputMessage, result);
        }

        @Test
        @DisplayName("Should return null when input is null")
        void shouldReturnNullWhenInputIsNull() {
            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertNull(result);
        }

        @Test
        @DisplayName("Should handle any object type as input")
        void shouldHandleAnyObjectTypeAsInput() {
            String stringInput = "test string input";

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, stringInput);

            assertSame(stringInput, result);
        }
    }
}
