package com.scbank.process.api.fw.message.metadata.factory.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.message.annotation.ConditionalField;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.metadata.DefaultConditionalFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ConditionalFieldMetadataFactory Tests")
class ConditionalFieldMetadataFactoryTest {

    private ConditionalFieldMetadataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ConditionalFieldMetadataFactory();
    }

    // Test class with ConditionalField annotation
    static class TestClassWithConditionalField {
        @MessageField(id = "CF001", name = "conditionalField", length = 10, scale = 2,
                required = true, padding = "0", align = AlignType.RIGHT, masking = true,
                defaultValue = "default", encoding = "UTF-8", encryptType = "AES")
        @ConditionalField(condition = "#useYn == 'Y'", description = "Test condition")
        private String conditionalField;

        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    // Test class without ConditionalField annotation
    static class TestClassWithoutConditionalField {
        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    @Nested
    @DisplayName("supports() method tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for field with @ConditionalField annotation")
        void testSupportsWithConditionalFieldAnnotation() throws Exception {
            Field field = TestClassWithConditionalField.class.getDeclaredField("conditionalField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for field without @ConditionalField annotation")
        void testSupportsWithoutConditionalFieldAnnotation() throws Exception {
            Field field = TestClassWithConditionalField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for field in class without @ConditionalField")
        void testSupportsFieldInClassWithoutConditionalField() throws Exception {
            Field field = TestClassWithoutConditionalField.class.getDeclaredField("normalField");

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
            Field field = TestClassWithConditionalField.class.getDeclaredField("conditionalField");
            int order = 1;
            String path = "root.conditionalField";

            IMessageMetadata result = factory.create(field, order, path);

            assertNotNull(result);
            assertInstanceOf(DefaultConditionalFieldMetadata.class, result);
            DefaultConditionalFieldMetadata metadata = (DefaultConditionalFieldMetadata) result;
            assertEquals("CF001", metadata.getId());
            assertEquals("conditionalField", metadata.getName());
            assertEquals(10, metadata.getLength());
            assertEquals(2, metadata.getScale());
            assertTrue(metadata.isRequired());
            assertEquals("0", metadata.getPadding());
            assertEquals(AlignType.RIGHT, metadata.getAlign());
            assertTrue(metadata.isMasking());
            assertEquals("default", metadata.getDefaultValue());
            assertEquals("UTF-8", metadata.getEncoding());
            assertEquals("AES", metadata.getEncryptType());
            assertEquals(order, metadata.getOrder());
            assertEquals(path, metadata.getPath());
        }

        @Test
        @DisplayName("Should set condition from @ConditionalField annotation")
        void testCreateWithCondition() throws Exception {
            Field field = TestClassWithConditionalField.class.getDeclaredField("conditionalField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultConditionalFieldMetadata.class, result);
            DefaultConditionalFieldMetadata metadata = (DefaultConditionalFieldMetadata) result;
            assertEquals("#useYn == 'Y'", metadata.getCondition());
        }

        @Test
        @DisplayName("Should set field name from reflection")
        void testCreateSetsFieldName() throws Exception {
            Field field = TestClassWithConditionalField.class.getDeclaredField("conditionalField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals("conditionalField", result.getFieldName());
        }

        @Test
        @DisplayName("Should set field type from reflection")
        void testCreateSetsFieldType() throws Exception {
            Field field = TestClassWithConditionalField.class.getDeclaredField("conditionalField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(String.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should create metadata with order and path parameters")
        void testCreateWithOrderAndPath() throws Exception {
            Field field = TestClassWithConditionalField.class.getDeclaredField("conditionalField");
            int expectedOrder = 5;
            String expectedPath = "root.parent.child";

            IMessageMetadata result = factory.create(field, expectedOrder, expectedPath);

            assertEquals(expectedOrder, result.getOrder());
            assertEquals(expectedPath, result.getPath());
        }
    }
}
