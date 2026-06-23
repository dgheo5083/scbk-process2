package com.scbank.process.api.fw.channel.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * ServiceTimeException Test Class
 */
class ServiceTimeExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with error code only")
        void shouldCreateWithErrorCodeOnly() {
            ServiceTimeException exception = new ServiceTimeException("TIME_ERR001");

            assertNotNull(exception);
            assertEquals("TIME_ERR001", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with error code and message")
        void shouldCreateWithErrorCodeAndMessage() {
            ServiceTimeException exception = new ServiceTimeException("TIME_ERR002", "Service not available at this time");

            assertNotNull(exception);
            assertEquals("TIME_ERR002", exception.getErrorCode());
            assertEquals("[TIME_ERR002]Service not available at this time", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend FrameworkRuntimeException")
        void shouldExtendFrameworkRuntimeException() {
            ServiceTimeException exception = new ServiceTimeException("TIME_ERR001");
            assertTrue(exception instanceof FrameworkRuntimeException);
        }

        @Test
        @DisplayName("Should extend RuntimeException")
        void shouldExtendRuntimeException() {
            ServiceTimeException exception = new ServiceTimeException("TIME_ERR001");
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null error code")
        void shouldHandleNullErrorCode() {
            ServiceTimeException exception = new ServiceTimeException(null);
            assertNotNull(exception);
            assertNull(exception.getErrorCode());
        }

        @Test
        @DisplayName("Should handle empty error code")
        void shouldHandleEmptyErrorCode() {
            ServiceTimeException exception = new ServiceTimeException("");
            assertNotNull(exception);
            assertEquals("", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            ServiceTimeException exception = new ServiceTimeException("TIME_ERR001", null);
            assertNotNull(exception);
            assertEquals("TIME_ERR001", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should be throwable and catchable")
        void shouldBeThrowableAndCatchable() {
            assertThrows(ServiceTimeException.class, () -> {
                throw new ServiceTimeException("TIME_ERR001");
            });
        }
    }

    @Nested
    @DisplayName("Error code retrieval tests")
    class ErrorCodeRetrievalTests {

        @Test
        @DisplayName("Should return error code via getErrorCode")
        void shouldReturnErrorCodeViaGetErrorCode() {
            ServiceTimeException exception = new ServiceTimeException("CUSTOM_ERROR_CODE");
            assertEquals("CUSTOM_ERROR_CODE", exception.getErrorCode());
        }
    }
}
