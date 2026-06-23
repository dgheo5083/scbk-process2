package com.scbank.process.api.fw.core.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StringUtils 테스트")
class StringUtilsTest {

    @Test
    @DisplayName("상수 값 확인")
    void constants() {
        assertEquals(" ", StringUtils.SPACE);
        assertEquals("0", StringUtils.ZERO);
        assertEquals(1, StringUtils.LEFT);
        assertEquals(2, StringUtils.CENTER);
        assertEquals(3, StringUtils.RIGHT);
    }

    @Nested
    @DisplayName("hasLength 메서드 테스트")
    class HasLengthTests {

        @Test
        @DisplayName("hasLength(String) - 정상")
        void hasLength_string() {
            assertTrue(StringUtils.hasLength("test"));
            assertTrue(StringUtils.hasLength("  "));
            assertFalse(StringUtils.hasLength(""));
            assertFalse(StringUtils.hasLength((String) null));
        }

        @Test
        @DisplayName("hasLength(CharSequence) - 정상")
        void hasLength_charSequence() {
            assertTrue(StringUtils.hasLength((CharSequence) "test"));
            assertFalse(StringUtils.hasLength((CharSequence) ""));
            assertFalse(StringUtils.hasLength((CharSequence) null));
        }
    }

    @Nested
    @DisplayName("hasText 메서드 테스트")
    class HasTextTests {

        @Test
        @DisplayName("hasText(String) - 정상")
        void hasText_string() {
            assertTrue(StringUtils.hasText("test"));
            assertFalse(StringUtils.hasText("  "));
            assertFalse(StringUtils.hasText(""));
            assertFalse(StringUtils.hasText((String) null));
        }

        @Test
        @DisplayName("hasText(CharSequence) - 정상")
        void hasText_charSequence() {
            assertTrue(StringUtils.hasText((CharSequence) "test"));
            assertFalse(StringUtils.hasText((CharSequence) "  "));
            assertFalse(StringUtils.hasText((CharSequence) null));
        }
    }

    @Test
    @DisplayName("getLetterOrDigitOtherSpace")
    void getLetterOrDigitOtherSpace() {
        assertEquals("", StringUtils.getLetterOrDigitOtherSpace(null));
        assertEquals("abc123", StringUtils.getLetterOrDigitOtherSpace("abc123"));
        assertEquals("abc 123", StringUtils.getLetterOrDigitOtherSpace("abc\t123"));
    }

    @Nested
    @DisplayName("isValid 메서드 테스트")
    class IsValidTests {

        @Test
        @DisplayName("isValid - 정상")
        void isValid() {
            assertTrue(StringUtils.isValid("a"));
            assertTrue(StringUtils.isValid("  "));
            assertFalse(StringUtils.isValid(""));
            assertFalse(StringUtils.isValid(null));
        }

        @Test
        @DisplayName("isValid - 길이 지정")
        void isValid_withLength() {
            assertTrue(StringUtils.isValid("abc", 3));
            assertTrue(StringUtils.isValid("abcd", 3));
            assertFalse(StringUtils.isValid("ab", 3));
            assertFalse(StringUtils.isValid(null, 1));
        }
    }

    @Test
    @DisplayName("isWord")
    void isWord() {
        assertTrue(StringUtils.isWord("abc"));
        assertTrue(StringUtils.isWord("가나다"));
        assertFalse(StringUtils.isWord("abc123"));
        assertFalse(StringUtils.isWord("abc def"));
        assertFalse(StringUtils.isWord(null));
    }

    @Test
    @DisplayName("isAlphanumeric")
    void isAlphanumeric() {
        assertTrue(StringUtils.isAlphanumeric("abc123"));
        assertTrue(StringUtils.isAlphanumeric("ABC"));
        assertFalse(StringUtils.isAlphanumeric("abc 123"));
        assertFalse(StringUtils.isAlphanumeric(null));
    }

    @Test
    @DisplayName("isNumeric")
    void isNumeric() {
        assertTrue(StringUtils.isNumeric("123"));
        assertFalse(StringUtils.isNumeric("123abc"));
        assertFalse(StringUtils.isNumeric(""));
        assertFalse(StringUtils.isNumeric(null));
    }

    @Test
    @DisplayName("quoteRegularExpression")
    void quoteRegularExpression() {
        assertEquals("\\^a\\[bc\\]", StringUtils.quoteRegularExpression("^a[bc]"));
        assertEquals("abc", StringUtils.quoteRegularExpression("abc"));
        assertNull(StringUtils.quoteRegularExpression(null));
    }

