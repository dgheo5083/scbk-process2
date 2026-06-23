package com.scbank.process.api.fw.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateUtils 테스트")
class DateUtilsTest {

    @Test
    @DisplayName("상수 값 확인")
    void constants() {
        assertNotNull(DateUtils.YYYYMMDD);
        assertNotNull(DateUtils.HHMM);
        assertNotNull(DateUtils.HHMMSS);
        assertNotNull(DateUtils.YYYYMMDDHHMMSS);
        assertNotNull(DateUtils.YYYYMMDDHHMMSSSSS);
        assertEquals(2, DateUtils.BIG);
        assertEquals(1, DateUtils.EQUAL);
        assertEquals(0, DateUtils.SMALL);
    }

    @Nested
    @DisplayName("getCurrentDate 메서드 테스트")
    class GetCurrentDateTests {

        @Test
        @DisplayName("getCurrentDate - 기본 (yyyyMMdd)")
        void getCurrentDate() {
            String result = DateUtils.getCurrentDate();

            assertNotNull(result);
            assertEquals(8, result.length());
            assertTrue(result.matches("\\d{8}"));
        }

        @Test
        @DisplayName("getCurrentDate - 포맷 지정")
        void getCurrentDate_withFormat() {
            String result = DateUtils.getCurrentDate("yyyy-MM-dd");

            assertNotNull(result);
            assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
        }

        @Test
        @DisplayName("getCurrentDate - null 포맷")
        void getCurrentDate_nullFormat() {
            String result = DateUtils.getCurrentDate((String) null);

            assertEquals(DateUtils.getCurrentDate(), result);
        }

        @Test
        @DisplayName("getCurrentDate - 빈 포맷")
        void getCurrentDate_emptyFormat() {
            String result = DateUtils.getCurrentDate("");

            assertEquals(DateUtils.getCurrentDate(), result);
        }

        @Test
        @DisplayName("getCurrentDate - yyyyMMdd 포맷")
        void getCurrentDate_yyyyMMddFormat() {
            String result = DateUtils.getCurrentDate("yyyyMMdd");

            assertEquals(8, result.length());
        }

        @Test
        @DisplayName("getCurrentDate - DateTimeFormatter 사용")
        void getCurrentDate_withFormatter() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String result = DateUtils.getCurrentDate(formatter);

            assertNotNull(result);
            assertTrue(result.matches("\\d{4}/\\d{2}/\\d{2}"));
        }

