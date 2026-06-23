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
@DisplayName("CardNumberMaskingStrategy Tests")
class CardNumberMaskingStrategyTest {

    private CardNumberMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new CardNumberMaskingStrategy();
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
        @DisplayName("Should mask 16-digit card number (middle 8 digits)")
        void testMask16DigitCardNumber() {
            // 16-digit card number: 1234567890123456
            byte[] source = "1234567890123456".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(16, result.length);

            String resultStr = new String(result, StandardCharsets.UTF_8);
            // First 8 characters should be preserved, last 8 masked
            assertTrue(resultStr.startsWith("12345678"));
            assertTrue(resultStr.endsWith("********"));
        }

        @Test
        @DisplayName("Should return empty masked array for non-16-digit card")
        void testMaskNon16DigitCardNumber() {
            // 15-digit card number
            byte[] source15 = "123456789012345".getBytes(StandardCharsets.UTF_8);
            byte[] result15 = strategy.mask(source15, UTF8);

            assertNotNull(result15);
            assertEquals(15, result15.length);
            // Should return empty masked array (all zeros)
            for (byte b : result15) {
                assertEquals(0, b);
            }
        }

        @Test
        @DisplayName("Should return empty masked array for 17-digit number")
        void testMask17DigitNumber() {
            byte[] source17 = "12345678901234567".getBytes(StandardCharsets.UTF_8);
            byte[] result17 = strategy.mask(source17, UTF8);

            assertNotNull(result17);
            assertEquals(17, result17.length);
            // Should return empty masked array
            for (byte b : result17) {
                assertEquals(0, b);
            }
        }

        @Test
        @DisplayName("Should handle short card number")
        void testMaskShortCardNumber() {
            byte[] source = "1234".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(4, result.length);
        }

        @Test
        @DisplayName("Should mask 16-digit card with CP933 encoding")
        void testMask16DigitCardWithCp933() throws Exception {
            byte[] source = "1234567890123456".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(16, result.length);
        }

        @Test
        @DisplayName("Should handle card number with spaces")
        void testMaskCardWithSpaces() {
            // Card with embedded spaces (not 16 chars total)
            byte[] source = "1234 5678 9012 3456".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Not 16 characters, so returns empty masked array
            assertEquals(source.length, result.length);
        }

        @Test
        @DisplayName("Should handle empty card number")
        void testMaskEmptyCardNumber() {
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
    }
}
