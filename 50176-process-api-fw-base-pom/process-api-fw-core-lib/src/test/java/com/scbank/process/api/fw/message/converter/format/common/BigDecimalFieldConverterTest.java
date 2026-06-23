package com.scbank.process.api.fw.message.converter.format.common;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.exception.MessageFieldConvertException;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("BigDecimalFieldConverter Tests")
class BigDecimalFieldConverterTest {

    private BigDecimalFieldConverter converter;

    @Mock
    private IMessageFieldMetadata fieldMetadata;

    @Mock
    private MessageContext messageContext;

    @Mock
    private DeserializationOptions deserializationOptions;

    @Mock
    private SerializationOptions serializationOptions;

    @BeforeEach
    void setUp() {
        converter = new BigDecimalFieldConverter();
    }

    @Nested
    @DisplayName("read() method tests")
    class ReadTests {

        @Test
        @DisplayName("Should read BigDecimal with UNPADDING enabled")
        void testReadWithUnpadding() throws Exception {
            byte[] source = "00012345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(8);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should read BigDecimal without UNPADDING")
        void testReadWithoutUnpadding() throws Exception {
            byte[] source = "123.45".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.NONE);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(new BigDecimal("123.45"), result);
        }

        @Test
        @DisplayName("Should return ZERO for empty string")
        void testReadEmptyString() throws Exception {
            byte[] source = "   ".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(3);
            when(fieldMetadata.getScale()).thenReturn(0);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("Should throw exception when LENGTH_CHECK enabled and length exceeded")
        void testReadLengthCheckExceeded() {
            byte[] source = "1234567890".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getScale()).thenReturn(0);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(true);

            assertThrows(MessageFieldConvertException.class, () ->
                converter.read(source, fieldMetadata, messageContext));
        }

        @Test
        @DisplayName("Should apply FORCE_SCALE_ON_READ when enabled")
        void testReadWithForceScale() throws Exception {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(true);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(2, result.scale());
        }

        @Test
        @DisplayName("Should read with scale larger than input length")
        void testReadWithScaleLargerThanLength() throws Exception {
            byte[] source = "12".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should read with scale zero")
        void testReadWithScaleZero() throws Exception {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(0);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(new BigDecimal("12345"), result);
        }

        @Test
        @DisplayName("Should read with decimal point in source")
        void testReadWithDecimalPointInSource() throws Exception {
            byte[] source = "123.45".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(new BigDecimal("123.45"), result);
        }

        @Test
        @DisplayName("Should log debug message when useDebugLog is true")
        void testReadWithDebugLog() throws Exception {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.isUseDebugLog()).thenReturn(true);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(fieldMetadata.getFieldType()).thenReturn((Class) BigDecimal.class);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should use hex padding bytes")
        void testReadWithHexPadding() throws Exception {
            byte[] source = "00012345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(8);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn("0x30");
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(deserializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            BigDecimal result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("write() method tests")
    class WriteTests {

        @Test
        @DisplayName("Should write BigDecimal with PADDING enabled")
        void testWriteWithPadding() throws Exception {
            BigDecimal value = new BigDecimal("123.45");

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(10, result.length);
        }

        @Test
        @DisplayName("Should write BigDecimal without PADDING")
        void testWriteWithoutPadding() throws Exception {
            BigDecimal value = new BigDecimal("123.45");

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.NONE);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write null as ZERO")
        void testWriteNull() throws Exception {
            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(null, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write without decimal point")
        void testWriteWithoutDecimalPoint() throws Exception {
            BigDecimal value = new BigDecimal("123.45");

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            String resultStr = new String(result, StandardCharsets.UTF_8);
            assertFalse(resultStr.contains("."));
        }

        @Test
        @DisplayName("Should throw exception when LENGTH_CHECK enabled and length exceeded")
        void testWriteLengthCheckExceeded() {
            BigDecimal value = new BigDecimal("1234567890.12");

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(true);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            assertThrows(MessageFieldConvertException.class, () ->
                converter.write(value, fieldMetadata, messageContext));
        }

        @Test
        @DisplayName("Should log debug message when useDebugLog is true")
        void testWriteWithDebugLog() throws Exception {
            BigDecimal value = new BigDecimal("123.45");

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.isUseDebugLog()).thenReturn(true);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(fieldMetadata.getFieldType()).thenReturn((Class) BigDecimal.class);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write with LEFT alignment")
        void testWriteWithLeftAlignment() throws Exception {
            BigDecimal value = new BigDecimal("123.45");

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(2);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
            assertEquals(10, result.length);
        }
    }
}
