package com.scbank.process.api.fw.integration.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * IntegrationTimeoutException Test Class
 */
class IntegrationTimeoutExceptionTest {

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with systemId only")
        void shouldCreateExceptionWithSystemIdOnly() {
            IntegrationTimeoutException exception = new IntegrationTimeoutException("MCI");

            assertEquals(FrameworkErrorCode.INTEG_TIMEOUT.getCode(), exception.getErrorCode());
            assertEquals("MCI", exception.getErrorLocation());
        }

        @Test
        @DisplayName("Should create exception with systemId and cause")
        void shouldCreateExceptionWithSystemIdAndCause() {
            Throwable cause = new RuntimeException("Socket timeout");
            IntegrationTimeoutException exception = new IntegrationTimeoutException("FEP", cause);

            assertEquals(FrameworkErrorCode.INTEG_TIMEOUT.getCode(), exception.getErrorCode());
            assertEquals("FEP", exception.getErrorLocation());
            assertSame(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend IntegrationSystemException")
        void shouldExtendIntegrationSystemException() {
            IntegrationTimeoutException exception = new IntegrationTimeoutException("MCI");

            assertTrue(exception instanceof IntegrationSystemException);
            assertTrue(exception instanceof IntegrationException);
        }
    }

    @Nested
    @DisplayName("error code tests")
    class ErrorCodeTests {

        @Test
        @DisplayName("Should use INTEG_TIMEOUT error code")
        void shouldUseIntegTimeoutErrorCode() {
            IntegrationTimeoutException exception1 = new IntegrationTimeoutException("MCI");
            IntegrationTimeoutException exception2 = new IntegrationTimeoutException("FEP", new RuntimeException());

            assertEquals(FrameworkErrorCode.INTEG_TIMEOUT.getCode(), exception1.getErrorCode());
            assertEquals(FrameworkErrorCode.INTEG_TIMEOUT.getCode(), exception2.getErrorCode());
        }
    }

    @Nested
    @DisplayName("systemId tests")
    class SystemIdTests {

        @Test
        @DisplayName("Should handle various systemId values")
        void shouldHandleVariousSystemIdValues() {
            IntegrationTimeoutException exception1 = new IntegrationTimeoutException("MCI");
            IntegrationTimeoutException exception2 = new IntegrationTimeoutException("FEP");
            IntegrationTimeoutException exception3 = new IntegrationTimeoutException("HOST");

            assertEquals("MCI", exception1.getErrorLocation());
            assertEquals("FEP", exception2.getErrorLocation());
            assertEquals("HOST", exception3.getErrorLocation());
        }

        @Test
        @DisplayName("Should handle null systemId")
        void shouldHandleNullSystemId() {
            IntegrationTimeoutException exception = new IntegrationTimeoutException(null);

            assertNull(exception.getErrorLocation());
        }
    }
}
