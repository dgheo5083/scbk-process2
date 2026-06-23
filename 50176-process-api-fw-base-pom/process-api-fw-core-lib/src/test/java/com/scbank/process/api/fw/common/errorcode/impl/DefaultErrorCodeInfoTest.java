package com.scbank.process.api.fw.common.errorcode.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.errorcode.IErrorButtonActionInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeInfo;
import com.scbank.process.api.fw.common.errorcode.IErrorGuideMessageInfo;

/**
 * DefaultErrorCodeInfo Test Class
 */
class DefaultErrorCodeInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            IErrorButtonActionInfo buttonAction = DefaultErrorButtonActionInfo.builder()
                    .type("REDIRECT")
                    .label("Home")
                    .target("/home")
                    .build();

            List<IErrorGuideMessageInfo> guideMessages = List.of(
                    DefaultErrorGuideMessageInfo.builder().message("Guide 1").order(1).build(),
                    DefaultErrorGuideMessageInfo.builder().message("Guide 2").order(2).build()
            );

            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR001")
                    .langCode("ko")
                    .message("에러가 발생했습니다.")
                    .errorGuideMessageInfos(guideMessages)
                    .displayType("POPUP")
                    .errorButtonActionInfo(buttonAction)
                    .build();

            assertEquals("ERR001", errorCodeInfo.getCode());
            assertEquals("ko", errorCodeInfo.getLangCode());
            assertEquals("에러가 발생했습니다.", errorCodeInfo.getMessage());
            assertEquals(2, errorCodeInfo.getErrorGuideMessageInfos().size());
            assertEquals("POPUP", errorCodeInfo.getDisplayType());
            assertNotNull(errorCodeInfo.getErrorButtonActionInfo());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code(null)
                    .langCode(null)
                    .message(null)
                    .errorGuideMessageInfos(null)
                    .displayType(null)
                    .errorButtonActionInfo(null)
                    .build();

            assertNull(errorCodeInfo.getCode());
            assertNull(errorCodeInfo.getLangCode());
            assertNull(errorCodeInfo.getMessage());
            assertNull(errorCodeInfo.getErrorGuideMessageInfos());
            assertNull(errorCodeInfo.getDisplayType());
            assertNull(errorCodeInfo.getErrorButtonActionInfo());
        }

        @Test
        @DisplayName("Should build with only required fields")
        void shouldBuildWithOnlyRequiredFields() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR002")
                    .message("Error message")
                    .build();

            assertEquals("ERR002", errorCodeInfo.getCode());
            assertEquals("Error message", errorCodeInfo.getMessage());
            assertNull(errorCodeInfo.getLangCode());
        }

        @Test
        @DisplayName("Should build with empty guide messages list")
        void shouldBuildWithEmptyGuideMessagesList() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR003")
                    .errorGuideMessageInfos(List.of())
                    .build();

            assertTrue(errorCodeInfo.getErrorGuideMessageInfos().isEmpty());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get code")
        void shouldSetAndGetCode() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();
            errorCodeInfo.setCode("NEW_CODE");

            assertEquals("NEW_CODE", errorCodeInfo.getCode());
        }

        @Test
        @DisplayName("Should set and get langCode")
        void shouldSetAndGetLangCode() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();
            errorCodeInfo.setLangCode("en");

            assertEquals("en", errorCodeInfo.getLangCode());
        }

        @Test
        @DisplayName("Should set and get message")
        void shouldSetAndGetMessage() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();
            errorCodeInfo.setMessage("New error message");

            assertEquals("New error message", errorCodeInfo.getMessage());
        }

        @Test
        @DisplayName("Should set and get errorGuideMessageInfos")
        void shouldSetAndGetErrorGuideMessageInfos() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();
            List<IErrorGuideMessageInfo> guides = List.of(
                    DefaultErrorGuideMessageInfo.builder().message("Guide").build()
            );
            errorCodeInfo.setErrorGuideMessageInfos(guides);

            assertEquals(1, errorCodeInfo.getErrorGuideMessageInfos().size());
        }

        @Test
        @DisplayName("Should set and get displayType")
        void shouldSetAndGetDisplayType() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();
            errorCodeInfo.setDisplayType("TOAST");

            assertEquals("TOAST", errorCodeInfo.getDisplayType());
        }

        @Test
        @DisplayName("Should set and get errorButtonActionInfo")
        void shouldSetAndGetErrorButtonActionInfo() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();
            IErrorButtonActionInfo buttonAction = DefaultErrorButtonActionInfo.builder()
                    .type("CLOSE")
                    .build();
            errorCodeInfo.setErrorButtonActionInfo(buttonAction);

            assertNotNull(errorCodeInfo.getErrorButtonActionInfo());
            assertEquals("CLOSE", errorCodeInfo.getErrorButtonActionInfo().getType());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IErrorCodeInfo")
        void shouldImplementIErrorCodeInfo() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder().build();

            assertTrue(errorCodeInfo instanceof IErrorCodeInfo);
        }

        @Test
        @DisplayName("Should return guide messages via implementation method")
        void shouldReturnGuideMessagesViaImplementationMethod() {
            List<IErrorGuideMessageInfo> guides = List.of(
                    DefaultErrorGuideMessageInfo.builder().message("Guide 1").order(1).build()
            );
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR")
                    .errorGuideMessageInfos(guides)
                    .build();

            List<IErrorGuideMessageInfo> result = errorCodeInfo.getErrorGuideMessageInfos();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should return empty list via interface default method")
        void shouldReturnEmptyListViaInterfaceDefaultMethod() {
            IErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR")
                    .build();

            // Interface default method returns empty list
            List<IErrorGuideMessageInfo> result = errorCodeInfo.getErrorGuideMessages();

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultErrorCodeInfo info1 = DefaultErrorCodeInfo.builder()
                    .code("ERR001")
                    .langCode("ko")
                    .message("Error")
                    .build();
            DefaultErrorCodeInfo info2 = DefaultErrorCodeInfo.builder()
                    .code("ERR001")
                    .langCode("ko")
                    .message("Error")
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different codes")
        void shouldNotBeEqualForDifferentCodes() {
            DefaultErrorCodeInfo info1 = DefaultErrorCodeInfo.builder().code("ERR001").build();
            DefaultErrorCodeInfo info2 = DefaultErrorCodeInfo.builder().code("ERR002").build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different langCodes")
        void shouldNotBeEqualForDifferentLangCodes() {
            DefaultErrorCodeInfo info1 = DefaultErrorCodeInfo.builder()
                    .code("ERR001")
                    .langCode("ko")
                    .build();
            DefaultErrorCodeInfo info2 = DefaultErrorCodeInfo.builder()
                    .code("ERR001")
                    .langCode("en")
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
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR001")
                    .message("Test error")
                    .build();

            assertNotNull(errorCodeInfo.toString());
            assertTrue(errorCodeInfo.toString().contains("ERR001"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("")
                    .langCode("")
                    .message("")
                    .displayType("")
                    .build();

            assertEquals("", errorCodeInfo.getCode());
            assertEquals("", errorCodeInfo.getLangCode());
            assertEquals("", errorCodeInfo.getMessage());
            assertEquals("", errorCodeInfo.getDisplayType());
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR<>&")
                    .message("Error with <special> & \"characters\"")
                    .build();

            assertEquals("ERR<>&", errorCodeInfo.getCode());
            assertTrue(errorCodeInfo.getMessage().contains("<special>"));
        }

        @Test
        @DisplayName("Should handle multilingual messages")
        void shouldHandleMultilingualMessages() {
            DefaultErrorCodeInfo errorCodeInfo = DefaultErrorCodeInfo.builder()
                    .code("ERR_ML")
                    .langCode("ko")
                    .message("한글 에러 메시지")
                    .build();

            assertEquals("ko", errorCodeInfo.getLangCode());
            assertEquals("한글 에러 메시지", errorCodeInfo.getMessage());
        }
    }
}
