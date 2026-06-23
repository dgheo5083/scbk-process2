package com.scbank.process.api.fw.channel.converter.form;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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

import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.form.FormMessageMapper;

/**
 * FormMessageHttpConverter Test Class
 *
 * Tests for {@link FormMessageHttpConverter} ensuring 100% Jacoco coverage.
 */
@ExtendWith(MockitoExtension.class)
class FormMessageHttpConverterTest {

    @Mock
    private FormMessageMapper formMessageMapper;

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

    private FormMessageHttpConverter<IMessageObject> converter;

    @BeforeEach
    void setUp() {
        try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
            runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
            converter = new FormMessageHttpConverter<>(formMessageMapper);
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
        @DisplayName("Should create converter with FormMessageMapper")
        void shouldCreateConverterWithFormMessageMapper() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                FormMessageHttpConverter<IMessageObject> newConverter =
                        new FormMessageHttpConverter<>(formMessageMapper);

                assertNotNull(newConverter);
                assertTrue(newConverter.getSupportedMediaTypes().contains(MediaType.APPLICATION_FORM_URLENCODED));
            }
        }

        @Test
        @DisplayName("Should create converter with default encoding from RuntimeContext")
        void shouldCreateConverterWithDefaultEncoding() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("EUC-KR");

                FormMessageHttpConverter<IMessageObject> newConverter =
                        new FormMessageHttpConverter<>(formMessageMapper);

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
            boolean result = converter.supports(TestFormMessageObject.class);

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
        @DisplayName("Should read form data and deserialize to object")
        void shouldReadFormDataAndDeserializeToObject() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] formBytes = "name=test&value=123".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(formBytes);
                TestFormMessageObject expectedObject = new TestFormMessageObject();
                expectedObject.setName("test");

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(eq(MessageFormat.FORM), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_FORM_URLENCODED);
                when(httpHeaders.getContentLength()).thenReturn((long) formBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(formMessageMapper.deserialize(any(byte[].class), eq(TestFormMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                IMessageObject result = converter.readInternal(TestFormMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
                assertTrue(result instanceof TestFormMessageObject);
                assertEquals("test", ((TestFormMessageObject) result).getName());
                verify(formMessageMapper).deserialize(any(byte[].class), eq(TestFormMessageObject.class), eq(messageContext));
            }
        }

        @Test
        @DisplayName("Should read form data with charset from content type")
        void shouldReadFormDataWithCharsetFromContentType() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] formBytes = "name=test".getBytes(StandardCharsets.UTF_16);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(formBytes);
                TestFormMessageObject expectedObject = new TestFormMessageObject();

                MediaType contentTypeWithCharset = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_16);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(eq(MessageFormat.FORM), eq("UTF-16"))).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(contentTypeWithCharset);
                when(httpHeaders.getContentLength()).thenReturn((long) formBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(formMessageMapper.deserialize(any(byte[].class), eq(TestFormMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                IMessageObject result = converter.readInternal(TestFormMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
                verify(messageContextCreator).create(MessageFormat.FORM, "UTF-16");
            }
        }

        @Test
        @DisplayName("Should use default encoding when content type has no charset")
        void shouldUseDefaultEncodingWhenNoCharset() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] formBytes = "name=test".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(formBytes);
                TestFormMessageObject expectedObject = new TestFormMessageObject();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(eq(MessageFormat.FORM), eq("UTF-8"))).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) formBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(formMessageMapper.deserialize(any(byte[].class), eq(TestFormMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                IMessageObject result = converter.readInternal(TestFormMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
                verify(messageContextCreator).create(MessageFormat.FORM, "UTF-8");
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotReadableException on deserialization error")
        void shouldThrowExceptionOnDeserializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] formBytes = "invalid=data".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(formBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) formBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(formMessageMapper.deserialize(any(byte[].class), eq(TestFormMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Deserialization error"));

                // When/Then
                HttpMessageNotReadableException exception = assertThrows(
                        HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestFormMessageObject.class, inputMessage)
                );

                assertTrue(exception.getMessage().contains("Form 데이터 역직렬화 실패"));
                assertNotNull(exception.getCause());
                assertEquals("Deserialization error", exception.getCause().getMessage());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful read")
        void shouldClearMessageContextHolderAfterSuccessfulRead() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] formBytes = "name=test".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(formBytes);
                TestFormMessageObject expectedObject = new TestFormMessageObject();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) formBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(formMessageMapper.deserialize(any(byte[].class), eq(TestFormMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                converter.readInternal(TestFormMessageObject.class, inputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on exception")
        void shouldClearMessageContextHolderOnException() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                byte[] formBytes = "invalid".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(formBytes);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(httpHeaders.getContentLength()).thenReturn((long) formBytes.length);
                when(inputMessage.getBody()).thenReturn(inputStream);
                when(formMessageMapper.deserialize(any(byte[].class), eq(TestFormMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Error"));

                // When
                assertThrows(HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestFormMessageObject.class, inputMessage));

                // Then - MessageContextHolder should be cleared even after exception
                assertNull(MessageContextHolder.get());
            }
        }
    }

    @Nested
    @DisplayName("writeInternal tests")
    class WriteInternalTests {

        @Test
        @DisplayName("Should serialize object and write to output")
        void shouldSerializeObjectAndWriteToOutput() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();
                messageObject.setName("test");
                byte[] serializedBytes = "name=test".getBytes(StandardCharsets.UTF_8);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(eq(MessageFormat.FORM), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_FORM_URLENCODED);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(formMessageMapper.serialize(eq(messageObject), any())).thenReturn(serializedBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then
                verify(formMessageMapper).serialize(eq(messageObject), eq(messageContext));
                verify(httpHeaders).setContentLength(serializedBytes.length);
                assertEquals("name=test", outputStream.toString(StandardCharsets.UTF_8));
            }
        }

        @Test
        @DisplayName("Should write with charset from content type")
        void shouldWriteWithCharsetFromContentType() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();
                byte[] serializedBytes = "name=test".getBytes(StandardCharsets.UTF_16);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                MediaType contentTypeWithCharset = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_16);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(eq(MessageFormat.FORM), eq("UTF-16"))).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(contentTypeWithCharset);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(formMessageMapper.serialize(eq(messageObject), any())).thenReturn(serializedBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then
                verify(messageContextCreator).create(MessageFormat.FORM, "UTF-16");
            }
        }

        @Test
        @DisplayName("Should use default encoding when content type has no charset")
        void shouldUseDefaultEncodingWhenWritingWithNoCharset() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();
                byte[] serializedBytes = "name=test".getBytes(StandardCharsets.UTF_8);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(eq(MessageFormat.FORM), eq("UTF-8"))).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(formMessageMapper.serialize(eq(messageObject), any())).thenReturn(serializedBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then
                verify(messageContextCreator).create(MessageFormat.FORM, "UTF-8");
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotWritableException on serialization error")
        void shouldThrowExceptionOnSerializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(formMessageMapper.serialize(eq(messageObject), any()))
                        .thenThrow(new RuntimeException("Serialization error"));

                // When/Then
                HttpMessageNotWritableException exception = assertThrows(
                        HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(messageObject, outputMessage)
                );

                assertTrue(exception.getMessage().contains("Form 데이터 직렬화 실패"));
                assertNotNull(exception.getCause());
                assertEquals("Serialization error", exception.getCause().getMessage());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful write")
        void shouldClearMessageContextHolderAfterSuccessfulWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();
                byte[] serializedBytes = "name=test".getBytes(StandardCharsets.UTF_8);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(outputMessage.getBody()).thenReturn(outputStream);
                when(formMessageMapper.serialize(eq(messageObject), any())).thenReturn(serializedBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on exception during write")
        void shouldClearMessageContextHolderOnWriteException() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(formMessageMapper.serialize(eq(messageObject), any()))
                        .thenThrow(new RuntimeException("Error"));

                // When
                assertThrows(HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(messageObject, outputMessage));

                // Then - MessageContextHolder should be cleared even after exception
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should handle IOException during body write")
        void shouldHandleIOExceptionDuringBodyWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                TestFormMessageObject messageObject = new TestFormMessageObject();
                byte[] serializedBytes = "name=test".getBytes(StandardCharsets.UTF_8);

                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(null);
                when(formMessageMapper.serialize(eq(messageObject), any())).thenReturn(serializedBytes);
                when(outputMessage.getBody()).thenThrow(new IOException("IO Error"));

                // When/Then
                HttpMessageNotWritableException exception = assertThrows(
                        HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(messageObject, outputMessage)
                );

                assertTrue(exception.getMessage().contains("Form 데이터 직렬화 실패"));
            }
        }
    }

    @Nested
    @DisplayName("MediaType support tests")
    class MediaTypeSupportTests {

        @Test
        @DisplayName("Should support application/x-www-form-urlencoded media type")
        void shouldSupportFormUrlencodedMediaType() {
            assertTrue(converter.getSupportedMediaTypes().contains(MediaType.APPLICATION_FORM_URLENCODED));
        }

        @Test
        @DisplayName("Should have exactly one supported media type")
        void shouldHaveExactlyOneSupportedMediaType() {
            assertEquals(1, converter.getSupportedMediaTypes().size());
        }
    }

    /**
     * Test IMessageObject implementation for form data
     */
    public static class TestFormMessageObject implements IMessageObject {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
