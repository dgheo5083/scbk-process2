package com.scbank.process.api.fw.channel.converter.fixedlength;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

/**
 * FixedLengthMessageHttpConverter Test Class
 * Comprehensive tests for 100% JaCoCo coverage
 */
@ExtendWith(MockitoExtension.class)
class FixedLengthMessageHttpConverterTest {

    @Mock
    private FixedLengthMessageMapper fixedLengthMessageMapper;

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
    private IIntegrationMessageMetadataRegistrar metadataRegistrar;

    @Mock
    private IIntegrationMessageMetadata integrationMetadata;

    private FixedLengthMessageHttpConverter<IMessageObject> converter;

    @BeforeEach
    void setUp() {
        try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
            runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
            converter = new FixedLengthMessageHttpConverter<>(fixedLengthMessageMapper);
        }
    }

    @AfterEach
    void tearDown() {
        MessageContextHolder.clear();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create converter with FixedLengthMessageMapper")
        void shouldCreateConverterWithFixedLengthMessageMapper() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                FixedLengthMessageHttpConverter<IMessageObject> newConverter =
                        new FixedLengthMessageHttpConverter<>(fixedLengthMessageMapper);

                assertNotNull(newConverter);
                assertTrue(newConverter.getSupportedMediaTypes().contains(MediaType.TEXT_PLAIN));
            }
        }

        @Test
        @DisplayName("Should create converter with custom encoding")
        void shouldCreateConverterWithCustomEncoding() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("EUC-KR");

                FixedLengthMessageHttpConverter<IMessageObject> newConverter =
                        new FixedLengthMessageHttpConverter<>(fixedLengthMessageMapper);

                assertNotNull(newConverter);
            }
        }
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
        @DisplayName("Should support nested IMessageObject implementation")
        void shouldSupportNestedIMessageObjectImplementation() {
            boolean result = converter.supports(TestParentMessageObject.class);

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

        @Test
        @DisplayName("Should not support primitive type wrapper")
        void shouldNotSupportPrimitiveTypeWrapper() {
            boolean result = converter.supports(Integer.class);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("readInternal tests")
    class ReadInternalTests {

        @Test
        @DisplayName("Should read fixed length message and deserialize to object")
        void shouldReadFixedLengthAndDeserializeToObject() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] fixedLengthBytes = "TEST_DATA_001".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fixedLengthBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.TEXT_PLAIN);
                when(httpHeaders.getContentLength()).thenReturn((long) fixedLengthBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(fixedLengthMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenReturn(new TestMessageObject());

                // When
                IMessageObject result = converter.readInternal(TestMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
                verify(fixedLengthMessageMapper).deserialize(any(byte[].class), eq(TestMessageObject.class), any());
            }
        }

        @Test
        @DisplayName("Should read with charset from content type")
        void shouldReadWithCharsetFromContentType() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] fixedLengthBytes = "한글데이터".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fixedLengthBytes);

                MediaType mediaTypeWithCharset = new MediaType("text", "plain", StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(mediaTypeWithCharset);
                when(httpHeaders.getContentLength()).thenReturn((long) fixedLengthBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(fixedLengthMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenReturn(new TestMessageObject());

                // When
                IMessageObject result = converter.readInternal(TestMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
            }
        }

        @Test
        @DisplayName("Should read with default charset when content type has no charset")
        void shouldReadWithDefaultCharsetWhenNoCharset() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] fixedLengthBytes = "DATA".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fixedLengthBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) fixedLengthBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(fixedLengthMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
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
                byte[] fixedLengthBytes = "INVALID".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fixedLengthBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.TEXT_PLAIN);
                when(httpHeaders.getContentLength()).thenReturn((long) fixedLengthBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(fixedLengthMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Deserialization error"));

                // When/Then
                assertThrows(HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestMessageObject.class, inputMessage));
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful read")
        void shouldClearMessageContextHolderAfterSuccessfulRead() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] fixedLengthBytes = "DATA".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fixedLengthBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) fixedLengthBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(fixedLengthMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenReturn(new TestMessageObject());

                // When
                converter.readInternal(TestMessageObject.class, inputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on error")
        void shouldClearMessageContextHolderOnError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] fixedLengthBytes = "DATA".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fixedLengthBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) fixedLengthBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(fixedLengthMessageMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Error"));

                // When
                try {
                    converter.readInternal(TestMessageObject.class, inputMessage);
                } catch (Exception e) {
                    // Expected
                }

                // Then - MessageContextHolder should be cleared even on error
                assertNull(MessageContextHolder.get());
            }
        }
    }

    @Nested
    @DisplayName("writeInternal tests")
    class WriteInternalTests {

        @Test
        @DisplayName("Should write fixed length message with child message objects")
        void shouldWriteFixedLengthWithChildMessageObjects() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestParentMessageObject parentObject = new TestParentMessageObject();
                parentObject.setChildMessage(new TestMessageObject());

                byte[] serializedBytes = "SERIALIZED_DATA".getBytes(StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMetadata);
                when(fixedLengthMessageMapper.serialize(any(IMessageObject.class), eq(integrationMetadata), any()))
                        .thenReturn(serializedBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(httpHeaders).setContentLength(serializedBytes.length);
                assertArrayEquals(serializedBytes, bodyOutputStream.toByteArray());
            }
        }

        @Test
        @DisplayName("Should skip non-IMessageObject fields during write")
        void shouldSkipNonIMessageObjectFieldsDuringWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestParentWithPrimitiveField parentObject = new TestParentWithPrimitiveField();
                parentObject.setName("test");
                parentObject.setChildMessage(new TestMessageObject());

                byte[] serializedBytes = "DATA".getBytes(StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMetadata);
                when(fixedLengthMessageMapper.serialize(any(IMessageObject.class), eq(integrationMetadata), any()))
                        .thenReturn(serializedBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(fixedLengthMessageMapper, times(1)).serialize(any(IMessageObject.class), any(), any());
            }
        }

        @Test
        @DisplayName("Should skip fields without metadata")
        void shouldSkipFieldsWithoutMetadata() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestParentMessageObject parentObject = new TestParentMessageObject();
                parentObject.setChildMessage(new TestMessageObject());

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(null);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(fixedLengthMessageMapper, never()).serialize(any(IMessageObject.class), any(), any());
                assertEquals(0, bodyOutputStream.toByteArray().length);
            }
        }

        @Test
        @DisplayName("Should write with charset from content type")
        void shouldWriteWithCharsetFromContentType() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestParentMessageObject parentObject = new TestParentMessageObject();
                parentObject.setChildMessage(new TestMessageObject());

                byte[] serializedBytes = "DATA".getBytes(StandardCharsets.UTF_8);
                MediaType mediaTypeWithCharset = new MediaType("text", "plain", StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(mediaTypeWithCharset);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMetadata);
                when(fixedLengthMessageMapper.serialize(any(IMessageObject.class), eq(integrationMetadata), any()))
                        .thenReturn(serializedBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                assertArrayEquals(serializedBytes, bodyOutputStream.toByteArray());
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotWritableException on serialization error")
        void shouldThrowExceptionOnSerializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestParentMessageObject parentObject = new TestParentMessageObject();
                parentObject.setChildMessage(new TestMessageObject());

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMetadata);
                when(fixedLengthMessageMapper.serialize(any(IMessageObject.class), eq(integrationMetadata), any()))
                        .thenThrow(new RuntimeException("Serialization error"));

                // When/Then
                assertThrows(HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(parentObject, outputMessage));
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful write")
        void shouldClearMessageContextHolderAfterSuccessfulWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestParentMessageObject parentObject = new TestParentMessageObject();
                parentObject.setChildMessage(new TestMessageObject());

                byte[] serializedBytes = "DATA".getBytes(StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMetadata);
                when(fixedLengthMessageMapper.serialize(any(IMessageObject.class), eq(integrationMetadata), any()))
                        .thenReturn(serializedBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should write empty content when no child message objects")
        void shouldWriteEmptyContentWhenNoChildMessageObjects() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestMessageObject simpleObject = new TestMessageObject();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);

                // When
                converter.writeInternal(simpleObject, outputMessage);

                // Then
                verify(httpHeaders).setContentLength(0);
                assertEquals(0, bodyOutputStream.toByteArray().length);
            }
        }

        @Test
        @DisplayName("Should write multiple child message objects")
        void shouldWriteMultipleChildMessageObjects() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
                TestParentWithMultipleChildren parentObject = new TestParentWithMultipleChildren();
                parentObject.setChild1(new TestMessageObject());
                parentObject.setChild2(new TestMessageObject2());

                byte[] serializedBytes1 = "DATA1".getBytes(StandardCharsets.UTF_8);
                byte[] serializedBytes2 = "DATA2".getBytes(StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(bodyOutputStream);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMetadata);
                when(metadataRegistrar.getMetadata(TestMessageObject2.class)).thenReturn(integrationMetadata);
                when(fixedLengthMessageMapper.serialize(any(TestMessageObject.class), eq(integrationMetadata), any()))
                        .thenReturn(serializedBytes1);
                when(fixedLengthMessageMapper.serialize(any(TestMessageObject2.class), eq(integrationMetadata), any()))
                        .thenReturn(serializedBytes2);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(httpHeaders).setContentLength(serializedBytes1.length + serializedBytes2.length);
            }
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should support TEXT_PLAIN media type")
        void shouldSupportTextPlainMediaType() {
            List<MediaType> supportedMediaTypes = converter.getSupportedMediaTypes();

            assertTrue(supportedMediaTypes.contains(MediaType.TEXT_PLAIN));
        }

        @Test
        @DisplayName("Should be able to read and write")
        void shouldCanReadAndWrite() {
            assertTrue(converter.canRead(TestMessageObject.class, MediaType.TEXT_PLAIN));
            assertTrue(converter.canWrite(TestMessageObject.class, MediaType.TEXT_PLAIN));
        }
    }

    /**
     * Test IMessageObject implementation
     */
    public static class TestMessageObject implements IMessageObject {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    /**
     * Another test IMessageObject implementation
     */
    public static class TestMessageObject2 implements IMessageObject {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * Parent message object with child
     */
    public static class TestParentMessageObject implements IMessageObject {
        private TestMessageObject childMessage;

        public TestMessageObject getChildMessage() {
            return childMessage;
        }

        public void setChildMessage(TestMessageObject childMessage) {
            this.childMessage = childMessage;
        }
    }

    /**
     * Parent message object with primitive field and child
     */
    public static class TestParentWithPrimitiveField implements IMessageObject {
        private String name;
        private TestMessageObject childMessage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TestMessageObject getChildMessage() {
            return childMessage;
        }

        public void setChildMessage(TestMessageObject childMessage) {
            this.childMessage = childMessage;
        }
    }

    /**
     * Parent message object with multiple children
     */
    public static class TestParentWithMultipleChildren implements IMessageObject {
        private TestMessageObject child1;
        private TestMessageObject2 child2;

        public TestMessageObject getChild1() {
            return child1;
        }

        public void setChild1(TestMessageObject child1) {
            this.child1 = child1;
        }

        public TestMessageObject2 getChild2() {
            return child2;
        }

        public void setChild2(TestMessageObject2 child2) {
            this.child2 = child2;
        }
    }
}
