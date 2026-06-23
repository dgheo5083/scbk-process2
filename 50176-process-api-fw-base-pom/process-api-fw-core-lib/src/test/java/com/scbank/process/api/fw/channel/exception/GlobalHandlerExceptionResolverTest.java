package com.scbank.process.api.fw.channel.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import com.scbank.process.api.fw.channel.converter.HttpMessageConverterComposite;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GlobalHandlerExceptionResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
class GlobalHandlerExceptionResolverTest {

    @Mock
    private HttpMessageConverterComposite<IMessageObject> messageConverterComposite;

    @Mock
    private IResponseMessageFactory<?, ?, ?> responseMessageFactory;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IResponseMessage<?, ?> responseMessage;

    private GlobalHandlerExceptionResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new GlobalHandlerExceptionResolver(messageConverterComposite, responseMessageFactory);
    }

    @Nested
    @DisplayName("resolveException tests")
    class ResolveExceptionTests {

        @Test
        @DisplayName("Should resolve exception and return ModelAndView")
        void shouldResolveExceptionAndReturnModelAndView() throws Exception {
            // Given
            Exception ex = new RuntimeException("Test error");
            when(request.getHeader("Accept")).thenReturn("application/json");
            doReturn(responseMessage).when(responseMessageFactory).fail(ex);

            // When
            ModelAndView result = resolver.resolveException(request, response, null, ex);

            // Then
            assertNotNull(result);
            verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

        @Test
        @DisplayName("Should use JSON as default when Accept header is null")
        void shouldUseJsonAsDefaultWhenAcceptNull() throws Exception {
            // Given
            Exception ex = new RuntimeException("Test");
            when(request.getHeader("Accept")).thenReturn(null);
            doReturn(responseMessage).when(responseMessageFactory).fail(ex);

            // When
            ModelAndView result = resolver.resolveException(request, response, null, ex);

            // Then
            assertNotNull(result);
            verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

        @Test
        @DisplayName("Should handle XML Accept header")
        void shouldHandleXmlAcceptHeader() throws Exception {
            // Given
            Exception ex = new RuntimeException("Test");
            when(request.getHeader("Accept")).thenReturn("application/xml");
            doReturn(responseMessage).when(responseMessageFactory).fail(ex);

            // When
            ModelAndView result = resolver.resolveException(request, response, null, ex);

            // Then
            assertNotNull(result);
            verify(response).setContentType(MediaType.APPLICATION_XML_VALUE);
        }

        @Test
        @DisplayName("Should handle write exception gracefully")
        void shouldHandleWriteExceptionGracefully() throws Exception {
            // Given
            Exception ex = new RuntimeException("Test");
            when(request.getHeader("Accept")).thenReturn("application/json");
            doReturn(responseMessage).when(responseMessageFactory).fail(ex);
            doThrow(new RuntimeException("Write error")).when(messageConverterComposite)
                    .write(any(), any(), any());

            // When
            ModelAndView result = resolver.resolveException(request, response, null, ex);

            // Then
            assertNotNull(result);
            // Should not throw exception
        }

        @Test
        @DisplayName("Should set 500 status code")
        void shouldSet500StatusCode() throws Exception {
            // Given
            Exception ex = new IllegalStateException("State error");
            when(request.getHeader("Accept")).thenReturn("application/json");
            doReturn(responseMessage).when(responseMessageFactory).fail(ex);

            // When
            resolver.resolveException(request, response, null, ex);

            // Then
            verify(response).setStatus(500);
        }
    }

    @Nested
    @DisplayName("resolveAccept tests")
    class ResolveAcceptTests {

        @Test
        @DisplayName("Should return JSON for null Accept header")
        void shouldReturnJsonForNullAccept() {
            when(request.getHeader("Accept")).thenReturn(null);

            MediaType result = resolver.resolveAccept(request);

            assertEquals(MediaType.APPLICATION_JSON, result);
        }

        @Test
        @DisplayName("Should parse valid Accept header")
        void shouldParseValidAcceptHeader() {
            when(request.getHeader("Accept")).thenReturn("application/xml");

            MediaType result = resolver.resolveAccept(request);

            assertEquals(MediaType.APPLICATION_XML, result);
        }

        @Test
        @DisplayName("Should return JSON for invalid Accept header")
        void shouldReturnJsonForInvalidAccept() {
            when(request.getHeader("Accept")).thenReturn("invalid/media/type///");

            MediaType result = resolver.resolveAccept(request);

            assertEquals(MediaType.APPLICATION_JSON, result);
        }

        @Test
        @DisplayName("Should handle text/plain Accept header")
        void shouldHandleTextPlainAccept() {
            when(request.getHeader("Accept")).thenReturn("text/plain");

            MediaType result = resolver.resolveAccept(request);

            assertEquals(MediaType.TEXT_PLAIN, result);
        }
    }

    @Nested
    @DisplayName("init tests")
    class InitTests {

        @Test
        @DisplayName("Should initialize without exception")
        void shouldInitializeWithoutException() {
            assertDoesNotThrow(() -> resolver.init());
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create resolver with dependencies")
        void shouldCreateResolverWithDependencies() {
            GlobalHandlerExceptionResolver newResolver =
                    new GlobalHandlerExceptionResolver(messageConverterComposite, responseMessageFactory);

            assertNotNull(newResolver);
        }
    }
}