        @Test
        @DisplayName("getCurrentDate - null DateTimeFormatter")
        void getCurrentDate_nullFormatter() {
            String result = DateUtils.getCurrentDate((DateTimeFormatter) null);

            assertEquals(DateUtils.getCurrentDate(), result);
        }
    }

    @Nested
    @DisplayName("getCurrentTime 메서드 테스트")
    class GetCurrentTimeTests {

        @Test
        @DisplayName("getCurrentTime - 기본 (yyyyMMddHHmmss)")
        void getCurrentTime() {
            String result = DateUtils.getCurrentTime();

            assertNotNull(result);
            assertEquals(14, result.length());
        }

        @Test
        @DisplayName("getCurrentTime - 포맷 지정")
        void getCurrentTime_withFormat() {
            String result = DateUtils.getCurrentTime("HH:mm:ss");

            assertNotNull(result);
            assertTrue(result.matches("\\d{2}:\\d{2}:\\d{2}"));
        }

        @Test
        @DisplayName("getCurrentTime - DateTimeFormatter 사용")
        void getCurrentTime_withFormatter() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
            String result = DateUtils.getCurrentTime(formatter);

            assertNotNull(result);
            assertEquals(6, result.length());
        }
    }

    @Nested
    @DisplayName("compare 메서드 테스트")
    class CompareTests {

        @Test
        @DisplayName("compare - date1 > date2")
        void compare_bigger() {
            int result = DateUtils.compare("20250115", "20250110");

            assertEquals(DateUtils.BIG, result);
        }

        @Test
        @DisplayName("compare - date1 = date2")
        void compare_equal() {
            int result = DateUtils.compare("20250115", "20250115");

            assertEquals(DateUtils.EQUAL, result);
        }

        @Test
        @DisplayName("compare - date1 < date2")
        void compare_smaller() {
            int result = DateUtils.compare("20250110", "20250115");

            assertEquals(DateUtils.SMALL, result);
        }

        @Test
        @DisplayName("compare - 잘못된 형식")
        void compare_invalidFormat() {
            int result = DateUtils.compare("invalid", "20250115");

            assertEquals(-1, result);
        }

        @Test
        @DisplayName("compare - 오늘과 비교")
        void compare_withToday() {
            String today = DateUtils.getCurrentDate();
            int result = DateUtils.compare(today);

            assertEquals(DateUtils.EQUAL, result);
        }
    }

    @Nested
    @DisplayName("parseToDateTime 메서드 테스트")
    class ParseToDateTimeTests {

        @Test
        @DisplayName("parseToDateTime - 8자리 (yyyyMMdd)")
        void parseToDateTime_8digits() {
            LocalDateTime result = DateUtils.parseToDateTime("20250115");

            assertNotNull(result);
            assertEquals(2025, result.getYear());
            assertEquals(1, result.getMonthValue());
            assertEquals(15, result.getDayOfMonth());
        }

        @Test
        @DisplayName("parseToDateTime - 14자리 (yyyyMMddHHmmss)")
        void parseToDateTime_14digits() {
            LocalDateTime result = DateUtils.parseToDateTime("20250115103045");

            assertNotNull(result);
            assertEquals(10, result.getHour());
            assertEquals(30, result.getMinute());
            assertEquals(45, result.getSecond());
        }

        @Test
        @DisplayName("parseToDateTime - 6자리 (HHmmss)")
        void parseToDateTime_6digits() {
            LocalDateTime result = DateUtils.parseToDateTime("103045");

            assertNotNull(result);
            assertEquals(LocalDate.now(), result.toLocalDate());
        }

        @Test
        @DisplayName("parseToDateTime - 잘못된 형식")
        void parseToDateTime_invalid() {
            assertNull(DateUtils.parseToDateTime("invalid"));
        }
    }

    @Nested
    @DisplayName("parseDate 메서드 테스트")
    class ParseDateTests {

        @Test
        @DisplayName("parseDate - 정상")
        void parseDate() {
            LocalDate result = DateUtils.parseDate("20250115");

            assertNotNull(result);
            assertEquals(2025, result.getYear());
            assertEquals(1, result.getMonthValue());
            assertEquals(15, result.getDayOfMonth());
        }

        @Test
        @DisplayName("parseDate - 잘못된 형식")
        void parseDate_invalid() {
            assertNull(DateUtils.parseDate("invalid"));
        }
    }

    @Nested
    @DisplayName("getDayBetween 메서드 테스트")
    class GetDayBetweenTests {

        @Test
        @DisplayName("getDayBetween - Date 객체")
        void getDayBetween_date() {
            Calendar cal1 = Calendar.getInstance();
            cal1.set(2025, Calendar.JANUARY, 10);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(2025, Calendar.JANUARY, 15);

            int result = DateUtils.getDayBetween(cal1.getTime(), cal2.getTime());

            assertEquals(5, result);
        }

        @Test
        @DisplayName("getDayBetween - 문자열")
        void getDayBetween_string() {
            long result = DateUtils.getDayBetween("20250110", "20250115");

            assertEquals(5, result);
        }

        @Test
        @DisplayName("getDayBetween - 잘못된 문자열")
        void getDayBetween_invalidString() {
            long result = DateUtils.getDayBetween("invalid", "20250115");

            assertEquals(0, result);
        }
    }

    @Nested
    @DisplayName("getEndDayOfMonth 메서드 테스트")
    class GetEndDayOfMonthTests {

        @Test
        @DisplayName("getEndDayOfMonth - 31일")
        void getEndDayOfMonth_31days() {
            assertEquals(31, DateUtils.getEndDayOfMonth("20250115"));
        }

        @Test
        @DisplayName("getEndDayOfMonth - 28일 (평년 2월)")
        void getEndDayOfMonth_28days() {
            assertEquals(28, DateUtils.getEndDayOfMonth("20250215"));
        }

        @Test
        @DisplayName("getEndDayOfMonth - 29일 (윤년 2월)")
        void getEndDayOfMonth_29days() {
            assertEquals(29, DateUtils.getEndDayOfMonth("20240215"));
        }

        @Test
        @DisplayName("getEndDayOfMonth - 잘못된 형식")
        void getEndDayOfMonth_invalid() {
            assertEquals(-1, DateUtils.getEndDayOfMonth("invalid"));
        }
    }

    @Nested
    @DisplayName("getYear/getMonth/getDay 메서드 테스트")
    class GetDatePartsTests {

        @Test
        @DisplayName("getYear")
        void getYear() {
            assertEquals(2025, DateUtils.getYear("20250115"));
        }

        @Test
        @DisplayName("getYear - 잘못된 형식")
        void getYear_invalid() {
            assertEquals(-1, DateUtils.getYear("invalid"));
        }

        @Test
        @DisplayName("getMonth")
        void getMonth() {
            assertEquals(1, DateUtils.getMonth("20250115"));
        }

        @Test
        @DisplayName("getMonth - 잘못된 형식")
        void getMonth_invalid() {
            assertEquals(-1, DateUtils.getMonth("invalid"));
        }

        @Test
        @DisplayName("getDay - 문자열")
        void getDay_string() {
            assertEquals(15, DateUtils.getDay("20250115"));
        }

        @Test
        @DisplayName("getDay - 잘못된 형식")
        void getDay_invalid() {
            assertEquals(-1, DateUtils.getDay("invalid"));
        }
    }

    @Nested
    @DisplayName("getWeekDay/getDay(Date) 메서드 테스트")
    class GetWeekDayTests {

        @Test
        @DisplayName("getWeekDay - Date 객체")
        void getWeekDay() {
            Calendar cal = Calendar.getInstance();
            cal.set(2025, Calendar.JANUARY, 15); // 수요일
            Date date = cal.getTime();

            int result = DateUtils.getWeekDay(date);

            assertTrue(result >= 1 && result <= 7);
        }

        @Test
        @DisplayName("getDay - Date 객체")
        void getDay_date() {
            Calendar cal = Calendar.getInstance();
            cal.set(2025, Calendar.JANUARY, 15);
            Date date = cal.getTime();

            String result = DateUtils.getDay(date);

            assertNotNull(result);
            assertTrue(result.matches("[1-7]"));
        }
    }

    @Nested
    @DisplayName("getZonedDateTime 메서드 테스트")
    class GetZonedDateTimeTests {

        @Test
        @DisplayName("getZonedDateTime - 문자열")
        void getZonedDateTime_string() {
            ZonedDateTime result = DateUtils.getZonedDateTime("20250115");

            assertNotNull(result);
            assertEquals(2025, result.getYear());
        }

        @Test
        @DisplayName("getZonedDateTime - 잘못된 문자열")
        void getZonedDateTime_invalidString() {
            assertNull(DateUtils.getZonedDateTime("invalid"));
        }

        @Test
        @DisplayName("getZonedDateTime - Date 객체")
        void getZonedDateTime_date() {
            Date date = new Date();
            ZonedDateTime result = DateUtils.getZonedDateTime(date);

            assertNotNull(result);
        }
    }

    @Test
    @DisplayName("isValidDate - 유효한 날짜")
    void isValidDate_valid() {
        assertTrue(DateUtils.isValidDate("20250115", "yyyyMMdd"));
    }

    @Test
    @DisplayName("isValidDate - 유효하지 않은 날짜")
    void isValidDate_invalid() {
        assertFalse(DateUtils.isValidDate("invalid", "yyyyMMdd"));
    }

    @Test
    @DisplayName("format - LocalDate를 문자열로")
    void format() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        String result = DateUtils.format(date, "yyyy-MM-dd");

        assertEquals("2025-01-15", result);
    }

    @Test
    @DisplayName("parse - 문자열을 LocalDate로")
    void parse() {
        LocalDate result = DateUtils.parse("2025-01-15", "yyyy-MM-dd");

        assertEquals(LocalDate.of(2025, 1, 15), result);
    }

    @Nested
    @DisplayName("isToday/isBeforeToday/isAfterToday 메서드 테스트")
    class TodayComparisonTests {

        @Test
        @DisplayName("isToday - 오늘")
        void isToday_true() {
            assertTrue(DateUtils.isToday(LocalDate.now()));
        }

        @Test
        @DisplayName("isToday - 오늘이 아님")
        void isToday_false() {
            assertFalse(DateUtils.isToday(LocalDate.now().minusDays(1)));
        }

        @Test
        @DisplayName("isBeforeToday")
        void isBeforeToday() {
            assertTrue(DateUtils.isBeforeToday(LocalDate.now().minusDays(1)));
            assertFalse(DateUtils.isBeforeToday(LocalDate.now()));
        }

        @Test
        @DisplayName("isAfterToday")
        void isAfterToday() {
            assertTrue(DateUtils.isAfterToday(LocalDate.now().plusDays(1)));
            assertFalse(DateUtils.isAfterToday(LocalDate.now()));
        }
    }

    @Nested
    @DisplayName("getDayOfWeekName 메서드 테스트")
    class GetDayOfWeekNameTests {

        @Test
        @DisplayName("getDayOfWeekName - 월요일")
        void getDayOfWeekName_monday() {
            assertEquals("월요일", DateUtils.getDayOfWeekName("20250113"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 화요일")
        void getDayOfWeekName_tuesday() {
            assertEquals("화요일", DateUtils.getDayOfWeekName("20250114"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 수요일")
        void getDayOfWeekName_wednesday() {
            assertEquals("수요일", DateUtils.getDayOfWeekName("20250115"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 목요일")
        void getDayOfWeekName_thursday() {
            assertEquals("목요일", DateUtils.getDayOfWeekName("20250116"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 금요일")
        void getDayOfWeekName_friday() {
            assertEquals("금요일", DateUtils.getDayOfWeekName("20250117"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 토요일")
        void getDayOfWeekName_saturday() {
            assertEquals("토요일", DateUtils.getDayOfWeekName("20250118"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 일요일")
        void getDayOfWeekName_sunday() {
            assertEquals("일요일", DateUtils.getDayOfWeekName("20250119"));
        }

        @Test
        @DisplayName("getDayOfWeekName - 잘못된 형식")
        void getDayOfWeekName_invalid() {
            assertEquals("", DateUtils.getDayOfWeekName("invalid"));
        }
    }

    @Test
    @DisplayName("getTodayDayOfWeek")
    void getTodayDayOfWeek() {
        DayOfWeek result = DateUtils.getTodayDayOfWeek();

        assertNotNull(result);
        assertEquals(LocalDate.now().getDayOfWeek(), result);
    }

    @Test
    @DisplayName("getTodayDayOfWeekName")
    void getTodayDayOfWeekName() {
        String result = DateUtils.getTodayDayOfWeekName();

        assertNotNull(result);
        assertTrue(result.endsWith("요일"));
    }

    @Test
    @DisplayName("addDays")
    void addDays() {
        LocalDate date = LocalDate.of(2025, 1, 15);

        assertEquals(LocalDate.of(2025, 1, 20), DateUtils.addDays(date, 5));
        assertEquals(LocalDate.of(2025, 1, 10), DateUtils.addDays(date, -5));
    }

    @Test
    @DisplayName("addMonths")
    void addMonths() {
        LocalDate date = LocalDate.of(2025, 1, 15);

        assertEquals(LocalDate.of(2025, 4, 15), DateUtils.addMonths(date, 3));
        assertEquals(LocalDate.of(2024, 10, 15), DateUtils.addMonths(date, -3));
    }

    @Nested
    @DisplayName("isHoliday/isWeekday 메서드 테스트")
    class HolidayWeekdayTests {

        @Test
        @DisplayName("isHoliday - 토요일")
        void isHoliday_saturday() {
            assertTrue(DateUtils.isHoliday("20250118"));
        }

        @Test
        @DisplayName("isHoliday - 일요일")
        void isHoliday_sunday() {
            assertTrue(DateUtils.isHoliday("20250119"));
        }

        @Test
        @DisplayName("isHoliday - 평일")
        void isHoliday_weekday() {
            assertFalse(DateUtils.isHoliday("20250115"));
        }

        @Test
        @DisplayName("isHoliday - 잘못된 형식")
        void isHoliday_invalid() {
            assertFalse(DateUtils.isHoliday("invalid"));
        }

        @Test
        @DisplayName("isWeekday - 평일")
        void isWeekday_weekday() {
            assertTrue(DateUtils.isWeekday("20250115"));
        }

        @Test
        @DisplayName("isWeekday - 주말")
        void isWeekday_weekend() {
            assertFalse(DateUtils.isWeekday("20250118"));
        }

        @Test
        @DisplayName("isWeekday - 잘못된 형식")
        void isWeekday_invalid() {
            assertFalse(DateUtils.isWeekday("invalid"));
        }
    }

    @Nested
    @DisplayName("isLeapYear 메서드 테스트")
    class IsLeapYearTests {

        @Test
        @DisplayName("isLeapYear - 윤년")
        void isLeapYear_true() {
            assertTrue(DateUtils.isLeapYear("20240115"));
        }

        @Test
        @DisplayName("isLeapYear - 평년")
        void isLeapYear_false() {
            assertFalse(DateUtils.isLeapYear("20250115"));
        }

        @Test
        @DisplayName("isLeapYear - 잘못된 형식")
        void isLeapYear_invalid() {
            assertFalse(DateUtils.isLeapYear("invalid"));
        }
    }

    @Test
    @DisplayName("formatDate - 포맷 변환")
    void formatDate() {
        String result = DateUtils.formatDate("20250115", "yyyyMMdd", "yyyy-MM-dd");

        assertEquals("2025-01-15", result);
    }

    @Test
    @DisplayName("formatDate - 잘못된 형식")
    void formatDate_invalid() {
        String result = DateUtils.formatDate("invalid", "yyyyMMdd", "yyyy-MM-dd");

        assertEquals("invalid", result);
    }

    @Nested
    @DisplayName("isEndOfMonth 메서드 테스트")
    class IsEndOfMonthTests {

        @Test
        @DisplayName("isEndOfMonth - 마지막 날")
        void isEndOfMonth_true() {
            assertTrue(DateUtils.isEndOfMonth("20250131"));
        }

        @Test
        @DisplayName("isEndOfMonth - 마지막 날 아님")
        void isEndOfMonth_false() {
            assertFalse(DateUtils.isEndOfMonth("20250115"));
        }

        @Test
        @DisplayName("isEndOfMonth - 잘못된 형식")
        void isEndOfMonth_invalid() {
            assertFalse(DateUtils.isEndOfMonth("invalid"));
        }
    }

    @Test
    @DisplayName("truncateToMonthStart")
    void truncateToMonthStart() {
        assertEquals("20250101", DateUtils.truncateToMonthStart("20250115"));
    }

    @Test
    @DisplayName("truncateToMonthStart - 잘못된 형식")
    void truncateToMonthStart_invalid() {
        assertEquals("", DateUtils.truncateToMonthStart("invalid"));
    }

    @Test
    @DisplayName("getDaysInMonth")
    void getDaysInMonth() {
        assertEquals(31, DateUtils.getDaysInMonth("20250115"));
        assertEquals(28, DateUtils.getDaysInMonth("20250215"));
    }

    @Test
    @DisplayName("getDaysInMonth - 잘못된 형식")
    void getDaysInMonth_invalid() {
        assertEquals(-1, DateUtils.getDaysInMonth("invalid"));
    }

    @Nested
    @DisplayName("isNowBetween 메서드 테스트")
    class IsNowBetweenTests {

        @Test
        @DisplayName("isNowBetween - 범위 내")
        void isNowBetween_inRange() {
            // 00:00:00 ~ 23:59:59 범위로 테스트
            boolean result = DateUtils.isNowBetween("000000", "235959");

            assertTrue(result);
        }

        @Test
        @DisplayName("isNowBetween - 잘못된 형식")
        void isNowBetween_invalid() {
            assertFalse(DateUtils.isNowBetween("invalid", "235959"));
        }
    }

    @Nested
    @DisplayName("isNowBetweenHHmm 메서드 테스트")
    class IsNowBetweenHHmmTests {

        @Test
        @DisplayName("isNowBetweenHHmm - 범위 내")
        void isNowBetweenHHmm_inRange() {
            // 00:00 ~ 23:59 범위로 테스트
            boolean result = DateUtils.isNowBetweenHHmm("0000", "2359");

            assertTrue(result);
        }

        @Test
        @DisplayName("isNowBetweenHHmm - 잘못된 형식")
        void isNowBetweenHHmm_invalid() {
            assertFalse(DateUtils.isNowBetweenHHmm("invalid", "2359"));
        }
    }

    @Nested
    @DisplayName("getDate 메서드 테스트")
    class GetDateTests {

        @Test
        @DisplayName("getDate - 기본 현재 날짜")
        void getDate_default() {
            String result = DateUtils.getDate("yyyyMMdd", 'D', 5);

            assertNotNull(result);
            assertEquals(8, result.length());
        }

        @Test
        @DisplayName("getDate - null 포맷")
        void getDate_nullFormat() {
            String result = DateUtils.getDate(null, 'D', 5);

            assertEquals("", result);
        }

        @Test
        @DisplayName("getDate - 빈 포맷")
        void getDate_emptyFormat() {
            String result = DateUtils.getDate("", 'D', 5);

            assertEquals("", result);
        }

        @Test
        @DisplayName("getDate - 8자리 날짜 문자열")
        void getDate_8digits() {
            String result = DateUtils.getDate("20250115", "yyyyMMdd", 'D', 5);

            assertEquals("20250120", result);
        }

        @Test
        @DisplayName("getDate - 14자리 날짜 문자열")
        void getDate_14digits() {
            String result = DateUtils.getDate("20250115103045", "yyyyMMddHHmmss", 'D', 5);

            assertEquals("20250120103045", result);
        }

        @Test
        @DisplayName("getDate - 년 추가")
        void getDate_addYear() {
            String result = DateUtils.getDate("20250115", "yyyyMMdd", 'Y', 2);

            assertEquals("20270115", result);
        }

        @Test
        @DisplayName("getDate - 월 추가")
        void getDate_addMonth() {
            String result = DateUtils.getDate("20250115", "yyyyMMdd", 'M', 3);

            assertEquals("20250415", result);
        }

        @Test
        @DisplayName("getDate - 시간 추가 (14자리)")
        void getDate_addHour() {
            String result = DateUtils.getDate("20250115100000", "yyyyMMddHHmmss", 'H', 5);

            assertEquals("20250115150000", result);
        }

        @Test
        @DisplayName("getDate - 분 추가 (14자리)")
        void getDate_addMinute() {
            String result = DateUtils.getDate("20250115100000", "yyyyMMddHHmmss", 'm', 30);

            assertEquals("20250115103000", result);
        }

        @Test
        @DisplayName("getDate - 초 추가 (14자리)")
        void getDate_addSecond() {
            String result = DateUtils.getDate("20250115100000", "yyyyMMddHHmmss", 'S', 45);

            assertEquals("20250115100045", result);
        }

        @Test
        @DisplayName("getDate - null 날짜 문자열")
        void getDate_nullDateString() {
            assertNull(DateUtils.getDate(null, "yyyyMMdd", 'D', 5));
        }

        @Test
        @DisplayName("getDate - 빈 날짜 문자열")
        void getDate_emptyDateString() {
            assertNull(DateUtils.getDate("", "yyyyMMdd", 'D', 5));
        }

        @Test
        @DisplayName("getDate - 다른 길이 문자열")
        void getDate_otherLength() {
            String result = DateUtils.getDate("2025", "yyyy", 'Y', 1);

            assertEquals("2025", result);
        }

        @Test
        @DisplayName("getDate - 기본 flag")
        void getDate_defaultFlag() {
            String result = DateUtils.getDate("20250115", "yyyyMMdd", 'X', 5);

            assertEquals("20250115", result);
        }
    }

    @Test
    @DisplayName("string2Date")
    void string2Date() {
        Date result = DateUtils.string2Date("20250115", "yyyyMMdd");

        assertNotNull(result);
    }

    @Test
    @DisplayName("string2Date - 잘못된 형식")
    void string2Date_invalid() {
        assertThrows(RuntimeException.class, () -> {
            DateUtils.string2Date(null, "yyyyMMdd");
        });
    }

    @Nested
    @DisplayName("getDate(Date, format) 메서드 테스트")
    class GetDateWithDateTests {

        @Test
        @DisplayName("getDate - null Date")
        void getDate_nullDate() {
            assertEquals("", DateUtils.getDate((Date) null, "yyyyMMdd"));
        }

        @Test
        @DisplayName("getDate - null 포맷")
        void getDate_nullFormat() {
            assertEquals("", DateUtils.getDate(new Date(), null));
        }

        @Test
        @DisplayName("getDate - 빈 포맷")
        void getDate_emptyFormat() {
            assertEquals("", DateUtils.getDate(new Date(), ""));
        }

        @Test
        @DisplayName("getDate - mmm 포맷")
        void getDate_mmmFormat() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "mmm");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - eee 포맷")
        void getDate_eeeFormat() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "eee");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - g 포맷")
        void getDate_gFormat() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "g");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - hh24 포맷")
        void getDate_hh24Format() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "hh24");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - ms 포맷")
        void getDate_msFormat() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "ms");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - mm 포맷")
        void getDate_mmFormat() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "yyyy/mm/dd");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - mi 포맷")
        void getDate_miFormat() {
            Date date = new Date();
            String result = DateUtils.getDate(date, "hh:mi:ss");

            assertNotNull(result);
        }

        @Test
        @DisplayName("getDate - yyyyMMdd 포맷")
        void getDate_yyyyMMddFormat() {
            Calendar cal = Calendar.getInstance();
            cal.set(2025, Calendar.JANUARY, 15);
            Date date = cal.getTime();

            String result = DateUtils.getDate(date, "yyyyMMdd");

            assertTrue(result.contains("2025"));
        }
    }
    
    @Test
    @DisplayName("정상 날짜 + day")
    void getDateFormatFrom_addDays() {
    	String result = DateUtils.getDateFormatFrom(
    		"yyyyMMdd",
    		"20240131",
    		1
    	);
    	
    	assertEquals("20240201", result);
    }
    
    @Test
    @DisplayName("dayCount 음수 처리")
    void getDateFormatFrom_minusDays() {
    	String result = DateUtils.getDateFormatFrom(
    		"yyyyMMdd",
    		"20240101",
    		-1
    	);
    	
    	assertEquals("20231231", result);
    }
    
    @Test
    @DisplayName("data가 null이면 00000000 반환")
    void getDateFormatFrom_nullData() {
    	String result = DateUtils.getDateFormatFrom(
    		"yyyyMMdd",
    		null,
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    @Test
    @DisplayName("data 길이가 8이 아니면 00000000 반환")
    void getDateFormatFrom_invalidLength() {
    	String result = DateUtils.getDateFormatFrom(
    		"yyyyMMdd",
    		"202401",
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    @Test
    @DisplayName("숫자가 아니면 00000000 반환")
    void getDateFormatFrom_invalidNumber() {
    	String result = DateUtils.getDateFormatFrom(
    		"yyyyMMdd",
    		"202401ㅁㅁㅁ",
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    @Test
    @DisplayName("숫자가 아니면 00000000 반환")
    void getDateFormatFrom_invalidDateFormat() {
    	String result = DateUtils.getDateFormatFrom(
    		"----------",
    		"202401ㅁㅁㅁ",
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    @Test
    @DisplayName("정상날짜 + monCount 증가")
    void getDateFormatFromMonth_addMonths() {
    	String result = DateUtils.getDateFormatFromMonth("yyyyMMdd", "20240131", 1);
    	
    	assertEquals("20240229", result);
    }
    
    @Test
    @DisplayName("monCount 음수처리")
    void getDateFormatFromMonth_minusMonths() {
    	String result = DateUtils.getDateFormatFromMonth("yyyyMMdd", "20240115", -1);
    	
    	assertEquals("20231215", result);
    }
    
    @Test
    @DisplayName("data가 null이면 00000000 반환")
    void getDateFormatFromMonth_nullData() {
    	String result = DateUtils.getDateFormatFromMonth(
    		"yyyyMMdd",
    		null,
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    
    @Test
    @DisplayName("data 길이가 8이 아니면 00000000 반환")
    void getDateFormatFromMonth_invalidLength() {
    	String result = DateUtils.getDateFormatFromMonth(
    		"yyyyMMdd",
    		"202401",
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    @Test
    @DisplayName("data 길이가 8이 아니면 00000000 반환")
    void getDateFormatFromMonth_invalidNumber() {
    	String result = DateUtils.getDateFormatFromMonth(
    		"yyyyMMdd",
    		"202401DDD",
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
    
    @Test
    @DisplayName("알수없는 date format인경우 00000000 반환")
    void getDateFormatFromMonth_unknownFormat() {
    	String result = DateUtils.getDateFormatFromMonth(
    		"--------",
    		"202401DDD",
    		1
    	);
    	
    	assertEquals("00000000", result);
    }
}
