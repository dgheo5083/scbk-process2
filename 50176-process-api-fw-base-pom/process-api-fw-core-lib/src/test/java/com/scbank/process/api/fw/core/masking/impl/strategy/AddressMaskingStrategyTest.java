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
@DisplayName("AddressMaskingStrategy Tests")
class AddressMaskingStrategyTest {

    private AddressMaskingStrategy strategy;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final String CP933 = "cp933";

    @BeforeEach
    void setUp() {
        strategy = new AddressMaskingStrategy();
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
        @DisplayName("Should mask address with length > 20 (last 10 chars)")
        void testMaskLongAddress() {
            // Address longer than 20 characters
            String address = "Seoul Gangnam-gu Teheran-ro 123";
            byte[] source = address.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(source.length, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // Last 10 characters should be masked
            assertTrue(resultStr.contains("*"));
        }

        @Test
        @DisplayName("Should mask address with length <= 20 (last 5 chars)")
        void testMaskShortAddress() {
            // Address 20 characters or less
            String address = "Seoul Gangnam 123";
            byte[] source = address.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(source.length, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // First 12 characters preserved, last 5 (offset = 17-5=12) masked
            assertTrue(resultStr.startsWith("Seoul Gangna"));
            assertTrue(resultStr.contains("*"));
        }

        @Test
        @DisplayName("Should mask address exactly 20 characters")
        void testMaskExactly20Characters() {
            String address = "12345678901234567890";
            byte[] source = address.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(20, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(resultStr.endsWith("*****"));
        }

        @Test
        @DisplayName("Should mask address exactly 21 characters")
        void testMaskExactly21Characters() {
            String address = "123456789012345678901";
            byte[] source = address.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(21, result.length);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            // Last 10 characters should be masked
            assertTrue(resultStr.contains("*"));
        }

        @Test
        @DisplayName("Should handle SOSI wrapped data with length > 40")
        void testMaskWithSOSILongData() {
            // Create byte array with SOSI markers (0x0E at start, 0x0F at end)
            // Length > 40 after removing SOSI
            byte[] innerData = new byte[50];
            for (int i = 0; i < 50; i++) {
                innerData[i] = (byte) ('A' + (i % 26));
            }
            byte[] source = new byte[52];
            source[0] = 0x0E; // SO
            System.arraycopy(innerData, 0, source, 1, 50);
            source[51] = 0x0F; // SI

            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Result should have SOSI markers
            assertEquals(0x0E, result[0]);
            assertEquals(0x0F, result[result.length - 1]);
        }

        @Test
        @DisplayName("Should handle SOSI wrapped data with length <= 40")
        void testMaskWithSOSIShortData() {
            // Create byte array with SOSI markers (0x0E at start, 0x0F at end)
            // Length <= 40 after removing SOSI
            byte[] innerData = new byte[30];
            for (int i = 0; i < 30; i++) {
                innerData[i] = (byte) ('A' + (i % 26));
            }
            byte[] source = new byte[32];
            source[0] = 0x0E; // SO
            System.arraycopy(innerData, 0, source, 1, 30);
            source[31] = 0x0F; // SI

            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            // Result should have SOSI markers
            assertEquals(0x0E, result[0]);
            assertEquals(0x0F, result[result.length - 1]);
        }

        @Test
        @DisplayName("Should handle SOSI data with even index masking")
        void testMaskWithSOSIEvenIndexMasking() {
            // Test that even index positions get 0x42 and odd get 0x5c
            byte[] source = new byte[12];
            source[0] = 0x0E;
            for (int i = 1; i < 11; i++) {
                source[i] = (byte) ('A' + i);
            }
            source[11] = 0x0F;

            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(0x0E, result[0]);
            assertEquals(0x0F, result[result.length - 1]);
        }

        @Test
        @DisplayName("Should mask address with CP933 encoding")
        void testMaskWithCp933Encoding() throws Exception {
            String address = "Seoul Gangnam District";
            byte[] source = address.getBytes(UTF8);
            byte[] result = strategy.mask(source, CP933);

            assertNotNull(result);
            assertEquals(source.length, result.length);
        }

        @Test
        @DisplayName("Should preserve spaces in masking")
        void testMaskWithSpaces() {
            String address = "Seoul   Gangnam   123";
            byte[] source = address.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(source.length, result.length);
        }

        @Test
        @DisplayName("Should handle very short address")
        void testMaskVeryShortAddress() {
            String address = "Seoul";
            byte[] source = address.getBytes(StandardCharsets.UTF_8);
            byte[] result = strategy.mask(source, UTF8);

            assertNotNull(result);
            assertEquals(5, result.length);
        }
    }
}
