package com.scbank.process.api.fw.channel.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * GlobalExceptionAdvice Test Class
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionAdviceTest {

    @Mock
    private IResponseMessageFactory<?, ?, ?> responseMessageFactory;

    @Mock
    private IResponseMessage<?, ?> responseMessage;

    private GlobalExceptionAdvice exceptionAdvice;

    @BeforeEach
    void setUp() {
        exceptionAdvice = new GlobalExceptionAdvice();
    }

    @AfterEach
    void tearDown() {
        TraceContextHolder.clear();
    }

    @Nested
    @DisplayName("handleValidationException tests")
    class HandleValidationExceptionTests {

        @Test
        @DisplayName("Should return BAD_REQUEST for ConstraintViolationException")
        void shouldReturnBadRequestForConstraintViolation() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                Set<ConstraintViolation<?>> violations = new HashSet<>();
                ConstraintViolationException ex = new ConstraintViolationException("Validation failed", violations);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                        .thenReturn(responseMessageFactory);
                doReturn(responseMessage).when(responseMessageFactory).fail(ex);

                // When
                ResponseEntity<?> result = exceptionAdvice.handleValidationException(ex);

                // Then
                assertNotNull(result);
                assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
                assertEquals(responseMessage, result.getBody());
            }
        }

        @Test
        @DisplayName("Should log trace when TraceContext is present")
        void shouldLogTraceWhenContextPresent() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                Set<ConstraintViolation<?>> violations = new HashSet<>();
                ConstraintViolationException ex = new ConstraintViolationException("Validation failed", violations);
                TraceContext traceContext = mock(TraceContext.class);
                TraceContextHolder.set(traceContext);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                        .thenReturn(responseMessageFactory);
                doReturn(responseMessage).when(responseMessageFactory).fail(ex);

                // When
                exceptionAdvice.handleValidationException(ex);

                // Then
                verify(traceContext).fail(ex);
            }
        }
    }

    @Nested
    @DisplayName("handleThrowable tests")
    class HandleThrowableTests {

        @Test
        @DisplayName("Should return INTERNAL_SERVER_ERROR for Throwable")
        void shouldReturnInternalServerErrorForThrowable() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                Throwable ex = new RuntimeException("Test error");

                runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                        .thenReturn(responseMessageFactory);
                doReturn(responseMessage).when(responseMessageFactory).fail(ex);

                // When
                ResponseEntity<IResponseMessage<Object, Object>> result = exceptionAdvice.handleThrowable(ex);

                // Then
                assertNotNull(result);
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            }
        }

        @Test
        @DisplayName("Should handle NullPointerException")
        void shouldHandleNullPointerException() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                NullPointerException ex = new NullPointerException("Null reference");

                runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                        .thenReturn(responseMessageFactory);
                doReturn(responseMessage).when(responseMessageFactory).fail(ex);

                // When
                ResponseEntity<IResponseMessage<Object, Object>> result = exceptionAdvice.handleThrowable(ex);

                // Then
                assertNotNull(result);
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            }
        }

        @Test
        @DisplayName("Should handle IllegalArgumentException")
        void shouldHandleIllegalArgumentException() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

                runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                        .thenReturn(responseMessageFactory);
                doReturn(responseMessage).when(responseMessageFactory).fail(ex);

                // When
                ResponseEntity<IResponseMessage<Object, Object>> result = exceptionAdvice.handleThrowable(ex);

                // Then
                assertNotNull(result);
                assertEquals(500, result.getStatusCode().value());
            }
        }

        @Test
        @DisplayName("Should log trace for Throwable when TraceContext is present")
        void shouldLogTraceForThrowable() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                RuntimeException ex = new RuntimeException("Test");
                TraceContext traceContext = mock(TraceContext.class);
                TraceContextHolder.set(traceContext);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                        .thenReturn(responseMessageFactory);
                doReturn(responseMessage).when(responseMessageFactory).fail(ex);

                // When
                exceptionAdvice.handleThrowable(ex);

                // Then
                verify(traceContext).fail(ex);
            }
        }
    }
}
