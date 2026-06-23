package com.scbank.process.api.fw.message.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;

/**
 * {@link MessageUtils} 단위 테스트
 */
@DisplayName("MessageUtils 테스트")
class MessageUtilsTest {

    @Nested
    @DisplayName("getAllFields 테스트")
    class GetAllFieldsTests {

        @Test
        @DisplayName("클래스의 모든 필드를 조회할 수 있다")
        void getAllFieldsFromClass() {
            // when
            List<Field> fields = MessageUtils.getAllFields(TestClass.class);

            // then
            assertNotNull(fields);
            assertTrue(fields.size() >= 2); // name, value
        }

        @Test
        @DisplayName("상위 클래스의 필드도 포함한다")
        void includesParentFields() {
            // when
            List<Field> fields = MessageUtils.getAllFields(ChildTestClass.class);

            // then
            assertNotNull(fields);
            assertTrue(fields.size() >= 3); // parentField, name, value
        }

        @Test
        @DisplayName("Object 클래스의 필드는 포함하지 않는다")
        void excludesObjectFields() {
            // when
            List<Field> fields = MessageUtils.getAllFields(TestClass.class);

            // then
            boolean hasObjectField = fields.stream()
                    .anyMatch(f -> f.getDeclaringClass() == Object.class);
            assertFalse(hasObjectField);
        }
    }

    @Nested
    @DisplayName("getFieldValue 테스트")
    class GetFieldValueTests {

        @Test
        @DisplayName("필드 값을 조회할 수 있다")
        void getFieldValue() {
            // given
            TestClass obj = new TestClass();
            obj.name = "testName";

            // when
            Object result = MessageUtils.getFieldValue(obj, "name");

            // then
            assertEquals("testName", result);
        }

        @Test
        @DisplayName("null 객체는 null을 반환한다")
        void nullObjectReturnsNull() {
            // when
            Object result = MessageUtils.getFieldValue(null, "name");

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("null 필드명은 예외를 발생시킨다")
        void nullFieldNameThrowsException() {
            // given
            TestClass obj = new TestClass();

            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> MessageUtils.getFieldValue(obj, null));
        }

        @Test
        @DisplayName("존재하지 않는 필드는 예외를 발생시킨다")
        void nonExistentFieldThrowsException() {
            // given
            TestClass obj = new TestClass();

            // when & then
            assertThrows(IllegalStateException.class,
                    () -> MessageUtils.getFieldValue(obj, "nonExistent"));
        }
    }

    @Nested
    @DisplayName("setFieldValue 테스트")
    class SetFieldValueTests {

        @Test
        @DisplayName("필드 값을 설정할 수 있다")
        void setFieldValue() {
            // given
            TestClass obj = new TestClass();

            // when
            MessageUtils.setFieldValue(obj, "name", "newValue");

            // then
            assertEquals("newValue", obj.name);
        }

        @Test
        @DisplayName("null 객체는 예외를 발생시킨다")
        void nullObjectThrowsException() {
            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> MessageUtils.setFieldValue(null, "name", "value"));
        }

