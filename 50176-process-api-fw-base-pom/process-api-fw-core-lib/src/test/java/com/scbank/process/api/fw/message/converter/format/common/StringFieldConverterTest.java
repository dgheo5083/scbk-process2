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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.core.masking.IMaskingManager;
import com.scbank.process.api.fw.core.masking.IMaskingStrategy.MaskingType;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.exception.MessageFieldConvertException;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("StringFieldConverter Tests")
class StringFieldConverterTest {

    private StringFieldConverter converter;

    @Mock
    private IMessageFieldMetadata fieldMetadata;

    @Mock
    private MessageContext messageContext;

    @Mock
    private DeserializationOptions deserializationOptions;

    @Mock
    private SerializationOptions serializationOptions;

    @Mock
    private IMaskingManager maskingManager;

    @BeforeEach
    void setUp() {
        converter = new StringFieldConverter();
    }

    @Nested
    @DisplayName("read() method tests")
    class ReadTests {

        @Test
        @DisplayName("Should read String from byte array")
        void testReadString() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertEquals("Hello", result);
        }

        @Test
        @DisplayName("Should read with default value when empty")
        void testReadWithDefaultValue() throws Exception {
            byte[] source = "     ".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn("DEFAULT");
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertEquals("DEFAULT", result);
        }

