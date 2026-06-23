package com.scbank.process.api.fw.core.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * RunMode Enum Test Class
 */
class RunModeTest {

    @Nested
    @DisplayName("Enum constants tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have LOCAL constant")
        void shouldHaveLocalConstant() {
            RunMode mode = RunMode.LOCAL;

            assertNotNull(mode);
            assertEquals("LOCAL", mode.name());
        }

        @Test
        @DisplayName("Should have UT constant")
        void shouldHaveUtConstant() {
            RunMode mode = RunMode.UT;

            assertNotNull(mode);
            assertEquals("UT", mode.name());
        }

        @Test
        @DisplayName("Should have SIT constant")
        void shouldHaveSitConstant() {
            RunMode mode = RunMode.SIT;

            assertNotNull(mode);
            assertEquals("SIT", mode.name());
        }

        @Test
        @DisplayName("Should have UAT constant")
        void shouldHaveUatConstant() {
            RunMode mode = RunMode.UAT;

            assertNotNull(mode);
            assertEquals("UAT", mode.name());
        }

        @Test
        @DisplayName("Should have PRD constant")
        void shouldHavePrdConstant() {
            RunMode mode = RunMode.PRD;

            assertNotNull(mode);
            assertEquals("PRD", mode.name());
        }

        @Test
        @DisplayName("Should have NONE constant")
        void shouldHaveNoneConstant() {
            RunMode mode = RunMode.NONE;

            assertNotNull(mode);
            assertEquals("NONE", mode.name());
        }

