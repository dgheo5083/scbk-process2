package com.scbank.process.api.fw.channel.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * HttpMessageConverterComposite Test Class
 */
@ExtendWith(MockitoExtension.class)
class HttpMessageConverterCompositeTest {

    @Mock
    private HttpMessageConverter<IMessageObject> delegate1;

    @Mock
    private HttpMessageConverter<IMessageObject> delegate2;

    @Mock
    private HttpInputMessage inputMessage;

    @Mock
    private HttpOutputMessage outputMessage;

    @Mock
    private HttpHeaders headers;

    @Mock
    private IMessageObject messageObject;

    private HttpMessageConverterComposite<IMessageObject> composite;

    @BeforeEach
    void setUp() {
        composite = new HttpMessageConverterComposite<>(Arrays.asList(delegate1, delegate2));
    }

    @Nested
    @DisplayName("canRead tests")
    class CanReadTests {

        @Test
        @DisplayName("Should return true when any delegate can read")
        void shouldReturnTrueWhenDelegateCanRead() {
            when(delegate1.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);
            when(delegate2.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(true);

            boolean result = composite.canRead(IMessageObject.class, MediaType.APPLICATION_JSON);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when no delegate can read")
        void shouldReturnFalseWhenNoDelegateCanRead() {
            when(delegate1.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);
            when(delegate2.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);

            boolean result = composite.canRead(IMessageObject.class, MediaType.APPLICATION_JSON);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true from first delegate")
        void shouldReturnTrueFromFirstDelegate() {
            when(delegate1.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(true);

            boolean result = composite.canRead(IMessageObject.class, MediaType.APPLICATION_JSON);

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("canWrite tests")
    class CanWriteTests {

        @Test
        @DisplayName("Should return true when any delegate can write")
        void shouldReturnTrueWhenDelegateCanWrite() {
            when(delegate1.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);
            when(delegate2.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(true);

            boolean result = composite.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when no delegate can write")
        void shouldReturnFalseWhenNoDelegateCanWrite() {
            when(delegate1.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);
            when(delegate2.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);

            boolean result = composite.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("getSupportedMediaTypes tests")
    class GetSupportedMediaTypesTests {

        @Test
        @DisplayName("Should return merged media types from all delegates")
        void shouldReturnMergedMediaTypes() {
            when(delegate1.getSupportedMediaTypes()).thenReturn(List.of(MediaType.APPLICATION_JSON));
            when(delegate2.getSupportedMediaTypes()).thenReturn(List.of(MediaType.APPLICATION_XML));

            List<MediaType> result = composite.getSupportedMediaTypes();

            assertEquals(2, result.size());
            assertTrue(result.contains(MediaType.APPLICATION_JSON));
            assertTrue(result.contains(MediaType.APPLICATION_XML));
        }

        @Test
        @DisplayName("Should return distinct media types")
        void shouldReturnDistinctMediaTypes() {
            when(delegate1.getSupportedMediaTypes()).thenReturn(List.of(MediaType.APPLICATION_JSON));
            when(delegate2.getSupportedMediaTypes()).thenReturn(List.of(MediaType.APPLICATION_JSON));

            List<MediaType> result = composite.getSupportedMediaTypes();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should handle empty media types")
        void shouldHandleEmptyMediaTypes() {
            when(delegate1.getSupportedMediaTypes()).thenReturn(Collections.emptyList());
            when(delegate2.getSupportedMediaTypes()).thenReturn(Collections.emptyList());

            List<MediaType> result = composite.getSupportedMediaTypes();

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("read tests")
    class ReadTests {

        @Test
        @DisplayName("Should delegate read to matching converter")
        void shouldDelegateReadToMatchingConverter() throws IOException {
            when(inputMessage.getHeaders()).thenReturn(headers);
            when(headers.getContentType()).thenReturn(MediaType.APPLICATION_JSON);
            when(delegate1.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(true);
            when(delegate1.read(IMessageObject.class, inputMessage)).thenReturn(messageObject);

            IMessageObject result = composite.read(IMessageObject.class, inputMessage);

            assertEquals(messageObject, result);
            verify(delegate1).read(IMessageObject.class, inputMessage);
        }

        @Test
        @DisplayName("Should throw exception when no converter can read")
        void shouldThrowExceptionWhenNoConverterCanRead() throws IOException {
            when(inputMessage.getHeaders()).thenReturn(headers);
            when(headers.getContentType()).thenReturn(MediaType.APPLICATION_JSON);
            when(delegate1.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);
            when(delegate2.canRead(IMessageObject.class, MediaType.APPLICATION_JSON)).thenReturn(false);

            assertThrows(HttpMessageNotReadableException.class,
                    () -> composite.read(IMessageObject.class, inputMessage));
        }

        @Test
        @DisplayName("Should use second delegate when first cannot read")
        void shouldUseSecondDelegateWhenFirstCannotRead() throws IOException {
            when(inputMessage.getHeaders()).thenReturn(headers);
            when(headers.getContentType()).thenReturn(MediaType.APPLICATION_XML);
            when(delegate1.canRead(IMessageObject.class, MediaType.APPLICATION_XML)).thenReturn(false);
            when(delegate2.canRead(IMessageObject.class, MediaType.APPLICATION_XML)).thenReturn(true);
            when(delegate2.read(IMessageObject.class, inputMessage)).thenReturn(messageObject);

            IMessageObject result = composite.read(IMessageObject.class, inputMessage);

            assertEquals(messageObject, result);
            verify(delegate1, never()).read(any(), any());
            verify(delegate2).read(IMessageObject.class, inputMessage);
        }
    }

    @Nested
    @DisplayName("write tests")
    class WriteTests {

        @Test
        @DisplayName("Should delegate write to matching converter")
        void shouldDelegateWriteToMatchingConverter() throws IOException {
            when(delegate1.canWrite(messageObject.getClass(), MediaType.APPLICATION_JSON)).thenReturn(true);

            composite.write(messageObject, MediaType.APPLICATION_JSON, outputMessage);

            verify(delegate1).write(messageObject, MediaType.APPLICATION_JSON, outputMessage);
        }

        @Test
        @DisplayName("Should throw exception when no converter can write")
        void shouldThrowExceptionWhenNoConverterCanWrite() {
            when(delegate1.canWrite(messageObject.getClass(), MediaType.APPLICATION_JSON)).thenReturn(false);
            when(delegate2.canWrite(messageObject.getClass(), MediaType.APPLICATION_JSON)).thenReturn(false);

            assertThrows(HttpMessageNotWritableException.class,
                    () -> composite.write(messageObject, MediaType.APPLICATION_JSON, outputMessage));
        }

        @Test
        @DisplayName("Should use second delegate when first cannot write")
        void shouldUseSecondDelegateWhenFirstCannotWrite() throws IOException {
            when(delegate1.canWrite(messageObject.getClass(), MediaType.APPLICATION_XML)).thenReturn(false);
            when(delegate2.canWrite(messageObject.getClass(), MediaType.APPLICATION_XML)).thenReturn(true);

            composite.write(messageObject, MediaType.APPLICATION_XML, outputMessage);

            verify(delegate1, never()).write(any(), any(), any());
            verify(delegate2).write(messageObject, MediaType.APPLICATION_XML, outputMessage);
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create composite with empty delegates")
        void shouldCreateWithEmptyDelegates() {
            HttpMessageConverterComposite<IMessageObject> emptyComposite =
                    new HttpMessageConverterComposite<>(Collections.emptyList());

            assertFalse(emptyComposite.canRead(IMessageObject.class, MediaType.APPLICATION_JSON));
            assertFalse(emptyComposite.canWrite(IMessageObject.class, MediaType.APPLICATION_JSON));
            assertTrue(emptyComposite.getSupportedMediaTypes().isEmpty());
        }
    }
}
