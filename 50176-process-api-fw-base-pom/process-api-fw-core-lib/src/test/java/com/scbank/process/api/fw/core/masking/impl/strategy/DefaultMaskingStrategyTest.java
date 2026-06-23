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
@DisplayName("DefaultMaskingStrategy Tests")
class DefaultMaskingStrategyTest {

    private DefaultMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new DefaultMaskingStrategy();
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
        @DisplayName("Should mask all characters with asterisks")
        void testMaskAllCharacters() {
            byte[] source = "HelloWorld".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(10, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("**********", resultStr);
        }

        @Test
        @DisplayName("Should preserve spaces when masking")
        void testMaskPreservesSpaces() {
            byte[] source = "Hello World".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(11, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("***** *****", resultStr);
        }

        @Test
        @DisplayName("Should mask single character")
        void testMaskSingleCharacter() {
            byte[] source = "A".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals('*', result[0]);
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
        @DisplayName("Should mask numbers")
        void testMaskNumbers() {
            byte[] source = "1234567890".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(10, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("**********", resultStr);
        }

        @Test
        @DisplayName("Should mask special characters")
        void testMaskSpecialCharacters() {
            byte[] source = "!@#$%".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(5, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*****", resultStr);
        }

        @Test
        @DisplayName("Should handle multiple spaces")
        void testMaskMultipleSpaces() {
            byte[] source = "A   B".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(5, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*   *", resultStr);
        }

        @Test
        @DisplayName("Should mask with CP933 encoding")
        void testMaskWithCp933Encoding() throws Exception {
            byte[] source = "TestData".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(8, result.length);
        }

        @Test
        @DisplayName("Should handle null bytes in source")
        void testMaskWithNullBytes() {
            byte[] source = new byte[] { 'A', 0x00, 'B', 0x00, 'C' };
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(5, result.length);
            // Null bytes should be preserved as spaces
            assertEquals('*', result[0]);
            assertEquals(' ', result[1]);
            assertEquals('*', result[2]);
            assertEquals(' ', result[3]);
            assertEquals('*', result[4]);
        }

        @Test
        @DisplayName("Should mask mixed content")
        void testMaskMixedContent() {
            byte[] source = "ABC 123 !@#".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(11, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*** *** ***", resultStr);
        }
    }
}
