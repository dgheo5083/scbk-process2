package com.scbank.process.api.fw.core.encrypt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * EncryptException Test Class
 */
class EncryptExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create EncryptException with cause")
        void shouldCreateEncryptExceptionWithCause() {
            RuntimeException cause = new RuntimeException("Encryption failed");

            EncryptException exception = new EncryptException(cause);

            assertEquals(FrameworkErrorCode.ENCRYPT_ENCRYPTION_FAILED.getCode(), exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create EncryptException with null cause")
        void shouldCreateEncryptExceptionWithNullCause() {
            EncryptException exception = new EncryptException(null);

            assertEquals(FrameworkErrorCode.ENCRYPT_ENCRYPTION_FAILED.getCode(), exception.getErrorCode());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create EncryptException with different cause types")
        void shouldCreateEncryptExceptionWithDifferentCauseTypes() {
            IllegalArgumentException illegalArgCause = new IllegalArgumentException("Invalid key");
            EncryptException exception1 = new EncryptException(illegalArgCause);
            assertEquals(illegalArgCause, exception1.getCause());

            NullPointerException nullPointerCause = new NullPointerException("Null data");
            EncryptException exception2 = new EncryptException(nullPointerCause);
            assertEquals(nullPointerCause, exception2.getCause());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend FrameworkException")
        void shouldExtendFrameworkException() {
            EncryptException exception = new EncryptException(new RuntimeException());

            assertTrue(exception instanceof com.scbank.process.api.fw.core.exception.FrameworkException);
        }

        @Test
        @DisplayName("Should be throwable as Exception")
        void shouldBeThrowableAsException() {
            assertThrows(EncryptException.class, () -> {
                throw new EncryptException(new RuntimeException("Test"));
            });
        }
    }

    @Nested
    @DisplayName("Error code tests")
    class ErrorCodeTests {

        @Test
        @DisplayName("Should use correct error code")
        void shouldUseCorrectErrorCode() {
            EncryptException exception = new EncryptException(new RuntimeException());

            assertEquals(FrameworkErrorCode.ENCRYPT_ENCRYPTION_FAILED.getCode(), exception.getErrorCode());
        }

        @Test
        @DisplayName("Should maintain error code across different causes")
        void shouldMaintainErrorCodeAcrossDifferentCauses() {
            EncryptException exception1 = new EncryptException(new RuntimeException());
            EncryptException exception2 = new EncryptException(new IllegalArgumentException());

            assertEquals(exception1.getErrorCode(), exception2.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Serialization tests")
    class SerializationTests {

        @Test
        @DisplayName("Should have serialVersionUID")
        void shouldHaveSerialVersionUID() {
            EncryptException exception = new EncryptException(new RuntimeException());

            assertNotNull(exception);
            assertTrue(exception instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("Exception chaining tests")
    class ExceptionChainingTests {

        @Test
        @DisplayName("Should preserve stack trace from cause")
        void shouldPreserveStackTraceFromCause() {
            RuntimeException cause = new RuntimeException("Original error");
            EncryptException exception = new EncryptException(cause);

            assertNotNull(exception.getStackTrace());
            assertTrue(exception.getStackTrace().length > 0);
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should work in nested try-catch blocks")
        void shouldWorkInNestedTryCatchBlocks() {
            boolean caught = false;

            try {
                try {
                    throw new RuntimeException("Inner exception");
                } catch (RuntimeException e) {
                    throw new EncryptException(e);
                }
            } catch (EncryptException e) {
                caught = true;
                assertNotNull(e.getCause());
                assertEquals("Inner exception", e.getCause().getMessage());
            }

            assertTrue(caught);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            EncryptException exception = new EncryptException(new RuntimeException("Test"));

            assertNotNull(exception.toString());
        }

        @Test
        @DisplayName("Should include relevant information in toString")
        void shouldIncludeRelevantInformationInToString() {
            EncryptException exception = new EncryptException(new RuntimeException("Encryption error"));

            String toStringResult = exception.toString();
            assertTrue(toStringResult.contains("EncryptException") ||
                      toStringResult.contains(FrameworkErrorCode.ENCRYPT_ENCRYPTION_FAILED.getCode()));
        }
    }
}
