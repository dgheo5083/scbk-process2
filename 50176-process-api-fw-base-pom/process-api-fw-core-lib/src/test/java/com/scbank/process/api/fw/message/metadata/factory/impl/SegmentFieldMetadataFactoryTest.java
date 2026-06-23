package com.scbank.process.api.fw.message.metadata.factory.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.SegmentField;
import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.DelimiterType;
import com.scbank.process.api.fw.message.metadata.DefaultSegmentMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SegmentFieldMetadataFactory Tests")
class SegmentFieldMetadataFactoryTest {

    private SegmentFieldMetadataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new SegmentFieldMetadataFactory();
    }

    // Nested segment object for testing
    static class NestedSegmentObject implements IMessageObject {
        @MessageField(id = "NS001", name = "nestedField")
        private String nestedField;
    }

    // Test class with SegmentField annotation
    static class TestClassWithSegmentField {
        @MessageField(id = "SF001", name = "segmentField", required = true, masking = false)
        @SegmentField(delimiter = { 0x1E }, type = DelimiterType.DELIMITER, position = DelimiterPosition.SUFFIX)
        private NestedSegmentObject segmentField;

        @MessageField(id = "SF002", name = "fixedSegmentField", required = false, masking = true)
        @SegmentField(type = DelimiterType.FIXED, position = DelimiterPosition.NONE)
        private NestedSegmentObject fixedSegmentField;

        @MessageField(id = "SF003", name = "prefixSegmentField")
        @SegmentField(delimiter = { 0x1F }, type = DelimiterType.DELIMITER, position = DelimiterPosition.PREFIX)
        private NestedSegmentObject prefixSegmentField;

        @MessageField(id = "SF004", name = "wrappedSegmentField")
        @SegmentField(delimiter = { 0x22 }, type = DelimiterType.DELIMITER, position = DelimiterPosition.WRAPPED)
        private NestedSegmentObject wrappedSegmentField;

        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    // Test class without SegmentField annotation
    static class TestClassWithoutSegmentField {
        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    @Nested
    @DisplayName("supports() method tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for field with @SegmentField annotation")
        void testSupportsWithSegmentFieldAnnotation() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for fixed type segment field")
        void testSupportsWithFixedSegmentField() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("fixedSegmentField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for prefix delimiter segment field")
        void testSupportsWithPrefixSegmentField() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("prefixSegmentField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for wrapped delimiter segment field")
        void testSupportsWithWrappedSegmentField() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("wrappedSegmentField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for field without @SegmentField annotation")
        void testSupportsWithoutSegmentFieldAnnotation() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for field in class without @SegmentField")
        void testSupportsFieldInClassWithoutSegmentField() throws Exception {
            Field field = TestClassWithoutSegmentField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create metadata with delimiter type and position")
        void testCreateWithDelimiterTypeAndPosition() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");
            int order = 1;
            String path = "root.segmentField";

            IMessageMetadata result = factory.create(field, order, path);

            assertNotNull(result);
            assertInstanceOf(DefaultSegmentMessageFieldMetadata.class, result);
            DefaultSegmentMessageFieldMetadata metadata = (DefaultSegmentMessageFieldMetadata) result;
            assertEquals("SF001", metadata.getId());
            assertEquals("segmentField", metadata.getName());
            assertEquals(DelimiterType.DELIMITER, metadata.getDelimiterType());
            assertEquals(DelimiterPosition.SUFFIX, metadata.getDelimiterPosition());
            assertArrayEquals(new byte[] { 0x1E }, metadata.getDelimiter());
            assertTrue(metadata.isRequired());
            assertFalse(metadata.isMasking());
            assertEquals(order, metadata.getOrder());
            assertEquals(path, metadata.getPath());
        }

        @Test
        @DisplayName("Should create metadata with fixed delimiter type")
        void testCreateWithFixedDelimiterType() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("fixedSegmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultSegmentMessageFieldMetadata.class, result);
            DefaultSegmentMessageFieldMetadata metadata = (DefaultSegmentMessageFieldMetadata) result;
            assertEquals(DelimiterType.FIXED, metadata.getDelimiterType());
            assertEquals(DelimiterPosition.NONE, metadata.getDelimiterPosition());
            assertFalse(metadata.isRequired());
            assertTrue(metadata.isMasking());
        }

        @Test
        @DisplayName("Should create metadata with prefix delimiter position")
        void testCreateWithPrefixDelimiterPosition() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("prefixSegmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultSegmentMessageFieldMetadata.class, result);
            DefaultSegmentMessageFieldMetadata metadata = (DefaultSegmentMessageFieldMetadata) result;
            assertEquals(DelimiterPosition.PREFIX, metadata.getDelimiterPosition());
            assertArrayEquals(new byte[] { 0x1F }, metadata.getDelimiter());
        }

        @Test
        @DisplayName("Should create metadata with wrapped delimiter position")
        void testCreateWithWrappedDelimiterPosition() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("wrappedSegmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultSegmentMessageFieldMetadata.class, result);
            DefaultSegmentMessageFieldMetadata metadata = (DefaultSegmentMessageFieldMetadata) result;
            assertEquals(DelimiterPosition.WRAPPED, metadata.getDelimiterPosition());
            assertArrayEquals(new byte[] { 0x22 }, metadata.getDelimiter());
        }

        @Test
        @DisplayName("Should set field name from reflection")
        void testCreateSetsFieldName() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals("segmentField", result.getFieldName());
        }

        @Test
        @DisplayName("Should set field type from reflection")
        void testCreateSetsFieldType() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(NestedSegmentObject.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should set message type to OBJECT")
        void testCreateSetsMessageTypeToObject() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(IMessageMetadata.MessageType.OBJECT, result.getType());
        }

        @Test
        @DisplayName("Should initialize empty children for IMessageObject type")
        void testCreateInitializesEmptyChildrenForMessageObject() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultSegmentMessageFieldMetadata.class, result);
            DefaultSegmentMessageFieldMetadata metadata = (DefaultSegmentMessageFieldMetadata) result;
            assertNotNull(metadata.getChildren());
            assertTrue(metadata.getChildren().isEmpty());
        }

        @Test
        @DisplayName("Should create metadata with order and path parameters")
        void testCreateWithOrderAndPath() throws Exception {
            Field field = TestClassWithSegmentField.class.getDeclaredField("segmentField");
            int expectedOrder = 5;
            String expectedPath = "root.parent.child";

            IMessageMetadata result = factory.create(field, expectedOrder, expectedPath);

            assertEquals(expectedOrder, result.getOrder());
            assertEquals(expectedPath, result.getPath());
        }
    }
}