    @Test
    @DisplayName("getWordCount")
    void getWordCount() {
        assertEquals(3, StringUtils.getWordCount("a b c"));
        assertEquals(2, StringUtils.getWordCount("hello world"));
        assertEquals(3, StringUtils.getWordCount("a,b,c", ","));
    }

    @Nested
    @DisplayName("trim 메서드 테스트")
    class TrimTests {

        @Test
        @DisplayName("trim - 정상")
        void trim() {
            assertEquals("abc", StringUtils.trim("  abc  "));
            assertNull(StringUtils.trim(null));
        }

        @Test
        @DisplayName("clean")
        void clean() {
            assertEquals("abc", StringUtils.clean("  abc  "));
            assertEquals("", StringUtils.clean(null));
        }

        @Test
        @DisplayName("trim - 기본값 지정")
        void trim_withDefault() {
            assertEquals("abc", StringUtils.trim("  abc  ", "default"));
            assertEquals("default", StringUtils.trim(null, "default"));
        }
    }

    @Test
    @DisplayName("parseString")
    void parseString() {
        assertEquals("abc", StringUtils.parseString("abc"));
        assertEquals("", StringUtils.parseString(null));
        assertEquals("default", StringUtils.parseString(null, "default"));
    }

    @Test
    @DisplayName("ltrim/rtrim")
    void ltrim_rtrim() {
        assertEquals("abc  ", StringUtils.ltrim("  abc  "));
        assertEquals("  abc", StringUtils.rtrim("  abc  "));
        assertNull(StringUtils.ltrim(null));
        assertNull(StringUtils.rtrim(null));
    }

    @Nested
    @DisplayName("padding 메서드 테스트")
    class PaddingTests {

        @Test
        @DisplayName("padding - LEFT")
        void padding_left() {
            assertEquals("  abc", StringUtils.padding("abc", 5, " ", StringUtils.LEFT));
        }

        @Test
        @DisplayName("padding - RIGHT")
        void padding_right() {
            assertEquals("abc  ", StringUtils.padding("abc", 5, " ", StringUtils.RIGHT));
        }

        @Test
        @DisplayName("padding - CENTER")
        void padding_center() {
            assertEquals(" abc ", StringUtils.padding("abc", 5, " ", StringUtils.CENTER));
        }

        @Test
        @DisplayName("padding - 잘못된 방향")
        void padding_invalidDirection() {
            assertEquals("abc", StringUtils.padding("abc", 5, " ", 99));
        }
    }

    @Nested
    @DisplayName("left 메서드 테스트")
    class LeftTests {

        @Test
        @DisplayName("left - 기본")
        void left() {
            assertEquals("  abc", StringUtils.left("abc", 5));
        }

        @Test
        @DisplayName("left - char 구분자")
        void left_char() {
            assertEquals("00abc", StringUtils.left("abc", 5, '0'));
        }

        @Test
        @DisplayName("left - String 구분자")
        void left_string() {
            assertEquals("xxabc", StringUtils.left("abc", 5, "x"));
        }

        @Test
        @DisplayName("left - null")
        void left_null() {
            assertEquals("xxxxx", StringUtils.left(null, 5, "x"));
        }

        @Test
        @DisplayName("left - 이미 길이가 충분함")
        void left_noNeed() {
            assertEquals("abcde", StringUtils.left("abcde", 3, "x"));
        }
    }

    @Nested
    @DisplayName("right 메서드 테스트")
    class RightTests {

        @Test
        @DisplayName("right - 기본")
        void right() {
            assertEquals("abc  ", StringUtils.right("abc", 5));
        }

        @Test
        @DisplayName("right - char 구분자")
        void right_char() {
            assertEquals("abc00", StringUtils.right("abc", 5, '0'));
        }

        @Test
        @DisplayName("right - String 구분자")
        void right_string() {
            assertEquals("abcxx", StringUtils.right("abc", 5, "x"));
        }

        @Test
        @DisplayName("right - null")
        void right_null() {
            assertEquals("xxxxx", StringUtils.right(null, 5, "x"));
        }
    }

    @Nested
    @DisplayName("center 메서드 테스트")
    class CenterTests {

        @Test
        @DisplayName("center - 기본")
        void center() {
            assertEquals(" abc ", StringUtils.center("abc", 5));
        }