        @Test
        @DisplayName("Should have exactly 6 enum values")
        void shouldHaveExactlySixEnumValues() {
            RunMode[] values = RunMode.values();

            assertEquals(6, values.length);
        }
    }

    @Nested
    @DisplayName("of() method tests")
    class OfMethodTests {

        @ParameterizedTest
        @CsvSource({
            "local, LOCAL",
            "LOCAL, LOCAL",
            "Local, LOCAL",
            "LoCaL, LOCAL"
        })
        @DisplayName("Should return LOCAL for local variations")
        void shouldReturnLocalForLocalVariations(String input, RunMode expected) {
            RunMode result = RunMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "ut, UT",
            "UT, UT",
            "Ut, UT",
            "uT, UT"
        })
        @DisplayName("Should return UT for ut variations")
        void shouldReturnUtForUtVariations(String input, RunMode expected) {
            RunMode result = RunMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "sit, SIT",
            "SIT, SIT",
            "Sit, SIT",
            "SiT, SIT"
        })
        @DisplayName("Should return SIT for sit variations")
        void shouldReturnSitForSitVariations(String input, RunMode expected) {
            RunMode result = RunMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "uat, UAT",
            "UAT, UAT",
            "Uat, UAT",
            "UaT, UAT"
        })
        @DisplayName("Should return UAT for uat variations")
        void shouldReturnUatForUatVariations(String input, RunMode expected) {
            RunMode result = RunMode.of(input);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "prd, PRD",
            "PRD, PRD",
            "Prd, PRD",
            "PrD, PRD"
        })
        @DisplayName("Should return PRD for prd variations")
        void shouldReturnPrdForPrdVariations(String input, RunMode expected) {
            RunMode result = RunMode.of(input);

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
            "random",
            "dev",
            "prod"
        })
        @DisplayName("Should return NONE for unknown or invalid values")
        void shouldReturnNoneForUnknownOrInvalidValues(String input) {
            RunMode result = RunMode.of(input);

            assertEquals(RunMode.NONE, result);
        }

        @Test
        @DisplayName("Should handle lowercase input")
        void shouldHandleLowercaseInput() {
            assertEquals(RunMode.LOCAL, RunMode.of("local"));
            assertEquals(RunMode.UT, RunMode.of("ut"));
            assertEquals(RunMode.SIT, RunMode.of("sit"));
            assertEquals(RunMode.UAT, RunMode.of("uat"));
            assertEquals(RunMode.PRD, RunMode.of("prd"));
        }

        @Test
        @DisplayName("Should handle uppercase input")
        void shouldHandleUppercaseInput() {
            assertEquals(RunMode.LOCAL, RunMode.of("LOCAL"));
            assertEquals(RunMode.UT, RunMode.of("UT"));
            assertEquals(RunMode.SIT, RunMode.of("SIT"));
            assertEquals(RunMode.UAT, RunMode.of("UAT"));
            assertEquals(RunMode.PRD, RunMode.of("PRD"));
        }

        @Test
        @DisplayName("Should handle mixed case input")
        void shouldHandleMixedCaseInput() {
            assertEquals(RunMode.LOCAL, RunMode.of("LoCaL"));
            assertEquals(RunMode.UT, RunMode.of("Ut"));
            assertEquals(RunMode.SIT, RunMode.of("SiT"));
            assertEquals(RunMode.UAT, RunMode.of("UaT"));
            assertEquals(RunMode.PRD, RunMode.of("PrD"));
        }
    }

    @Nested
    @DisplayName("Enum valueOf tests")
    class ValueOfTests {

        @Test
        @DisplayName("Should get LOCAL using valueOf")
        void shouldGetLocalUsingValueOf() {
            RunMode mode = RunMode.valueOf("LOCAL");

            assertEquals(RunMode.LOCAL, mode);
        }

        @Test
        @DisplayName("Should get UT using valueOf")
        void shouldGetUtUsingValueOf() {
            RunMode mode = RunMode.valueOf("UT");

            assertEquals(RunMode.UT, mode);
        }

        @Test
        @DisplayName("Should get SIT using valueOf")
        void shouldGetSitUsingValueOf() {
            RunMode mode = RunMode.valueOf("SIT");

            assertEquals(RunMode.SIT, mode);
        }

        @Test
        @DisplayName("Should get UAT using valueOf")
        void shouldGetUatUsingValueOf() {
            RunMode mode = RunMode.valueOf("UAT");

            assertEquals(RunMode.UAT, mode);
        }

        @Test
        @DisplayName("Should get PRD using valueOf")
        void shouldGetPrdUsingValueOf() {
            RunMode mode = RunMode.valueOf("PRD");

            assertEquals(RunMode.PRD, mode);
        }

        @Test
        @DisplayName("Should get NONE using valueOf")
        void shouldGetNoneUsingValueOf() {
            RunMode mode = RunMode.valueOf("NONE");

            assertEquals(RunMode.NONE, mode);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid valueOf")
        void shouldThrowExceptionForInvalidValueOf() {
            assertThrows(IllegalArgumentException.class, () -> RunMode.valueOf("INVALID"));
        }

        @Test
        @DisplayName("Should throw NullPointerException for null valueOf")
        void shouldThrowExceptionForNullValueOf() {
            assertThrows(NullPointerException.class, () -> RunMode.valueOf(null));
        }
    }

    @Nested
    @DisplayName("Enum comparison tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should have correct ordinal values")
        void shouldHaveCorrectOrdinalValues() {
            assertEquals(0, RunMode.LOCAL.ordinal());
            assertEquals(1, RunMode.UT.ordinal());
            assertEquals(2, RunMode.SIT.ordinal());
            assertEquals(3, RunMode.UAT.ordinal());
            assertEquals(4, RunMode.PRD.ordinal());
            assertEquals(5, RunMode.NONE.ordinal());
        }

        @Test
        @DisplayName("Should support equality comparison")
        void shouldSupportEqualityComparison() {
            RunMode mode1 = RunMode.LOCAL;
            RunMode mode2 = RunMode.LOCAL;
            RunMode mode3 = RunMode.PRD;

            assertEquals(mode1, mode2);
            assertNotEquals(mode1, mode3);
        }

        @Test
        @DisplayName("Should support switch statements")
        void shouldSupportSwitchStatements() {
            String result = switch (RunMode.PRD) {
                case LOCAL -> "Local Environment";
                case UT -> "Unit Test Environment";
                case SIT -> "System Integration Test";
                case UAT -> "User Acceptance Test";
                case PRD -> "Production Environment";
                case NONE -> "No Environment";
            };

            assertEquals("Production Environment", result);
        }
    }
}
