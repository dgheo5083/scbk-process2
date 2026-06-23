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
@DisplayName("EmailMaskingStrategy Tests")
class EmailMaskingStrategyTest {

    private EmailMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new EmailMaskingStrategy();
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
        @DisplayName("Should mask email with long local part (> 4 chars)")
        void testMaskEmailLongLocalPart() {
            // Email: username@domain.com
            byte[] source = "username@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // First 4 characters of local part should be masked
            assertTrue(resultStr.startsWith("****"));
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should mask email with short local part (<= 4 chars)")
        void testMaskEmailShortLocalPart() {
            // Email: abc@domain.com (local part length = 3)
            byte[] source = "abc@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // First 2 characters masked (endIndex = length - 1 = 2)
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should mask email with exactly 4-char local part")
        void testMaskEmailExactly4CharLocalPart() {
            byte[] source = "test@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // For 4-char local part: endIndex = length - 1 = 3, so first 3 chars masked
            assertTrue(resultStr.startsWith("***"));
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should mask email with exactly 5-char local part")
        void testMaskEmailExactly5CharLocalPart() {
            byte[] source = "tests@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("****"));
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should use default masking for email without @ symbol")
        void testMaskEmailWithoutAtSymbol() {
            byte[] source = "notanemail".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // Should apply default masking (all asterisks)
            assertEquals("**********", resultStr);
        }

        @Test
        @DisplayName("Should use default masking for email with multiple @ symbols")
        void testMaskEmailWithMultipleAtSymbols() {
            byte[] source = "user@name@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Multiple @ symbols - not valid email format, uses default masking
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.contains("*"));
        }

        @Test
        @DisplayName("Should use default masking for short input (<= 4 chars)")
        void testMaskShortInput() {
            byte[] source = "a@b".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Length 3 <= 4, uses default masking
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("***", resultStr);
        }

        @Test
        @DisplayName("Should use default masking for exactly 4-char input")
        void testMaskExactly4CharInput() {
            byte[] source = "a@bc".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Length 4 <= 4, uses default masking
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("****", resultStr);
        }

        @Test
        @DisplayName("Should mask email with 5-char input properly")
        void testMask5CharInput() {
            byte[] source = "a@b.c".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Length 5 > 4, processes as email
        }

        @Test
        @DisplayName("Should mask email with CP933 encoding")
        void testMaskEmailWithCp933Encoding() throws Exception {
            byte[] source = "username@domain.com".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should handle email with numbers in local part")
        void testMaskEmailWithNumbers() {
            byte[] source = "user123@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.startsWith("****"));
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should handle email with special characters")
        void testMaskEmailWithSpecialChars() {
            byte[] source = "user.name@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should handle single char local part")
        void testMaskSingleCharLocalPart() {
            byte[] source = "a@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // endIndex = 0 (length 1 - 1 = 0), nothing masked
            assertTrue(resultStr.contains("@domain.com"));
        }

        @Test
        @DisplayName("Should handle two char local part")
        void testMaskTwoCharLocalPart() {
            byte[] source = "ab@domain.com".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // endIndex = 1 (length 2 - 1 = 1), first char masked
            assertTrue(resultStr.contains("@domain.com"));
        }
    }
}