        @Test
        @DisplayName("center - char 구분자")
        void center_char() {
            assertEquals("0abc0", StringUtils.center("abc", 5, '0'));
        }

        @Test
        @DisplayName("center - null")
        void center_null() {
            assertEquals("xxxxx", StringUtils.center(null, 5, "x"));
        }
    }

    @Nested
    @DisplayName("replace 메서드 테스트")
    class ReplaceTests {

        @Test
        @DisplayName("replace - char 대체")
        void replace_char() {
            assertEquals("IMS''S HOPE", StringUtils.replace("IMS'S HOPE", '\'', "''"));
        }

        @Test
        @DisplayName("replace - String 대체")
        void replace_string() {
            assertEquals("Java <BR> is <BR> Wonderful", StringUtils.replace("Java \r\n is \r\n Wonderful", "\r\n", "<BR>"));
        }

        @Test
        @DisplayName("replace - 횟수 제한")
        void replace_max() {
            assertEquals("Java <BR> is \r\n Wonderful", StringUtils.replace("Java \r\n is \r\n Wonderful", "\r\n", "<BR>", 1));
        }

        @Test
        @DisplayName("replace - null")
        void replace_null() {
            assertNull(StringUtils.replace(null, "a", "b"));
        }
    }

    @Nested
    @DisplayName("overlay 메서드 테스트")
    class OverlayTests {

        @Test
        @DisplayName("overlay - char")
        void overlay_char() {
            assertEquals("JaVa", StringUtils.overlay("Java", 'V', 2, 3));
        }

        @Test
        @DisplayName("overlay - String")
        void overlay_string() {
            assertEquals("JaXXa", StringUtils.overlay("Java", "XX", 2, 3));
        }

        @Test
        @DisplayName("overlay - null")
        void overlay_null() {
            assertNull(StringUtils.overlay(null, "XX", 2, 3));
        }
    }

    @Test
    @DisplayName("interpolate")
    void interpolate() {
        Map<String, String> map = new HashMap<>();
        map.put("ip", "192.168.1.1");
        map.put("port", "8080");

        String result = StringUtils.interpolate("${ip}:${port}", map);

        assertEquals("192.168.1.1:8080", result);
        assertNull(StringUtils.interpolate(null, map));
        assertEquals("${ip}", StringUtils.interpolate("${ip}", null));
    }

    @Nested
    @DisplayName("대소문자 변환 메서드 테스트")
    class CaseConversionTests {

        @Test
        @DisplayName("toFirstUpperCase")
        void toFirstUpperCase() {
            assertEquals("Java", StringUtils.toFirstUpperCase("java"));
            assertNull(StringUtils.toFirstUpperCase(null));
        }

        @Test
        @DisplayName("toUpperCase")
        void toUpperCase() {
            assertEquals("JAVA", StringUtils.toUpperCase("java", 4));
            assertEquals("JAva", StringUtils.toUpperCase("java", 2));
            assertNull(StringUtils.toUpperCase(null, 4));
        }

        @Test
        @DisplayName("toTitleCase")
        void toTitleCase() {
            assertEquals("JAVA", StringUtils.toTitleCase("java", 4));
            assertNull(StringUtils.toTitleCase(null, 4));
        }

        @Test
        @DisplayName("toFirstLowerCase")
        void toFirstLowerCase() {
            assertEquals("java", StringUtils.toFirstLowerCase("Java"));
            assertNull(StringUtils.toFirstLowerCase(null));
        }

        @Test
        @DisplayName("toLowerCase")
        void toLowerCase() {
            assertEquals("java", StringUtils.toLowerCase("JAVA", 4));
            assertEquals("jaVA", StringUtils.toLowerCase("JAVA", 2));
            assertNull(StringUtils.toLowerCase(null, 4));
        }

        @Test
        @DisplayName("swapCase")
        void swapCase() {
            assertEquals("jAVA", StringUtils.swapCase("Java"));
            assertNull(StringUtils.swapCase(null));
        }
    }

    @Nested
    @DisplayName("reverse 메서드 테스트")
    class ReverseTests {

        @Test
        @DisplayName("reverse - 단순")
        void reverse() {
            assertEquals("cba", StringUtils.reverse("abc"));
            assertNull(StringUtils.reverse(null));
        }

