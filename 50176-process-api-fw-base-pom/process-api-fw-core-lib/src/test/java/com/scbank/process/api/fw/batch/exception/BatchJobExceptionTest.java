package com.scbank.process.api.fw.batch.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkException;

/**
 * BatchJobException Test Class
 */
class BatchJobExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with error code only")
        void shouldCreateWithErrorCodeOnly() {
            String errorCode = "ERR001";
            BatchJobException exception = new BatchJobException(errorCode);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with error code and message args and cause")
        void shouldCreateWithErrorCodeAndMessageArgsAndCause() {
            String errorCode = "ERR002";
            List<Object> messageArgs = Arrays.asList("arg1", "arg2");
            Throwable cause = new RuntimeException("Original error");

            BatchJobException exception = new BatchJobException(errorCode, messageArgs, cause);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with error code and message args")
        void shouldCreateWithErrorCodeAndMessageArgs() {
            String errorCode = "ERR003";
            List<Object> messageArgs = Arrays.asList("arg1", 123);

            BatchJobException exception = new BatchJobException(errorCode, messageArgs);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

//        @Test
//        @DisplayName("Should create exception with error code and message")
//        void shouldCreateWithErrorCodeAndMessage() {
//            String errorCode = "ERR004";
//            String message = "Test error message";
//
//            BatchJobException exception = new BatchJobException(errorCode, message);
//
//            assertNotNull(exception);
//            assertEquals(errorCode, exception.getErrorCode());
//            assertEquals(message, exception.getMessage());
//        }

        @Test
        @DisplayName("Should create exception with error code and cause")
        void shouldCreateWithErrorCodeAndCause() {
            String errorCode = "ERR005";
            Throwable cause = new IllegalArgumentException("Invalid argument");

            BatchJobException exception = new BatchJobException(errorCode, cause);

            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with cause only")
        void shouldCreateWithCauseOnly() {
            Throwable cause = new NullPointerException("Null value");

            BatchJobException exception = new BatchJobException(cause);

            assertNotNull(exception);
            assertEquals(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend FrameworkException")
        void shouldExtendFrameworkException() {
            BatchJobException exception = new BatchJobException("ERR001");
            assertTrue(exception instanceof FrameworkException);
        }

        @Test
        @DisplayName("Should extend Exception")
        void shouldExtendException() {
            BatchJobException exception = new BatchJobException("ERR001");
            assertTrue(exception instanceof Exception);
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null error code")
        void shouldHandleNullErrorCode() {
            BatchJobException exception = new BatchJobException((String) null);
            assertNotNull(exception);
            assertNull(exception.getErrorCode());
        }

        @Test
        @DisplayName("Should handle empty error code")
        void shouldHandleEmptyErrorCode() {
            BatchJobException exception = new BatchJobException("");
            assertNotNull(exception);
            assertEquals("", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should handle null message args")
        void shouldHandleNullMessageArgs() {
            BatchJobException exception = new BatchJobException("ERR001", (List<Object>) null);
            assertNotNull(exception);
        }

        @Test
        @DisplayName("Should handle empty message args")
        void shouldHandleEmptyMessageArgs() {
            List<Object> emptyArgs = Arrays.asList();
            BatchJobException exception = new BatchJobException("ERR001", emptyArgs);
            assertNotNull(exception);
        }
    }
}
