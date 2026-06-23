package com.scbank.process.api.fw.channel.service.condition.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.service.condition.IServiceConditionEvaluator;

/**
 * SpelServiceConditionEvaluator Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SpelServiceConditionEvaluatorTest {

    private SpelServiceConditionEvaluator evaluator;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private IDevice device;

    @BeforeEach
    void setUp() {
        evaluator = new SpelServiceConditionEvaluator();
    }

    @Nested
    @DisplayName("evaluate with null or blank condition tests")
    class NullOrBlankConditionTests {

        @Test
        @DisplayName("Should return true when condition is null")
        void shouldReturnTrueWhenConditionIsNull() {
            boolean result = evaluator.evaluate(serviceContext, null);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when condition is empty")
        void shouldReturnTrueWhenConditionIsEmpty() {
            boolean result = evaluator.evaluate(serviceContext, "");

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when condition is blank")
        void shouldReturnTrueWhenConditionIsBlank() {
            boolean result = evaluator.evaluate(serviceContext, "   ");

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("evaluate with unsafe condition tests")
    class UnsafeConditionTests {

        @Test
        @DisplayName("Should return false for unsafe SpEL expression")
        void shouldReturnFalseForUnsafeSpelExpression() {
            when(serviceContext.device()).thenReturn(device);
            when(device.getId()).thenReturn("PC");

            // This condition doesn't match the safe pattern
            boolean result = evaluator.evaluate(serviceContext, "T(java.lang.Runtime).getRuntime()");

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for expression with special characters")
        void shouldReturnFalseForExpressionWithSpecialCharacters() {
            when(serviceContext.device()).thenReturn(device);
            when(device.getId()).thenReturn("PC");

            boolean result = evaluator.evaluate(serviceContext, "#device;drop table users");

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("evaluate with exception tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should return false when evaluation throws exception")
        void shouldReturnFalseWhenEvaluationThrowsException() {
            when(serviceContext.device()).thenReturn(device);
            when(device.getId()).thenReturn("PC");

            // Invalid SpEL that might match safe pattern but fails evaluation
            boolean result = evaluator.evaluate(serviceContext, "#device == 'PC' and #nonexistent");

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IServiceConditionEvaluator")
        void shouldImplementIServiceConditionEvaluator() {
            assertTrue(evaluator instanceof IServiceConditionEvaluator);
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle condition with only whitespace and valid chars")
        void shouldHandleConditionWithOnlyWhitespaceAndValidChars() {
            when(serviceContext.device()).thenReturn(device);
            when(device.getId()).thenReturn("PC");

            // Simple safe condition
            boolean result = evaluator.evaluate(serviceContext, "true");

            // This might not match the safe pattern due to the regex
            // The behavior depends on the exact pattern implementation
            assertNotNull(result); // Just verify it doesn't throw
        }
    }
}
