package com.scbank.process.api.fw.common.errorcode.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;

/**
 * DefaultErrorGuideMessageInfo Test Class
 */
class DefaultErrorGuideMessageInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("Please try again later")
                    .order(1)
                    .build();

            assertEquals("Please try again later", guideMessageInfo.getMessage());
            assertEquals(1, guideMessageInfo.getOrder());
        }

        @Test
        @DisplayName("Should build with null message")
        void shouldBuildWithNullMessage() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message(null)
                    .order(0)
                    .build();

            assertNull(guideMessageInfo.getMessage());
        }

        @Test
        @DisplayName("Should build with default order")
        void shouldBuildWithDefaultOrder() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("Guide message")
                    .build();

            assertEquals(0, guideMessageInfo.getOrder());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get message")
        void shouldSetAndGetMessage() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder().build();
            guideMessageInfo.setMessage("New message");

            assertEquals("New message", guideMessageInfo.getMessage());
        }

        @Test
        @DisplayName("Should set and get order")
        void shouldSetAndGetOrder() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder().build();
            guideMessageInfo.setOrder(5);

            assertEquals(5, guideMessageInfo.getOrder());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IErrorGuideMessageInfo")
        void shouldImplementIErrorGuideMessageInfo() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder().build();

            assertTrue(guideMessageInfo instanceof IErrorGuideMessageInfo);
        }
    }

    @Nested
    @DisplayName("Comparable tests")
    class ComparableTests {

        @Test
        @DisplayName("Should compare by order ascending")
        void shouldCompareByOrderAscending() {
            DefaultErrorGuideMessageInfo info1 = DefaultErrorGuideMessageInfo.builder()
                    .message("First")
                    .order(1)
                    .build();
            DefaultErrorGuideMessageInfo info2 = DefaultErrorGuideMessageInfo.builder()
                    .message("Second")
                    .order(2)
                    .build();

            assertTrue(info1.compareTo(info2) < 0);
            assertTrue(info2.compareTo(info1) > 0);
        }

        @Test
        @DisplayName("Should compare equal for same order")
        void shouldCompareEqualForSameOrder() {
            DefaultErrorGuideMessageInfo info1 = DefaultErrorGuideMessageInfo.builder()
                    .message("First")
                    .order(1)
                    .build();
            DefaultErrorGuideMessageInfo info2 = DefaultErrorGuideMessageInfo.builder()
                    .message("Second")
                    .order(1)
                    .build();

            assertEquals(0, info1.compareTo(info2));
        }

        @Test
        @DisplayName("Should handle negative orders")
        void shouldHandleNegativeOrders() {
            DefaultErrorGuideMessageInfo info1 = DefaultErrorGuideMessageInfo.builder()
                    .message("Negative")
                    .order(-1)
                    .build();
            DefaultErrorGuideMessageInfo info2 = DefaultErrorGuideMessageInfo.builder()
                    .message("Zero")
                    .order(0)
                    .build();

            assertTrue(info1.compareTo(info2) < 0);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultErrorGuideMessageInfo info1 = DefaultErrorGuideMessageInfo.builder()
                    .message("Message")
                    .order(1)
                    .build();
            DefaultErrorGuideMessageInfo info2 = DefaultErrorGuideMessageInfo.builder()
                    .message("Message")
                    .order(1)
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different messages")
        void shouldNotBeEqualForDifferentMessages() {
            DefaultErrorGuideMessageInfo info1 = DefaultErrorGuideMessageInfo.builder()
                    .message("Message 1")
                    .order(1)
                    .build();
            DefaultErrorGuideMessageInfo info2 = DefaultErrorGuideMessageInfo.builder()
                    .message("Message 2")
                    .order(1)
                    .build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different orders")
        void shouldNotBeEqualForDifferentOrders() {
            DefaultErrorGuideMessageInfo info1 = DefaultErrorGuideMessageInfo.builder()
                    .message("Message")
                    .order(1)
                    .build();
            DefaultErrorGuideMessageInfo info2 = DefaultErrorGuideMessageInfo.builder()
                    .message("Message")
                    .order(2)
                    .build();

            assertNotEquals(info1, info2);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("Test message")
                    .order(1)
                    .build();

            assertNotNull(guideMessageInfo.toString());
            assertTrue(guideMessageInfo.toString().contains("Test message"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty message")
        void shouldHandleEmptyMessage() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("")
                    .build();

            assertEquals("", guideMessageInfo.getMessage());
        }

        @Test
        @DisplayName("Should handle large order values")
        void shouldHandleLargeOrderValues() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("Large order")
                    .order(Integer.MAX_VALUE)
                    .build();

            assertEquals(Integer.MAX_VALUE, guideMessageInfo.getOrder());
        }

        @Test
        @DisplayName("Should handle minimum order values")
        void shouldHandleMinimumOrderValues() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("Min order")
                    .order(Integer.MIN_VALUE)
                    .build();

            assertEquals(Integer.MIN_VALUE, guideMessageInfo.getOrder());
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            DefaultErrorGuideMessageInfo guideMessageInfo = DefaultErrorGuideMessageInfo.builder()
                    .message("메시지 with <html> & 'special' \"chars\"")
                    .build();

            assertTrue(guideMessageInfo.getMessage().contains("<html>"));
            assertTrue(guideMessageInfo.getMessage().contains("메시지"));
        }
    }
}
