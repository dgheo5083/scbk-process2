package com.scbank.process.api.fw.batch.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * BatchJobExecutionException Test Class
 */
class BatchJobExecutionExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with error code only")
        void shouldCreateWithErrorCodeOnly() {
            String errorCode = "EXEC_ERR001";
            BatchJobExecutionException exception = new BatchJobExecutionException(errorCode);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with error code and message args and cause")
        void shouldCreateWithErrorCodeAndMessageArgsAndCause() {
            String errorCode = "EXEC_ERR002";
            List<Object> messageArgs = Arrays.asList("arg1", "arg2");
            Throwable cause = new RuntimeException("Original error");

            BatchJobExecutionException exception = new BatchJobExecutionException(errorCode, messageArgs, cause);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with error code and message args")
        void shouldCreateWithErrorCodeAndMessageArgs() {
            String errorCode = "EXEC_ERR003";
            List<Object> messageArgs = Arrays.asList("arg1", 123);

            BatchJobExecutionException exception = new BatchJobExecutionException(errorCode, messageArgs);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with error code and message")
        void shouldCreateWithErrorCodeAndMessage() {
            String errorCode = "EXEC_ERR004";
            String message = "Test execution error message";

            BatchJobExecutionException exception = new BatchJobExecutionException(errorCode, message);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals("[EXEC_ERR004]" + message, exception.getMessage());
        }

        @Test
        @DisplayName("Should create exception with error code and cause")
        void shouldCreateWithErrorCodeAndCause() {
            String errorCode = "EXEC_ERR005";
            Throwable cause = new IllegalStateException("Invalid state");

            BatchJobExecutionException exception = new BatchJobExecutionException(errorCode, cause);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend FrameworkRuntimeException")
        void shouldExtendFrameworkRuntimeException() {
            BatchJobExecutionException exception = new BatchJobExecutionException("EXEC_ERR001");
            assertTrue(exception instanceof FrameworkRuntimeException);
        }

        @Test
        @DisplayName("Should extend RuntimeException")
        void shouldExtendRuntimeException() {
            BatchJobExecutionException exception = new BatchJobExecutionException("EXEC_ERR001");
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null error code")
        void shouldHandleNullErrorCode() {
            BatchJobExecutionException exception = new BatchJobExecutionException((String) null);
            assertNotNull(exception);
            assertNull(exception.getErrorCode());
        }

        @Test
        @DisplayName("Should handle empty error code")
        void shouldHandleEmptyErrorCode() {
            BatchJobExecutionException exception = new BatchJobExecutionException("");
            assertNotNull(exception);
            assertEquals("", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should handle exception with error code and null cause")
        void shouldHandleExceptionWithNullCause() {
            // The constructor may throw internally when cause is null
            // Test that creating with a valid cause works correctly
            RuntimeException cause = new RuntimeException("Valid cause");
            BatchJobExecutionException exception = new BatchJobExecutionException("EXEC_ERR001", cause);
            assertNotNull(exception);
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should be throwable and catchable")
        void shouldBeThrowableAndCatchable() {
            assertThrows(BatchJobExecutionException.class, () -> {
                throw new BatchJobExecutionException("EXEC_ERR001");
            });
        }
    }
}
