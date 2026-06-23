package com.scbank.process.api.fw.common.holiday.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.holiday.IHolidayInfo;

/**
 * DefaultHolidayInfo Test Class
 */
class DefaultHolidayInfoTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("New Year's Day")
                    .build();

            assertEquals("20250101", holidayInfo.getDate());
            assertEquals("New Year's Day", holidayInfo.getDescription());
        }

        @Test
        @DisplayName("Should build with null values")
        void shouldBuildWithNullValues() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date(null)
                    .description(null)
                    .build();

            assertNull(holidayInfo.getDate());
            assertNull(holidayInfo.getDescription());
        }

        @Test
        @DisplayName("Should build with only date")
        void shouldBuildWithOnlyDate() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("20250301")
                    .build();

            assertEquals("20250301", holidayInfo.getDate());
            assertNull(holidayInfo.getDescription());
        }

        @Test
        @DisplayName("Should build with Korean description")
        void shouldBuildWithKoreanDescription() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("신정")
                    .build();

            assertEquals("신정", holidayInfo.getDescription());
        }
    }

    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get date")
        void shouldSetAndGetDate() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder().build();
            holidayInfo.setDate("20251225");

            assertEquals("20251225", holidayInfo.getDate());
        }

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder().build();
            holidayInfo.setDescription("Christmas");

            assertEquals("Christmas", holidayInfo.getDescription());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IHolidayInfo")
        void shouldImplementIHolidayInfo() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder().build();

            assertTrue(holidayInfo instanceof IHolidayInfo);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for same values")
        void shouldBeEqualForSameValues() {
            DefaultHolidayInfo info1 = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("New Year")
                    .build();
            DefaultHolidayInfo info2 = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("New Year")
                    .build();

            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different dates")
        void shouldNotBeEqualForDifferentDates() {
            DefaultHolidayInfo info1 = DefaultHolidayInfo.builder().date("20250101").build();
            DefaultHolidayInfo info2 = DefaultHolidayInfo.builder().date("20250102").build();

            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("Should not be equal for different descriptions")
        void shouldNotBeEqualForDifferentDescriptions() {
            DefaultHolidayInfo info1 = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("Holiday 1")
                    .build();
            DefaultHolidayInfo info2 = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("Holiday 2")
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
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("New Year")
                    .build();

            assertNotNull(holidayInfo.toString());
            assertTrue(holidayInfo.toString().contains("20250101"));
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("")
                    .description("")
                    .build();

            assertEquals("", holidayInfo.getDate());
            assertEquals("", holidayInfo.getDescription());
        }

        @Test
        @DisplayName("Should handle various date formats")
        void shouldHandleVariousDateFormats() {
            // yyyyMMdd format
            DefaultHolidayInfo info1 = DefaultHolidayInfo.builder().date("20250101").build();
            assertEquals("20250101", info1.getDate());

            // Different date string
            DefaultHolidayInfo info2 = DefaultHolidayInfo.builder().date("2025-01-01").build();
            assertEquals("2025-01-01", info2.getDate());
        }

        @Test
        @DisplayName("Should handle long description")
        void shouldHandleLongDescription() {
            String longDescription = "A".repeat(1000);
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description(longDescription)
                    .build();

            assertEquals(1000, holidayInfo.getDescription().length());
        }

        @Test
        @DisplayName("Should handle special characters in description")
        void shouldHandleSpecialCharactersInDescription() {
            DefaultHolidayInfo holidayInfo = DefaultHolidayInfo.builder()
                    .date("20250101")
                    .description("Holiday & Special <Day> \"Event\"")
                    .build();

            assertTrue(holidayInfo.getDescription().contains("&"));
            assertTrue(holidayInfo.getDescription().contains("<Day>"));
        }
    }
}
