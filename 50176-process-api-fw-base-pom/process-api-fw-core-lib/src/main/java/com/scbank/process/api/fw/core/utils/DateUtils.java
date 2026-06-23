package com.scbank.process.api.fw.core.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 날짜 및 시간 관련 유틸리티 클래스
 * Java 8+ Date/Time API 기반으로 구현됨
 */
public class DateUtils {

    public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HHmm");
    public static final DateTimeFormatter HHMMSS = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter YYYYMMDDHHMMSSSSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static final int BIG = 2;
    public static final int EQUAL = 1;
    public static final int SMALL = 0;
    
    private DateUtils() {
    	
    }

    /**
     * 오늘 날짜를 yyyyMMdd 형식의 문자열로 반환합니다.
     *
     * @return yyyyMMdd 형식의 오늘 날짜 문자열
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(YYYYMMDD);
    }

    /**
     * 오늘 날짜를 yyyyMMddHHmmss 형식의 문자열로 반환합니다.
     *
     * @return yyyyMMddHHmmss 형식의 오늘 날짜 문자열
     */
    public static String getCurrentTime() {
        return LocalDateTime.now().format(YYYYMMDDHHMMSS);
    }

    /**
     * 
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        return getCurrentDate(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 
     * @param formatter
     * @return
     */
    public static String getCurrentTime(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 오늘 날짜를 주어진 포맷의 문자열로 반환합니다.
     *
     * @param format 날짜 포맷 문자열
     * @return 포맷된 오늘 날짜 문자열
     */
    public static String getCurrentDate(String format) {
        if (format == null || format.isEmpty()) {
            return getCurrentDate();
        }

        if (format.length() == 8 || "yyyyMMdd".equals(format)) {
            return getCurrentDate(DateTimeFormatter.ofPattern(format));
        }

        return getCurrentDate(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 
     * @param formatter
     * @return
     */
    public static String getCurrentDate(DateTimeFormatter formatter) {
        if (formatter == null) {
            return getCurrentDate();
        }

        return LocalDateTime.now().format(formatter);
    }

    /**
     * 날짜 문자열을 비교합니다. yyyyMMdd 형식 기준.
     *
     * @param date1 기준 날짜 문자열
     * @param date2 비교할 날짜 문자열
     * @return date1 > date2 → BIG(2), date1 = date2 → EQUAL(1), date1 &lt; date2 →
     *         SMALL(0)
     */
    public static int compare(String date1, String date2) {
        try {
            int d1 = Integer.parseInt(date1);
            int d2 = Integer.parseInt(date2);
            return Integer.compare(d1, d2) > 0 ? BIG : Integer.compare(d1, d2) == 0 ? EQUAL : SMALL;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 오늘 날짜와 비교하여 선후를 판단합니다.
     *
     * @param date 날짜 문자열 (yyyyMMdd)
     * @return date > today → BIG(2), date = today → EQUAL(1), date &lt; today →
     *         SMALL(0)
     */
    public static int compare(String date) {
        return compare(date, getCurrentDate());
    }

    /**
     * 날짜 문자열을 yyyyMMdd 또는 yyyyMMddHHmmss 형식으로 파싱
     *
     * @param str 날짜 문자열 (8자리, 14자리 또는 6자리 HHmmss)
     * @return LocalDateTime 객체 또는 null
     */
    public static LocalDateTime parseToDateTime(String str) {
        try {
            if (str.length() == 8) {
                return LocalDate.parse(str, YYYYMMDD).atStartOfDay();
            } else if (str.length() == 14) {
                return LocalDateTime.parse(str, YYYYMMDDHHMMSS);
            } else if (str.length() == 6) {
                String full = LocalDate.now().format(YYYYMMDD) + str;
                return LocalDateTime.parse(full, YYYYMMDDHHMMSS);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 날짜 문자열을 yyyyMMdd 형식으로 파싱
     *
     * @param yyyymmdd 8자리 날짜 문자열
     * @return LocalDate 객체 또는 null
     */
    public static LocalDate parseDate(String yyyymmdd) {
        try {
            return LocalDate.parse(yyyymmdd, YYYYMMDD);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 두 날짜 간 일 수 차이를 계산
     *
     * @param from 시작일자 java.util.Date
     * @param to   종료일자 java.util.Date
     * @return 날짜 차이 (일 수)
     */
    public static int getDayBetween(java.util.Date from, java.util.Date to) {
        LocalDate f = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate t = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(f, t);
    }

    /**
     * 두 날짜 문자열 사이의 일수 차이를 계산합니다.
     *
     * @param fromDate yyyyMMdd 형식의 시작일 문자열
     * @param toDate   yyyyMMdd 형식의 종료일 문자열
     * @return 일 수 차이 (fromDate → toDate)
     */
    public static long getDayBetween(String fromDate, String toDate) {
        LocalDate from = parseDate(fromDate);
        LocalDate to = parseDate(toDate);
        if (from == null || to == null)
            return 0;
        return ChronoUnit.DAYS.between(from, to);
    }

    /**
     * yyyyMMdd 형식의 날짜 문자열에서 해당 월의 마지막 날을 반환합니다.
     *
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     * @return 마지막 일자 (1~31), 파싱 실패 시 -1
     */
    public static int getEndDayOfMonth(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) ? date.lengthOfMonth() : -1;
    }

    /**
     * yyyyMMdd 형식의 날짜 문자열에서 연도를 추출합니다.
     *
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     * @return 연도 값 (예: 2025)
     */
    public static int getYear(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) ? date.getYear() : -1;
    }

    /**
     * yyyyMMdd 형식의 날짜 문자열에서 월을 추출합니다.
     *
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     * @return 월 값 (1~12)
     */
    public static int getMonth(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) ? date.getMonthValue() : -1;
    }

    /**
     * 요일 반환 (1:일요일 ~ 7:토요일)
     *
     * @param date java.util.Date
     * @return 요일 숫자 (1~7)
     */
    public static int getWeekDay(java.util.Date date) {
        return Integer.parseInt(getDay(date));
    }

    /**
     * Date 객체 기준 요일 반환
     *
     * @param date java.util.Date
     * @return 요일 (1~7)
     */
    public static String getDay(java.util.Date date) {
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return String.valueOf(localDate.getDayOfWeek().getValue() % 7 + 1);
    }

    /**
     * 특정 포맷의 날짜 문자열을 ZonedDateTime으로 변환
     *
     * @param dateString yyyyMMdd 형식 문자열
     * @return ZonedDateTime 객체
     */
    public static ZonedDateTime getZonedDateTime(String dateString) {
        LocalDate localDate = parseDate(dateString);
        if (localDate == null)
            return null;
        return localDate.atStartOfDay(ZoneId.systemDefault());
    }

    /**
     * java.util.Date 객체를 ZonedDateTime으로 변환
     *
     * @param date java.util.Date 객체
     * @return ZonedDateTime 객체
     */
    public static ZonedDateTime getZonedDateTime(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault());
    }

    /**
     * 문자열이 주어진 포맷의 날짜로 유효한지 검사
     *
     * @param dateStr 날짜 문자열
     * @param format  날짜 포맷
     * @return 유효 여부
     */
    public static boolean isValidDate(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * LocalDate를 포맷 문자열로 변환
     * 
     * @param date
     * @param format
     * @return
     */
    public static String format(LocalDate date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 포맷 문자열을 LocalDate로 변환
     * 
     * @param dateStr
     * @param format
     * @return
     */
    public static LocalDate parse(String dateStr, String format) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return LocalDate.now().isEqual(date);
    }

    /**
     * 
     * @param date
     * @return
     */
    public static boolean isBeforeToday(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * 
     * @param date
     * @return
     */
    public static boolean isAfterToday(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    /**
     * 
     * @param dateStr
     * @return
     */
    public static String getDayOfWeekName(String dateStr) {
        LocalDate date = parseDate(dateStr);
        if (date == null)
            return "";
        return switch (date.getDayOfWeek()) {
            case MONDAY -> "월요일";
            case TUESDAY -> "화요일";
            case WEDNESDAY -> "수요일";
            case THURSDAY -> "목요일";
            case FRIDAY -> "금요일";
            case SATURDAY -> "토요일";
            case SUNDAY -> "일요일";
        };
    }

    /**
     * 오늘 날짜의 요일명을 DayOfWeek enum으로 반환합니다.
     *
     * @return DayOfWeek (예: MONDAY)
     */
    public static DayOfWeek getTodayDayOfWeek() {
        return LocalDate.now().getDayOfWeek();
    }

    /**
     * 오늘 날짜의 요일명을 한글로 반환합니다.
     *
     * @return 오늘 요일명 (예: 월요일)
     */
    public static String getTodayDayOfWeekName() {
        return getDayOfWeekName(getCurrentDate());
    }

    /**
     * 지정된 날짜에 일(day)을 더한 날짜를 반환합니다.
     *
     * @param date 기준이 되는 LocalDate
     * @param days 더하거나 뺄 일 수 (음수 가능)
     * @return 계산된 LocalDate
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    /**
     * 지정된 날짜에 월(month)을 더한 날짜를 반환합니다.
     *
     * @param date   기준이 되는 LocalDate
     * @param months 더하거나 뺄 월 수 (음수 가능)
     * @return 계산된 LocalDate
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        return date.plusMonths(months);
    }

    /**
     * 해당 날짜가 주말(토/일)인지 여부를 확인합니다.
     *
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     * @return 토요일 또는 일요일이면 true, 아니면 false
     */
    public static boolean isHoliday(String dateStr) {
        LocalDate date = parseDate(dateStr);
        if (date == null)
            return false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 해당 연도가 윤년인지 확인합니다.
     * 
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     */
    public static boolean isLeapYear(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) && date.isLeapYear();
    }

    /**
     * 날짜 포맷 변환
     * 
     * @param dateStr    포맷 변환 대상 날짜 문자열
     * @param fromFormat 원 포맷
     * @param toFormat   변환 포맷
     * @return
     */
    public static String formatDate(String dateStr, String fromFormat, String toFormat) {
        try {
            DateTimeFormatter from = DateTimeFormatter.ofPattern(fromFormat);
            DateTimeFormatter to = DateTimeFormatter.ofPattern(toFormat);
            return LocalDate.parse(dateStr, from).format(to);
        } catch (Exception e) {
            return dateStr;
        }
    }

    /**
     * yyyyMMdd 형식 날짜의 일(day)을 추출합니다.
     * 
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     */
    public static int getDay(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) ? date.getDayOfMonth() : -1;
    }

    /**
     * yyyyMMdd 형식 날짜가 평일인지 확인합니다.
     * 
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     */
    public static boolean isWeekday(String dateStr) {
        LocalDate date = parseDate(dateStr);
        if (date == null)
            return false;
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    /**
     * yyyyMMdd 형식 날짜가 해당 월의 마지막 날인지 확인합니다.
     * 
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     */
    public static boolean isEndOfMonth(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) && (date.getDayOfMonth() == date.lengthOfMonth());
    }

    /**
     * yyyyMMdd 형식 날짜를 해당 월의 1일로 자릅니다.
     * 
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     */
    public static String truncateToMonthStart(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) ? date.withDayOfMonth(1).format(YYYYMMDD) : "";
    }

    /**
     * yyyyMMdd 형식 날짜의 해당 월 총 일 수를 반환합니다.
     * 
     * @param dateStr yyyyMMdd 형식의 날짜 문자열
     */
    public static int getDaysInMonth(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return (date != null) ? date.lengthOfMonth() : -1;
    }

    /**
     * 현재 시간이 주어진 시작/종료 시간 사이에 있는지 확인합니다.
     * 시간 형식은 HHmmss로 입력받습니다.
     *
     * @param startTime 시작 시간 문자열 (예: "090000")
     * @param endTime   종료 시간 문자열 (예: "180000")
     * @return true: 범위 내, false: 범위 외 또는 형식 오류
     */
    public static boolean isNowBetween(String startTime, String endTime) {
        try {
            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.parse(startTime, HHMMSS);
            LocalTime end = LocalTime.parse(endTime, HHMMSS);
            return !now.isBefore(start) && !now.isAfter(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 현재 시간이 주어진 HHmm 형식 시작/종료 시간 사이에 있는지 확인합니다.
     *
     * @param startTime 시작 시간 (예: "0900")
     * @param endTime   종료 시간 (예: "1730")
     * @return true: 현재 시간이 범위 내에 있음, false: 아님 또는 포맷 오류
     */
    public static boolean isNowBetweenHHmm(String startTime, String endTime) {
        try {
            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.parse(startTime, HHMM);
            LocalTime end = LocalTime.parse(endTime, HHMM);
            return !now.isBefore(start) && !now.isAfter(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String getDate(String pStrFmt, char pChrTermFlag, int pIntVal) {
        if (pStrFmt == null || pStrFmt.isEmpty()) {
            return "";
        }
        return getDate(getCurrentDate(YYYYMMDD), pStrFmt, pChrTermFlag, pIntVal);
    }

    /**
     * 
     * @param strDate
     * @param strFmt
     * @param termFlag
     * @param val
     * @return
     */
    public static String getDate(String strDate, String strFmt, char termFlag, int val) {
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }

        DateTimeFormatter returnFormatter = DateTimeFormatter.ofPattern(strFmt);
        if (strDate.length() == 8) {
            LocalDate date = LocalDate.parse(strDate, YYYYMMDD);
            date = addByFlag(date, termFlag, val);
            return date.format(returnFormatter);
        } else if (strDate.length() == 14) {
            LocalDateTime dateTime = LocalDateTime.parse(strDate, YYYYMMDDHHMMSS);
            dateTime = addByFlag(dateTime, termFlag, val);
            return dateTime.format(returnFormatter);
        } else {
            return strDate;
        }
    }

    /**
     * 
     * @param date
     * @param flag
     * @param val
     * @return
     */
    private static LocalDate addByFlag(LocalDate date, char flag, int val) {
        switch (flag) {
            case 'Y':
                return date.plusYears(val);
            case 'M':
                return date.plusMonths(val);
            case 'D':
                return date.plusDays(val);
            default:
                return date;
        }
    }

    /**
     * 
     * @param dateTime
     * @param flag
     * @param val
     * @return
     */
    private static LocalDateTime addByFlag(LocalDateTime dateTime, char flag, int val) {
        switch (flag) {
            case 'Y':
                return dateTime.plusYears(val);
            case 'M':
                return dateTime.plusMonths(val);
            case 'D':
                return dateTime.plusDays(val);
            case 'H':
                return dateTime.plusHours(val);
            case 'm':
                return dateTime.plusMinutes(val);
            case 'S':
                return dateTime.plusSeconds(val);
            default:
                return dateTime;
        }
    }

    public static java.util.Date string2Date(String s, String format) {
        java.util.Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            d = sdf.parse(s, new ParsePosition(0));
        } catch (Exception e) {
            throw new RuntimeException("Date format not valid.");
        }
        return d;
    }

    /**
     * 사용자가 입력한 Date 를 format 형식에따라 값을 반환한다.
     * format 형식은 YYYYMMDDHH24MISS 형식으로 가져올수 있다.
     *
     * @param pDate   Date 사용자가 Date
     * @param pStrFmt String Date Format
     * @return String
     */
    public static String getDate(Date pDate, String pStrFmt) {
        if (pDate == null) {
            return "";
        }
        StringBuffer sbFormat = new StringBuffer();
        if (StringUtils.isEmpty(pStrFmt)) {
            return "";
        }

        pStrFmt = pStrFmt.toLowerCase();
        int i;
        // --------------------------------------------------------------------------------
        if ((i = pStrFmt.indexOf("mmm")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("MMM").append(pStrFmt.substring(i + 3));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);
        }
        // --------------------------------------------------------------------------------
        if ((i = pStrFmt.indexOf("eee")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("EEE").append(pStrFmt.substring(i + 3));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);

        }
        // --------------------------------------------------------------------------------
        if ((i = pStrFmt.indexOf("g")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("G").append(pStrFmt.substring(i + 1));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);

        }

        if ((i = pStrFmt.indexOf("hh24")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("HH").append(pStrFmt.substring(i + 4));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);
        }

        if ((i = pStrFmt.indexOf("ms")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("S").append(pStrFmt.substring(i + 2));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);
        }

        if ((i = pStrFmt.indexOf("mm")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("MM").append(pStrFmt.substring(i + 2));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);
        }

        if ((i = pStrFmt.indexOf("mi")) != -1) {
            sbFormat.append(pStrFmt.substring(0, i)).append("mm").append(pStrFmt.substring(i + 2));
            pStrFmt = sbFormat.toString();
            sbFormat.setLength(0);

        }

        return new SimpleDateFormat(pStrFmt).format(pDate);
    }

    public static String getDateFormatFrom(String format, String data, int dayCount) {
      try {
         int year = 0000;
         int month = 00;
         int day = 00;
         if (data != null && data.length() >= 8) {
            // int year;
            // int month;
            // int day;
            try {
               year = Integer.parseInt(data.substring(0, 4));
               month = Integer.parseInt(data.substring(4, 6)) - 1;
               day = Integer.parseInt(data.substring(6, 8));
            } catch (NumberFormatException var9) {
               return "00000000";
            }

            Calendar cdate = new GregorianCalendar(year, month, day);
            cdate.add(5, dayCount);
            Date date = cdate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
         } else {
            return "00000000";
         }
      } catch (Exception var10) {
         return "00000000";
      }
   }

    public static String getDateFormatFromMonth(String format, String data, int monCount) {
      try {
         int year = 0000;
         int month = 00;
         int day = 00;
         if (data != null && data.length() >= 8) {
            // int year;
            // int month;
            // int day;
            try {
               year = Integer.parseInt(data.substring(0, 4));
               month = Integer.parseInt(data.substring(4, 6)) - 1;
               day = Integer.parseInt(data.substring(6, 8));
            } catch (NumberFormatException var9) {
               return "00000000";
            }

            Calendar cdate = new GregorianCalendar(year, month, day);
            cdate.add(2, monCount);
            Date date = cdate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
         } else {
            return "00000000";
         }
      } catch (Exception var10) {
         return "00000000";
      }
   }
}
