package com.scbank.process.api.fw.message.metadata.factory.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.fw.message.metadata.DefaultRepeatFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RepeatedFieldMetadataFactory Tests")
class RepeatedFieldMetadataFactoryTest {

    private RepeatedFieldMetadataFactory factory;
    private BiFunction<Class<?>, String, List<IMessageMetadata>> mockRecursiveExtractor;

    @BeforeEach
    void setUp() {
        mockRecursiveExtractor = (clazz, path) -> new ArrayList<>();
        factory = new RepeatedFieldMetadataFactory(mockRecursiveExtractor);
    }

    // Nested IMessageObject for testing
    static class NestedMessageObject implements IMessageObject {
        @MessageField(id = "NM001", name = "nestedField")
        private String nestedField;
    }

    // Test class with RepeatedField annotation
    static class TestClassWithRepeatedField {
        @MessageField(id = "RF001", name = "repeatedField")
        @RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "5",
                wrapperName = "items", elementName = "item")
        private List<String> repeatedField;

        @MessageField(id = "RF002", name = "referenceRepeatedField")
        @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "countField",
                wrapperName = "data", elementName = "datum")
        private List<Integer> referenceRepeatedField;

        @MessageField(id = "RF003", name = "nestedRepeatedField")
        @RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "3")
        private List<NestedMessageObject> nestedRepeatedField;

        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    // Test class without RepeatedField annotation
    static class TestClassWithoutRepeatedField {
        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    @Nested
    @DisplayName("supports() method tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for field with @RepeatedField annotation")
        void testSupportsWithRepeatedFieldAnnotation() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for reference type repeated field")
        void testSupportsWithReferenceRepeatedField() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("referenceRepeatedField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for nested object repeated field")
        void testSupportsWithNestedRepeatedField() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("nestedRepeatedField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for field without @RepeatedField annotation")
        void testSupportsWithoutRepeatedFieldAnnotation() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for field in class without @RepeatedField")
        void testSupportsFieldInClassWithoutRepeatedField() throws Exception {
            Field field = TestClassWithoutRepeatedField.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create metadata with all RepeatedField properties")
        void testCreateWithAllProperties() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");
            int order = 1;
            String path = "root.repeatedField";

            IMessageMetadata result = factory.create(field, order, path);

            assertNotNull(result);
            assertInstanceOf(DefaultRepeatFieldMetadata.class, result);
            DefaultRepeatFieldMetadata metadata = (DefaultRepeatFieldMetadata) result;
            assertEquals("RF001", metadata.getId());
            assertEquals("repeatedField", metadata.getName());
            assertEquals(RepeatType.FIXED, metadata.getRepeatType());
            assertEquals("5", metadata.getRepeatCount());
            assertEquals("items", metadata.getWrapperName());
            assertEquals("item", metadata.getElementName());
            assertEquals(order, metadata.getOrder());
            assertEquals(path, metadata.getPath());
        }

        @Test
        @DisplayName("Should create metadata with reference repeat type")
        void testCreateWithReferenceRepeatType() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("referenceRepeatedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultRepeatFieldMetadata.class, result);
            DefaultRepeatFieldMetadata metadata = (DefaultRepeatFieldMetadata) result;
            assertEquals(RepeatType.REFERENCE, metadata.getRepeatType());
            assertEquals("countField", metadata.getRepeatCount());
            assertEquals("data", metadata.getWrapperName());
            assertEquals("datum", metadata.getElementName());
        }

        @Test
        @DisplayName("Should set field name from reflection")
        void testCreateSetsFieldName() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals("repeatedField", result.getFieldName());
        }

        @Test
        @DisplayName("Should set field type from reflection")
        void testCreateSetsFieldType() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(List.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should set repeated generic type for primitive type list")
        void testCreateSetsRepeatedGenericTypeForPrimitive() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultRepeatFieldMetadata.class, result);
            DefaultRepeatFieldMetadata metadata = (DefaultRepeatFieldMetadata) result;
            assertEquals(String.class, metadata.getRepeatedGenericType());
        }

        @Test
        @DisplayName("Should set repeated generic type for nested object list")
        void testCreateSetsRepeatedGenericTypeForNestedObject() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("nestedRepeatedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultRepeatFieldMetadata.class, result);
            DefaultRepeatFieldMetadata metadata = (DefaultRepeatFieldMetadata) result;
            assertEquals(NestedMessageObject.class, metadata.getRepeatedGenericType());
        }

        @Test
        @DisplayName("Should create children for primitive type list")
        void testCreateCreatesChildrenForPrimitiveType() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");

            IMessageMetadata result = factory.create(field, 0, "root");

            assertInstanceOf(DefaultRepeatFieldMetadata.class, result);
            DefaultRepeatFieldMetadata metadata = (DefaultRepeatFieldMetadata) result;
            assertNotNull(metadata.getChildren());
            assertFalse(metadata.getChildren().isEmpty());
            assertEquals(1, metadata.getChildren().size());
        }

        @Test
        @DisplayName("Should invoke recursive extractor for IMessageObject type")
        void testCreateInvokesRecursiveExtractorForMessageObject() throws Exception {
            List<IMessageMetadata> mockChildren = new ArrayList<>();
            BiFunction<Class<?>, String, List<IMessageMetadata>> customExtractor = (clazz, path) -> {
                if (clazz == NestedMessageObject.class) {
                    return mockChildren;
                }
                return new ArrayList<>();
            };
            factory = new RepeatedFieldMetadataFactory(customExtractor);
            Field field = TestClassWithRepeatedField.class.getDeclaredField("nestedRepeatedField");

            IMessageMetadata result = factory.create(field, 0, "root");

            assertInstanceOf(DefaultRepeatFieldMetadata.class, result);
            DefaultRepeatFieldMetadata metadata = (DefaultRepeatFieldMetadata) result;
            assertNotNull(metadata.getChildren());
        }

        @Test
        @DisplayName("Should create metadata with order and path parameters")
        void testCreateWithOrderAndPath() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");
            int expectedOrder = 3;
            String expectedPath = "root.parent.child";

            IMessageMetadata result = factory.create(field, expectedOrder, expectedPath);

            assertEquals(expectedOrder, result.getOrder());
            assertEquals(expectedPath, result.getPath());
        }

        @Test
        @DisplayName("Should set message type to REPEATED")
        void testCreateSetsMessageTypeToRepeated() throws Exception {
            Field field = TestClassWithRepeatedField.class.getDeclaredField("repeatedField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(IMessageMetadata.MessageType.REPEATED, result.getType());
        }
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should accept non-null recursive extractor")
        void testConstructorWithValidExtractor() {
            BiFunction<Class<?>, String, List<IMessageMetadata>> extractor = (c, p) -> new ArrayList<>();

            RepeatedFieldMetadataFactory newFactory = new RepeatedFieldMetadataFactory(extractor);

            assertNotNull(newFactory);
        }
    }
}
