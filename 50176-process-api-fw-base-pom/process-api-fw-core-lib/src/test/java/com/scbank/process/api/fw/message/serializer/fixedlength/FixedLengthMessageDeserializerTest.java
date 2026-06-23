package com.scbank.process.api.fw.message.serializer.fixedlength;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.fw.message.enums.VariableFieldType;
import com.scbank.process.api.fw.message.evaluate.ConditionEvaluatorComposite;
import com.scbank.process.api.fw.message.metadata.DefaultIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultRepeatFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultSegmentMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultVariableLengthMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

@DisplayName("FixedLengthMessageDeserializer 테스트")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class FixedLengthMessageDeserializerTest {

    private FixedLengthMessageDeserializer deserializer;

    @Mock
    private IIntegrationMessageMetadataRegistrar metadataRegistrar;

    @Mock
    private IMessageFieldConverterRegistry converterRegistry;

    @Mock
    private IMessageFieldConverter stringConverter;

    @Mock
    private IMessageFieldConverter integerConverter;

    private MessageContext ctx;

    @BeforeEach
    void setUp() {
        deserializer = new FixedLengthMessageDeserializer();
        ctx = new MessageContext();
        ctx.setDefaultEncoding("UTF-8");
        ctx.setMessageFieldConverterRegistry(converterRegistry);
        ctx.setConditionEvaluator(new ConditionEvaluatorComposite());
    }

    @Nested
    @DisplayName("deserialize 메서드 테스트")
    class DeserializeTests {

        @Test
        @DisplayName("deserialize - Class 타입으로 역직렬화")
        void deserialize_withClass() throws Exception {
            byte[] source = "TestName  ".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
                    .align(AlignType.LEFT)
                    .path("name")
                    .fieldType(String.class)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("TestName");

            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMeta);

                TestMessageObject result = deserializer.deserialize(source, TestMessageObject.class, ctx);

                assertNotNull(result);
                assertEquals("TestName", result.getName());
            }
        }

        @Test
        @DisplayName("deserialize - metadata로 역직렬화")
        void deserialize_withMetadata() throws Exception {
            byte[] source = "TestValue ".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
                    .align(AlignType.LEFT)
                    .path("name")
                    .fieldType(String.class)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("TestValue");

            TestMessageObject result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("TestValue", result.getName());
        }
    }

    @Nested
    @DisplayName("readFromStream 메서드 테스트")
    class ReadFromStreamTests {

        @Test
        @DisplayName("readFromStream - 빈 메타데이터 리스트")
        void readFromStream_emptyMetadataList() throws Exception {
            byte[] source = "test".getBytes(StandardCharsets.UTF_8);

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(new ArrayList<>())
                    .build();

            TestMessageObject result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
        }

        @Test
        @DisplayName("readFromStream - null 메타데이터 리스트")
        void readFromStream_nullMetadataList() throws Exception {
            byte[] source = "test".getBytes(StandardCharsets.UTF_8);

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(null)
                    .build();

            TestMessageObject result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("readPrimitiveTypeField 메서드 테스트")
    class ReadPrimitiveTypeFieldTests {

        @Test
        @DisplayName("readPrimitiveTypeField - STRING 타입")
        void readPrimitiveTypeField_string() throws Exception {
            byte[] source = "Hello     ".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
                    .align(AlignType.LEFT)
                    .path("name")
                    .fieldType(String.class)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("Hello");

            TestMessageObject result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("Hello", result.getName());
        }

        @Test
        @DisplayName("readPrimitiveTypeField - INTEGER 타입")
        void readPrimitiveTypeField_integer() throws Exception {
            byte[] source = "00123".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("count")
                    .name("건수")
                    .fieldName("count")
                    .type(MessageType.INTEGER)
                    .length(5)
                    .align(AlignType.RIGHT)
                    .path("count")
                    .fieldType(Integer.class)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithCount.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            when(converterRegistry.get(MessageType.INTEGER)).thenReturn(integerConverter);
            when(integerConverter.read(any(byte[].class), any(), any())).thenReturn(123);

            TestMessageWithCount result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(123, result.getCount());
        }

        @Test
        @DisplayName("readPrimitiveTypeField - 지원하지 않는 타입")
        void readPrimitiveTypeField_unsupportedType() {
            byte[] source = "test".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(4)
                    .path("name")
                    .fieldType(String.class)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(null);

            assertThrows(FrameworkRuntimeException.class, () ->
                    deserializer.deserialize(source, integrationMeta, ctx));
        }
    }

    @Nested
    @DisplayName("readRepeatedField 메서드 테스트")
    class ReadRepeatedFieldTests {

        @Test
        @DisplayName("readRepeatedField - 고정 반복 횟수")
        void readRepeatedField_fixed() throws Exception {
            byte[] source = "Item1     Item2     ".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata itemNameMeta = DefaultMessageFieldMetadata.builder()
                    .id("itemName")
                    .name("항목명")
                    .fieldName("itemName")
                    .type(MessageType.STRING)
                    .length(10)
                    .path("items[*].itemName")
                    .fieldType(String.class)
                    .build();

            DefaultRepeatFieldMetadata repeatMeta = DefaultRepeatFieldMetadata.builder()
                    .id("items")
                    .name("항목목록")
                    .fieldName("items")
                    .type(MessageType.REPEATED)
                    .path("items")
                    .fieldType(List.class)
                    .repeatedGenericType(TestItem.class)
                    .repeatType(RepeatType.FIXED)
                    .repeatCount("2")
                    .children(Arrays.asList(itemNameMeta))
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithItems.class)
                    .children(Arrays.asList(repeatMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any()))
                    .thenReturn("Item1")
                    .thenReturn("Item2");

            TestMessageWithItems result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertNotNull(result.getItems());
            assertEquals(2, result.getItems().size());
        }

        @Test
        @DisplayName("readRepeatedField - 빈 자식 메타데이터")
        void readRepeatedField_emptyChildren() throws Exception {
            byte[] source = "test".getBytes(StandardCharsets.UTF_8);

            DefaultRepeatFieldMetadata repeatMeta = DefaultRepeatFieldMetadata.builder()
                    .id("items")
                    .name("항목목록")
                    .fieldName("items")
                    .type(MessageType.REPEATED)
                    .path("items")
                    .fieldType(List.class)
                    .repeatType(RepeatType.FIXED)
                    .repeatCount("2")
                    .children(null)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithItems.class)
                    .children(Arrays.asList(repeatMeta))
                    .build();

            TestMessageWithItems result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("readVariableLengthField 메서드 테스트")
    class ReadVariableLengthFieldTests {

        @Test
        @DisplayName("readVariableLengthField - LENGTH_PREFIX")
        void readVariableLengthField_lengthPrefix() throws Exception {
            byte[] lengthPrefix = new byte[]{0, 5};
            byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
            byte[] source = new byte[lengthPrefix.length + data.length];
            System.arraycopy(lengthPrefix, 0, source, 0, lengthPrefix.length);
            System.arraycopy(data, 0, source, lengthPrefix.length, data.length);

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.LENGTH_PREFIX)
                    .lengthPrefixSize(2)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            TestMessageWithVariableData result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("Hello", result.getVariableData());
        }

        @Test
        @DisplayName("readVariableLengthField - DELIMITER")
        void readVariableLengthField_delimiter() throws Exception {
            byte[] source = "Hello|".getBytes(StandardCharsets.UTF_8);

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.DELIMITER)
                    .delimiter((byte) '|')
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            TestMessageWithVariableData result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("Hello", result.getVariableData());
        }

        @Test
        @DisplayName("readVariableLengthField - FIXED")
        void readVariableLengthField_fixed() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.FIXED)
                    .fixedLength(5)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            TestMessageWithVariableData result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("Hello", result.getVariableData());
        }

        @Test
        @DisplayName("readVariableLengthField - FIELD_REFERENCE")
        void readVariableLengthField_fieldReference() throws Exception {
            byte[] source = "Hello".getBytes(StandardCharsets.UTF_8);

            ctx.addPathValue("length", 5);

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.FIELD_REFERENCE)
                    .referenceField("length")
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            TestMessageWithVariableData result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("Hello", result.getVariableData());
        }

        @Test
        @DisplayName("readVariableLengthField - ByteBuffWrap 타입")
        void readVariableLengthField_byteBuffWrap() throws Exception {
            byte[] source = "Data".getBytes(StandardCharsets.UTF_8);

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("byteData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("byteData")
                    .fieldType(ByteBuffWrap.class)
                    .variableFieldType(VariableFieldType.FIXED)
                    .fixedLength(4)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithByteData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();

            TestMessageWithByteData result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertNotNull(result.getByteData());
        }
    }

    @Nested
    @DisplayName("readSegmentObjectFromStream 메서드 테스트")
    class ReadSegmentObjectFromStreamTests {

        @Test
        @DisplayName("readSegmentObjectFromStream - PREFIX delimiter")
        void readSegmentObjectFromStream_prefix() throws Exception {
            byte[] source = "|TestData  ".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata childFieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
                    .path("segment.name")
                    .fieldType(String.class)
                    .build();

            DefaultSegmentMessageFieldMetadata segmentMeta = DefaultSegmentMessageFieldMetadata.builder()
                    .id("segment")
                    .name("세그먼트")
                    .fieldName("segment")
                    .type(MessageType.OBJECT)
                    .path("segment")
                    .fieldType(TestMessageObject.class)
                    .delimiterPosition(DelimiterPosition.PREFIX)
                    .delimiter(new byte[]{'|'})
                    .children(Arrays.asList(childFieldMeta))
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithSegment.class)
                    .children(Arrays.asList(segmentMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("TestData");

            TestMessageWithSegment result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertNotNull(result.getSegment());
        }

        @Test
        @DisplayName("readSegmentObjectFromStream - SUFFIX delimiter")
        void readSegmentObjectFromStream_suffix() throws Exception {
            byte[] source = "TestData|".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata childFieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(8)
                    .path("segment.name")
                    .fieldType(String.class)
                    .build();

            DefaultSegmentMessageFieldMetadata segmentMeta = DefaultSegmentMessageFieldMetadata.builder()
                    .id("segment")
                    .name("세그먼트")
                    .fieldName("segment")
                    .type(MessageType.OBJECT)
                    .path("segment")
                    .fieldType(TestMessageObject.class)
                    .delimiterPosition(DelimiterPosition.SUFFIX)
                    .delimiter(new byte[]{'|'})
                    .children(Arrays.asList(childFieldMeta))
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithSegment.class)
                    .children(Arrays.asList(segmentMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("TestData");

            TestMessageWithSegment result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertNotNull(result.getSegment());
        }

        @Test
        @DisplayName("readSegmentObjectFromStream - WRAPPED delimiter")
        void readSegmentObjectFromStream_wrapped() throws Exception {
            byte[] source = "|TestData|".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata childFieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(8)
                    .path("segment.name")
                    .fieldType(String.class)
                    .build();

            DefaultSegmentMessageFieldMetadata segmentMeta = DefaultSegmentMessageFieldMetadata.builder()
                    .id("segment")
                    .name("세그먼트")
                    .fieldName("segment")
                    .type(MessageType.OBJECT)
                    .path("segment")
                    .fieldType(TestMessageObject.class)
                    .delimiterPosition(DelimiterPosition.WRAPPED)
                    .delimiter(new byte[]{'|'})
                    .children(Arrays.asList(childFieldMeta))
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithSegment.class)
                    .children(Arrays.asList(segmentMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("TestData");

            TestMessageWithSegment result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertNotNull(result.getSegment());
        }

        @Test
        @DisplayName("readSegmentObjectFromStream - NONE delimiter throws exception")
        void readSegmentObjectFromStream_none() {
            byte[] source = "TestData".getBytes(StandardCharsets.UTF_8);

            DefaultSegmentMessageFieldMetadata segmentMeta = DefaultSegmentMessageFieldMetadata.builder()
                    .id("segment")
                    .name("세그먼트")
                    .fieldName("segment")
                    .type(MessageType.OBJECT)
                    .path("segment")
                    .fieldType(TestMessageObject.class)
                    .delimiterPosition(DelimiterPosition.NONE)
                    .delimiter(new byte[]{'|'})
                    .children(new ArrayList<>())
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithSegment.class)
                    .children(Arrays.asList(segmentMeta))
                    .build();

            assertThrows(FrameworkRuntimeException.class, () ->
                    deserializer.deserialize(source, integrationMeta, ctx));
        }
    }

    @Nested
    @DisplayName("readUntilDelimiter 메서드 테스트")
    class ReadUntilDelimiterTests {

        @Test
        @DisplayName("readUntilDelimiter - 정상 케이스")
        void readUntilDelimiter_success() throws Exception {
            byte[] source = "Hello|World".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream input = new ByteArrayInputStream(source);

            byte[] result = deserializer.readUntilDelimiter(input, new byte[]{'|'});

            assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("readUntilDelimiter - 다중 바이트 delimiter")
        void readUntilDelimiter_multiByteDelimiter() throws Exception {
            byte[] source = "Hello||World".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream input = new ByteArrayInputStream(source);

            byte[] result = deserializer.readUntilDelimiter(input, new byte[]{'|', '|'});

            assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("readUntilDelimiter - delimiter 없음")
        void readUntilDelimiter_noDelimiter() throws Exception {
            byte[] source = "HelloWorld".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream input = new ByteArrayInputStream(source);

            byte[] result = deserializer.readUntilDelimiter(input, new byte[]{'|'});

            assertEquals("HelloWorld", new String(result, StandardCharsets.UTF_8));
        }
    }

    @Nested
    @DisplayName("readObjectFromStream 메서드 테스트")
    class ReadObjectFromStreamTests {

        @Test
        @DisplayName("readObjectFromStream - 중첩 객체")
        void readObjectFromStream_nestedObject() throws Exception {
            byte[] source = "NestedTest".getBytes(StandardCharsets.UTF_8);

            DefaultMessageFieldMetadata nestedFieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
                    .path("nested.name")
                    .fieldType(String.class)
                    .build();

            DefaultMessageFieldMetadata objectMeta = DefaultMessageFieldMetadata.builder()
                    .id("nested")
                    .name("중첩객체")
                    .fieldName("nested")
                    .type(MessageType.OBJECT)
                    .path("nested")
                    .fieldType(TestMessageObject.class)
                    .children(Arrays.asList(nestedFieldMeta))
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithNested.class)
                    .children(Arrays.asList(objectMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.read(any(byte[].class), any(), any())).thenReturn("NestedTest");

            TestMessageWithNested result = deserializer.deserialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertNotNull(result.getNested());
            assertEquals("NestedTest", result.getNested().getName());
        }
    }

    // 테스트용 클래스들
    public static class TestMessageObject implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestMessageWithCount implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public static class TestMessageWithItems implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private List<TestItem> items;

        public List<TestItem> getItems() {
            return items;
        }

        public void setItems(List<TestItem> items) {
            this.items = items;
        }
    }

    public static class TestItem implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private String itemName;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }

    public static class TestMessageWithVariableData implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private String variableData;

        public String getVariableData() {
            return variableData;
        }

        public void setVariableData(String variableData) {
            this.variableData = variableData;
        }
    }

    public static class TestMessageWithByteData implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private ByteBuffWrap byteData;

        public ByteBuffWrap getByteData() {
            return byteData;
        }

        public void setByteData(ByteBuffWrap byteData) {
            this.byteData = byteData;
        }
    }

    public static class TestMessageWithSegment implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private TestMessageObject segment;

        public TestMessageObject getSegment() {
            return segment;
        }

        public void setSegment(TestMessageObject segment) {
            this.segment = segment;
        }
    }

    public static class TestMessageWithNested implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private TestMessageObject nested;

        public TestMessageObject getNested() {
            return nested;
        }

        public void setNested(TestMessageObject nested) {
            this.nested = nested;
        }
    }
}
