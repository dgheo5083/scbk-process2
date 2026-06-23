package com.scbank.process.api.fw.common.property.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.property.IPropertyInfo;

/**
 * DefaultPropertyInfo Test Class
 */
class DefaultPropertyInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("app.name")
                    .propertyValue("TestApplication")
                    .build();

            assertEquals("app.name", propertyInfo.getPropertyName());
            assertEquals("TestApplication", propertyInfo.getPropertyValue());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName(null)
                    .propertyValue(null)
                    .build();

            assertNull(propertyInfo.getPropertyName());
            assertNull(propertyInfo.getPropertyValue());
        }

        @Test
        @DisplayName("Should build with only property name")
        void shouldBuildWithOnlyPropertyName() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("test.property")
                    .build();

            assertEquals("test.property", propertyInfo.getPropertyName());
            assertNull(propertyInfo.getPropertyValue());
        }

        @Test
        @DisplayName("Should build with only property value")
        void shouldBuildWithOnlyPropertyValue() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyValue("value123")
                    .build();

            assertNull(propertyInfo.getPropertyName());
            assertEquals("value123", propertyInfo.getPropertyValue());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get property name")
        void shouldSetAndGetPropertyName() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder().build();
            propertyInfo.setPropertyName("new.property.name");

            assertEquals("new.property.name", propertyInfo.getPropertyName());
        }

        @Test
        @DisplayName("Should set and get property value")
        void shouldSetAndGetPropertyValue() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder().build();
            propertyInfo.setPropertyValue("newValue");

            assertEquals("newValue", propertyInfo.getPropertyValue());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IPropertyInfo")
        void shouldImplementIPropertyInfo() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder().build();

            assertTrue(propertyInfo instanceof IPropertyInfo);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultPropertyInfo info1 = DefaultPropertyInfo.builder()
                    .propertyName("name")
                    .propertyValue("value")
                    .build();
            DefaultPropertyInfo info2 = DefaultPropertyInfo.builder()
                    .propertyName("name")
                    .propertyValue("value")
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different names")
        void shouldNotBeEqualForDifferentNames() {
            DefaultPropertyInfo info1 = DefaultPropertyInfo.builder()
                    .propertyName("name1")
                    .propertyValue("value")
                    .build();
            DefaultPropertyInfo info2 = DefaultPropertyInfo.builder()
                    .propertyName("name2")
                    .propertyValue("value")
                    .build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different values")
        void shouldNotBeEqualForDifferentValues() {
            DefaultPropertyInfo info1 = DefaultPropertyInfo.builder()
                    .propertyName("name")
                    .propertyValue("value1")
                    .build();
            DefaultPropertyInfo info2 = DefaultPropertyInfo.builder()
                    .propertyName("name")
                    .propertyValue("value2")
                    .build();

            assertNotEquals(info1, info2);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("test.name")
                    .propertyValue("testValue")
                    .build();

            assertNotNull(propertyInfo.toString());
            assertTrue(propertyInfo.toString().contains("test.name"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("")
                    .propertyValue("")
                    .build();

            assertEquals("", propertyInfo.getPropertyName());
            assertEquals("", propertyInfo.getPropertyValue());
        }

        @Test
        @DisplayName("Should handle dot-separated property names")
        void shouldHandleDotSeparatedPropertyNames() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("app.config.database.url")
                    .propertyValue("jdbc:mysql://localhost:3306/db")
                    .build();

            assertEquals("app.config.database.url", propertyInfo.getPropertyName());
        }

        @Test
        @DisplayName("Should handle special characters in value")
        void shouldHandleSpecialCharactersInValue() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("password")
                    .propertyValue("p@ssw0rd!#$%")
                    .build();

            assertEquals("p@ssw0rd!#$%", propertyInfo.getPropertyValue());
        }

        @Test
        @DisplayName("Should handle multiline value")
        void shouldHandleMultilineValue() {
            String multilineValue = "line1\nline2\nline3";
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("multiline.property")
                    .propertyValue(multilineValue)
                    .build();

            assertEquals(multilineValue, propertyInfo.getPropertyValue());
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void shouldHandleUnicodeCharacters() {
            DefaultPropertyInfo propertyInfo = DefaultPropertyInfo.builder()
                    .propertyName("greeting")
                    .propertyValue("안녕하세요, こんにちは, 你好")
                    .build();

            assertTrue(propertyInfo.getPropertyValue().contains("안녕하세요"));
        }
    }
}
