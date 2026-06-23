package com.scbank.process.api.fw.common.servicetime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ServiceTimeGroup Enum Test Class
 */
class ServiceTimeGroupTest {

    @Nested
    @DisplayName("Enum values tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have MENU value")
        void shouldHaveMenuValue() {
            ServiceTimeGroup menu = ServiceTimeGroup.MENU;

            assertNotNull(menu);
            assertEquals("MENU", menu.name());
        }

        @Test
        @DisplayName("Should have MESSAGE value")
        void shouldHaveMessageValue() {
            ServiceTimeGroup message = ServiceTimeGroup.MESSAGE;

            assertNotNull(message);
            assertEquals("MESSAGE", message.name());
        }

        @Test
        @DisplayName("Should have UNKNOWN value")
        void shouldHaveUnknownValue() {
            ServiceTimeGroup unknown = ServiceTimeGroup.UNKNOWN;

            assertNotNull(unknown);
            assertEquals("UNKNOWN", unknown.name());
        }

        @Test
        @DisplayName("Should have exactly 3 enum values")
        void shouldHaveExactlyThreeEnumValues() {
            ServiceTimeGroup[] values = ServiceTimeGroup.values();

            assertEquals(3, values.length);
        }
    }

    @Nested
    @DisplayName("of method tests")
    class OfMethodTests {

        @Test
        @DisplayName("Should return MENU for 'menu' lowercase")
        void shouldReturnMenuForMenuLowercase() {
            ServiceTimeGroup result = ServiceTimeGroup.of("menu");

            assertEquals(ServiceTimeGroup.MENU, result);
        }

        @Test
        @DisplayName("Should return MENU for 'MENU' uppercase")
        void shouldReturnMenuForMenuUppercase() {
            ServiceTimeGroup result = ServiceTimeGroup.of("MENU");

            assertEquals(ServiceTimeGroup.MENU, result);
        }

        @Test
        @DisplayName("Should return MENU for 'Menu' mixed case")
        void shouldReturnMenuForMenuMixedCase() {
            ServiceTimeGroup result = ServiceTimeGroup.of("Menu");

            assertEquals(ServiceTimeGroup.MENU, result);
        }

        @Test
        @DisplayName("Should return MESSAGE for 'message' lowercase")
        void shouldReturnMessageForMessageLowercase() {
            ServiceTimeGroup result = ServiceTimeGroup.of("message");

            assertEquals(ServiceTimeGroup.MESSAGE, result);
        }

        @Test
        @DisplayName("Should return MESSAGE for 'MESSAGE' uppercase")
        void shouldReturnMessageForMessageUppercase() {
            ServiceTimeGroup result = ServiceTimeGroup.of("MESSAGE");

            assertEquals(ServiceTimeGroup.MESSAGE, result);
        }

        @Test
        @DisplayName("Should return MESSAGE for 'Message' mixed case")
        void shouldReturnMessageForMessageMixedCase() {
            ServiceTimeGroup result = ServiceTimeGroup.of("Message");

            assertEquals(ServiceTimeGroup.MESSAGE, result);
        }

        @Test
        @DisplayName("Should return UNKNOWN for unrecognized type")
        void shouldReturnUnknownForUnrecognizedType() {
            ServiceTimeGroup result = ServiceTimeGroup.of("invalid");

            assertEquals(ServiceTimeGroup.UNKNOWN, result);
        }

        @Test
        @DisplayName("Should return UNKNOWN for empty string")
        void shouldReturnUnknownForEmptyString() {
            ServiceTimeGroup result = ServiceTimeGroup.of("");

            assertEquals(ServiceTimeGroup.UNKNOWN, result);
        }

        @Test
        @DisplayName("Should return UNKNOWN for whitespace")
        void shouldReturnUnknownForWhitespace() {
            ServiceTimeGroup result = ServiceTimeGroup.of("   ");

            assertEquals(ServiceTimeGroup.UNKNOWN, result);
        }

        @Test
        @DisplayName("Should return UNKNOWN for similar but different strings")
        void shouldReturnUnknownForSimilarButDifferentStrings() {
            assertEquals(ServiceTimeGroup.UNKNOWN, ServiceTimeGroup.of("menus"));
            assertEquals(ServiceTimeGroup.UNKNOWN, ServiceTimeGroup.of("msg"));
            assertEquals(ServiceTimeGroup.UNKNOWN, ServiceTimeGroup.of("messages"));
        }
    }

    @Nested
    @DisplayName("valueOf tests")
    class ValueOfTests {

        @Test
        @DisplayName("Should get MENU by valueOf")
        void shouldGetMenuByValueOf() {
            ServiceTimeGroup result = ServiceTimeGroup.valueOf("MENU");

            assertEquals(ServiceTimeGroup.MENU, result);
        }

        @Test
        @DisplayName("Should get MESSAGE by valueOf")
        void shouldGetMessageByValueOf() {
            ServiceTimeGroup result = ServiceTimeGroup.valueOf("MESSAGE");

            assertEquals(ServiceTimeGroup.MESSAGE, result);
        }

        @Test
        @DisplayName("Should get UNKNOWN by valueOf")
        void shouldGetUnknownByValueOf() {
            ServiceTimeGroup result = ServiceTimeGroup.valueOf("UNKNOWN");

            assertEquals(ServiceTimeGroup.UNKNOWN, result);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void shouldThrowExceptionForInvalidValueOf() {
            assertThrows(IllegalArgumentException.class, () -> {
                ServiceTimeGroup.valueOf("INVALID");
            });
        }
    }

    @Nested
    @DisplayName("Ordinal tests")
    class OrdinalTests {

        @Test
        @DisplayName("Should have correct ordinal for MENU")
        void shouldHaveCorrectOrdinalForMenu() {
            assertEquals(0, ServiceTimeGroup.MENU.ordinal());
        }

        @Test
        @DisplayName("Should have correct ordinal for MESSAGE")
        void shouldHaveCorrectOrdinalForMessage() {
            assertEquals(1, ServiceTimeGroup.MESSAGE.ordinal());
        }

        @Test
        @DisplayName("Should have correct ordinal for UNKNOWN")
        void shouldHaveCorrectOrdinalForUnknown() {
            assertEquals(2, ServiceTimeGroup.UNKNOWN.ordinal());
        }
    }

    @Nested
    @DisplayName("Equals and comparison tests")
    class EqualsComparisonTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            ServiceTimeGroup menu = ServiceTimeGroup.MENU;

            assertEquals(menu, ServiceTimeGroup.MENU);
        }

        @Test
        @DisplayName("Should not be equal to other enum values")
        void shouldNotBeEqualToOtherEnumValues() {
            assertNotEquals(ServiceTimeGroup.MENU, ServiceTimeGroup.MESSAGE);
            assertNotEquals(ServiceTimeGroup.MENU, ServiceTimeGroup.UNKNOWN);
            assertNotEquals(ServiceTimeGroup.MESSAGE, ServiceTimeGroup.UNKNOWN);
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle leading and trailing whitespace in type")
        void shouldHandleLeadingAndTrailingWhitespaceInType() {
            // Note: the of() method doesn't trim, so these should return UNKNOWN
            ServiceTimeGroup result1 = ServiceTimeGroup.of(" menu ");
            ServiceTimeGroup result2 = ServiceTimeGroup.of(" message ");

            // Since the implementation doesn't trim, these should be UNKNOWN
            assertEquals(ServiceTimeGroup.UNKNOWN, result1);
            assertEquals(ServiceTimeGroup.UNKNOWN, result2);
        }

        @Test
        @DisplayName("Should handle MeNu case insensitively")
        void shouldHandleMixedCaseInsensitively() {
            ServiceTimeGroup result = ServiceTimeGroup.of("MeNu");

            assertEquals(ServiceTimeGroup.MENU, result);
        }

        @Test
        @DisplayName("Should handle mEsSaGe case insensitively")
        void shouldHandleMessageMixedCaseInsensitively() {
            ServiceTimeGroup result = ServiceTimeGroup.of("mEsSaGe");

            assertEquals(ServiceTimeGroup.MESSAGE, result);
        }
    }
}
