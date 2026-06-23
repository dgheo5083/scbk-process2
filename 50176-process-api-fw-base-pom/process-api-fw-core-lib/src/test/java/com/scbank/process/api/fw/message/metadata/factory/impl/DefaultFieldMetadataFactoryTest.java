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
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.metadata.DefaultMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("DefaultFieldMetadataFactory Tests")
class DefaultFieldMetadataFactoryTest {

    private DefaultFieldMetadataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DefaultFieldMetadataFactory();
    }

    // Test class with MessageField annotation and all properties
    static class TestClassWithMessageField {
        @MessageField(id = "MF001", name = "fullField", length = 20, scale = 3,
                required = true, padding = " ", align = AlignType.LEFT, masking = true,
                maskingType = "ACCOUNT", defaultValue = "N/A", encoding = "EUC-KR",
                encryptType = "SHA256", sosi = true, multiBytes = true, example = "sample")
        private String fullField;

        @MessageField(id = "MF002", name = "simpleField")
        private String simpleField;

        @MessageField(id = "MF003", name = "intField", length = 10)
        private Integer intField;

        private String noAnnotationField;
    }

    // Test class without any MessageField annotation
    static class TestClassWithoutMessageField {
        private String plainField;
    }

    @Nested
    @DisplayName("supports() method tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for field with @MessageField annotation")
        void testSupportsWithMessageFieldAnnotation() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("fullField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for simple field with @MessageField annotation")
        void testSupportsWithSimpleMessageFieldAnnotation() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("simpleField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for field without @MessageField annotation")
        void testSupportsWithoutMessageFieldAnnotation() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("noAnnotationField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for field in class without @MessageField")
        void testSupportsFieldInClassWithoutMessageField() throws Exception {
            Field field = TestClassWithoutMessageField.class.getDeclaredField("plainField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create metadata with all MessageField properties")
        void testCreateWithAllProperties() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("fullField");
            int order = 1;
            String path = "root.fullField";

            IMessageMetadata result = factory.create(field, order, path);

            assertNotNull(result);
            assertInstanceOf(DefaultMessageFieldMetadata.class, result);
            DefaultMessageFieldMetadata metadata = (DefaultMessageFieldMetadata) result;
            assertEquals("MF001", metadata.getId());
            assertEquals("fullField", metadata.getName());
            assertEquals(20, metadata.getLength());
            assertEquals(3, metadata.getScale());
            assertTrue(metadata.isRequired());
            assertEquals(" ", metadata.getPadding());
            assertEquals(AlignType.LEFT, metadata.getAlign());
            assertTrue(metadata.isMasking());
            assertEquals("ACCOUNT", metadata.getMaskingType());
            assertEquals("N/A", metadata.getDefaultValue());
            assertEquals("EUC-KR", metadata.getEncoding());
            assertEquals("SHA256", metadata.getEncryptType());
            assertEquals(order, metadata.getOrder());
            assertEquals(path, metadata.getPath());
            assertTrue(metadata.isSosi());
            assertTrue(metadata.isMultiBytes());
            assertEquals("sample", metadata.getExample());
        }

        @Test
        @DisplayName("Should create metadata with default values for simple field")
        void testCreateWithDefaultValues() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("simpleField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultMessageFieldMetadata.class, result);
            DefaultMessageFieldMetadata metadata = (DefaultMessageFieldMetadata) result;
            assertEquals("MF002", metadata.getId());
            assertEquals("simpleField", metadata.getName());
            assertEquals(0, metadata.getLength());
            assertEquals(0, metadata.getScale());
            assertFalse(metadata.isRequired());
            assertEquals(AlignType.NONE, metadata.getAlign());
            assertFalse(metadata.isMasking());
            assertEquals("", metadata.getMaskingType());
            assertEquals("", metadata.getDefaultValue());
            assertEquals("", metadata.getEncoding());
            assertEquals("", metadata.getEncryptType());
            assertFalse(metadata.isSosi());
            assertFalse(metadata.isMultiBytes());
            assertEquals("", metadata.getExample());
        }

        @Test
        @DisplayName("Should set field name from reflection")
        void testCreateSetsFieldName() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("fullField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals("fullField", result.getFieldName());
        }

        @Test
        @DisplayName("Should set field type from reflection for String")
        void testCreateSetsFieldTypeString() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("fullField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(String.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should set field type from reflection for Integer")
        void testCreateSetsFieldTypeInteger() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("intField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(Integer.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should create metadata with order parameter")
        void testCreateWithOrder() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("simpleField");
            int expectedOrder = 10;

            IMessageMetadata result = factory.create(field, expectedOrder, "path");

            assertEquals(expectedOrder, result.getOrder());
        }

        @Test
        @DisplayName("Should create metadata with path parameter")
        void testCreateWithPath() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("simpleField");
            String expectedPath = "root.parent.child.field";

            IMessageMetadata result = factory.create(field, 0, expectedPath);

            assertEquals(expectedPath, result.getPath());
        }

        @Test
        @DisplayName("Should determine message type from Java type")
        void testCreateDeterminesMessageType() throws Exception {
            Field field = TestClassWithMessageField.class.getDeclaredField("simpleField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertNotNull(result.getType());
        }
    }
}
