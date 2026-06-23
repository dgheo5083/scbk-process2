package com.scbank.process.api.fw.common.code.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.code.ICodeItemInfo;

/**
 * DefaultCodeItemInfo Test Class
 */
class DefaultCodeItemInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder()
                    .key("ITEM_KEY")
                    .value("Item Value")
                    .order(10)
                    .build();

            assertEquals("ITEM_KEY", itemInfo.getKey());
            assertEquals("Item Value", itemInfo.getValue());
            assertEquals(10, itemInfo.getOrder());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder()
                    .key(null)
                    .value(null)
                    .build();

            assertNull(itemInfo.getKey());
            assertNull(itemInfo.getValue());
        }

        @Test
        @DisplayName("Should build with default order")
        void shouldBuildWithDefaultOrder() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder()
                    .key("KEY")
                    .value("VALUE")
                    .build();

            assertEquals(0, itemInfo.getOrder());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get key")
        void shouldSetAndGetKey() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder().build();
            itemInfo.setKey("NEW_KEY");

            assertEquals("NEW_KEY", itemInfo.getKey());
        }

        @Test
        @DisplayName("Should set and get value")
        void shouldSetAndGetValue() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder().build();
            itemInfo.setValue("New Value");

            assertEquals("New Value", itemInfo.getValue());
        }

        @Test
        @DisplayName("Should set and get order")
        void shouldSetAndGetOrder() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder().build();
            itemInfo.setOrder(5);

            assertEquals(5, itemInfo.getOrder());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement ICodeItemInfo")
        void shouldImplementICodeItemInfo() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder().build();

            assertTrue(itemInfo instanceof ICodeItemInfo);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultCodeItemInfo info1 = DefaultCodeItemInfo.builder()
                    .key("KEY")
                    .value("VALUE")
                    .order(1)
                    .build();
            DefaultCodeItemInfo info2 = DefaultCodeItemInfo.builder()
                    .key("KEY")
                    .value("VALUE")
                    .order(1)
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different keys")
        void shouldNotBeEqualForDifferentKeys() {
            DefaultCodeItemInfo info1 = DefaultCodeItemInfo.builder().key("KEY1").build();
            DefaultCodeItemInfo info2 = DefaultCodeItemInfo.builder().key("KEY2").build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different orders")
        void shouldNotBeEqualForDifferentOrders() {
            DefaultCodeItemInfo info1 = DefaultCodeItemInfo.builder().key("KEY").order(1).build();
            DefaultCodeItemInfo info2 = DefaultCodeItemInfo.builder().key("KEY").order(2).build();

            assertNotEquals(info1, info2);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder()
                    .key("KEY")
                    .value("VALUE")
                    .order(1)
                    .build();

            assertNotNull(itemInfo.toString());
            assertTrue(itemInfo.toString().contains("KEY"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder()
                    .key("")
                    .value("")
                    .build();

            assertEquals("", itemInfo.getKey());
            assertEquals("", itemInfo.getValue());
        }

        @Test
        @DisplayName("Should handle negative order")
        void shouldHandleNegativeOrder() {
            DefaultCodeItemInfo itemInfo = DefaultCodeItemInfo.builder()
                    .order(-1)
                    .build();

            assertEquals(-1, itemInfo.getOrder());
        }
    }
}
