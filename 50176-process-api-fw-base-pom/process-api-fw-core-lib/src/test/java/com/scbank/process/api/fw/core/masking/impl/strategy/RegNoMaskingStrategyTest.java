package com.scbank.process.api.fw.core.masking.impl.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RegNoMaskingStrategy Tests")
class RegNoMaskingStrategyTest {

    private RegNoMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new RegNoMaskingStrategy();
    }

    @Nested
    @DisplayName("mask() method tests")
    class MaskTests {

        @Test
        @DisplayName("Should return null for null input")
        void testMaskWithNullInput() {
            byte[] result = strategy.mask((byte[]) null, UTF8);
            assertNull(result);
        }

        @Test
        @DisplayName("Should fully mask 7-digit registration number (resident ID back part)")
        void testMask7DigitRegNo() {
            // 7-digit: back part of resident registration number
            byte[] source = "1234567".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(7, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // Full default masking
            assertEquals("*******", resultStr);
        }

        @Test
        @DisplayName("Should mask last 3 digits of 10-digit number (business number)")
        void testMask10DigitBusinessNumber() {
            // 10-digit: business registration number
            byte[] source = "1234567890".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(10, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("1234567"));
            assertTrue(resultStr.endsWith("***"));
        }

        @Test
        @DisplayName("Should mask last 7 digits of 13-digit number (resident ID)")
        void testMask13DigitResidentId() {
            // 13-digit: full resident registration number
            byte[] source = "9001011234567".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(13, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("900101"));
            assertTrue(resultStr.endsWith("*******"));
        }

        @Test
        @DisplayName("Should handle other length numbers without specific masking offset")
        void testMaskOtherLengthNumber() {
            // 8-digit: not 7, 10, or 13
            byte[] source = "12345678".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(8, result.length);
            // maskOffset = 0, so masking starts from beginning
        }

        @Test
        @DisplayName("Should handle 6-digit number")
        void testMask6DigitNumber() {
            byte[] source = "123456".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(6, result.length);
        }

        @Test
        @DisplayName("Should handle 11-digit number")
        void testMask11DigitNumber() {
            byte[] source = "12345678901".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(11, result.length);
        }

        @Test
        @DisplayName("Should handle 14-digit number")
        void testMask14DigitNumber() {
            byte[] source = "12345678901234".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(14, result.length);
        }

        @Test
        @DisplayName("Should handle resident ID with dash (13 chars)")
        void testMaskResidentIdWithDash() {
            // Note: with dash it's 14 chars, not 13
            byte[] source = "900101-1234567".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(14, result.length);
        }

        @Test
        @DisplayName("Should handle empty input")
        void testMaskEmptyInput() {
            byte[] source = "".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        @DisplayName("Should handle single character")
        void testMaskSingleCharacter() {
            byte[] source = "1".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(1, result.length);
        }

        @Test
        @DisplayName("Should mask with CP933 encoding")
        void testMaskWithCp933Encoding() throws Exception {
            byte[] source = "9001011234567".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(13, result.length);
        }

        @Test
        @DisplayName("Should mask 10-digit with CP933 encoding")
        void testMask10DigitWithCp933() throws Exception {
            byte[] source = "1234567890".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(10, result.length);
        }

        @Test
        @DisplayName("Should handle input with trailing spaces")
        void testMaskWithTrailingSpaces() {
            byte[] source = "9001011234567   ".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Should preserve original length including spaces
            assertEquals(16, result.length);
        }

        @Test
        @DisplayName("Should handle 10-digit with spaces for padding")
        void testMask10DigitWithPadding() {
            // Test totLen - mLen > 0 branch
            String paddedNumber = "1234567890  ";
            byte[] source = paddedNumber.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should handle 13-digit with leading spaces")
        void testMask13DigitWithLeadingSpaces() {
            byte[] source = "   9001011234567".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(16, result.length);
        }
    }
}
