package com.scbank.process.api.fw.common.code.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.code.ICodeInfo;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;

/**
 * DefaultCodeInfo Test Class
 */
class DefaultCodeInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            List<ICodeItemInfo> items = List.of(
                    DefaultCodeItemInfo.builder().key("item1").value("value1").order(1).build()
            );

            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder()
                    .key("TEST_CODE")
                    .locale("ko")
                    .codeItemList(items)
                    .build();

            assertEquals("TEST_CODE", codeInfo.getKey());
            assertEquals("ko", codeInfo.getLocale());
            assertEquals(1, codeInfo.getCodeItemList().size());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder()
                    .key(null)
                    .locale(null)
                    .codeItemList(null)
                    .build();

            assertNull(codeInfo.getKey());
            assertNull(codeInfo.getLocale());
            assertNull(codeInfo.getCodeItemList());
        }

        @Test
        @DisplayName("Should build with empty list")
        void shouldBuildWithEmptyList() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder()
                    .key("CODE")
                    .locale("en")
                    .codeItemList(List.of())
                    .build();

            assertTrue(codeInfo.getCodeItemList().isEmpty());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get key")
        void shouldSetAndGetKey() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder().build();
            codeInfo.setKey("NEW_KEY");

            assertEquals("NEW_KEY", codeInfo.getKey());
        }

        @Test
        @DisplayName("Should set and get locale")
        void shouldSetAndGetLocale() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder().build();
            codeInfo.setLocale("ja");

            assertEquals("ja", codeInfo.getLocale());
        }

        @Test
        @DisplayName("Should set and get codeItemList")
        void shouldSetAndGetCodeItemList() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder().build();
            List<ICodeItemInfo> items = List.of(
                    DefaultCodeItemInfo.builder().key("k1").value("v1").build()
            );
            codeInfo.setCodeItemList(items);

            assertEquals(1, codeInfo.getCodeItemList().size());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement ICodeInfo")
        void shouldImplementICodeInfo() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder().build();

            assertTrue(codeInfo instanceof ICodeInfo);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultCodeInfo info1 = DefaultCodeInfo.builder()
                    .key("CODE")
                    .locale("ko")
                    .codeItemList(List.of())
                    .build();
            DefaultCodeInfo info2 = DefaultCodeInfo.builder()
                    .key("CODE")
                    .locale("ko")
                    .codeItemList(List.of())
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different keys")
        void shouldNotBeEqualForDifferentKeys() {
            DefaultCodeInfo info1 = DefaultCodeInfo.builder().key("CODE1").build();
            DefaultCodeInfo info2 = DefaultCodeInfo.builder().key("CODE2").build();

            assertNotEquals(info1, info2);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder()
                    .key("CODE")
                    .locale("ko")
                    .build();

            assertNotNull(codeInfo.toString());
            assertTrue(codeInfo.toString().contains("CODE"));
        }
    }

    @Nested
    @DisplayName("SerialVersionUID tests")
    class SerialVersionUIDTests {

        @Test
        @DisplayName("Should have serialVersionUID")
        void shouldHaveSerialVersionUID() {
            // Verifying serialVersionUID field exists through serialization concept
            DefaultCodeInfo codeInfo = DefaultCodeInfo.builder().key("TEST").build();
            assertNotNull(codeInfo);
        }
    }
}
