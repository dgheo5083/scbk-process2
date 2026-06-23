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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.exception.MessageFieldConvertException;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("NumberFieldConverter Tests")
class NumberFieldConverterTest {

    @Mock
    private IMessageFieldMetadata fieldMetadata;

    @Mock
    private MessageContext messageContext;

    @Mock
    private DeserializationOptions deserializationOptions;

    @Mock
    private SerializationOptions serializationOptions;

    @Nested
    @DisplayName("Integer type converter tests")
    class IntegerConverterTests {

        private NumberFieldConverter<Integer> converter;

        @BeforeEach
        void setUp() {
            converter = new NumberFieldConverter<>(Integer.class);
        }

        @Test
        @DisplayName("Should read Integer from byte array")
        void testReadInteger() throws Exception {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);

            Integer result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(12345, result);
        }

        @Test
        @DisplayName("Should read Integer with trim")
        void testReadIntegerWithTrim() throws Exception {
            byte[] source = "  12345  ".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(true);

            Integer result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(12345, result);
        }

        @Test
        @DisplayName("Should return zero for empty string")
        void testReadEmptyString() throws Exception {
            byte[] source = "   ".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(true);

            Integer result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(0, result);
        }

        @Test
        @DisplayName("Should log debug message when useDebugLog is true")
        void testReadWithDebugLog() throws Exception {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.isUseDebugLog()).thenReturn(true);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(fieldMetadata.getFieldType()).thenReturn((Class) Integer.class);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);

            Integer result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(12345, result);
        }

        @Test
        @DisplayName("Should write Integer to byte array")
        void testWriteInteger() throws Exception {
            Integer value = 12345;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("12345", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should write Integer with padding")
        void testWriteIntegerWithPadding() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(5, result.length);
        }

        @Test
        @DisplayName("Should write null as zero")
        void testWriteNull() throws Exception {
            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(null, fieldMetadata, messageContext);

            assertEquals("0", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should throw exception when LENGTH_CHECK enabled and length exceeded")
        void testWriteLengthCheckExceeded() {
            Integer value = 1234567890;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(true);

            assertThrows(MessageFieldConvertException.class, () ->
                converter.write(value, fieldMetadata, messageContext));
        }

        @Test
        @DisplayName("Should warn when field length is zero in FIXEDLENGTH format")
        void testWriteZeroLengthWarning() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.getFormat()).thenReturn(MessageFormat.FIXEDLENGTH);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(0);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write with LEFT alignment")
        void testWriteWithLeftAlignment() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(5, result.length);
        }

        @Test
        @DisplayName("Should log debug message when writing with useDebugLog true")
        void testWriteWithDebugLog() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.isUseDebugLog()).thenReturn(true);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(fieldMetadata.getFieldType()).thenReturn((Class) Integer.class);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("Long type converter tests")
    class LongConverterTests {

        private NumberFieldConverter<Long> converter;

        @BeforeEach
        void setUp() {
            converter = new NumberFieldConverter<>(Long.class);
        }

        @Test
        @DisplayName("Should read Long from byte array")
        void testReadLong() throws Exception {
            byte[] source = "9876543210".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);

            Long result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(9876543210L, result);
        }

        @Test
        @DisplayName("Should write Long to byte array")
        void testWriteLong() throws Exception {
            Long value = 9876543210L;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(15);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("9876543210", new String(result, StandardCharsets.UTF_8));
        }
    }

    @Nested
    @DisplayName("Short type converter tests")
    class ShortConverterTests {

        private NumberFieldConverter<Short> converter;

        @BeforeEach
        void setUp() {
            converter = new NumberFieldConverter<>(Short.class);
        }

        @Test
        @DisplayName("Should read Short from byte array")
        void testReadShort() throws Exception {
            byte[] source = "32767".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);

            Short result = converter.read(source, fieldMetadata, messageContext);

            assertEquals((short) 32767, result);
        }

        @Test
        @DisplayName("Should write Short to byte array")
        void testWriteShort() throws Exception {
            Short value = 12345;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("12345", new String(result, StandardCharsets.UTF_8));
        }
    }

    @Nested
    @DisplayName("Primitive type wrapper tests")
    class PrimitiveTypeWrapperTests {

        @Test
        @DisplayName("Should convert int.class to Integer.class")
        void testIntPrimitiveConversion() {
            NumberFieldConverter<Integer> converter = new NumberFieldConverter<>(int.class);
            assertNotNull(converter);
        }

        @Test
        @DisplayName("Should convert long.class to Long.class")
        void testLongPrimitiveConversion() {
            NumberFieldConverter<Long> converter = new NumberFieldConverter<>(long.class);
            assertNotNull(converter);
        }

        @Test
        @DisplayName("Should convert short.class to Short.class")
        void testShortPrimitiveConversion() {
            NumberFieldConverter<Short> converter = new NumberFieldConverter<>(short.class);
            assertNotNull(converter);
        }

        @Test
        @DisplayName("Should throw exception for unsupported primitive type")
        void testUnsupportedPrimitiveType() {
            assertThrows(IllegalArgumentException.class, () ->
                new NumberFieldConverter<>(double.class));
        }
    }

    @Nested
    @DisplayName("Unsupported type tests")
    class UnsupportedTypeTests {

        @Test
        @DisplayName("Should throw exception for unsupported number type when parsing")
        void testUnsupportedNumberType() throws Exception {
            NumberFieldConverter<Byte> converter = new NumberFieldConverter<>(Byte.class);
            byte[] source = "123".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);

            assertThrows(MessageFieldConvertException.class, () ->
                converter.read(source, fieldMetadata, messageContext));
        }
    }

    @Nested
    @DisplayName("Alignment and padding tests")
    class AlignmentPaddingTests {

        private NumberFieldConverter<Integer> converter;

        @BeforeEach
        void setUp() {
            converter = new NumberFieldConverter<>(Integer.class);
        }

        @Test
        @DisplayName("Should use NONE alignment as RIGHT")
        void testNoneAlignmentDefaultsToRight() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.NONE);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(5, result.length);
        }

        @Test
        @DisplayName("Should use custom padding")
        void testCustomPadding() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getPadding()).thenReturn("*");
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(5, result.length);
        }

        @Test
        @DisplayName("Should write with trim enabled")
        void testWriteWithTrimEnabled() throws Exception {
            Integer value = 123;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("123", new String(result, StandardCharsets.UTF_8));
        }
    }
}
