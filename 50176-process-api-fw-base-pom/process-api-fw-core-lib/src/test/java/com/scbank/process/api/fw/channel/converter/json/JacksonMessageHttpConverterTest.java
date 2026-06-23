package com.scbank.process.api.fw.channel.converter.json;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

/**
 * JacksonMessageHttpConverter Test Class
 */
@ExtendWith(MockitoExtension.class)
class JacksonMessageHttpConverterTest {

    @Mock
    private JacksonMessageMapper jacksonMessageMapper;

    @Mock
    private HttpInputMessage inputMessage;

    @Mock
    private HttpOutputMessage outputMessage;

    @Mock
    private HttpHeaders httpHeaders;

    @Mock
    private ChannelMessageContextCreator messageContextCreator;

    @Mock
    private MessageContext messageContext;

    @Mock
    private IMessageObject messageObject;

    @Mock
    private IIntegrationMessageMetadataRegistrar metadataRegistrar;

    @Mock
    private IIntegrationMessageMetadata integrationMetadata;

    @Mock
    private ObjectMapper objectMapper;

    private JacksonMessageHttpConverter<IMessageObject> converter;

    @BeforeEach
    void setUp() {
        try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
            runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
            converter = new JacksonMessageHttpConverter<>(jacksonMessageMapper);
        }
    }

    @AfterEach
    void tearDown() {
        MessageContextHolder.clear();
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should support IMessageObject class")
        void shouldSupportIMessageObjectClass() {
            boolean result = converter.supports(IMessageObject.class);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should support subclass of IMessageObject")
        void shouldSupportSubclassOfIMessageObject() {
            boolean result = converter.supports(TestMessageObject.class);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should not support non-IMessageObject class")
        void shouldNotSupportNonIMessageObjectClass() {
            boolean result = converter.supports(String.class);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should not support Object class")
        void shouldNotSupportObjectClass() {
            boolean result = converter.supports(Object.class);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("readInternal tests")
    class ReadInternalTests {

        @Test
        @DisplayName("Should read JSON and deserialize to object")
        void shouldReadJsonAndDeserializeToObject() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] jsonBytes = "{\"name\":\"test\"}".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_JSON);
                when(httpHeaders.getContentLength()).thenReturn((long) jsonBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(jacksonMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenReturn(new TestMessageObject());

                // When
                IMessageObject result = converter.readInternal(TestMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotReadableException on deserialization error")
        void shouldThrowExceptionOnDeserializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] jsonBytes = "invalid".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) jsonBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(jacksonMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Parse error"));

                // When/Then
                assertThrows(HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestMessageObject.class, inputMessage));
            }
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create converter with JacksonMessageMapper")
        void shouldCreateConverterWithJacksonMessageMapper() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                JacksonMessageHttpConverter<IMessageObject> newConverter =
                        new JacksonMessageHttpConverter<>(jacksonMessageMapper);

                assertNotNull(newConverter);
                assertTrue(newConverter.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON));
            }
        }
    }

    @Nested
    @DisplayName("writeInternal tests")
    class WriteInternalTests {

        @Test
        @DisplayName("Should serialize object with IMessageObject child fields")
        void shouldSerializeObjectWithMessageObjectChildFields() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                TestChildMessageObject childObject = new TestChildMessageObject();
                childObject.setChildName("childValue");
                parentObject.setChild(childObject);
                parentObject.setSimpleField("simpleValue");

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] serializedChild = "{\"childName\":\"childValue\"}".getBytes(StandardCharsets.UTF_8);
                ObjectMapper realObjectMapper = new ObjectMapper();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_JSON);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(realObjectMapper);
                when(metadataRegistrar.getMetadata(TestChildMessageObject.class)).thenReturn(integrationMetadata);
                when(jacksonMessageMapper.serialize(eq(childObject), eq(integrationMetadata), any()))
                        .thenReturn(serializedChild);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(jacksonMessageMapper).serialize(eq(childObject), eq(integrationMetadata), eq(messageContext));
                verify(httpHeaders).setContentLength(anyLong());
                assertTrue(outputStream.size() > 0);
            }
        }

        @Test
        @DisplayName("Should skip non-IMessageObject fields")
        void shouldSkipNonMessageObjectFields() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                parentObject.setSimpleField("simpleValue");
                // No IMessageObject child set

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectMapper realObjectMapper = new ObjectMapper();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(realObjectMapper);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then - serialize should not be called since no IMessageObject child
                verify(jacksonMessageMapper, never()).serialize(any(IMessageObject.class), any(IIntegrationMessageMetadata.class), any());
            }
        }

        @Test
        @DisplayName("Should skip IMessageObject fields with null metadata")
        void shouldSkipFieldsWithNullMetadata() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                TestChildMessageObject childObject = new TestChildMessageObject();
                childObject.setChildName("childValue");
                parentObject.setChild(childObject);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectMapper realObjectMapper = new ObjectMapper();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_JSON);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(realObjectMapper);
                when(metadataRegistrar.getMetadata(TestChildMessageObject.class)).thenReturn(null);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then - serialize should not be called since metadata is null
                verify(jacksonMessageMapper, never()).serialize(any(IMessageObject.class), any(IIntegrationMessageMetadata.class), any());
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotWritableException on serialization error")
        void shouldThrowExceptionOnSerializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                TestChildMessageObject childObject = new TestChildMessageObject();
                parentObject.setChild(childObject);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(new ObjectMapper());
                when(metadataRegistrar.getMetadata(TestChildMessageObject.class)).thenReturn(integrationMetadata);
                when(jacksonMessageMapper.serialize(any(IMessageObject.class), any(IIntegrationMessageMetadata.class), any()))
                        .thenThrow(new RuntimeException("Serialization error"));

                // When/Then
                HttpMessageNotWritableException exception = assertThrows(
                        HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(parentObject, outputMessage)
                );

                assertTrue(exception.getMessage().contains("JSON 데이터 직렬화 실패"));
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful write")
        void shouldClearMessageContextHolderAfterSuccessfulWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectMapper realObjectMapper = new ObjectMapper();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(realObjectMapper);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on exception during write")
        void shouldClearMessageContextHolderOnWriteException() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                TestChildMessageObject childObject = new TestChildMessageObject();
                parentObject.setChild(childObject);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(new ObjectMapper());
                when(metadataRegistrar.getMetadata(TestChildMessageObject.class)).thenReturn(integrationMetadata);
                when(jacksonMessageMapper.serialize(any(IMessageObject.class), any(IIntegrationMessageMetadata.class), any()))
                        .thenThrow(new RuntimeException("Error"));

                // When
                assertThrows(HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(parentObject, outputMessage));

                // Then - MessageContextHolder should be cleared even after exception
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should use charset from output message content type")
        void shouldUseCharsetFromContentType() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectMapper realObjectMapper = new ObjectMapper();
                MediaType contentTypeWithCharset = new MediaType("application", "json", StandardCharsets.UTF_16);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), eq("UTF-16"))).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(contentTypeWithCharset);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(realObjectMapper);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(messageContextCreator).create(any(), eq("UTF-16"));
            }
        }

        @Test
        @DisplayName("Should handle IOException during body write")
        void shouldHandleIOExceptionDuringBodyWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                ObjectMapper realObjectMapper = new ObjectMapper();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(jacksonMessageMapper.getObjectMapper()).thenReturn(realObjectMapper);
                when(outputMessage.getBody()).thenThrow(new IOException("IO Error"));

                // When/Then
                HttpMessageNotWritableException exception = assertThrows(
                        HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(parentObject, outputMessage)
                );

                assertTrue(exception.getMessage().contains("JSON 데이터 직렬화 실패"));
            }
        }
    }

    /**
     * Test IMessageObject implementation
     */
    public static class TestMessageObject implements IMessageObject {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Test parent IMessageObject with child IMessageObject field
     */
    public static class TestParentMessageObject implements IMessageObject {
        private String simpleField;
        private TestChildMessageObject child;

        public String getSimpleField() {
            return simpleField;
        }

        public void setSimpleField(String simpleField) {
            this.simpleField = simpleField;
        }

        public TestChildMessageObject getChild() {
            return child;
        }

        public void setChild(TestChildMessageObject child) {
            this.child = child;
        }
    }

    /**
     * Test child IMessageObject implementation
     */
    public static class TestChildMessageObject implements IMessageObject {
        private String childName;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }
}
