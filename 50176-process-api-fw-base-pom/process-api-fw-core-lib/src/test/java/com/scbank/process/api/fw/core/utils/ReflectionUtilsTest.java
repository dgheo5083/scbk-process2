package com.scbank.process.api.fw.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReflectionUtils 테스트")
class ReflectionUtilsTest {

    @Nested
    @DisplayName("getFieldValue 메서드 테스트")
    class GetFieldValueTests {

        @Test
        @DisplayName("getFieldValue - 정상 케이스")
        void getFieldValue_success() {
            TestClass obj = new TestClass();
            obj.name = "TestName";

            String result = ReflectionUtils.getFieldValue(obj, "name");

            assertEquals("TestName", result);
        }

        @Test
        @DisplayName("getFieldValue - private 필드")
        void getFieldValue_privateField() {
            TestClass obj = new TestClass();
            obj.setPrivateValue(100);

            Integer result = ReflectionUtils.getFieldValue(obj, "privateValue");

            assertEquals(100, result);
        }

        @Test
        @DisplayName("getFieldValue - 상속된 필드")
        void getFieldValue_inheritedField() {
            ChildClass obj = new ChildClass();
            obj.name = "ParentName";

            String result = ReflectionUtils.getFieldValue(obj, "name");

            assertEquals("ParentName", result);
        }

        @Test
        @DisplayName("getFieldValue - null 대상")
        void getFieldValue_nullTarget() {
            assertNull(ReflectionUtils.getFieldValue(null, "name"));
        }

        @Test
        @DisplayName("getFieldValue - null 필드명")
        void getFieldValue_nullFieldName() {
            TestClass obj = new TestClass();
            assertNull(ReflectionUtils.getFieldValue(obj, null));
        }

        @Test
        @DisplayName("getFieldValue - 존재하지 않는 필드")
        void getFieldValue_nonExistentField() {
            TestClass obj = new TestClass();
            assertNull(ReflectionUtils.getFieldValue(obj, "nonExistent"));
        }
    }

    @Nested
    @DisplayName("setFieldValue 메서드 테스트")
    class SetFieldValueTests {

        @Test
        @DisplayName("setFieldValue - 정상 케이스")
        void setFieldValue_success() {
            TestClass obj = new TestClass();

            ReflectionUtils.setFieldValue(obj, "name", "NewName");

            assertEquals("NewName", obj.name);
        }

        @Test
        @DisplayName("setFieldValue - private 필드")
        void setFieldValue_privateField() {
            TestClass obj = new TestClass();

            ReflectionUtils.setFieldValue(obj, "privateValue", 200);

            assertEquals(200, obj.getPrivateValue());
        }

        @Test
        @DisplayName("setFieldValue - null 대상")
        void setFieldValue_nullTarget() {
            // 예외 없이 그냥 반환
            assertDoesNotThrow(() -> ReflectionUtils.setFieldValue(null, "name", "value"));
        }

        @Test
        @DisplayName("setFieldValue - null 필드명")
        void setFieldValue_nullFieldName() {
            TestClass obj = new TestClass();
            assertDoesNotThrow(() -> ReflectionUtils.setFieldValue(obj, null, "value"));
        }

        @Test
        @DisplayName("setFieldValue - 존재하지 않는 필드")
        void setFieldValue_nonExistentField() {
            TestClass obj = new TestClass();
            assertDoesNotThrow(() -> ReflectionUtils.setFieldValue(obj, "nonExistent", "value"));
        }

        @Test
        @DisplayName("setFieldValue - 잘못된 타입")
        void setFieldValue_wrongType() {
            TestClass obj = new TestClass();
            // 예외가 내부적으로 처리됨
            assertDoesNotThrow(() -> ReflectionUtils.setFieldValue(obj, "privateValue", "not an integer"));
        }
    }

    @Nested
    @DisplayName("getListField 메서드 테스트")
    class GetListFieldTests {

        @Test
        @DisplayName("getListField - 정상 케이스")
        void getListField_success() {
            TestClass obj = new TestClass();
            obj.items = Arrays.asList("A", "B", "C");

            List<Object> result = ReflectionUtils.getListField(obj, "items");

            assertEquals(3, result.size());
            assertTrue(result.contains("A"));
        }

