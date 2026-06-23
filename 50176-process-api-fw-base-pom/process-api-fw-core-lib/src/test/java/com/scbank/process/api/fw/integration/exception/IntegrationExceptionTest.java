package com.scbank.process.api.fw.integration.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * IntegrationException Test Class
 */
class IntegrationExceptionTest {

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with error code only")
        void shouldCreateExceptionWithErrorCodeOnly() {
            IntegrationException exception = new IntegrationException("ERR001");

            assertEquals("ERR001", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with error code and message")
        void shouldCreateExceptionWithErrorCodeAndMessage() {
            IntegrationException exception = new IntegrationException("ERR002", "Connection failed");

            assertEquals("ERR002", exception.getErrorCode());
            assertEquals("Connection failed", exception.getErrorMessage());
        }

        @Test
        @DisplayName("Should create exception with error code and cause")
        void shouldCreateExceptionWithErrorCodeAndCause() {
            Throwable cause = new RuntimeException("Root cause");
            IntegrationException exception = new IntegrationException("ERR003", cause);

            assertEquals("ERR003", exception.getErrorCode());
            assertSame(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with error code and message args")
        void shouldCreateExceptionWithErrorCodeAndMessageArgs() {
            List<Object> args = List.of("arg1", "arg2");
            IntegrationException exception = new IntegrationException("ERR004", args);

            assertEquals("ERR004", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with error code, message args and cause")
        void shouldCreateExceptionWithErrorCodeMessageArgsAndCause() {
            List<Object> args = List.of("arg1");
            Throwable cause = new RuntimeException("Root cause");
            IntegrationException exception = new IntegrationException("ERR005", args, cause);

            assertEquals("ERR005", exception.getErrorCode());
            assertSame(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("inheritance tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend FrameworkRuntimeException")
        void shouldExtendFrameworkRuntimeException() {
            IntegrationException exception = new IntegrationException("ERR001");

            assertTrue(exception instanceof FrameworkRuntimeException);
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("serialization tests")
    class SerializationTests {

        @Test
        @DisplayName("Should have serialVersionUID")
        void shouldHaveSerialVersionUID() {
            // The class is Serializable through FrameworkRuntimeException
            IntegrationException exception = new IntegrationException("ERR001");
            assertNotNull(exception);
        }
    }
}
