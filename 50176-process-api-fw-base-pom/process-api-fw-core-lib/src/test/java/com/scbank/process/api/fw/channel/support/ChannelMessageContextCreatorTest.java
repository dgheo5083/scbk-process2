package com.scbank.process.api.fw.channel.support;

import static org.junit.jupiter.api.Assertions.*;
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

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.core.encrypt.IEncryptProcessorRegistrar;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

/**
 * ChannelMessageContextCreator Test Class
 */
@ExtendWith(MockitoExtension.class)
class ChannelMessageContextCreatorTest {

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private MessageContextFactory messageContextFactory;

    @Mock
    private MessageContext messageContext;

    @Mock
    private IMessageFieldConverterRegistry fieldConverterRegistry;

    @Mock
    private IEncryptProcessorRegistrar encryptProcessorRegistrar;

    private ChannelMessageContextCreator creator;

    @BeforeEach
    void setUp() {
        creator = new ChannelMessageContextCreator(channelProperties, messageContextFactory);
    }

    @Nested
    @DisplayName("create tests for JSON format")
    class CreateJsonFormatTests {

        @Test
        @DisplayName("Should create message context for JSON format")
        void shouldCreateMessageContextForJson() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                String encoding = "UTF-8";
                when(channelProperties.getMessageFormatOptions()).thenReturn(null);
                when(messageContextFactory.create(eq(MessageFormat.JSON), eq(encoding), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(encryptProcessorRegistrar);

                // When
                MessageContext result = creator.create(MessageFormat.JSON, encoding);

                // Then
                assertNotNull(result);
                verify(messageContext).setMessageFieldConverterRegistry(fieldConverterRegistry);
                verify(messageContext).setEncryptProcessorRegistrar(encryptProcessorRegistrar);
            }
        }

        @Test
        @DisplayName("Should use default options when no custom options provided")
        void shouldUseDefaultOptionsWhenNoCustomOptions() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                when(channelProperties.getMessageFormatOptions()).thenReturn(null);
                when(messageContextFactory.create(eq(MessageFormat.JSON), eq("UTF-8"), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(null);

                // When
                MessageContext result = creator.create(MessageFormat.JSON, "UTF-8");

                // Then
                assertNotNull(result);
                verify(messageContextFactory).create(eq(MessageFormat.JSON), eq("UTF-8"), any(), any());
            }
        }
    }

    @Nested
    @DisplayName("create tests for XML format")
    class CreateXmlFormatTests {

        @Test
        @DisplayName("Should create message context for XML format")
        void shouldCreateMessageContextForXml() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                when(channelProperties.getMessageFormatOptions()).thenReturn(null);
                when(messageContextFactory.create(eq(MessageFormat.XML), eq("UTF-8"), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(encryptProcessorRegistrar);

                // When
                MessageContext result = creator.create(MessageFormat.XML, "UTF-8");

                // Then
                assertNotNull(result);
                verify(messageContext).setMessageFieldConverterRegistry(fieldConverterRegistry);
            }
        }
    }

    @Nested
    @DisplayName("create tests for FIXEDLENGTH format")
    class CreateFixedLengthFormatTests {

        @Test
        @DisplayName("Should create message context for FIXEDLENGTH format")
        void shouldCreateMessageContextForFixedLength() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                when(channelProperties.getMessageFormatOptions()).thenReturn(null);
                when(messageContextFactory.create(eq(MessageFormat.FIXEDLENGTH), eq("EUC-KR"), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("fixedLengthMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(encryptProcessorRegistrar);

                // When
                MessageContext result = creator.create(MessageFormat.FIXEDLENGTH, "EUC-KR");

                // Then
                assertNotNull(result);
                verify(messageContext).setMessageFieldConverterRegistry(fieldConverterRegistry);
            }
        }
    }

    @Nested
    @DisplayName("Custom options merge tests")
    class CustomOptionsMergeTests {

        @Test
        @DisplayName("Should merge custom serialization options")
        void shouldMergeCustomSerializationOptions() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                Map<MessageFormat, MessageFormatOptionConfig> formatOptions = new HashMap<>();
                MessageFormatOptionConfig optionConfig = new MessageFormatOptionConfig();
                Map<String, Object> serializationOpts = Map.of("FIELD_TRIM", true);
                optionConfig.setSerialization(serializationOpts);
                formatOptions.put(MessageFormat.JSON, optionConfig);

                when(channelProperties.getMessageFormatOptions()).thenReturn(formatOptions);
                when(messageContextFactory.create(eq(MessageFormat.JSON), eq("UTF-8"), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(encryptProcessorRegistrar);

                // When
                MessageContext result = creator.create(MessageFormat.JSON, "UTF-8");

                // Then
                assertNotNull(result);
            }
        }

        @Test
        @DisplayName("Should merge custom deserialization options")
        void shouldMergeCustomDeserializationOptions() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                Map<MessageFormat, MessageFormatOptionConfig> formatOptions = new HashMap<>();
                MessageFormatOptionConfig optionConfig = new MessageFormatOptionConfig();
                Map<String, Object> deserializationOpts = Map.of("FIELD_TRIM", true);
                optionConfig.setDeserialization(deserializationOpts);
                formatOptions.put(MessageFormat.JSON, optionConfig);

                when(channelProperties.getMessageFormatOptions()).thenReturn(formatOptions);
                when(messageContextFactory.create(eq(MessageFormat.JSON), eq("UTF-8"), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenReturn(encryptProcessorRegistrar);

                // When
                MessageContext result = creator.create(MessageFormat.JSON, "UTF-8");

                // Then
                assertNotNull(result);
            }
        }
    }

    @Nested
    @DisplayName("Error handling tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle missing encrypt processor gracefully")
        void shouldHandleMissingEncryptProcessorGracefully() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                when(channelProperties.getMessageFormatOptions()).thenReturn(null);
                when(messageContextFactory.create(eq(MessageFormat.JSON), eq("UTF-8"), any(), any()))
                        .thenReturn(messageContext);
                runtimeContextMock.when(() -> RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                                IMessageFieldConverterRegistry.class))
                        .thenReturn(fieldConverterRegistry);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IEncryptProcessorRegistrar.class))
                        .thenThrow(new RuntimeException("Bean not found"));

                // When
                MessageContext result = creator.create(MessageFormat.JSON, "UTF-8");

                // Then
                assertNotNull(result);
                verify(messageContext).setEncryptProcessorRegistrar(null);
            }
        }
    }
}
