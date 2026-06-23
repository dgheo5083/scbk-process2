package com.scbank.process.api.fw.integration.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.core.encrypt.IEncryptProcessorRegistrar;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

/**
 * IntegrationMessageContextCreator Test Class
 */
@ExtendWith(MockitoExtension.class)
class IntegrationMessageContextCreatorTest {

    @Mock
    private MessageContextFactory mockContextFactory;

    @Mock
    private MessageContext mockMessageContext;

    @Mock
    private IntegrationSystemConfig mockSystemConfig;

    @Mock
    private MessageFormatOptionConfig mockFormatOptionConfig;

    @Mock
    private IMessageFieldConverterRegistry mockFieldConverterRegistry;

    @Mock
    private IEncryptProcessorRegistrar mockEncryptProcessorRegistrar;

    private IntegrationMessageContextCreator creator;

    @BeforeEach
    void setUp() {
        creator = new IntegrationMessageContextCreator(mockContextFactory);
    }

    @Nested
    @DisplayName("create(MessageFormat, String) tests")
    class CreateWithFormatAndEncodingTests {

        @Test
        @DisplayName("Should create MessageContext for XML format")
        void shouldCreateMessageContextForXmlFormat() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("jacksonMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                when(mockContextFactory.create(eq(MessageFormat.XML), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(MessageFormat.XML, "UTF-8");

                assertNotNull(result);
                verify(mockMessageContext).setMessageFieldConverterRegistry(mockFieldConverterRegistry);
                verify(mockMessageContext).setEncryptProcessorRegistrar(mockEncryptProcessorRegistrar);
            }
        }

        @Test
        @DisplayName("Should create MessageContext for FIXEDLENGTH format")
        void shouldCreateMessageContextForFixedLengthFormat() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("fixedLengthMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                when(mockContextFactory.create(eq(MessageFormat.FIXEDLENGTH), eq("EUC-KR"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(MessageFormat.FIXEDLENGTH, "EUC-KR");

                assertNotNull(result);
                verify(mockMessageContext).setMessageFieldConverterRegistry(mockFieldConverterRegistry);
            }
        }

        @Test
        @DisplayName("Should create MessageContext for JSON format")
        void shouldCreateMessageContextForJsonFormat() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("jacksonMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                when(mockContextFactory.create(eq(MessageFormat.JSON), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(MessageFormat.JSON, "UTF-8");

                assertNotNull(result);
            }
        }

        @Test
        @DisplayName("Should handle missing encrypt processor registrar")
        void shouldHandleMissingEncryptProcessorRegistrar() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("jacksonMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenThrow(new RuntimeException("Bean not found"));

                when(mockContextFactory.create(eq(MessageFormat.XML), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(MessageFormat.XML, "UTF-8");

                assertNotNull(result);
                verify(mockMessageContext).setEncryptProcessorRegistrar(null);
            }
        }
    }

    @Nested
    @DisplayName("create(IntegrationSystemConfig) tests")
    class CreateWithSystemConfigTests {

        @Test
        @DisplayName("Should create MessageContext from system config without format options")
        void shouldCreateMessageContextFromSystemConfigWithoutFormatOptions() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("jacksonMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);
                when(mockSystemConfig.charset()).thenReturn("UTF-8");
                when(mockSystemConfig.messageFormatOptions()).thenReturn(null);

                when(mockContextFactory.create(eq(MessageFormat.XML), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(mockSystemConfig);

                assertNotNull(result);
                verify(mockContextFactory).create(eq(MessageFormat.XML), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class));
            }
        }

        @Test
        @DisplayName("Should merge serialization options from system config")
        void shouldMergeSerializationOptionsFromSystemConfig() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("jacksonMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                Map<String, Object> serOptions = new HashMap<>();
                serOptions.put("customOption", "value");

                when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);
                when(mockSystemConfig.charset()).thenReturn("UTF-8");
                when(mockSystemConfig.messageFormatOptions()).thenReturn(mockFormatOptionConfig);
                when(mockFormatOptionConfig.getSerialization()).thenReturn(serOptions);
                when(mockFormatOptionConfig.getDeserialization()).thenReturn(null);

                when(mockContextFactory.create(eq(MessageFormat.XML), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(mockSystemConfig);

                assertNotNull(result);
            }
        }

        @Test
        @DisplayName("Should merge deserialization options from system config")
        void shouldMergeDeserializationOptionsFromSystemConfig() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("jacksonMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                Map<String, Object> deserOptions = new HashMap<>();
                deserOptions.put("customDeserOption", "value");

                when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);
                when(mockSystemConfig.charset()).thenReturn("UTF-8");
                when(mockSystemConfig.messageFormatOptions()).thenReturn(mockFormatOptionConfig);
                when(mockFormatOptionConfig.getSerialization()).thenReturn(null);
                when(mockFormatOptionConfig.getDeserialization()).thenReturn(deserOptions);

                when(mockContextFactory.create(eq(MessageFormat.XML), eq("UTF-8"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(mockSystemConfig);

                assertNotNull(result);
            }
        }

        @Test
        @DisplayName("Should create for FIXEDLENGTH format from system config")
        void shouldCreateForFixedLengthFormatFromSystemConfig() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(eq("fixedLengthMessageFieldConverterRegistry"),
                                eq(IMessageFieldConverterRegistry.class)))
                        .thenReturn(mockFieldConverterRegistry);
                mockedRuntime.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(mockEncryptProcessorRegistrar);

                when(mockSystemConfig.format()).thenReturn(MessageFormat.FIXEDLENGTH);
                when(mockSystemConfig.charset()).thenReturn("EUC-KR");
                when(mockSystemConfig.messageFormatOptions()).thenReturn(null);

                when(mockContextFactory.create(eq(MessageFormat.FIXEDLENGTH), eq("EUC-KR"),
                        any(SerializationOptions.class), any(DeserializationOptions.class)))
                        .thenReturn(mockMessageContext);

                MessageContext result = creator.create(mockSystemConfig);

                assertNotNull(result);
            }
        }
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with message context factory")
        void shouldCreateWithMessageContextFactory() {
            IntegrationMessageContextCreator newCreator =
                    new IntegrationMessageContextCreator(mockContextFactory);

            assertNotNull(newCreator);
        }
    }
}
