package com.scbank.process.api.fw.common.servicetime.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;

/**
 * DefaultServiceTimeInfo Test Class
 */
class DefaultServiceTimeInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("MENU001")
                    .chkYn("Y")
                    .description("메인 메뉴")
                    .nextPage("/error")
                    .nextPageParameter("errorCode=500")
                    .startTime("0900")
                    .endTime("1800")
                    .build();

            assertEquals(ServiceTimeGroup.MENU, serviceTimeInfo.getGroup());
            assertEquals("MENU001", serviceTimeInfo.getCode());
            assertEquals("Y", serviceTimeInfo.getChkYn());
            assertEquals("메인 메뉴", serviceTimeInfo.getDescription());
            assertEquals("/error", serviceTimeInfo.getNextPage());
            assertEquals("errorCode=500", serviceTimeInfo.getNextPageParameter());
            assertEquals("0900", serviceTimeInfo.getStartTime());
            assertEquals("1800", serviceTimeInfo.getEndTime());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .group(null)
                    .code(null)
                    .chkYn(null)
                    .description(null)
                    .nextPage(null)
                    .nextPageParameter(null)
                    .startTime(null)
                    .endTime(null)
                    .build();

            assertNull(serviceTimeInfo.getGroup());
            assertNull(serviceTimeInfo.getCode());
            assertNull(serviceTimeInfo.getChkYn());
            assertNull(serviceTimeInfo.getDescription());
            assertNull(serviceTimeInfo.getNextPage());
            assertNull(serviceTimeInfo.getNextPageParameter());
            assertNull(serviceTimeInfo.getStartTime());
            assertNull(serviceTimeInfo.getEndTime());
        }

        @Test
        @DisplayName("Should build with MESSAGE group")
        void shouldBuildWithMessageGroup() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MESSAGE)
                    .code("MSG001")
                    .build();

            assertEquals(ServiceTimeGroup.MESSAGE, serviceTimeInfo.getGroup());
            assertEquals("MSG001", serviceTimeInfo.getCode());
        }

        @Test
        @DisplayName("Should build with UNKNOWN group")
        void shouldBuildWithUnknownGroup() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.UNKNOWN)
                    .code("UNK001")
                    .build();

            assertEquals(ServiceTimeGroup.UNKNOWN, serviceTimeInfo.getGroup());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get group")
        void shouldSetAndGetGroup() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setGroup(ServiceTimeGroup.MENU);

            assertEquals(ServiceTimeGroup.MENU, serviceTimeInfo.getGroup());
        }

        @Test
        @DisplayName("Should set and get code")
        void shouldSetAndGetCode() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setCode("NEW_CODE");

            assertEquals("NEW_CODE", serviceTimeInfo.getCode());
        }

        @Test
        @DisplayName("Should set and get chkYn")
        void shouldSetAndGetChkYn() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setChkYn("N");

            assertEquals("N", serviceTimeInfo.getChkYn());
        }

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setDescription("New description");

            assertEquals("New description", serviceTimeInfo.getDescription());
        }

        @Test
        @DisplayName("Should set and get nextPage")
        void shouldSetAndGetNextPage() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setNextPage("/new/page");

            assertEquals("/new/page", serviceTimeInfo.getNextPage());
        }

        @Test
        @DisplayName("Should set and get nextPageParameter")
        void shouldSetAndGetNextPageParameter() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setNextPageParameter("param=value");

            assertEquals("param=value", serviceTimeInfo.getNextPageParameter());
        }

        @Test
        @DisplayName("Should set and get startTime")
        void shouldSetAndGetStartTime() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setStartTime("0800");

            assertEquals("0800", serviceTimeInfo.getStartTime());
        }

        @Test
        @DisplayName("Should set and get endTime")
        void shouldSetAndGetEndTime() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();
            serviceTimeInfo.setEndTime("2300");

            assertEquals("2300", serviceTimeInfo.getEndTime());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IServiceTimeInfo")
        void shouldImplementIServiceTimeInfo() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder().build();

            assertTrue(serviceTimeInfo instanceof IServiceTimeInfo);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultServiceTimeInfo info1 = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("CODE001")
                    .startTime("0900")
                    .endTime("1800")
                    .build();
            DefaultServiceTimeInfo info2 = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("CODE001")
                    .startTime("0900")
                    .endTime("1800")
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different groups")
        void shouldNotBeEqualForDifferentGroups() {
            DefaultServiceTimeInfo info1 = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("CODE001")
                    .build();
            DefaultServiceTimeInfo info2 = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MESSAGE)
                    .code("CODE001")
                    .build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different codes")
        void shouldNotBeEqualForDifferentCodes() {
            DefaultServiceTimeInfo info1 = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("CODE001")
                    .build();
            DefaultServiceTimeInfo info2 = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("CODE002")
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
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU)
                    .code("CODE001")
                    .build();

            assertNotNull(serviceTimeInfo.toString());
            assertTrue(serviceTimeInfo.toString().contains("CODE001"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .code("")
                    .chkYn("")
                    .description("")
                    .nextPage("")
                    .nextPageParameter("")
                    .startTime("")
                    .endTime("")
                    .build();

            assertEquals("", serviceTimeInfo.getCode());
            assertEquals("", serviceTimeInfo.getChkYn());
            assertEquals("", serviceTimeInfo.getDescription());
        }

        @Test
        @DisplayName("Should handle various time formats")
        void shouldHandleVariousTimeFormats() {
            DefaultServiceTimeInfo info1 = DefaultServiceTimeInfo.builder()
                    .startTime("0900")
                    .endTime("1800")
                    .build();

            DefaultServiceTimeInfo info2 = DefaultServiceTimeInfo.builder()
                    .startTime("09:00")
                    .endTime("18:00")
                    .build();

            assertEquals("0900", info1.getStartTime());
            assertEquals("09:00", info2.getStartTime());
        }

        @Test
        @DisplayName("Should handle 24-hour time format")
        void shouldHandle24HourTimeFormat() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .startTime("0000")
                    .endTime("2359")
                    .build();

            assertEquals("0000", serviceTimeInfo.getStartTime());
            assertEquals("2359", serviceTimeInfo.getEndTime());
        }

        @Test
        @DisplayName("Should handle special characters in description")
        void shouldHandleSpecialCharactersInDescription() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .description("서비스 <메뉴> & '특수' \"문자\"")
                    .build();

            assertTrue(serviceTimeInfo.getDescription().contains("<메뉴>"));
            assertTrue(serviceTimeInfo.getDescription().contains("서비스"));
        }

        @Test
        @DisplayName("Should handle URL with parameters in nextPage")
        void shouldHandleUrlWithParametersInNextPage() {
            DefaultServiceTimeInfo serviceTimeInfo = DefaultServiceTimeInfo.builder()
                    .nextPage("/error/page")
                    .nextPageParameter("code=500&message=error")
                    .build();

            assertEquals("/error/page", serviceTimeInfo.getNextPage());
            assertTrue(serviceTimeInfo.getNextPageParameter().contains("code=500"));
        }
    }
}