        @Test
        @DisplayName("null 필드명은 예외를 발생시킨다")
        void nullFieldNameThrowsException() {
            // given
            TestClass obj = new TestClass();

            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> MessageUtils.setFieldValue(obj, null, "value"));
        }
    }

    @Nested
    @DisplayName("fromJavaType 테스트")
    class FromJavaTypeTests {

        @Test
        @DisplayName("String 타입은 STRING을 반환한다")
        void stringType() {
            assertEquals(MessageType.STRING, MessageUtils.fromJavaType(String.class));
        }

        @Test
        @DisplayName("Byte 타입은 BYTE를 반환한다")
        void byteType() {
            assertEquals(MessageType.BYTE, MessageUtils.fromJavaType(Byte.class));
            assertEquals(MessageType.BYTE, MessageUtils.fromJavaType(byte.class));
        }

        @Test
        @DisplayName("Short 타입은 SHORT를 반환한다")
        void shortType() {
            assertEquals(MessageType.SHORT, MessageUtils.fromJavaType(Short.class));
            assertEquals(MessageType.SHORT, MessageUtils.fromJavaType(short.class));
        }

        @Test
        @DisplayName("Character 타입은 CHAR를 반환한다")
        void charType() {
            assertEquals(MessageType.CHAR, MessageUtils.fromJavaType(Character.class));
            assertEquals(MessageType.CHAR, MessageUtils.fromJavaType(char.class));
        }

        @Test
        @DisplayName("Integer 타입은 INTEGER를 반환한다")
        void integerType() {
            assertEquals(MessageType.INTEGER, MessageUtils.fromJavaType(Integer.class));
            assertEquals(MessageType.INTEGER, MessageUtils.fromJavaType(int.class));
        }

        @Test
        @DisplayName("Long 타입은 LONG을 반환한다")
        void longType() {
            assertEquals(MessageType.LONG, MessageUtils.fromJavaType(Long.class));
            assertEquals(MessageType.LONG, MessageUtils.fromJavaType(long.class));
        }

        @Test
        @DisplayName("Double 타입은 DOUBLE을 반환한다")
        void doubleType() {
            assertEquals(MessageType.DOUBLE, MessageUtils.fromJavaType(Double.class));
            assertEquals(MessageType.DOUBLE, MessageUtils.fromJavaType(double.class));
        }

        @Test
        @DisplayName("Float 타입은 FLOAT를 반환한다")
        void floatType() {
            assertEquals(MessageType.FLOAT, MessageUtils.fromJavaType(Float.class));
            assertEquals(MessageType.FLOAT, MessageUtils.fromJavaType(float.class));
        }

        @Test
        @DisplayName("BigDecimal 타입은 BIGDECIMAL을 반환한다")
        void bigDecimalType() {
            assertEquals(MessageType.BIGDECIMAL, MessageUtils.fromJavaType(BigDecimal.class));
        }

        @Test
        @DisplayName("byte[] 타입은 VARIABLE_LENGTH를 반환한다")
        void byteArrayType() {
            assertEquals(MessageType.VARIABLE_LENGTH, MessageUtils.fromJavaType(byte[].class));
        }

        @Test
        @DisplayName("null 입력은 NONE을 반환한다")
        void nullInput() {
            assertEquals(MessageType.NONE, MessageUtils.fromJavaType(null));
        }
    }

    @Nested
    @DisplayName("isPrimitiveType 테스트")
    class IsPrimitiveTypeTests {

        @Test
        @DisplayName("기본 타입은 true를 반환한다")
        void primitiveTypes() {
            assertTrue(MessageUtils.isPrimitiveType(int.class));
            assertTrue(MessageUtils.isPrimitiveType(long.class));
            assertTrue(MessageUtils.isPrimitiveType(double.class));
            assertTrue(MessageUtils.isPrimitiveType(float.class));
            assertTrue(MessageUtils.isPrimitiveType(boolean.class));
        }

        @Test
        @DisplayName("Wrapper 타입은 true를 반환한다")
        void wrapperTypes() {
            assertTrue(MessageUtils.isPrimitiveType(Integer.class));
            assertTrue(MessageUtils.isPrimitiveType(Long.class));
            assertTrue(MessageUtils.isPrimitiveType(Double.class));
            assertTrue(MessageUtils.isPrimitiveType(Float.class));
            assertTrue(MessageUtils.isPrimitiveType(Boolean.class));
        }

        @Test
        @DisplayName("String은 true를 반환한다")
        void stringType() {
            assertTrue(MessageUtils.isPrimitiveType(String.class));
        }

        @Test
        @DisplayName("BigDecimal은 true를 반환한다")
        void bigDecimalType() {
            assertTrue(MessageUtils.isPrimitiveType(BigDecimal.class));
        }

        @Test
        @DisplayName("LocalDate는 true를 반환한다")
        void localDateType() {
            assertTrue(MessageUtils.isPrimitiveType(LocalDate.class));
        }

        @Test
        @DisplayName("LocalDateTime은 true를 반환한다")
        void localDateTimeType() {
            assertTrue(MessageUtils.isPrimitiveType(LocalDateTime.class));
        }

        @Test
        @DisplayName("UUID는 true를 반환한다")
        void uuidType() {
            assertTrue(MessageUtils.isPrimitiveType(UUID.class));
        }

        @Test
        @DisplayName("커스텀 클래스는 false를 반환한다")
        void customClassType() {
            assertFalse(MessageUtils.isPrimitiveType(TestClass.class));
        }
    }

    @Nested
    @DisplayName("addToListField 테스트")
    class AddToListFieldTests {

        @Test
        @DisplayName("리스트 필드에 요소를 추가할 수 있다")
        void addElement() {
            // given
            TestListClass obj = new TestListClass();
            obj.items = new ArrayList<>();

            // when
            MessageUtils.addToListField(obj, "items", "item1");
            MessageUtils.addToListField(obj, "items", "item2");

            // then
            assertEquals(2, obj.items.size());
            assertEquals("item1", obj.items.get(0));
            assertEquals("item2", obj.items.get(1));
        }

        @Test
        @DisplayName("null 리스트에 요소를 추가하면 새 리스트가 생성된다")
        void addElementToNullList() {
            // given
            TestListClass obj = new TestListClass();
            obj.items = null;

            // when
            MessageUtils.addToListField(obj, "items", "item1");

            // then
            assertNotNull(obj.items);
            assertEquals(1, obj.items.size());
        }
    }

    // 테스트용 내부 클래스
    static class TestClass {
        String name;
        int value;
    }

    static class ParentTestClass {
        String parentField;
    }

    static class ChildTestClass extends ParentTestClass {
        String name;
        int value;
    }

    static class TestListClass {
        List<String> items;
    }

    static class TestMessageObject implements IMessageObject {
        String field;
    }
}
