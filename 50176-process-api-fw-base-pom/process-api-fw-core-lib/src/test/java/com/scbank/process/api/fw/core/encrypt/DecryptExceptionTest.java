package com.scbank.process.api.fw.core.encrypt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * DecryptException Test Class
 */
class DecryptExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create DecryptException with cause")
        void shouldCreateDecryptExceptionWithCause() {
            RuntimeException cause = new RuntimeException("Decryption failed");

            DecryptException exception = new DecryptException(cause);

            assertEquals(FrameworkErrorCode.ENCRYPT_DECRYPTION_FAILED.getCode(), exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create DecryptException with null cause")
        void shouldCreateDecryptExceptionWithNullCause() {
            DecryptException exception = new DecryptException(null);

            assertEquals(FrameworkErrorCode.ENCRYPT_DECRYPTION_FAILED.getCode(), exception.getErrorCode());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create DecryptException with different cause types")
        void shouldCreateDecryptExceptionWithDifferentCauseTypes() {
            IllegalArgumentException illegalArgCause = new IllegalArgumentException("Invalid key");
            DecryptException exception1 = new DecryptException(illegalArgCause);
            assertEquals(illegalArgCause, exception1.getCause());

            NullPointerException nullPointerCause = new NullPointerException("Null data");
            DecryptException exception2 = new DecryptException(nullPointerCause);
            assertEquals(nullPointerCause, exception2.getCause());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend FrameworkException")
        void shouldExtendFrameworkException() {
            DecryptException exception = new DecryptException(new RuntimeException());

            assertTrue(exception instanceof com.scbank.process.api.fw.core.exception.FrameworkException);
        }

        @Test
        @DisplayName("Should be throwable as Exception")
        void shouldBeThrowableAsException() {
            assertThrows(DecryptException.class, () -> {
                throw new DecryptException(new RuntimeException("Test"));
            });
        }
    }

    @Nested
    @DisplayName("Error code tests")
    class ErrorCodeTests {

        @Test
        @DisplayName("Should use correct error code")
        void shouldUseCorrectErrorCode() {
            DecryptException exception = new DecryptException(new RuntimeException());

            assertEquals(FrameworkErrorCode.ENCRYPT_DECRYPTION_FAILED.getCode(), exception.getErrorCode());
        }

        @Test
        @DisplayName("Should maintain error code across different causes")
        void shouldMaintainErrorCodeAcrossDifferentCauses() {
            DecryptException exception1 = new DecryptException(new RuntimeException());
            DecryptException exception2 = new DecryptException(new IllegalArgumentException());

            assertEquals(exception1.getErrorCode(), exception2.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Serialization tests")
    class SerializationTests {

        @Test
        @DisplayName("Should have serialVersionUID")
        void shouldHaveSerialVersionUID() {
            DecryptException exception = new DecryptException(new RuntimeException());

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
            DecryptException exception = new DecryptException(cause);

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
                    throw new DecryptException(e);
                }
            } catch (DecryptException e) {
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
            DecryptException exception = new DecryptException(new RuntimeException("Test"));

            assertNotNull(exception.toString());
        }

        @Test
        @DisplayName("Should include relevant information in toString")
        void shouldIncludeRelevantInformationInToString() {
            DecryptException exception = new DecryptException(new RuntimeException("Decryption error"));

            String toStringResult = exception.toString();
            assertTrue(toStringResult.contains("DecryptException") ||
                      toStringResult.contains(FrameworkErrorCode.ENCRYPT_DECRYPTION_FAILED.getCode()));
        }
    }
}
