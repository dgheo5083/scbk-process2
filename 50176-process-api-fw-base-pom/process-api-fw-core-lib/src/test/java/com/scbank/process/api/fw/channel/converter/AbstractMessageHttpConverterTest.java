package com.scbank.process.api.fw.channel.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * AbstractMessageHttpConverter Test Class
 */
@ExtendWith(MockitoExtension.class)
class AbstractMessageHttpConverterTest {

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

    private TestMessageHttpConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TestMessageHttpConverter(MediaType.APPLICATION_JSON);
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create converter with single media type")
        void shouldCreateConverterWithSingleMediaType() {
            TestMessageHttpConverter singleMediaConverter = new TestMessageHttpConverter(MediaType.APPLICATION_JSON);

            assertNotNull(singleMediaConverter);
            assertTrue(singleMediaConverter.getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Should create converter with multiple media types")
        void shouldCreateConverterWithMultipleMediaTypes() {
            TestMessageHttpConverter multiMediaConverter = new TestMessageHttpConverter(
                    MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);

            assertNotNull(multiMediaConverter);
            assertEquals(2, multiMediaConverter.getSupportedMediaTypes().size());
        }

        @Test
        @DisplayName("Should create converter with default charset and media types")
        void shouldCreateConverterWithDefaultCharsetAndMediaTypes() {
            TestMessageHttpConverterWithCharset charsetConverter =
                    new TestMessageHttpConverterWithCharset(StandardCharsets.UTF_8, MediaType.APPLICATION_JSON);

            assertNotNull(charsetConverter);
        }
    }

    @Nested
    @DisplayName("resolveCharset for HttpInputMessage tests")
    class ResolveCharsetInputTests {

        @Test
        @DisplayName("Should return charset from content type when present")
        void shouldReturnCharsetFromContentTypeWhenPresent() {
            MediaType contentType = new MediaType("application", "json", StandardCharsets.UTF_8);
            when(inputMessage.getHeaders()).thenReturn(httpHeaders);
            when(httpHeaders.getContentType()).thenReturn(contentType);

            Charset result = converter.resolveCharset(inputMessage, StandardCharsets.ISO_8859_1);

            assertEquals(StandardCharsets.UTF_8, result);
        }

        @Test
        @DisplayName("Should return default charset when content type charset is null")
        void shouldReturnDefaultCharsetWhenContentTypeCharsetNull() {
            MediaType contentType = new MediaType("application", "json");
            when(inputMessage.getHeaders()).thenReturn(httpHeaders);
            when(httpHeaders.getContentType()).thenReturn(contentType);

            Charset result = converter.resolveCharset(inputMessage, StandardCharsets.ISO_8859_1);

            assertEquals(StandardCharsets.ISO_8859_1, result);
        }

        @Test
        @DisplayName("Should return default charset when content type is null")
        void shouldReturnDefaultCharsetWhenContentTypeNull() {
            when(inputMessage.getHeaders()).thenReturn(httpHeaders);
            when(httpHeaders.getContentType()).thenReturn(null);

            Charset result = converter.resolveCharset(inputMessage, StandardCharsets.UTF_8);

            assertEquals(StandardCharsets.UTF_8, result);
        }
    }

    @Nested
    @DisplayName("resolveCharset for HttpOutputMessage tests")
    class ResolveCharsetOutputTests {

        @Test
        @DisplayName("Should return charset from content type when present")
        void shouldReturnCharsetFromContentTypeWhenPresent() {
            MediaType contentType = new MediaType("application", "json", StandardCharsets.UTF_16);
            when(outputMessage.getHeaders()).thenReturn(httpHeaders);
            when(httpHeaders.getContentType()).thenReturn(contentType);

            Charset result = converter.resolveCharset(outputMessage, StandardCharsets.UTF_8);

            assertEquals(StandardCharsets.UTF_16, result);
        }

        @Test
        @DisplayName("Should return default charset when output content type charset is null")
        void shouldReturnDefaultCharsetWhenOutputContentTypeCharsetNull() {
            MediaType contentType = new MediaType("application", "json");
            when(outputMessage.getHeaders()).thenReturn(httpHeaders);
            when(httpHeaders.getContentType()).thenReturn(contentType);

            Charset result = converter.resolveCharset(outputMessage, StandardCharsets.UTF_8);

            assertEquals(StandardCharsets.UTF_8, result);
        }

        @Test
        @DisplayName("Should return default charset when output content type is null")
        void shouldReturnDefaultCharsetWhenOutputContentTypeNull() {
            when(outputMessage.getHeaders()).thenReturn(httpHeaders);
            when(httpHeaders.getContentType()).thenReturn(null);

            Charset result = converter.resolveCharset(outputMessage, StandardCharsets.ISO_8859_1);

            assertEquals(StandardCharsets.ISO_8859_1, result);
        }
    }

    @Nested
    @DisplayName("createMessageContext tests")
    class CreateMessageContextTests {

        @Test
        @DisplayName("Should create message context using RuntimeContext")
        void shouldCreateMessageContextUsingRuntimeContext() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(MessageFormat.JSON, "UTF-8")).thenReturn(messageContext);

                MessageContext result = converter.createMessageContext(MessageFormat.JSON, "UTF-8");

                assertNotNull(result);
                assertEquals(messageContext, result);
            }
        }
    }

    /**
     * Test implementation of AbstractMessageHttpConverter
     */
    private static class TestMessageHttpConverter extends AbstractMessageHttpConverter<Object> {

        protected TestMessageHttpConverter(MediaType supportedMediaType) {
            super(supportedMediaType);
        }

        protected TestMessageHttpConverter(MediaType... supportedMediaTypes) {
            super(supportedMediaTypes);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) {
            return null;
        }

        @Override
        protected void writeInternal(Object o, HttpOutputMessage outputMessage) {
        }

        // Expose protected methods for testing
        @Override
        public Charset resolveCharset(HttpInputMessage inputMessage, Charset defaultCharset) {
            return super.resolveCharset(inputMessage, defaultCharset);
        }

        @Override
        public Charset resolveCharset(HttpOutputMessage outputMessage, Charset defaultCharset) {
            return super.resolveCharset(outputMessage, defaultCharset);
        }

        @Override
        public MessageContext createMessageContext(MessageFormat format, String encoding) {
            return super.createMessageContext(format, encoding);
        }
    }

    /**
     * Test implementation with charset constructor
     */
    private static class TestMessageHttpConverterWithCharset extends AbstractMessageHttpConverter<Object> {

        protected TestMessageHttpConverterWithCharset(Charset defaultCharset, MediaType... supportedMediaTypes) {
            super(defaultCharset, supportedMediaTypes);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) {
            return null;
        }

        @Override
        protected void writeInternal(Object o, HttpOutputMessage outputMessage) {
        }
    }
}
