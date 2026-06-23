package com.scbank.process.api.fw.message.converter.format.common;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

@ExtendWith(MockitoExtension.class)
@DisplayName("ByteFieldConverter Tests")
class ByteFieldConverterTest {

    private ByteFieldConverter converter;

    @Mock
    private IMessageFieldMetadata fieldMetadata;

    @Mock
    private MessageContext messageContext;

    @BeforeEach
    void setUp() {
        converter = new ByteFieldConverter();
    }

    @Nested
    @DisplayName("read() method tests")
    class ReadTests {

        @Test
        @DisplayName("Should read byte from non-empty source")
        void testReadNonEmptySource() throws Exception {
            byte[] source = "A".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);

            Byte result = converter.read(source, fieldMetadata, messageContext);

            assertEquals((byte) 'A', result);
        }

        @Test
        @DisplayName("Should return padding byte for empty source")
        void testReadEmptySource() throws Exception {
            byte[] source = "".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getPadding()).thenReturn(null);

            Byte result = converter.read(source, fieldMetadata, messageContext);

            assertEquals((byte) ' ', result);
        }

        @Test
        @DisplayName("Should use custom padding byte")
        void testReadWithCustomPadding() throws Exception {
            byte[] source = "".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn("0");

            Byte result = converter.read(source, fieldMetadata, messageContext);

            assertEquals((byte) '0', result);
        }

        @Test
        @DisplayName("Should use hex padding byte")
        void testReadWithHexPadding() throws Exception {
            byte[] source = "".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn("0x30");

            Byte result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should read first byte from multi-byte source")
        void testReadMultiByteSource() throws Exception {
            byte[] source = "ABC".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);

            Byte result = converter.read(source, fieldMetadata, messageContext);

            assertEquals((byte) 'A', result);
        }
    }

    @Nested
    @DisplayName("write() method tests")
    class WriteTests {

        @Test
        @DisplayName("Should write byte to byte array")
        void testWriteByte() throws Exception {
            Byte value = (byte) 'X';

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals((byte) 'X', result[0]);
        }

        @Test
        @DisplayName("Should write byte with specific encoding")
        void testWriteByteWithEncoding() throws Exception {
            Byte value = (byte) 'A';

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn("UTF-8");

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals((byte) 'A', result[0]);
        }

        @Test
        @DisplayName("Should write numeric byte value")
        void testWriteNumericByte() throws Exception {
            Byte value = 65;

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(1, result.length);
        }

        @Test
        @DisplayName("Should write zero byte")
        void testWriteZeroByte() throws Exception {
            Byte value = (byte) '0';

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals((byte) '0', result[0]);
        }

        @Test
        @DisplayName("Should write space byte")
        void testWriteSpaceByte() throws Exception {
            Byte value = (byte) ' ';

            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals((byte) ' ', result[0]);
        }
    }
}
