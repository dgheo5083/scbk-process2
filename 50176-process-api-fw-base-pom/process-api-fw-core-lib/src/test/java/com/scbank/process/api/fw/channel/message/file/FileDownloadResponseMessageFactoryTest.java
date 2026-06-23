package com.scbank.process.api.fw.channel.message.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import jakarta.validation.ConstraintViolationException;

/**
 * FileDownloadResponseMessageFactory Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileDownloadResponseMessageFactoryTest {

    @Mock
    private FileDownloadObject fileDownloadObject;

    private FileDownloadResponseMessageFactory factory;

    @BeforeEach
    void setUp() {
        factory = new FileDownloadResponseMessageFactory();
    }

    @Nested
    @DisplayName("ok tests")
    class OkTests {

        @Test
        @DisplayName("Should create success response with FileDownloadObject")
        void shouldCreateSuccessResponseWithFileDownloadObject() {
            FileDownloadResponseMessage result = factory.ok(fileDownloadObject);

            assertNotNull(result);
            assertEquals(fileDownloadObject, result.getBody());
        }

        @Test
        @DisplayName("Should handle null body")
        void shouldHandleNullBody() {
            FileDownloadResponseMessage result = factory.ok(null);

            assertNotNull(result);
            assertNull(result.getBody());
        }

        @Test
        @DisplayName("Should create response with file download details")
        void shouldCreateResponseWithFileDownloadDetails() {
            when(fileDownloadObject.getFilename()).thenReturn("test.pdf");
            when(fileDownloadObject.getContentType()).thenReturn("application/pdf");

            FileDownloadResponseMessage result = factory.ok(fileDownloadObject);

            assertNotNull(result);
            assertNotNull(result.getBody());
        }
    }

    @Nested
    @DisplayName("fail(Throwable) tests")
    class FailThrowableTests {

        @Test
        @DisplayName("Should return null for failure response")
        void shouldReturnNullForFailureResponse() {
            RuntimeException exception = new RuntimeException("Download failed");

            FileDownloadResponseMessage result = factory.fail(exception);

            assertNull(result);
        }

        @Test
        @DisplayName("Should return null for null exception")
        void shouldReturnNullForNullException() {
            FileDownloadResponseMessage result = factory.fail((Throwable) null);

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("fail(ConstraintViolationException) tests")
    class FailConstraintViolationTests {

        @Test
        @DisplayName("Should throw UnsupportedOperationException")
        void shouldThrowUnsupportedOperationException() {
            ConstraintViolationException exception =
                    new ConstraintViolationException("Validation failed", new HashSet<>());

            assertThrows(UnsupportedOperationException.class, () -> factory.fail(exception));
        }

        @Test
        @DisplayName("Should throw UnsupportedOperationException with specific message")
        void shouldThrowUnsupportedOperationExceptionWithMessage() {
            ConstraintViolationException exception =
                    new ConstraintViolationException("Validation failed", new HashSet<>());

            UnsupportedOperationException thrown = assertThrows(
                    UnsupportedOperationException.class,
                    () -> factory.fail(exception)
            );

            assertTrue(thrown.getMessage().contains("ConstraintViolationException"));
        }
    }
}