        @Test
        @DisplayName("Should read with UNPADDING enabled")
        void testReadWithUnpadding() throws Exception {
            byte[] source = "Hello     ".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should read with FIELD_TRIM enabled")
        void testReadWithFieldTrim() throws Exception {
            byte[] source = "  Hello  ".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(9);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertEquals("Hello", result);
        }

        @Test
        @DisplayName("Should read with FULLWIDTH_TO_HALFWIDTH enabled")
        void testReadWithFullwidthToHalfwidth() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.isMultiBytes()).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH)).thenReturn(true);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should log debug message when useDebugLog is true")
        void testReadWithDebugLog() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.isUseDebugLog()).thenReturn(true);
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertEquals("Hello", result);
        }

        @Test
        @DisplayName("Should use RIGHT alignment when NONE")
        void testReadWithNoneAlignment() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.NONE);
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should read with zero length using source length")
        void testReadWithZeroLength() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(0);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            String result = converter.read(source, fieldMetadata, messageContext);

            assertEquals("Hello", result);
        }

        @Test
        @DisplayName("Should read with masking enabled")
        void testReadWithMasking() throws Exception {
            byte[] source = "12345".getBytes(StandardCharsets.UTF_8);

            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(() -> RuntimeContext.getBean(IMaskingManager.class))
                        .thenReturn(maskingManager);
                when(maskingManager.apply(any(MaskingType.class), anyString(), anyString()))
                        .thenReturn("*****");

                when(messageContext.getDeserializationOptions()).thenReturn(deserializationOptions);
                when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
                when(fieldMetadata.getLength()).thenReturn(5);
                when(fieldMetadata.getEncoding()).thenReturn(null);
                when(fieldMetadata.getPadding()).thenReturn(null);
                when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
                when(fieldMetadata.getId()).thenReturn("testField");
                when(fieldMetadata.isMasking()).thenReturn(true);
                when(fieldMetadata.getMaskingType()).thenReturn("");
                when(deserializationOptions.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)).thenReturn(false);
                when(deserializationOptions.enabled(MessageFormatOption.UNPADDING)).thenReturn(false);
                when(deserializationOptions.enabled(MessageFormatOption.DECRYPT)).thenReturn(false);
                when(fieldMetadata.isSosi()).thenReturn(false);
                when(fieldMetadata.isMultiBytes()).thenReturn(false);
                when(deserializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
                when(deserializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(true);

                String result = converter.read(source, fieldMetadata, messageContext);

                assertEquals("*****", result);
            }
        }
    }

    @Nested
    @DisplayName("write() method tests")
    class WriteTests {

        @Test
        @DisplayName("Should write String to byte array")
        void testWriteString() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should write with default value when null")
        void testWriteWithDefaultValue() throws Exception {
            String value = null;

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn("DEFAULT");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("DEFAULT", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should write with PADDING enabled")
        void testWriteWithPadding() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(10, result.length);
        }

        @Test
        @DisplayName("Should write with FIELD_TRIM enabled")
        void testWriteWithFieldTrim() throws Exception {
            String value = "  Hello  ";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(true);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should throw exception when LENGTH_CHECK enabled and length exceeded")
        void testWriteLengthCheckExceeded() {
            String value = "HelloWorld!!!";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(fieldMetadata.getPath()).thenReturn("testPath");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_LENGTH_TRUNCATE)).thenReturn(false);

            assertThrows(MessageFieldConvertException.class, () ->
                converter.write(value, fieldMetadata, messageContext));
        }

        @Test
        @DisplayName("Should truncate when LENGTH_CHECK and FIELD_LENGTH_TRUNCATE enabled")
        void testWriteWithTruncate() throws Exception {
            String value = "HelloWorld!!!";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(5);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_LENGTH_TRUNCATE)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(5, result.length);
            assertTrue(new String(result, StandardCharsets.UTF_8).startsWith("Hello"));
        }

        @Test
        @DisplayName("Should write with HALFWIDTH_TO_FULLWIDTH enabled")
        void testWriteWithHalfwidthToFullwidth() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(20);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(fieldMetadata.isMultiBytes()).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.HALFWIDTH_TO_FULLWIDTH)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH)).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should write with FULLWIDTH_TO_HALFWIDTH enabled")
        void testWriteWithFullwidthToHalfwidth() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(fieldMetadata.isMultiBytes()).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.HALFWIDTH_TO_FULLWIDTH)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH)).thenReturn(true);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should log debug message when useDebugLog is true")
        void testWriteWithDebugLog() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(messageContext.isUseDebugLog()).thenReturn(true);
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should use LEFT alignment when NONE")
        void testWriteWithNoneAlignment() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.NONE);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(10, result.length);
        }

        @Test
        @DisplayName("Should write with masking enabled")
        void testWriteWithMasking() throws Exception {
            String value = "12345";

            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(() -> RuntimeContext.getBean(IMaskingManager.class))
                        .thenReturn(maskingManager);
                when(maskingManager.apply(any(MaskingType.class), any(byte[].class), anyString()))
                        .thenReturn("*****".getBytes(StandardCharsets.UTF_8));

                when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
                when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
                when(fieldMetadata.getLength()).thenReturn(5);
                when(fieldMetadata.getEncoding()).thenReturn(null);
                when(fieldMetadata.getPadding()).thenReturn(null);
                when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
                when(fieldMetadata.getDefaultValue()).thenReturn(null);
                when(fieldMetadata.getId()).thenReturn("testField");
                when(fieldMetadata.isMasking()).thenReturn(true);
                when(fieldMetadata.getMaskingType()).thenReturn("");
                when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
                when(fieldMetadata.isMultiBytes()).thenReturn(false);
                when(fieldMetadata.isSosi()).thenReturn(false);
                when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
                when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
                when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
                when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(true);

                byte[] result = converter.write(value, fieldMetadata, messageContext);

                assertEquals("*****", new String(result, StandardCharsets.UTF_8));
            }
        }

        @Test
        @DisplayName("Should write empty string with default value")
        void testWriteEmptyStringWithDefaultValue() throws Exception {
            String value = "";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.LEFT);
            when(fieldMetadata.getDefaultValue()).thenReturn("DEFAULT");
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals("DEFAULT", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should write with RIGHT alignment and padding")
        void testWriteWithRightAlignmentAndPadding() throws Exception {
            String value = "Hello";

            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(messageContext.getDefaultEncoding()).thenReturn("UTF-8");
            when(fieldMetadata.getLength()).thenReturn(10);
            when(fieldMetadata.getEncoding()).thenReturn(null);
            when(fieldMetadata.getPadding()).thenReturn(null);
            when(fieldMetadata.getAlign()).thenReturn(AlignType.RIGHT);
            when(fieldMetadata.getDefaultValue()).thenReturn(null);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_TRIM)).thenReturn(false);
            when(fieldMetadata.isMultiBytes()).thenReturn(false);
            when(fieldMetadata.isSosi()).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.ENCRYPT)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.LENGTH_CHECK)).thenReturn(false);
            when(serializationOptions.enabled(MessageFormatOption.PADDING)).thenReturn(true);
            when(serializationOptions.enabled(MessageFormatOption.FIELD_MASK)).thenReturn(false);

            byte[] result = converter.write(value, fieldMetadata, messageContext);

            assertEquals(10, result.length);
        }
    }
}
