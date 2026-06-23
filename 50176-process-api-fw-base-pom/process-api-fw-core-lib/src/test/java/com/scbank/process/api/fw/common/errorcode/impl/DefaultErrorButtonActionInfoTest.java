package com.scbank.process.api.fw.common.errorcode.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.errorcode.IErrorButtonActionInfo;

/**
 * DefaultErrorButtonActionInfo Test Class
 */
class DefaultErrorButtonActionInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Go to Home")
                    .target("/home")
                    .build();

            assertEquals("REDIRECT", buttonActionInfo.getType());
            assertEquals("Go to Home", buttonActionInfo.getLabel());
            assertEquals("/home", buttonActionInfo.getTarget());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .type(null)
                    .label(null)
                    .target(null)
                    .build();

            assertNull(buttonActionInfo.getType());
            assertNull(buttonActionInfo.getLabel());
            assertNull(buttonActionInfo.getTarget());
        }

        @Test
        @DisplayName("Should build with only type")
        void shouldBuildWithOnlyType() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .type("CLOSE")
                    .build();

            assertEquals("CLOSE", buttonActionInfo.getType());
            assertNull(buttonActionInfo.getLabel());
            assertNull(buttonActionInfo.getTarget());
        }

        @Test
        @DisplayName("Should build with different action types")
        void shouldBuildWithDifferentActionTypes() {
            DefaultErrorButtonActionInfo redirect = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .target("/error")
                    .build();
            DefaultErrorButtonActionInfo close = DefaultErrorButtonActionInfo.builder()
                    .type("CLOSE")
                    .build();
            DefaultErrorButtonActionInfo callback = DefaultErrorButtonActionInfo.builder()
                    .type("CALLBACK")
                    .target("handleError")
                    .build();

            assertEquals("REDIRECT", redirect.getType());
            assertEquals("CLOSE", close.getType());
            assertEquals("CALLBACK", callback.getType());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get type")
        void shouldSetAndGetType() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder().build();
            buttonActionInfo.setType("NEW_TYPE");

            assertEquals("NEW_TYPE", buttonActionInfo.getType());
        }

        @Test
        @DisplayName("Should set and get label")
        void shouldSetAndGetLabel() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder().build();
            buttonActionInfo.setLabel("New Label");

            assertEquals("New Label", buttonActionInfo.getLabel());
        }

        @Test
        @DisplayName("Should set and get target")
        void shouldSetAndGetTarget() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder().build();
            buttonActionInfo.setTarget("/new/target");

            assertEquals("/new/target", buttonActionInfo.getTarget());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IErrorButtonActionInfo")
        void shouldImplementIErrorButtonActionInfo() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder().build();

            assertTrue(buttonActionInfo instanceof IErrorButtonActionInfo);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultErrorButtonActionInfo info1 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Home")
                    .target("/home")
                    .build();
            DefaultErrorButtonActionInfo info2 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Home")
                    .target("/home")
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different types")
        void shouldNotBeEqualForDifferentTypes() {
            DefaultErrorButtonActionInfo info1 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .build();
            DefaultErrorButtonActionInfo info2 = DefaultErrorButtonActionInfo.builder()
                    .type("CLOSE")
                    .build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different labels")
        void shouldNotBeEqualForDifferentLabels() {
            DefaultErrorButtonActionInfo info1 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Label1")
                    .build();
            DefaultErrorButtonActionInfo info2 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Label2")
                    .build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different targets")
        void shouldNotBeEqualForDifferentTargets() {
            DefaultErrorButtonActionInfo info1 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .target("/target1")
                    .build();
            DefaultErrorButtonActionInfo info2 = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .target("/target2")
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
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Test")
                    .target("/test")
                    .build();

            assertNotNull(buttonActionInfo.toString());
            assertTrue(buttonActionInfo.toString().contains("REDIRECT"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .type("")
                    .label("")
                    .target("")
                    .build();

            assertEquals("", buttonActionInfo.getType());
            assertEquals("", buttonActionInfo.getLabel());
            assertEquals("", buttonActionInfo.getTarget());
        }

        @Test
        @DisplayName("Should handle URL-like targets")
        void shouldHandleUrlLikeTargets() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .target("https://example.com/error?code=500")
                    .build();

            assertEquals("https://example.com/error?code=500", buttonActionInfo.getTarget());
        }

        @Test
        @DisplayName("Should handle special characters in label")
        void shouldHandleSpecialCharactersInLabel() {
            DefaultErrorButtonActionInfo buttonActionInfo = DefaultErrorButtonActionInfo.builder()
                    .label("확인 & 닫기")
                    .build();

            assertEquals("확인 & 닫기", buttonActionInfo.getLabel());
        }
    }
}