        @Test
        @DisplayName("reverse - 구분자 기준")
        void reverse_withDelim() {
            assertEquals("cdef.abcd", StringUtils.reverse("abcd.cdef", "."));
        }

        @Test
        @DisplayName("reverse - null 입력 시 NullPointerException")
        void reverse_withDelim_null() {
            // split 메서드가 null 체크를 하지 않아 NPE 발생
            assertThrows(NullPointerException.class, () -> StringUtils.reverse(null, "."));
        }

        @Test
        @DisplayName("reverseAsArray")
        void reverseAsArray() {
            Object[] arr = {"a", "b", "c"};
            StringUtils.reverseAsArray(arr);
            assertArrayEquals(new Object[]{"c", "b", "a"}, arr);
        }
    }

    @Nested
    @DisplayName("lpad/rpad 메서드 테스트")
    class LpadRpadTests {

        @Test
        @DisplayName("lpad - String")
        void lpad_string() {
            assertEquals("00abc", StringUtils.lpad("abc", 5, '0'));
            assertEquals("00000", StringUtils.lpad(null, 5, '0'));
        }

        @Test
        @DisplayName("lpad - 숫자 타입들")
        void lpad_numbers() {
            assertEquals("00123", StringUtils.lpad((short) 123, 5, '0'));
            assertEquals("00123", StringUtils.lpad(123, 5, '0'));
            assertEquals("00123", StringUtils.lpad(123L, 5, '0'));
            assertEquals("01.5", StringUtils.lpad(1.5f, 4, '0'));
            assertEquals("01.5", StringUtils.lpad(1.5d, 4, '0'));
        }

        @Test
        @DisplayName("rpad - String")
        void rpad_string() {
            assertEquals("abc00", StringUtils.rpad("abc", 5, '0'));
            assertEquals("00000", StringUtils.rpad(null, 5, '0'));
            assertEquals("abcde", StringUtils.rpad("abcde", 3, '0'));
        }

        @Test
        @DisplayName("rpad - 숫자 타입들")
        void rpad_numbers() {
            assertEquals("12300", StringUtils.rpad((short) 123, 5, '0'));
            assertEquals("12300", StringUtils.rpad(123, 5, '0'));
            assertEquals("12300", StringUtils.rpad(123L, 5, '0'));
            assertEquals("1.50", StringUtils.rpad(1.5f, 4, '0'));
            assertEquals("1.50", StringUtils.rpad(1.5d, 4, '0'));
        }
    }

    @Nested
    @DisplayName("split 메서드 테스트")
    class SplitTests {

        @Test
        @DisplayName("split - 기본 공백")
        void split() {
            String[] result = StringUtils.split("a b c");
            assertArrayEquals(new String[]{"a", "b", "c"}, result);
        }

        @Test
        @DisplayName("split - char 구분자")
        void split_char() {
            String[] result = StringUtils.split("a,b,c", ',');
            assertArrayEquals(new String[]{"a", "b", "c"}, result);
        }

        @Test
        @DisplayName("split - String 구분자")
        void split_string() {
            String[] result = StringUtils.split("a=b&b=c&c=d", "&");
            assertArrayEquals(new String[]{"a=b", "b=c", "c=d"}, result);
        }

        @Test
        @DisplayName("split - 횟수 제한")
        void split_max() {
            String[] result = StringUtils.split("a,b,c,d", ",", 2);
            assertEquals(3, result.length);
        }
    }

    @Nested
    @DisplayName("tokenize 메서드 테스트")
    class TokenizeTests {

        @Test
        @DisplayName("tokenize - 기본")
        void tokenize() {
            String[] result = StringUtils.tokenize("a=b&b=c", "=&");
            assertArrayEquals(new String[]{"a", "b", "b", "c"}, result);
        }

        @Test
        @DisplayName("tokenize - 횟수 제한")
        void tokenize_max() {
            String[] result = StringUtils.tokenize("a=b&b=c", "=&", 2);
            assertEquals(2, result.length);
        }
    }

    @Test
    @DisplayName("splitAsList")
    void splitAsList() {
        List<String> result = StringUtils.splitAsList("a,b,c", ",");
        assertEquals(3, result.size());

        List<String> limited = StringUtils.splitAsList("a,b,c,d", ",", 2);
        assertEquals(3, limited.size());
    }

    @Nested
    @DisplayName("join 메서드 테스트")
    class JoinTests {

        @Test
        @DisplayName("join - 기본")
        void join() {
            assertEquals("a b c", StringUtils.join(new String[]{"a", "b", "c"}));
        }

