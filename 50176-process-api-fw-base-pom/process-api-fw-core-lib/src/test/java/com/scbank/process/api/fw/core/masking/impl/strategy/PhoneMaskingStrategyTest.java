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
@DisplayName("PhoneMaskingStrategy Tests")
class PhoneMaskingStrategyTest {

    private PhoneMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new PhoneMaskingStrategy();
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
        @DisplayName("Should mask last 4 digits of phone number (length > 4)")
        void testMaskPhoneNumberLong() {
            // Phone: 01012345678 (11 digits)
            byte[] source = "01012345678".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(11, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("0101234"));
            assertTrue(resultStr.endsWith("****"));
        }

        @Test
        @DisplayName("Should mask 10-digit phone number")
        void testMask10DigitPhoneNumber() {
            byte[] source = "0101234567".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(10, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("010123"));
            assertTrue(resultStr.endsWith("****"));
        }

        @Test
        @DisplayName("Should mask 8-digit phone number")
        void testMask8DigitPhoneNumber() {
            byte[] source = "12345678".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(8, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("1234"));
            assertTrue(resultStr.endsWith("****"));
        }

        @Test
        @DisplayName("Should mask exactly 5-digit phone number")
        void testMask5DigitPhoneNumber() {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(5, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("1****", resultStr);
        }

        @Test
        @DisplayName("Should use default masking for exactly 4-digit phone")
        void testMaskExactly4DigitPhone() {
            byte[] source = "1234".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(4, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // Default masking - all asterisks
            assertEquals("****", resultStr);
        }

        @Test
        @DisplayName("Should use default masking for 3-digit phone")
        void testMask3DigitPhone() {
            byte[] source = "123".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(3, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("***", resultStr);
        }

        @Test
        @DisplayName("Should use default masking for 2-digit phone")
        void testMask2DigitPhone() {
            byte[] source = "12".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(2, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("**", resultStr);
        }

        @Test
        @DisplayName("Should use default masking for 1-digit phone")
        void testMask1DigitPhone() {
            byte[] source = "1".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(1, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*", resultStr);
        }

        @Test
        @DisplayName("Should handle empty phone number")
        void testMaskEmptyPhone() {
            byte[] source = "".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        @DisplayName("Should mask phone with CP933 encoding")
        void testMaskPhoneWithCp933Encoding() throws Exception {
            byte[] source = "01012345678".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(11, result.length);
        }

        @Test
        @DisplayName("Should mask phone with dashes")
        void testMaskPhoneWithDashes() {
            byte[] source = "010-1234-5678".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(13, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // Last 4 characters should be masked
            assertTrue(resultStr.endsWith("****"));
        }

        @Test
        @DisplayName("Should mask phone with spaces")
        void testMaskPhoneWithSpaces() {
            byte[] source = "010 1234 5678".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(13, result.length);
        }

        @Test
        @DisplayName("Should mask international format phone")
        void testMaskInternationalPhone() {
            byte[] source = "+821012345678".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(13, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.endsWith("****"));
        }
    }
}
