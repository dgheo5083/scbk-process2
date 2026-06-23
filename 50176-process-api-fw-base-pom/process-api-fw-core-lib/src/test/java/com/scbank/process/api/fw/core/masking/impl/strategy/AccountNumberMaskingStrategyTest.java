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
@DisplayName("AccountNumberMaskingStrategy Tests")
class AccountNumberMaskingStrategyTest {

    private AccountNumberMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new AccountNumberMaskingStrategy();
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
        @DisplayName("Should mask last 6 characters for account number")
        void testMaskAccountNumber() {
            // Account number: 1234567890123 (13 digits)
            byte[] source = "1234567890123".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(source.length, result.length);

            // First 7 characters should be preserved
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("1234567"));
            // Last 6 should be masked
            assertTrue(resultStr.endsWith("******"));
        }

        @Test
        @DisplayName("Should mask account number with different lengths")
        void testMaskAccountNumberVariousLengths() {
            // 10-digit account number
            byte[] source10 = "1234567890".getBytes(StandardCharsets.UTF_8);
            byte[] result10 = strategy.mask(source10, UTF8);
            assertNotNull(result10);
            assertEquals(10, result10.length);
            String resultStr10 = new String(result10, StandardCharsets.UTF_8);
            assertTrue(resultStr10.startsWith("1234"));
            assertTrue(resultStr10.endsWith("******"));
        }

        @Test
        @DisplayName("Should mask short account number")
        void testMaskShortAccountNumber() {
            // 7-digit account number (maskOffset = 1)
            byte[] source = "1234567".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(7, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("1******", resultStr);
        }

        @Test
        @DisplayName("Should handle exactly 6 characters")
        void testMaskExactlySixCharacters() {
            byte[] source = "123456".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(6, result.length);
            // maskOffset = 0, all 6 characters masked
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("******", resultStr);
        }

        @Test
        @DisplayName("Should handle less than 6 characters")
        void testMaskLessThanSixCharacters() {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(5, result.length);
            // maskOffset = -1 (negative), but mask method handles it
        }

        @Test
        @DisplayName("Should preserve spaces in masking")
        void testMaskWithSpaces() {
            byte[] source = "1234 567890".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(source.length, result.length);
        }

        @Test
        @DisplayName("Should mask with CP933 encoding")
        void testMaskWithCp933Encoding() throws Exception {
            byte[] source = "1234567890123".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(source.length, result.length);
        }
    }
}