        @Test
        @DisplayName("join - char 구분자")
        void join_char() {
            assertEquals("a,b,c", StringUtils.join(new String[]{"a", "b", "c"}, ','));
        }

        @Test
        @DisplayName("join - String 구분자")
        void join_string() {
            assertEquals("a&b&c", StringUtils.join(new String[]{"a", "b", "c"}, "&"));
        }

        @Test
        @DisplayName("join - null")
        void join_null() {
            assertNull(StringUtils.join((Object[]) null, "&"));
        }

        @Test
        @DisplayName("join - 빈 배열")
        void join_empty() {
            assertEquals("", StringUtils.join(new String[]{}, "&"));
        }
    }

    @Test
    @DisplayName("repeat")
    void repeat() {
        assertEquals("11111", StringUtils.repeat("1", 5));
        assertNull(StringUtils.repeat(null, 5));
    }

    @Test
    @DisplayName("repeat - StringBuffer")
    void repeat_stringBuffer() {
        StringBuffer sb = new StringBuffer();
        StringUtils.repeat(sb, 'x', 3);
        assertEquals("xxx", sb.toString());

        StringBuffer sb2 = new StringBuffer();
        StringUtils.repeat(sb2, (String) null, 3);
        assertEquals("", sb2.toString());
    }

    @Nested
    @DisplayName("insert 메서드 테스트")
    class InsertTests {

        @Test
        @DisplayName("insertRight - long")
        void insertRight_long() {
            assertEquals("1.234", StringUtils.insertRight(1234L, 3, '.'));
        }

        @Test
        @DisplayName("insertRight - String")
        void insertRight_string() {
            assertEquals("02-03-11", StringUtils.insertRight("020311", 2, "-"));
            assertNull(StringUtils.insertRight((String) null, 2, "-"));
        }

        @Test
        @DisplayName("insertLeft")
        void insertLeft() {
            assertEquals("12-34-56", StringUtils.insertLeft("123456", 2, "-"));
            assertNull(StringUtils.insertLeft((String) null, 2, "-"));
        }

        @Test
        @DisplayName("insert - 방향 지정")
        void insert_withDirection() {
            assertEquals("12-34-56", StringUtils.insert("123456", 2, "-", StringUtils.LEFT));
            assertEquals("12-34-56", StringUtils.insert("123456", 2, "-", StringUtils.RIGHT));
            assertNull(StringUtils.insert("123456", 2, "-", 99));
        }
    }

    @Nested
    @DisplayName("strip 메서드 테스트")
    class StripTests {

        @Test
        @DisplayName("strip")
        void strip() {
            assertEquals("cd", StringUtils.strip("abcdab", "ab"));
        }

        @Test
        @DisplayName("stripStart")
        void stripStart() {
            assertEquals("ef", StringUtils.stripStart("abcdef", "abcd"));
            // null prefix인 경우 앞쪽 공백 제거
            assertEquals("abc  ", StringUtils.stripStart("  abc  ", null));
            assertEquals("", StringUtils.stripStart("abc", "abc"));
            assertNull(StringUtils.stripStart(null, "abc"));
        }

        @Test
        @DisplayName("stripEnd")
        void stripEnd() {
            assertEquals("abcd", StringUtils.stripEnd("abcdef", "ef"));
            assertEquals("  abc", StringUtils.stripEnd("  abc  ", null));
            assertEquals("", StringUtils.stripEnd("abc", "abc"));
            assertNull(StringUtils.stripEnd(null, "abc"));
        }
    }

    @Nested
    @DisplayName("chomp 메서드 테스트")
    class ChompTests {

        @Test
        @DisplayName("chompFirst")
        void chompFirst() {
            assertEquals("de", StringUtils.chompFirst("abcd\\cdef\\de", "\\"));
            assertNull(StringUtils.chompFirst(null, "\\"));
        }

        @Test
        @DisplayName("chompFirst - max")
        void chompFirst_max() {
            assertEquals("\\cdef\\de", StringUtils.chompFirst("abcd\\cdef\\de", "\\", 1));
        }

        @Test
        @DisplayName("chompLast")
        void chompLast() {
            assertEquals("abcd", StringUtils.chompLast("abcd\\cdef\\de", "\\"));
            assertNull(StringUtils.chompLast(null, "\\"));
        }

