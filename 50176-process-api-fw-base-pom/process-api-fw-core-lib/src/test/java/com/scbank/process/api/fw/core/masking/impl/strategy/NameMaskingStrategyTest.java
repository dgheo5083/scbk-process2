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
@DisplayName("NameMaskingStrategy Tests")
class NameMaskingStrategyTest {

    private NameMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new NameMaskingStrategy();
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
        @DisplayName("Should mask all characters of name")
        void testMaskName() {
            byte[] source = "JohnDoe".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(7, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*******", resultStr);
        }

        @Test
        @DisplayName("Should preserve spaces when masking name")
        void testMaskNameWithSpaces() {
            byte[] source = "John Doe".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(8, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("**** ***", resultStr);
        }

        @Test
        @DisplayName("Should mask single character name")
        void testMaskSingleCharacterName() {
            byte[] source = "J".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals('*', result[0]);
        }

        @Test
        @DisplayName("Should handle empty name")
        void testMaskEmptyName() {
            byte[] source = "".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        @DisplayName("Should mask Korean name")
        void testMaskKoreanName() throws Exception {
            byte[] source = "홍길동".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(source.length, result.length);
        }

        @Test
        @DisplayName("Should mask name with multiple spaces")
        void testMaskNameWithMultipleSpaces() {
            byte[] source = "John  Middle  Doe".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(17, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("****  ******  ***", resultStr);
        }

        @Test
        @DisplayName("Should mask name with CP933 encoding")
        void testMaskNameWithCp933Encoding() throws Exception {
            byte[] source = "TestName".getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(8, result.length);
        }

        @Test
        @DisplayName("Should mask name with hyphen")
        void testMaskNameWithHyphen() {
            byte[] source = "Mary-Jane".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(9, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*********", resultStr);
        }

        @Test
        @DisplayName("Should mask name with apostrophe")
        void testMaskNameWithApostrophe() {
            byte[] source = "O'Brien".getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(7, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("*******", resultStr);
        }

        @Test
        @DisplayName("Should handle null bytes in name")
        void testMaskNameWithNullBytes() {
            byte[] source = new byte[] { 'J', 0x00, 'D' };
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals('*', result[0]);
            assertEquals(' ', result[1]); // null bytes become spaces
            assertEquals('*', result[2]);
        }
    }
}
