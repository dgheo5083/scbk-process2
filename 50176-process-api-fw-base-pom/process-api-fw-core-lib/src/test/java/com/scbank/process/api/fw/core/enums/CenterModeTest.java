package com.scbank.process.api.fw.core.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * CenterMode Enum Test Class
 */
class CenterModeTest {

    @Nested
    @DisplayName("Enum constants tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have MAIN constant")
        void shouldHaveMainConstant() {
            CenterMode mode = CenterMode.MAIN;

            assertNotNull(mode);
            assertEquals("MAIN", mode.name());
        }

        @Test
        @DisplayName("Should have DR constant")
        void shouldHaveDrConstant() {
            CenterMode mode = CenterMode.DR;

            assertNotNull(mode);
            assertEquals("DR", mode.name());
        }

        @Test
        @DisplayName("Should have STRESS constant")
        void shouldHaveStressConstant() {
            CenterMode mode = CenterMode.STRESS;

            assertNotNull(mode);
            assertEquals("STRESS", mode.name());
        }

        @Test
        @DisplayName("Should have NONE constant")
        void shouldHaveNoneConstant() {
            CenterMode mode = CenterMode.NONE;

            assertNotNull(mode);
            assertEquals("NONE", mode.name());
        }

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourEnumValues() {
            CenterMode[] values = CenterMode.values();

            assertEquals(4, values.length);
        }
    }

    @Nested
    @DisplayName("of() method tests")
    class OfMethodTests {

        @ParameterizedTest
        @CsvSource({
            "main, MAIN",
            "MAIN, MAIN",
            "Main, MAIN",
            "mAiN, MAIN"
        })
        @DisplayName("Should return MAIN for main variations")
        void shouldReturnMainForMainVariations(String input, CenterMode expected) {
            CenterMode result = CenterMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "dr, DR",
            "DR, DR",
            "Dr, DR",
            "dR, DR"
        })
        @DisplayName("Should return DR for dr variations")
        void shouldReturnDrForDrVariations(String input, CenterMode expected) {
            CenterMode result = CenterMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "stress, STRESS",
            "STRESS, STRESS",
            "Stress, STRESS",
            "StReSs, STRESS"
        })
        @DisplayName("Should return STRESS for stress variations")
        void shouldReturnStressForStressVariations(String input, CenterMode expected) {
            CenterMode result = CenterMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "none",
            "NONE",
            "invalid",
            "unknown",
            "test",
            "",
            "  ",
            "random"
        })
        @DisplayName("Should return NONE for unknown or invalid values")
        void shouldReturnNoneForUnknownOrInvalidValues(String input) {
            CenterMode result = CenterMode.of(input);

            assertEquals(CenterMode.NONE, result);
        }

        @Test
        @DisplayName("Should handle lowercase input")
        void shouldHandleLowercaseInput() {
            assertEquals(CenterMode.MAIN, CenterMode.of("main"));
            assertEquals(CenterMode.DR, CenterMode.of("dr"));
            assertEquals(CenterMode.STRESS, CenterMode.of("stress"));
        }

        @Test
        @DisplayName("Should handle uppercase input")
        void shouldHandleUppercaseInput() {
            assertEquals(CenterMode.MAIN, CenterMode.of("MAIN"));
            assertEquals(CenterMode.DR, CenterMode.of("DR"));
            assertEquals(CenterMode.STRESS, CenterMode.of("STRESS"));
        }

        @Test
        @DisplayName("Should handle mixed case input")
        void shouldHandleMixedCaseInput() {
            assertEquals(CenterMode.MAIN, CenterMode.of("MaIn"));
            assertEquals(CenterMode.DR, CenterMode.of("Dr"));
            assertEquals(CenterMode.STRESS, CenterMode.of("StReSs"));
        }
    }

    @Nested
    @DisplayName("Enum valueOf tests")
    class ValueOfTests {

        @Test
        @DisplayName("Should get MAIN using valueOf")
        void shouldGetMainUsingValueOf() {
            CenterMode mode = CenterMode.valueOf("MAIN");

            assertEquals(CenterMode.MAIN, mode);
        }

        @Test
        @DisplayName("Should get DR using valueOf")
        void shouldGetDrUsingValueOf() {
            CenterMode mode = CenterMode.valueOf("DR");

            assertEquals(CenterMode.DR, mode);
        }

        @Test
        @DisplayName("Should get STRESS using valueOf")
        void shouldGetStressUsingValueOf() {
            CenterMode mode = CenterMode.valueOf("STRESS");

            assertEquals(CenterMode.STRESS, mode);
        }

        @Test
        @DisplayName("Should get NONE using valueOf")
        void shouldGetNoneUsingValueOf() {
            CenterMode mode = CenterMode.valueOf("NONE");

            assertEquals(CenterMode.NONE, mode);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid valueOf")
        void shouldThrowExceptionForInvalidValueOf() {
            assertThrows(IllegalArgumentException.class, () -> CenterMode.valueOf("INVALID"));
        }

        @Test
        @DisplayName("Should throw NullPointerException for null valueOf")
        void shouldThrowExceptionForNullValueOf() {
            assertThrows(NullPointerException.class, () -> CenterMode.valueOf(null));
        }
    }

    @Nested
    @DisplayName("Enum comparison tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should have correct ordinal values")
        void shouldHaveCorrectOrdinalValues() {
            assertEquals(0, CenterMode.MAIN.ordinal());
            assertEquals(1, CenterMode.DR.ordinal());
            assertEquals(2, CenterMode.STRESS.ordinal());
            assertEquals(3, CenterMode.NONE.ordinal());
        }

        @Test
        @DisplayName("Should support equality comparison")
        void shouldSupportEqualityComparison() {
            CenterMode mode1 = CenterMode.MAIN;
            CenterMode mode2 = CenterMode.MAIN;
            CenterMode mode3 = CenterMode.DR;

            assertEquals(mode1, mode2);
            assertNotEquals(mode1, mode3);
        }

        @Test
        @DisplayName("Should support switch statements")
        void shouldSupportSwitchStatements() {
            String result = switch (CenterMode.MAIN) {
                case MAIN -> "Main Center";
                case DR -> "DR Center";
                case STRESS -> "Stress Center";
                case NONE -> "No Center";
            };

            assertEquals("Main Center", result);
        }
    }
}