        @Test
        @DisplayName("chompLast - max")
        void chompLast_max() {
            assertEquals("abcd\\cdef", StringUtils.chompLast("abcd\\cdef\\de", "\\", 1));
        }

        @Test
        @DisplayName("chomp - 방향 지정")
        void chomp_withDirection() {
            assertEquals("abcd", StringUtils.chomp("abcd\\cdef", "\\", -1, StringUtils.LEFT));
            assertEquals("cdef", StringUtils.chomp("abcd\\cdef", "\\", -1, StringUtils.RIGHT));
            assertNull(StringUtils.chomp("abcd\\cdef", "\\", -1, 99));
        }

        @Test
        @DisplayName("chopNewline")
        void chopNewline() {
            assertEquals("abc", StringUtils.chopNewline("abc\r\n"));
            assertEquals("abc", StringUtils.chopNewline("abc\n"));
            assertEquals("abc", StringUtils.chopNewline("abc\r"));
            assertEquals("abc", StringUtils.chopNewline("abc"));
            assertNull(StringUtils.chopNewline(null));
        }
    }

    @Test
    @DisplayName("substring")
    void substring() {
        assertEquals("cd", StringUtils.substring("abcdef", "ab", "ef"));
        assertNull(StringUtils.substring(null, "ab", "ef"));
        assertNull(StringUtils.substring("abcdef", "xy", "ef"));
        assertNull(StringUtils.substring("abcdef", "ab", "xy"));
    }

    @Nested
    @DisplayName("indexOf 메서드 테스트")
    class IndexOfTests {

        @Test
        @DisplayName("indexOf - 배열 (첫 번째 발견된 요소의 위치 반환)")
        void indexOf_array() {
            // 메서드는 배열 순서대로 검색하여 첫 번째 발견된 요소의 위치를 반환
            int result = StringUtils.indexOf("abcd", new String[]{"bc", "ab"});
            assertEquals(1, result); // "bc"가 먼저 검색되어 위치 1 반환

            // 배열 순서 변경 시 결과 변경
            int result2 = StringUtils.indexOf("abcd", new String[]{"ab", "bc"});
            assertEquals(0, result2); // "ab"가 먼저 검색되어 위치 0 반환
        }

        @Test
        @DisplayName("indexOf - null")
        void indexOf_null() {
            assertEquals(-1, StringUtils.indexOf(null, new String[]{"bc"}));
        }

        @Test
        @DisplayName("indexOf - 없는 경우")
        void indexOf_notFound() {
            assertEquals(-1, StringUtils.indexOf("abcd", new String[]{"xy", "yz"}));
        }

        @Test
        @DisplayName("lastIndexOf - 배열")
        void lastIndexOf_array() {
            int result = StringUtils.lastIndexOf("abcd", new String[]{"ab", "bc"});
            assertEquals(1, result);
        }

        @Test
        @DisplayName("lastIndexOf - null")
        void lastIndexOf_null() {
            assertEquals(-1, StringUtils.lastIndexOf(null, new String[]{"ab"}));
        }
    }

    @Test
    @DisplayName("countMatches")
    void countMatches() {
        assertEquals(3, StringUtils.countMatches("abcabcabc", "abc"));
        assertEquals(0, StringUtils.countMatches(null, "abc"));
    }

    @Test
    @DisplayName("translate")
    void translate() {
        assertEquals("jelly", StringUtils.translate("hello", "ho", "jy"));
        assertNull(StringUtils.translate(null, "ho", "jy"));
    }

    @Test
    @DisplayName("align")
    void align() {
        byte[] leftResult = StringUtils.align("abc", 5, true, ' ');
        assertEquals(5, leftResult.length);
        assertEquals("abc  ", new String(leftResult));

        byte[] rightResult = StringUtils.align("abc", 5, false, ' ');
        assertEquals("  abc", new String(rightResult));

        byte[] nullResult = StringUtils.align(null, 5, true, ' ');
        assertEquals(0, nullResult.length);

        byte[] truncatedLeft = StringUtils.align("abcdefgh", 5, true, ' ');
        assertEquals(5, truncatedLeft.length);

        byte[] truncatedRight = StringUtils.align("abcdefgh", 5, false, ' ');
        assertEquals(5, truncatedRight.length);
    }

    @Test
    @DisplayName("fillSpace")
    void fillSpace() {
        assertEquals("abc  ", StringUtils.fillSpace("abc", 5));
        assertEquals("abc", StringUtils.fillSpace("abcde", 3));
    }

