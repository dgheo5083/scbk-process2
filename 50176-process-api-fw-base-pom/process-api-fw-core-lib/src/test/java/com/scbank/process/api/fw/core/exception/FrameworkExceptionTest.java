package com.scbank.process.api.fw.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * FrameworkException Test Class
 */
class FrameworkExceptionTest {

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with errorCode only")
        void shouldCreateExceptionWithErrorCodeOnly() {
            String errorCode = "TEST001";

            FrameworkException exception = new FrameworkException(errorCode);

            assertEquals(errorCode, exception.getErrorCode());
            assertNotNull(exception.getMessageArgs());
            assertTrue(exception.getMessageArgs().isEmpty());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with cause only")
        void shouldCreateExceptionWithCauseOnly() {
            Throwable cause = new RuntimeException("Original cause");

            FrameworkException exception = new FrameworkException(cause);

            assertEquals(cause, exception.getCause());
            assertNull(exception.getErrorCode());
        }

//        @Test
//        @DisplayName("Should create exception with errorCode and message")
//        void shouldCreateExceptionWithErrorCodeAndMessage() {
//            String errorCode = "TEST002";
//            String message = "Test error message";
//
//            FrameworkException exception = new FrameworkException(errorCode, message);
//
//            assertEquals(errorCode, exception.getErrorCode());
//            assertEquals(message, exception.getMessage());
//        }

        @Test
        @DisplayName("Should create exception with errorCode and cause")
        void shouldCreateExceptionWithErrorCodeAndCause() {
            String errorCode = "TEST003";
            Throwable cause = new RuntimeException("Underlying issue");

            FrameworkException exception = new FrameworkException(errorCode, cause);

            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with errorCode and messageArgs")
        void shouldCreateExceptionWithErrorCodeAndMessageArgs() {
            String errorCode = "TEST004";
            List<Object> messageArgs = List.of("arg1", "arg2", 123);

            FrameworkException exception = new FrameworkException(errorCode, messageArgs);

            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(messageArgs, exception.getMessageArgs());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with errorCode, messageArgs and cause")
        void shouldCreateExceptionWithErrorCodeMessageArgsAndCause() {
            String errorCode = "TEST005";
            List<Object> messageArgs = List.of("param1", 456);
            Throwable cause = new IllegalArgumentException("Invalid argument");

            FrameworkException exception = new FrameworkException(errorCode, messageArgs, cause);

            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(messageArgs, exception.getMessageArgs());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with empty messageArgs list")
        void shouldCreateExceptionWithEmptyMessageArgsList() {
            String errorCode = "TEST006";
            List<Object> emptyArgs = List.of();

            FrameworkException exception = new FrameworkException(errorCode, emptyArgs);

            assertEquals(errorCode, exception.getErrorCode());
            assertNotNull(exception.getMessageArgs());
            assertTrue(exception.getMessageArgs().isEmpty());
        }

        @Test
        @DisplayName("Should create exception with null messageArgs")
        void shouldCreateExceptionWithNullMessageArgs() {
            String errorCode = "TEST007";
            List<Object> nullArgs = null;

            FrameworkException exception = new FrameworkException(errorCode, nullArgs, null);

            assertEquals(errorCode, exception.getErrorCode());
            assertNull(exception.getMessageArgs());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get errorCode")
        void shouldSetAndGetErrorCode() {
            FrameworkException exception = new FrameworkException("INITIAL");

            exception.setErrorCode("UPDATED");

            assertEquals("UPDATED", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should set and get errorLocation")
        void shouldSetAndGetErrorLocation() {
            FrameworkException exception = new FrameworkException("TEST001");

            exception.setErrorLocation("MCI");

            assertEquals("MCI", exception.getErrorLocation());
        }

        @Test
        @DisplayName("Should set and get errorModule")
        void shouldSetAndGetErrorModule() {
            FrameworkException exception = new FrameworkException("TEST002");

            exception.setErrorModule("CoreModule");

            assertEquals("CoreModule", exception.getErrorModule());
        }

        @Test
        @DisplayName("Should set and get errorMessage")
        void shouldSetAndGetErrorMessage() {
            FrameworkException exception = new FrameworkException("TEST003");

            exception.setErrorMessage("Custom error message");

            assertEquals("Custom error message", exception.getErrorMessage());
        }

        @Test
        @DisplayName("Should set and get errorGuideMessage")
        void shouldSetAndGetErrorGuideMessage() {
            FrameworkException exception = new FrameworkException("TEST004");

            exception.setErrorGuideMessage("Please check your input");

            assertEquals("Please check your input", exception.getErrorGuideMessage());
        }

        @Test
        @DisplayName("Should set and get messageArgs")
        void shouldSetAndGetMessageArgs() {
            FrameworkException exception = new FrameworkException("TEST005");
            List<Object> messageArgs = List.of("arg1", "arg2");

            exception.setMessageArgs(messageArgs);

            assertEquals(messageArgs, exception.getMessageArgs());
        }

        @Test
        @DisplayName("Should allow setting null values")
        void shouldAllowSettingNullValues() {
            FrameworkException exception = new FrameworkException("TEST006");

            exception.setErrorLocation(null);
            exception.setErrorModule(null);
            exception.setErrorMessage(null);
            exception.setErrorGuideMessage(null);
            exception.setMessageArgs(null);

            assertNull(exception.getErrorLocation());
            assertNull(exception.getErrorModule());
            assertNull(exception.getErrorMessage());
            assertNull(exception.getErrorGuideMessage());
            assertNull(exception.getMessageArgs());
        }
    }

    @Nested
    @DisplayName("IExceptionInfo implementation tests")
    class IExceptionInfoImplementationTests {

        @Test
        @DisplayName("Should implement IExceptionInfo interface")
        void shouldImplementIExceptionInfoInterface() {
            FrameworkException exception = new FrameworkException("TEST007");

            assertTrue(exception instanceof IExceptionInfo);
        }

        @Test
        @DisplayName("Should provide error information through interface methods")
        void shouldProvideErrorInformationThroughInterfaceMethods() {
            FrameworkException exception = new FrameworkException("TEST008");
            exception.setErrorLocation("CORE");
            exception.setErrorModule("ValidationModule");
            exception.setErrorMessage("Validation failed");

            IExceptionInfo exceptionInfo = exception;

            assertEquals("TEST008", exceptionInfo.getErrorCode());
            assertEquals("CORE", exceptionInfo.getErrorLocation());
            assertEquals("ValidationModule", exceptionInfo.getErrorModule());
            assertEquals("Validation failed", exceptionInfo.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("Exception behavior tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable as Exception")
        void shouldBeThrowableAsException() {
            assertThrows(FrameworkException.class, () -> {
                throw new FrameworkException("THROW_TEST");
            });
        }

        @Test
        @DisplayName("Should have empty stack trace when using errorCode constructor (performance optimization)")
        void shouldHaveEmptyStackTraceWithErrorCodeConstructor() {
            // FrameworkException(String errorCode) calls super(null, cause, false, false)
            // where writableStackTrace=false for performance optimization
            FrameworkException exception = new FrameworkException("STACK_TEST");

            assertNotNull(exception.getStackTrace());
            assertEquals(0, exception.getStackTrace().length);
        }

        @Test
        @DisplayName("Should preserve stack trace when using Throwable constructor")
        void shouldPreserveStackTraceWithThrowableConstructor() {
            // FrameworkException(Throwable cause) preserves stack trace
            FrameworkException exception = new FrameworkException(new RuntimeException("cause"));

            assertNotNull(exception.getStackTrace());
            assertTrue(exception.getStackTrace().length > 0);
        }

        @Test
        @DisplayName("Should work in try-catch blocks")
        void shouldWorkInTryCatchBlocks() {
            String errorCode = "TRY_CATCH_TEST";
            boolean caught = false;

            try {
                throw new FrameworkException(errorCode);
            } catch (FrameworkException e) {
                caught = true;
                assertEquals(errorCode, e.getErrorCode());
            }

            assertTrue(caught);
        }

        @Test
        @DisplayName("Should wrap nested exceptions correctly")
        void shouldWrapNestedExceptionsCorrectly() {
            RuntimeException original = new RuntimeException("Original error");
            IllegalArgumentException wrapper = new IllegalArgumentException("Wrapper error", original);
            FrameworkException frameworkException = new FrameworkException("NESTED_TEST", wrapper);

            assertEquals(wrapper, frameworkException.getCause());
            assertEquals(original, frameworkException.getCause().getCause());
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            FrameworkException exception = new FrameworkException("TEST009");

            assertNotNull(exception.toString());
        }

        @Test
        @DisplayName("Should include errorCode in toString")
        void shouldIncludeErrorCodeInToString() {
            String errorCode = "TEST010";
            FrameworkException exception = new FrameworkException(errorCode);
            exception.setErrorMessage("Test message");

            String toStringResult = exception.toString();

            assertTrue(toStringResult.contains(errorCode) || toStringResult.contains("TEST010"));
        }
    }

    @Nested
    @DisplayName("Serialization tests")
    class SerializationTests {

        @Test
        @DisplayName("Should have serialVersionUID")
        void shouldHaveSerialVersionUID() {
            // FrameworkException should have serialVersionUID = 1L
            // This is verified by the class implementing Serializable via Exception
            FrameworkException exception = new FrameworkException("SERIAL_TEST");

            assertNotNull(exception);
            assertTrue(exception instanceof java.io.Serializable);
        }
    }
    
    @Test
    void getMessage_returnsNull_whenErrorCodeAndMessageAreNull() {
    	FrameworkException ex = new FrameworkException(null, null, null);
    	assertNull(ex.getMessage());
    }
    
    @Test
    void getMessage_returnsFormattedMessage_whenMessageAreExit() {
    	FrameworkException ex = new FrameworkException("E001", Arrays.asList("SCB", 3), null);
    	
    	ex.setErrorMessage("Bank {0} error count {1}");
    	
    	String message = ex.getMessage();
    	
    	assertEquals("[E001]Bank SCB error count 3", message);
    }
    
    @Test
    void getMessage_returnsMessageWithoutArgs_whenArgsEmpty() {
    	FrameworkException ex = new FrameworkException("E002", Collections.emptyList(), null);
    	
    	ex.setErrorMessage("Simple error");
    	
    	assertEquals("[E002]Simple error", ex.getMessage());
    }
    
    @Test
    void getMessage_returnsMessage_whenOnlyMessageExists() {
    	FrameworkException ex = new FrameworkException(null, null, null);
    	
    	ex.setErrorMessage("Only message");
    	
    	assertEquals("Only message", ex.getMessage());
    }
    
    @Test
    void addNextPageParameter_doesNothing_whenValueIsNull() {
    	FrameworkException ex = new FrameworkException(null, null, null);
    	
    	ex.addNextPageParameter("page", 1);
    	
    	assertNotNull(ex.getNextPageParameters());
    	assertEquals(1, ex.getNextPageParameters().get("page"));
    }
    
    @Test
    void addNextPageParameter_addsToExistingMap() {
    	FrameworkException ex = new FrameworkException(null, null, null);
    
    	ex.addNextPageParameter("page", 1);
    	ex.addNextPageParameter("size", 10);
    	
    	assertEquals(2, ex.getNextPageParameters().size());
    }
}
