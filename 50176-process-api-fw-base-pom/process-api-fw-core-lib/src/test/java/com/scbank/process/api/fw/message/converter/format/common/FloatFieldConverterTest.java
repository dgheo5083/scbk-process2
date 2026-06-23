package com.scbank.process.api.fw.message.converter.format.common;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FloatFieldConverter Tests")
class FloatFieldConverterTest {

    private FloatFieldConverter converter;

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
        converter = new FloatFieldConverter();
    }

    @Nested
    @DisplayName("read() method tests")
    class ReadTests {

        @Test
        @DisplayName("Should read Float from byte array")
        void testReadFloat() throws Exception {
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
            when(deserializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.FORCE_SCALE_ON_READ)).thenReturn(false);
            when(deserializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.UNNECESSARY));

            Float result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(123.45f, result, 0.01f);
        }

        @Test
        @DisplayName("Should read zero for empty string")
        void testReadZeroForEmptyString() throws Exception {
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

            Float result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(0.0f, result, 0.001f);
        }

        @Test
        @DisplayName("Should read Float with unpadding")
        void testReadFloatWithUnpadding() throws Exception {
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

            Float result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should read integer as Float")
        void testReadIntegerAsFloat() throws Exception {
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

            Float result = converter.read(source, fieldMetadata, messageContext);

            assertEquals(12345.0f, result, 0.001f);
        }
    }

    @Nested
    @DisplayName("write() method tests")
    class WriteTests {

        @Test
        @DisplayName("Should write Float to byte array")
        void testWriteFloat() throws Exception {
            Float value = 123.45f;

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

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write null as zero")
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
        @DisplayName("Should write Float with padding")
        void testWriteFloatWithPadding() throws Exception {
            Float value = 123.45f;

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
        @DisplayName("Should write integer Float value")
        void testWriteIntegerFloatValue() throws Exception {
            Float value = 12345.0f;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getScale()).thenReturn(0);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(serializationOptions.enabled(MessageFormatOption.INCLUDE_DECIMAL_POINT)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.get(MessageFormatOption.DECIMAL_ROUNDING_MODE))
                    .thenReturn(java.util.Optional.of(RoundingMode.HALF_UP));

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write without decimal point")
        void testWriteWithoutDecimalPoint() throws Exception {
            Float value = 123.45f;

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
    }
}
