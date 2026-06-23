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

import com.scbank.process.api.fw.message.annotation.ExtE2EIgnoreSegment;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.metadata.DefaultExtE2EIgnoreSegmentFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ExtE2EIgnoreSegmentFieldMetadataFactory Tests")
class ExtE2EIgnoreSegmentFieldMetadataFactoryTest {

    private ExtE2EIgnoreSegmentFieldMetadataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ExtE2EIgnoreSegmentFieldMetadataFactory();
    }

    // Test class with ExtE2EIgnoreSegment annotation
    static class TestClassWithExtE2EIgnoreSegment {
        @MessageField(id = "E2E001", name = "e2eField", length = 100, scale = 0,
                required = true, padding = " ", align = AlignType.LEFT, masking = true,
                defaultValue = "", encoding = "UTF-8", encryptType = "")
        @ExtE2EIgnoreSegment(start = "_DNFE2E_", end = "_/DNFE2E_")
        private String e2eField;

        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    // Test class with different start/end values
    static class TestClassWithDifferentE2EValues {
        @MessageField(id = "E2E002", name = "customE2eField")
        @ExtE2EIgnoreSegment(start = "<START>", end = "</END>")
        private String customE2eField;
    }

    // Test class without ExtE2EIgnoreSegment annotation
    static class TestClassWithoutExtE2EIgnoreSegment {
        @MessageField(id = "NF001", name = "normalField")
        private String normalField;
    }

    @Nested
    @DisplayName("supports() method tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true for field with @ExtE2EIgnoreSegment annotation")
        void testSupportsWithExtE2EIgnoreSegmentAnnotation() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for field without @ExtE2EIgnoreSegment annotation")
        void testSupportsWithoutExtE2EIgnoreSegmentAnnotation() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false for field in class without @ExtE2EIgnoreSegment")
        void testSupportsFieldInClassWithoutExtE2EIgnoreSegment() throws Exception {
            Field field = TestClassWithoutExtE2EIgnoreSegment.class.getDeclaredField("normalField");

            boolean result = factory.supports(field);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return true for field with custom start/end values")
        void testSupportsWithCustomStartEndValues() throws Exception {
            Field field = TestClassWithDifferentE2EValues.class.getDeclaredField("customE2eField");

            boolean result = factory.supports(field);

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create metadata with all MessageField properties")
        void testCreateWithAllProperties() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");
            int order = 1;
            String path = "root.e2eField";

            IMessageMetadata result = factory.create(field, order, path);

            assertNotNull(result);
            assertInstanceOf(DefaultExtE2EIgnoreSegmentFieldMetadata.class, result);
            DefaultExtE2EIgnoreSegmentFieldMetadata metadata = (DefaultExtE2EIgnoreSegmentFieldMetadata) result;
            assertEquals("E2E001", metadata.getId());
            assertEquals("e2eField", metadata.getName());
            assertEquals(100, metadata.getLength());
            assertEquals(0, metadata.getScale());
            assertTrue(metadata.isRequired());
            assertEquals(" ", metadata.getPadding());
            assertEquals(AlignType.LEFT, metadata.getAlign());
            assertTrue(metadata.isMasking());
            assertEquals("", metadata.getDefaultValue());
            assertEquals("UTF-8", metadata.getEncoding());
            assertEquals("", metadata.getEncryptType());
            assertEquals(order, metadata.getOrder());
            assertEquals(path, metadata.getPath());
        }

        @Test
        @DisplayName("Should set start from @ExtE2EIgnoreSegment annotation")
        void testCreateWithStart() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultExtE2EIgnoreSegmentFieldMetadata.class, result);
            DefaultExtE2EIgnoreSegmentFieldMetadata metadata = (DefaultExtE2EIgnoreSegmentFieldMetadata) result;
            assertEquals("_DNFE2E_", metadata.getStart());
        }

        @Test
        @DisplayName("Should set end from @ExtE2EIgnoreSegment annotation")
        void testCreateWithEnd() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultExtE2EIgnoreSegmentFieldMetadata.class, result);
            DefaultExtE2EIgnoreSegmentFieldMetadata metadata = (DefaultExtE2EIgnoreSegmentFieldMetadata) result;
            assertEquals("_/DNFE2E_", metadata.getEnd());
        }

        @Test
        @DisplayName("Should set custom start/end values")
        void testCreateWithCustomStartEndValues() throws Exception {
            Field field = TestClassWithDifferentE2EValues.class.getDeclaredField("customE2eField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertInstanceOf(DefaultExtE2EIgnoreSegmentFieldMetadata.class, result);
            DefaultExtE2EIgnoreSegmentFieldMetadata metadata = (DefaultExtE2EIgnoreSegmentFieldMetadata) result;
            assertEquals("<START>", metadata.getStart());
            assertEquals("</END>", metadata.getEnd());
        }

        @Test
        @DisplayName("Should set field name from reflection")
        void testCreateSetsFieldName() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals("e2eField", result.getFieldName());
        }

        @Test
        @DisplayName("Should set field type from reflection")
        void testCreateSetsFieldType() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");

            IMessageMetadata result = factory.create(field, 0, "path");

            assertEquals(String.class, result.getFieldType());
        }

        @Test
        @DisplayName("Should create metadata with order and path parameters")
        void testCreateWithOrderAndPath() throws Exception {
            Field field = TestClassWithExtE2EIgnoreSegment.class.getDeclaredField("e2eField");
            int expectedOrder = 7;
            String expectedPath = "root.parent.child";

            IMessageMetadata result = factory.create(field, expectedOrder, expectedPath);

            assertEquals(expectedOrder, result.getOrder());
            assertEquals(expectedPath, result.getPath());
        }
    }
}