        @Test
        @DisplayName("getListField - null 리스트")
        void getListField_nullList() {
            TestClass obj = new TestClass();
            obj.items = null;

            List<Object> result = ReflectionUtils.getListField(obj, "items");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("getListField - 리스트가 아닌 필드")
        void getListField_nonListField() {
            TestClass obj = new TestClass();
            obj.name = "test";

            List<Object> result = ReflectionUtils.getListField(obj, "name");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("mergeListField 메서드 테스트")
    class MergeListFieldTests {

        @Test
        @DisplayName("mergeListField - 정상 케이스")
        void mergeListField_success() {
            TestClass obj = new TestClass();
            obj.items = new ArrayList<>(Arrays.asList("A", "B"));

            ReflectionUtils.mergeListField(obj, "items", Arrays.asList("C", "D"));

            List<Object> result = ReflectionUtils.getListField(obj, "items");
            assertEquals(4, result.size());
        }

        @Test
        @DisplayName("mergeListField - null 추가 리스트")
        void mergeListField_nullToAdd() {
            TestClass obj = new TestClass();
            obj.items = new ArrayList<>(Arrays.asList("A", "B"));

            ReflectionUtils.mergeListField(obj, "items", null);

            List<Object> result = ReflectionUtils.getListField(obj, "items");
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("mergeListField - 빈 추가 리스트")
        void mergeListField_emptyToAdd() {
            TestClass obj = new TestClass();
            obj.items = new ArrayList<>(Arrays.asList("A", "B"));

            ReflectionUtils.mergeListField(obj, "items", Arrays.asList());

            List<Object> result = ReflectionUtils.getListField(obj, "items");
            assertEquals(2, result.size());
        }
    }

    @Nested
    @DisplayName("isSimpleType 메서드 테스트")
    class IsSimpleTypeTests {

        @Test
        @DisplayName("isSimpleType - primitive 타입")
        void isSimpleType_primitive() {
            assertTrue(ReflectionUtils.isSimpleType(int.class));
            assertTrue(ReflectionUtils.isSimpleType(boolean.class));
            assertTrue(ReflectionUtils.isSimpleType(double.class));
            assertTrue(ReflectionUtils.isSimpleType(long.class));
        }

        @Test
        @DisplayName("isSimpleType - String 타입")
        void isSimpleType_string() {
            assertTrue(ReflectionUtils.isSimpleType(String.class));
        }

        @Test
        @DisplayName("isSimpleType - Number 하위 타입")
        void isSimpleType_number() {
            assertTrue(ReflectionUtils.isSimpleType(Integer.class));
            assertTrue(ReflectionUtils.isSimpleType(Long.class));
            assertTrue(ReflectionUtils.isSimpleType(Double.class));
        }

        @Test
        @DisplayName("isSimpleType - Boolean 타입")
        void isSimpleType_boolean() {
            assertTrue(ReflectionUtils.isSimpleType(Boolean.class));
        }

        @Test
        @DisplayName("isSimpleType - 복합 타입")
        void isSimpleType_complexType() {
            assertFalse(ReflectionUtils.isSimpleType(TestClass.class));
            assertFalse(ReflectionUtils.isSimpleType(List.class));
        }
    }

    @Nested
    @DisplayName("isListType 메서드 테스트")
    class IsListTypeTests {

        @Test
        @DisplayName("isListType - List 필드")
        void isListType_listField() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("items");
            assertTrue(ReflectionUtils.isListType(field));
        }

        @Test
        @DisplayName("isListType - ArrayList 필드")
        void isListType_arrayListField() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("arrayListItems");
            assertTrue(ReflectionUtils.isListType(field));
        }

        @Test
        @DisplayName("isListType - 리스트가 아닌 필드")
        void isListType_nonListField() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("name");
            assertFalse(ReflectionUtils.isListType(field));
        }
    }

    @Nested
    @DisplayName("getListItemType 메서드 테스트")
    class GetListItemTypeTests {

        @Test
        @DisplayName("getListItemType - 제네릭 타입 지정됨")
        void getListItemType_withGenericType() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("stringList");
            Class<?> result = ReflectionUtils.getListItemType(field);

            assertEquals(String.class, result);
        }

        @Test
        @DisplayName("getListItemType - raw 타입")
        void getListItemType_rawType() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("rawList");
            Class<?> result = ReflectionUtils.getListItemType(field);

            assertEquals(Object.class, result);
        }

        @Test
        @DisplayName("getListItemType - Integer 타입")
        void getListItemType_integerType() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("integerList");
            Class<?> result = ReflectionUtils.getListItemType(field);

            assertEquals(Integer.class, result);
        }

        @Test
        @DisplayName("getListItemType - wildcard 타입")
        void getListItemType_wildcardType() throws NoSuchFieldException {
            Field field = TestClass.class.getDeclaredField("wildcardList");
            Class<?> result = ReflectionUtils.getListItemType(field);

            // wildcard는 Object.class로 반환
            assertEquals(Object.class, result);
        }
    }

    // 테스트용 클래스
    static class TestClass {
        public String name;
        private Integer privateValue;
        public List<String> items;
        public ArrayList<String> arrayListItems;
        public List<String> stringList;
        public List<Integer> integerList;
        @SuppressWarnings("rawtypes")
        public List rawList;
        public List<?> wildcardList;

        public void setPrivateValue(Integer value) {
            this.privateValue = value;
        }

        public Integer getPrivateValue() {
            return this.privateValue;
        }
    }

    static class ChildClass extends TestClass {
        public String childField;
    }
}