    @Test
    @DisplayName("fillZero")
    void fillZero() {
        assertEquals("00abc", StringUtils.fillZero("abc", 5));
        assertEquals("cde", StringUtils.fillZero("abcde", 3));
    }

    @Test
    @DisplayName("concatPath")
    void concatPath() {
        String sep = java.io.File.separator;
        assertEquals("a" + sep + "b", StringUtils.concatPath("a", "b"));
        assertEquals("a" + sep + "b", StringUtils.concatPath("a" + sep, sep + "b"));
        assertEquals("b", StringUtils.concatPath(null, "b"));
        assertEquals("a", StringUtils.concatPath("a", null));
        assertEquals("b", StringUtils.concatPath("", "b"));
        assertEquals("a", StringUtils.concatPath("a", ""));
    }

    @Test
    @DisplayName("nvl")
    void nvl() {
        assertEquals("abc", StringUtils.nvl("abc", "default"));
        assertEquals("default", StringUtils.nvl(null, "default"));
    }

    @Test
    @DisplayName("getHex/getHexUpper")
    void getHex() {
        byte[] bytes = "AB".getBytes();
        String hex = StringUtils.getHex(bytes);
        assertNotNull(hex);

        String hexUpper = StringUtils.getHexUpper(bytes);
        assertNotNull(hexUpper);
    }

    @Test
    @DisplayName("decodeHex")
    void decodeHex() {
        byte[] hex = "4142".getBytes();
        byte[] result = StringUtils.decodeHex(hex);
        assertEquals("AB", new String(result));

        // 홀수 길이
        assertNull(StringUtils.decodeHex("414".getBytes()));
    }

    @Test
    @DisplayName("makeWithHangulLen")
    void makeWithHangulLen() {
        String[] result = StringUtils.makeWithHangulLen("Hello안녕World", 10, "UTF-8");
        assertNotNull(result);

        String[] nullResult = StringUtils.makeWithHangulLen(null, 10, "UTF-8");
        assertNull(nullResult);

        String[] shortResult = StringUtils.makeWithHangulLen("Hi", 10, "UTF-8");
        assertEquals(1, shortResult.length);
    }

    @Nested
    @DisplayName("addSOSI/delSOSI 메서드 테스트")
    class SOSITests {

        @Test
        @DisplayName("addSOSI - 기본")
        void addSOSI() {
            String result = StringUtils.addSOSI("Test");
            assertNotNull(result);
            assertTrue(result.length() > 4);
        }

        @Test
        @DisplayName("addSOSI - 크기 지정")
        void addSOSI_withSize() {
            String result = StringUtils.addSOSI("TestData", 10);
            assertNotNull(result);
        }

        @Test
        @DisplayName("addSOSI - 빈 문자열")
        void addSOSI_empty() {
            assertEquals("   ", StringUtils.addSOSI("   "));
        }

        @Test
        @DisplayName("delSOSI")
        void delSOSI() {
            String original = StringUtils.addSOSI("Test");
            String result = StringUtils.delSOSI(original);
            assertEquals("Test", result);
        }
    }

    @Test
    @DisplayName("toHalfChar")
    void toHalfChar() {
        assertEquals("ABC", StringUtils.toHalfChar("ＡＢＣ"));
        assertEquals("123", StringUtils.toHalfChar("１２３"));
    }

    @Test
    @DisplayName("toFullChar")
    void toFullChar() {
        assertEquals("ＡＢＣ", StringUtils.toFullChar("ABC"));
        assertEquals("１２３", StringUtils.toFullChar("123"));
    }

    @Test
    @DisplayName("commaDelimitedListToSet")
    void commaDelimitedListToSet() {
        Set<String> result = StringUtils.commaDelimitedListToSet("a, b, c");
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));

        assertTrue(StringUtils.commaDelimitedListToSet(null).isEmpty());
        assertTrue(StringUtils.commaDelimitedListToSet("").isEmpty());
        assertTrue(StringUtils.commaDelimitedListToSet("   ").isEmpty());
    }

    @Test
    @DisplayName("printHex - 콘솔 출력 확인")
    void printHex() {
        // 출력만 확인 (예외 없이 실행되면 성공)
        assertDoesNotThrow(() -> StringUtils.printHex("test".getBytes()));
        assertDoesNotThrow(() -> StringUtils.printHex("label", "test".getBytes()));
    }
}
