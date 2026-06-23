package com.scbank.process.api.fw.message.serializer.fixedlength;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
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
import com.scbank.process.api.fw.message.metadata.DefaultIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultRepeatFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultSegmentMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultVariableLengthMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

@DisplayName("FixedLengthMessageSerializer 테스트")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class FixedLengthMessageSerializerTest {

    private FixedLengthMessageSerializer serializer;

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
        serializer = new FixedLengthMessageSerializer();
        ctx = new MessageContext();
        ctx.setDefaultEncoding("UTF-8");
        ctx.setMessageFieldConverterRegistry(converterRegistry);
    }

    @Nested
    @DisplayName("serialize 메서드 테스트")
    class SerializeTests {

        @Test
        @DisplayName("serialize - source만으로 직렬화")
        void serialize_withSourceOnly() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setName("TestName");

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
            when(stringConverter.write(any(), any(), any())).thenReturn("TestName  ".getBytes(StandardCharsets.UTF_8));

            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMeta);

                byte[] result = serializer.serialize(source, ctx);

                assertNotNull(result);
                assertEquals(10, result.length);
            }
        }

        @Test
        @DisplayName("serialize - metadata가 null인 경우")
        void serialize_nullMetadata() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setName("TestName");

            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(null);

                byte[] result = serializer.serialize(source, ctx);
                assertNull(result);
            }
        }

        @Test
        @DisplayName("serialize - metadata와 함께 직렬화")
        void serialize_withMetadata() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setName("Hello");

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
            when(stringConverter.write(any(), any(), any())).thenReturn("Hello     ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(10, result.length);
        }
    }

    @Nested
    @DisplayName("writePrimitiveField 메서드 테스트")
    class WritePrimitiveFieldTests {

        @Test
        @DisplayName("writePrimitiveField - STRING 타입")
        void writePrimitiveField_string() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setName("Test");

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
            when(stringConverter.write(any(), any(), any())).thenReturn("Test      ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(10, result.length);
        }

        @Test
        @DisplayName("writePrimitiveField - INTEGER 타입")
        void writePrimitiveField_integer() throws Exception {
            TestMessageWithCount source = new TestMessageWithCount();
            source.setCount(123);

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
            when(integerConverter.write(any(), any(), any())).thenReturn("00123".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(5, result.length);
        }

        @Test
        @DisplayName("writePrimitiveField - 지원하지 않는 타입")
        void writePrimitiveField_unsupportedType() {
            TestMessageObject source = new TestMessageObject();
            source.setName("Test");

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
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
                    serializer.serialize(source, integrationMeta, ctx));
        }
    }

    @Nested
    @DisplayName("writeRepeatedField 메서드 테스트")
    class WriteRepeatedFieldTests {

        @Test
        @DisplayName("writeRepeatedField - 리스트 직렬화")
        void writeRepeatedField_list() throws Exception {
            TestMessageWithItems source = new TestMessageWithItems();
            List<TestItem> items = new ArrayList<>();
            TestItem item1 = new TestItem();
            item1.setItemName("Item1");
            items.add(item1);
            TestItem item2 = new TestItem();
            item2.setItemName("Item2");
            items.add(item2);
            source.setItems(items);

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
            when(stringConverter.write(any(), any(), any()))
                    .thenReturn("Item1     ".getBytes(StandardCharsets.UTF_8))
                    .thenReturn("Item2     ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(20, result.length);
        }

        @Test
        @DisplayName("writeRepeatedField - 리스트가 아닌 값")
        void writeRepeatedField_notList() {
            TestMessageObject source = new TestMessageObject();
            source.setName("NotList");

            DefaultMessageFieldMetadata repeatMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.REPEATED)
                    .path("name")
                    .fieldType(String.class)
                    .children(new ArrayList<>())
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageObject.class)
                    .children(Arrays.asList(repeatMeta))
                    .build();

//            assertThrows(IllegalArgumentException.class, () ->
//                    serializer.serialize(source, integrationMeta, ctx));
        }

        @Test
        @DisplayName("writeRepeatedField - 빈 자식 메타데이터")
        void writeRepeatedField_emptyChildren() throws Exception {
            TestMessageWithItems source = new TestMessageWithItems();
            List<TestItem> items = new ArrayList<>();
            items.add(new TestItem());
            source.setItems(items);

            DefaultMessageFieldMetadata repeatMeta = DefaultMessageFieldMetadata.builder()
                    .id("items")
                    .name("항목목록")
                    .fieldName("items")
                    .type(MessageType.REPEATED)
                    .path("items")
                    .fieldType(List.class)
                    .children(null)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithItems.class)
                    .children(Arrays.asList(repeatMeta))
                    .build();

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(0, result.length);
        }
    }

    @Nested
    @DisplayName("writeSegmentObjectField 메서드 테스트")
    class WriteSegmentObjectFieldTests {

        @Test
        @DisplayName("writeSegmentObjectField - PREFIX delimiter")
        void writeSegmentObjectField_prefix() throws Exception {
            TestMessageWithSegment source = new TestMessageWithSegment();
            TestMessageObject segment = new TestMessageObject();
            segment.setName("Test");
            source.setSegment(segment);

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
            when(stringConverter.write(any(), any(), any())).thenReturn("Test      ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(11, result.length); // 1 (delimiter) + 10 (data)
            assertEquals('|', result[0]);
        }

        @Test
        @DisplayName("writeSegmentObjectField - SUFFIX delimiter")
        void writeSegmentObjectField_suffix() throws Exception {
            TestMessageWithSegment source = new TestMessageWithSegment();
            TestMessageObject segment = new TestMessageObject();
            segment.setName("Test");
            source.setSegment(segment);

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
            when(stringConverter.write(any(), any(), any())).thenReturn("Test      ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(11, result.length); // 10 (data) + 1 (delimiter)
            assertEquals('|', result[10]);
        }

        @Test
        @DisplayName("writeSegmentObjectField - WRAPPED delimiter")
        void writeSegmentObjectField_wrapped() throws Exception {
            TestMessageWithSegment source = new TestMessageWithSegment();
            TestMessageObject segment = new TestMessageObject();
            segment.setName("Test");
            source.setSegment(segment);

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
            when(stringConverter.write(any(), any(), any())).thenReturn("Test      ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(12, result.length); // 1 + 10 + 1
            assertEquals('|', result[0]);
            assertEquals('|', result[11]);
        }

        @Test
        @DisplayName("writeSegmentObjectField - NONE delimiter")
        void writeSegmentObjectField_none() throws Exception {
            TestMessageWithSegment source = new TestMessageWithSegment();
            TestMessageObject segment = new TestMessageObject();
            segment.setName("Test");
            source.setSegment(segment);

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
                    .delimiterPosition(DelimiterPosition.NONE)
                    .delimiter(new byte[]{'|'})
                    .children(Arrays.asList(childFieldMeta))
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithSegment.class)
                    .children(Arrays.asList(segmentMeta))
                    .build();

            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Test      ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(10, result.length); // no delimiter
        }

        @Test
        @DisplayName("writeSegmentObjectField - null segment value")
        void writeSegmentObjectField_nullValue() throws Exception {
            TestMessageWithSegment source = new TestMessageWithSegment();
            source.setSegment(null);

            DefaultSegmentMessageFieldMetadata segmentMeta = DefaultSegmentMessageFieldMetadata.builder()
                    .id("segment")
                    .name("세그먼트")
                    .fieldName("segment")
                    .type(MessageType.OBJECT)
                    .path("segment")
                    .fieldType(TestMessageObject.class)
                    .delimiterPosition(DelimiterPosition.PREFIX)
                    .delimiter(new byte[]{'|'})
                    .children(new ArrayList<>())
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithSegment.class)
                    .children(Arrays.asList(segmentMeta))
                    .build();

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(0, result.length);
        }
    }

    @Nested
    @DisplayName("writeVariableLengthField 메서드 테스트")
    class WriteVariableLengthFieldTests {

        @Test
        @DisplayName("writeVariableLengthField - FIELD_REFERENCE")
        void writeVariableLengthField_fieldReference() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Hello");

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
            
            when(converterRegistry.get(MessageType.VARIABLE_LENGTH)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Hello".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("writeVariableLengthField - LENGTH_PREFIX")
        void writeVariableLengthField_lengthPrefix() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Hello");

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
            
            when(converterRegistry.get(MessageType.VARIABLE_LENGTH)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Hello".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(7, result.length); // 2 (prefix) + 5 (data)
        }

        @Test
        @DisplayName("writeVariableLengthField - LENGTH_PREFIX 1 byte")
        void writeVariableLengthField_lengthPrefix1Byte() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Hi");

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.LENGTH_PREFIX)
                    .lengthPrefixSize(1)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();
            
            when(converterRegistry.get(MessageType.VARIABLE_LENGTH)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Hi".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(3, result.length); // 1 + 2
        }

        @Test
        @DisplayName("writeVariableLengthField - LENGTH_PREFIX 4 byte")
        void writeVariableLengthField_lengthPrefix4Byte() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Test");

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.LENGTH_PREFIX)
                    .lengthPrefixSize(4)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();
            
            when(converterRegistry.get(MessageType.VARIABLE_LENGTH)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Test".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(8, result.length); // 4 + 4
        }

        @Test
        @DisplayName("writeVariableLengthField - DELIMITER")
        void writeVariableLengthField_delimiter() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Hello");

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
            
            when(converterRegistry.get(MessageType.VARIABLE_LENGTH)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Hello".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(6, result.length); // 5 + 1 (delimiter)
            assertEquals('|', result[5]);
        }

        @Test
        @DisplayName("writeVariableLengthField - FIXED")
        void writeVariableLengthField_fixed() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Hi");

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.FIXED)
                    .fixedLength(10)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();
            
            when(converterRegistry.get(MessageType.VARIABLE_LENGTH)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Hi".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(10, result.length); // padded to fixed length
        }

        @Test
        @DisplayName("writeVariableLengthField - FIXED 길이 초과")
        void writeVariableLengthField_fixed_overflow() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("TooLongValue");

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
            
            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("TooLongValue".getBytes(StandardCharsets.UTF_8));

            assertThrows(Exception.class, () ->
                    serializer.serialize(source, integrationMeta, ctx));
        }

        @Test
        @DisplayName("writeVariableLengthField - ByteBuffWrap 타입")
        void writeVariableLengthField_byteBuffWrap() throws Exception {
            TestMessageWithByteData source = new TestMessageWithByteData();
            source.setByteData(ByteBuffWrap.wrap("Test".getBytes(StandardCharsets.UTF_8)));

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("byteData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("byteData")
                    .fieldType(ByteBuffWrap.class)
                    .variableFieldType(VariableFieldType.FIELD_REFERENCE)
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithByteData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();
            
            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(4, result.length);
        }

        @Test
        @DisplayName("writeVariableLengthField - unsupported lengthPrefixSize")
        void writeVariableLengthField_unsupportedPrefixSize() throws Exception {
            TestMessageWithVariableData source = new TestMessageWithVariableData();
            source.setVariableData("Test");

            DefaultVariableLengthMessageFieldMetadata fieldMeta = DefaultVariableLengthMessageFieldMetadata.builder()
                    .id("data")
                    .name("데이터")
                    .fieldName("variableData")
                    .type(MessageType.VARIABLE_LENGTH)
                    .path("variableData")
                    .fieldType(String.class)
                    .variableFieldType(VariableFieldType.LENGTH_PREFIX)
                    .lengthPrefixSize(3) // unsupported
                    .build();

            DefaultIntegrationMessageMetadata integrationMeta = DefaultIntegrationMessageMetadata.builder()
                    .id("TestMessage")
                    .targetClass(TestMessageWithVariableData.class)
                    .children(Arrays.asList(fieldMeta))
                    .build();
            
            when(converterRegistry.get(MessageType.STRING)).thenReturn(stringConverter);
            when(stringConverter.write(any(), any(), any())).thenReturn("Test".getBytes(StandardCharsets.UTF_8));

            assertThrows(Exception.class, () ->
                    serializer.serialize(source, integrationMeta, ctx));
        }
    }

    @Nested
    @DisplayName("writeObjectField 메서드 테스트")
    class WriteObjectFieldTests {

        @Test
        @DisplayName("writeObjectField - 중첩 객체")
        void writeObjectField_nestedObject() throws Exception {
            TestMessageWithNested source = new TestMessageWithNested();
            TestMessageObject nested = new TestMessageObject();
            nested.setName("Nested");
            source.setNested(nested);

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
            when(stringConverter.write(any(), any(), any())).thenReturn("Nested    ".getBytes(StandardCharsets.UTF_8));

            byte[] result = serializer.serialize(source, integrationMeta, ctx);

            assertNotNull(result);
            assertEquals(10, result.length);
        }
    }

    @Nested
    @DisplayName("tryWriteExtE2EIgnoreField 메서드 테스트")
    class TryWriteExtE2EIgnoreFieldTests {

        @Test
        @DisplayName("tryWriteExtE2EIgnoreField - 반환값 false (일반 필드)")
        void tryWriteExtE2EIgnoreField_normalField() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            DefaultMessageFieldMetadata fieldMeta = DefaultMessageFieldMetadata.builder()
                    .id("name")
                    .name("이름")
                    .fieldName("name")
                    .type(MessageType.STRING)
                    .length(10)
                    .path("name")
                    .fieldType(String.class)
                    .build();

            boolean result = serializer.tryWriteExtE2EIgnoreField(out, fieldMeta, "test", -1, ctx);

            assertFalse(result);
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
