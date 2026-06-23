package com.scbank.process.api.fw.channel.response.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.file.FileDownloadObject;
import com.scbank.process.api.fw.channel.message.file.FileDownloadResponseMessage;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import jakarta.servlet.http.HttpServletRequest;

/**
 * FileDownloadResponseRenderer Test Class
 */
@ExtendWith(MockitoExtension.class)
class FileDownloadResponseRendererTest {

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private FileDownloadResponseMessage responseMessage;

    @Mock
    private FileDownloadObject fileDownloadObject;

    private FileDownloadResponseRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new FileDownloadResponseRenderer();
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return false when data is null")
        void shouldReturnFalseWhenDataNull() {
            boolean result = renderer.supports(null, serviceContext);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when body is null")
        void shouldReturnFalseWhenBodyNull() {
            when(responseMessage.getBody()).thenReturn(null);

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true for octet-stream without accept header")
        void shouldReturnTrueForOctetStreamWithoutAccept() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("application/octet-stream");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn(null);

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for PDF with compatible accept")
        void shouldReturnTrueForPdfWithCompatibleAccept() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("application/pdf");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn("application/pdf");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for image/png with compatible accept")
        void shouldReturnTrueForImageWithCompatibleAccept() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("image/png");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn("image/*");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for incompatible accept header")
        void shouldReturnFalseForIncompatibleAccept() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("application/pdf");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn("text/html");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true for wildcard accept")
        void shouldReturnTrueForWildcardAccept() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("application/pdf");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn("*/*");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for binary application type without accept")
        void shouldReturnTrueForBinaryApplicationType() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("application/zip");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn(null);

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for JSON type without accept")
        void shouldReturnFalseForJsonTypeWithoutAccept() {
            when(responseMessage.getBody()).thenReturn(fileDownloadObject);
            when(fileDownloadObject.getContentType()).thenReturn("application/json");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("Accept")).thenReturn("");

            boolean result = renderer.supports(responseMessage, serviceContext);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("render tests")
    class RenderTests {

        @Test
        @DisplayName("Should render PDF file with inline disposition")
        void shouldRenderPdfWithInlineDisposition() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                byte[] fileData = "test content".getBytes();
                when(responseMessage.getBody()).thenReturn(fileDownloadObject);
                when(fileDownloadObject.getContentType()).thenReturn("application/pdf");
                when(fileDownloadObject.getFilename()).thenReturn("test.pdf");
                when(fileDownloadObject.getFileData()).thenReturn(fileData);

                ResponseEntity<byte[]> result = renderer.render(responseMessage, serviceContext);

                assertNotNull(result);
                assertEquals(200, result.getStatusCode().value());
                assertEquals(MediaType.APPLICATION_PDF, result.getHeaders().getContentType());
                assertTrue(result.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("inline"));
                assertArrayEquals(fileData, result.getBody());
            }
        }

        @Test
        @DisplayName("Should render ZIP file with attachment disposition")
        void shouldRenderZipWithAttachmentDisposition() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                byte[] fileData = "zip content".getBytes();
                when(responseMessage.getBody()).thenReturn(fileDownloadObject);
                when(fileDownloadObject.getContentType()).thenReturn("application/zip");
                when(fileDownloadObject.getFilename()).thenReturn("archive.zip");
                when(fileDownloadObject.getFileData()).thenReturn(fileData);

                ResponseEntity<byte[]> result = renderer.render(responseMessage, serviceContext);

                assertNotNull(result);
                assertTrue(result.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("attachment"));
            }
        }

        @Test
        @DisplayName("Should render image file with inline disposition")
        void shouldRenderImageWithInlineDisposition() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                byte[] fileData = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47};
                when(responseMessage.getBody()).thenReturn(fileDownloadObject);
                when(fileDownloadObject.getContentType()).thenReturn("image/png");
                when(fileDownloadObject.getFilename()).thenReturn("image.png");
                when(fileDownloadObject.getFileData()).thenReturn(fileData);

                ResponseEntity<byte[]> result = renderer.render(responseMessage, serviceContext);

                assertNotNull(result);
                assertEquals(MediaType.IMAGE_PNG, result.getHeaders().getContentType());
                assertTrue(result.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("inline"));
            }
        }

        @Test
        @DisplayName("Should set content length header")
        void shouldSetContentLengthHeader() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                byte[] fileData = "test content with length".getBytes();
                when(responseMessage.getBody()).thenReturn(fileDownloadObject);
                when(fileDownloadObject.getContentType()).thenReturn("application/pdf");
                when(fileDownloadObject.getFilename()).thenReturn("test.pdf");
                when(fileDownloadObject.getFileData()).thenReturn(fileData);

                ResponseEntity<byte[]> result = renderer.render(responseMessage, serviceContext);

                assertEquals(String.valueOf(fileData.length),
                        result.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH));
            }
        }

        @Test
        @DisplayName("Should include filename in content disposition")
        void shouldIncludeFilenameInContentDisposition() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                byte[] fileData = "test".getBytes();
                when(responseMessage.getBody()).thenReturn(fileDownloadObject);
                when(fileDownloadObject.getContentType()).thenReturn("application/pdf");
                when(fileDownloadObject.getFilename()).thenReturn("my-document.pdf");
                when(fileDownloadObject.getFileData()).thenReturn(fileData);

                ResponseEntity<byte[]> result = renderer.render(responseMessage, serviceContext);

                assertTrue(result.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("my-document.pdf"));
            }
        }
    }
}
