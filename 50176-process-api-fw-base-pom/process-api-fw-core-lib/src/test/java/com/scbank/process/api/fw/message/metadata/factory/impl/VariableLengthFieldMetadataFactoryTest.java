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

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.VariableLengthField;
import com.scbank.process.api.fw.message.enums.VariableFieldType;
import com.scbank.process.api.fw.message.metadata.DefaultVariableLengthMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VariableLengthFieldMetadataFactory Tests")
class VariableLengthFieldMetadataFactoryTest {

    private VariableLengthFieldMetadataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new VariableLengthFieldMetadataFactory();
    }

    // Test class with VariableLengthField annotation
    static class TestClassWithVariableLengthField {
        @MessageField(id = "VL001", name = "lengthPrefixField", required = true, masking = true)
        @VariableLengthField(lengthType = VariableFieldType.LENGTH_PREFIX, lengthPrefixSize = 2, encoding = "UTF-8")
        private String lengthPrefixField;

        @MessageField(id = "VL002", name = "referenceField", required = false, masking = false)
        @VariableLengthField(lengthType = VariableFieldType.FIELD_REFERENCE, referenceField = "lengthField", encoding = "EUC-KR")
        private String referenceField;

        @MessageField(id = "VL003", name = "delimiterField")
        @VariableLengthField(lengthType = VariableFieldType.DELIMITER, delimiter = 0x1F, encoding = "UTF-8")
        private String delimiterField;

        @MessageField(id = "VL004", name = "fixedField")
        @VariableLengthField(lengthType = VariableFieldType.FIXED, fixedLength = 100, encoding = "")
        private String fixedField;

        @MessageField(id = "VL005", name = "defaultsField")
        @VariableLengthField()
        private String defaultsField;

        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    // Test class without VariableLengthField annotation
    static class TestClassWithoutVariableLengthField {
        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    @Nested
    @DisplayName("supports() method tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for field with @VariableLengthField annotation")
        void testSupportsWithVariableLengthFieldAnnotation() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for reference type variable length field")
        void testSupportsWithReferenceVariableLengthField() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("referenceField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for delimiter type variable length field")
        void testSupportsWithDelimiterVariableLengthField() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("delimiterField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for fixed type variable length field")
        void testSupportsWithFixedVariableLengthField() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("fixedField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for default variable length field")
        void testSupportsWithDefaultsVariableLengthField() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("defaultsField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for field without @VariableLengthField annotation")
        void testSupportsWithoutVariableLengthFieldAnnotation() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for field in class without @VariableLengthField")
        void testSupportsFieldInClassWithoutVariableLengthField() throws Exception {
            Field field = TestClassWithoutVariableLengthField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create metadata with LENGTH_PREFIX type")
        void testCreateWithLengthPrefixType() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");
            int order = 1;
            String path = "root.lengthPrefixField";

            IMessageMetadata result = factory.create(field, order, path);

            assertNotNull(result);
            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertEquals("VL001", metadata.getId());
            assertEquals("lengthPrefixField", metadata.getName());
            assertEquals(VariableFieldType.LENGTH_PREFIX, metadata.getVariableFieldType());
            assertEquals(2, metadata.getLengthPrefixSize());
            assertEquals("UTF-8", metadata.getEncoding());
            assertTrue(metadata.isRequired());
            assertTrue(metadata.isMasking());
            assertEquals(order, metadata.getOrder());
            assertEquals(path, metadata.getPath());
        }

        @Test
        @DisplayName("Should create metadata with FIELD_REFERENCE type")
        void testCreateWithFieldReferenceType() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("referenceField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertEquals(VariableFieldType.FIELD_REFERENCE, metadata.getVariableFieldType());
            assertEquals("lengthField", metadata.getReferenceField());
            assertEquals("EUC-KR", metadata.getEncoding());
            assertFalse(metadata.isRequired());
            assertFalse(metadata.isMasking());
        }

        @Test
        @DisplayName("Should create metadata with DELIMITER type")
        void testCreateWithDelimiterType() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("delimiterField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertEquals(VariableFieldType.DELIMITER, metadata.getVariableFieldType());
            assertEquals((byte) 0x1F, metadata.getDelimiter());
            assertEquals("UTF-8", metadata.getEncoding());
        }

        @Test
        @DisplayName("Should create metadata with FIXED type")
        void testCreateWithFixedType() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("fixedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertEquals(VariableFieldType.FIXED, metadata.getVariableFieldType());
            assertEquals(100, metadata.getFixedLength());
            assertEquals("", metadata.getEncoding());
        }

        @Test
        @DisplayName("Should create metadata with default values")
        void testCreateWithDefaultValues() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("defaultsField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertEquals(VariableFieldType.FIXED, metadata.getVariableFieldType());
            assertEquals("", metadata.getReferenceField());
            assertEquals(0, metadata.getLengthPrefixSize());
            assertEquals(-1, metadata.getFixedLength());
            assertEquals((byte) 0x00, metadata.getDelimiter());
            assertEquals("", metadata.getEncoding());
        }

        @Test
        @DisplayName("Should set field name from reflection")
        void testCreateSetsFieldName() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals("lengthPrefixField", result.getFieldName());
        }

        @Test
        @DisplayName("Should set field type from reflection")
        void testCreateSetsFieldType() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(String.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should set message type to VARIABLE_LENGTH")
        void testCreateSetsMessageTypeToVariableLength() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(IMessageMetadata.MessageType.VARIABLE_LENGTH, result.getType());
        }

        @Test
        @DisplayName("Should create metadata with order and path parameters")
        void testCreateWithOrderAndPath() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");
            int expectedOrder = 7;
            String expectedPath = "root.parent.child";

            IMessageMetadata result = factory.create(field, expectedOrder, expectedPath);

            assertEquals(expectedOrder, result.getOrder());
            assertEquals(expectedPath, result.getPath());
        }

        @Test
        @DisplayName("Should copy masking from MessageField annotation")
        void testCreateCopiesMaskingFromMessageField() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertTrue(metadata.isMasking());
        }

        @Test
        @DisplayName("Should copy required from MessageField annotation")
        void testCreateCopiesRequiredFromMessageField() throws Exception {
            Field field = TestClassWithVariableLengthField.class.getDeclaredField("lengthPrefixField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultVariableLengthMessageFieldMetadata.class, result);
            DefaultVariableLengthMessageFieldMetadata metadata = (DefaultVariableLengthMessageFieldMetadata) result;
            assertTrue(metadata.isRequired());
        }
    }
}
