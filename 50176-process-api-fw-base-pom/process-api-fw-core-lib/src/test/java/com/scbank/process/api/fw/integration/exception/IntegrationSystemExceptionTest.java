package com.scbank.process.api.fw.integration.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * IntegrationSystemException Test Class
 */
class IntegrationSystemExceptionTest {

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with systemId and cause")
        void shouldCreateExceptionWithSystemIdAndCause() {
            Throwable cause = new RuntimeException("Root cause");
            IntegrationSystemException exception = new IntegrationSystemException("MCI", cause);

            assertNotNull(exception.getErrorCode());
            assertSame(cause, exception.getCause());
            assertEquals("MCI", exception.getErrorLocation());
        }

        @Test
        @DisplayName("Should create exception with systemId and errorCode")
        void shouldCreateExceptionWithSystemIdAndErrorCode() {
            IntegrationSystemException exception = new IntegrationSystemException("FEP", "CUSTOM_ERR");

            assertEquals("CUSTOM_ERR", exception.getErrorCode());
            assertEquals("FEP", exception.getErrorLocation());
        }

        @Test
        @DisplayName("Should create exception with systemId, errorCode and cause")
        void shouldCreateExceptionWithSystemIdErrorCodeAndCause() {
            Throwable cause = new RuntimeException("Root cause");
            IntegrationSystemException exception = new IntegrationSystemException("HOST", "ERR001", cause);

            assertEquals("ERR001", exception.getErrorCode());
            assertSame(cause, exception.getCause());
            assertEquals("HOST", exception.getErrorLocation());
        }
    }

    @Nested
    @DisplayName("inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend IntegrationException")
        void shouldExtendIntegrationException() {
            IntegrationSystemException exception = new IntegrationSystemException("MCI", "ERR001");

            assertTrue(exception instanceof IntegrationException);
        }
    }

    @Nested
    @DisplayName("errorLocation tests")
    class ErrorLocationTests {

        @Test
        @DisplayName("Should set and get errorLocation")
        void shouldSetAndGetErrorLocation() {
            IntegrationSystemException exception = new IntegrationSystemException("MCI", "ERR001");

            assertEquals("MCI", exception.getErrorLocation());

            exception.setErrorLocation("FEP");
            assertEquals("FEP", exception.getErrorLocation());
        }

        @Test
        @DisplayName("Should handle null systemId")
        void shouldHandleNullSystemId() {
            IntegrationSystemException exception = new IntegrationSystemException(null, "ERR001");

            assertNull(exception.getErrorLocation());
        }
    }
}
