package com.scbank.process.api.fw.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;

/**
 * @author sungdon.choi
 */
@SuppressWarnings("deprecation")
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final String SPACE = " ";

    public static final String ZERO = "0";
    /**
     * 좌측을 나타낸다
     */
    public static final int LEFT = 1;

    /**
     * 가운데를 나타낸다
     */
    public static final int CENTER = 2;

    /**
     * 우측을 나타낸다
     */
    public static final int RIGHT = 3;

    /**
     * @param str
     * @return
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * @param str
     * @return
     */
    public static boolean hasLength(@Nullable CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * @param str
     * @return
     */
    public static boolean hasText(@Nullable CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * @param str
     * @return
     */
    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 정상적인지 않는 문자는 '스페이스'처리한다.
     *
     * @param str 원 문자열
     * @return
     */
    public static String getLetterOrDigitOtherSpace(@Nullable String str) {
        if (str == null) {
            return "";
        }

        char[] chs = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chs) {
            /* space ~ 0x7e(~) */
            if (Character.isLetterOrDigit(ch) || (ch >= 0x20 && ch <= 0x7e)) {
                sb.append(ch);
            } else {
                sb.append(' ');
            }
        }

        return sb.toString();
    }

    /**
     * 문자열의 길이가 1이상이면 true이다. 이때 문자열은 java.lang.String.trim()되지 않은 길이로 판단된다.
     *
     * <blockquote>
     *
     * <pre>
     * boolean b = StringUtil.isValid("  "); // true
     * String str = null;
     * beolean b1 = StringUtil.isEmpty(str); // false
     * </pre>
     *
     * </blockquote>
     *
     * @see #isValid(String, int)
     */
    public static boolean isValid(String str) {
        return isValid(str, 1);
    }

    /**
     * 문자열이 특정 길이보다 크면 true를 리턴한다. 이때 문자열은 java.String.trim()되지 않은 길이로 판단된다.
     *
     * @param str 체크하고자 하는 문자열을 나타낸다.
     * @param len 체크하고자 하는 길이를 나타낸다.
     * @return 문자열이 비어있으면 <code>true</code>를 리턴하고 비어있지 않다면 <code>false</code>를 리턴한다.
     *         문자열이
     *         <code>null</code>이면 <code>false</code>를 리턴한다
     */
    public static boolean isValid(String str, int len) {
        return (str != null && str.length() >= len);
    }

    /**
     * 유니코드 단어인지를 나타낸다.
     *
     * @return 단어라면 <code>true</code>를 리턴하고 아니라면 <code>false</code>를 리턴한다. 문자열이
     *         <code>null</code>이면 <code>false</code>를 리턴한다
     */
    public static boolean isWord(String str) {
        if (null == str) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 유니코드 숫자나 문자인지를 나타낸다.
     *
     * @return 숫자나 문자라면 <code>true</code>를 리턴하고 아니라면 <code>false</code>를 리턴한다. 문자열이
     *         <code>null</code>이면 <code>false</code>를 리턴한다
     */
    public static boolean isAlphanumeric(String str) {
        if (null == str) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 유니코드 숫자인지를 나타낸다.
     *
     * @return 숫자라면 <code>true</code>를 리턴하고 아니라면 <code>false</code>를 리턴한다. 문자열이
     *         <code>null</code>이면 <code>false</code>를 리턴한다
     */
    public static boolean isNumeric(String str) {
        if (null == str || str.length() == 0) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * reqular expression의 meta sybmol를 quotation처리한다. 문자열이 null이면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * 		String str = StringUtil.quoteRegularExpression("^a[bc]");
     * 		output --> "\^a\[bc\]"
     * </pre>
     *
     * </blockquote>
     */
    public static String quoteRegularExpression(String str) {
        if (null == str) {
            return null;
        }
        char[] chars = str.toCharArray();
        int len = chars.length;
        StringBuffer sb = new StringBuffer(2 * len);
        for (int i = 0; i < len; i++) {
            switch (chars[i]) {
                case '[':
                case ']':
                case '?':
                case '+':
                case '*':
                case '/':
                case '.':
                case '^':
                case '$':
                    sb.append("\\");
                default:
                    sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 공백을 기준으로 하는 토큰의 갯수
     *
     * @param str 워드의 갯수를 판단한 target
     */
    public static int getWordCount(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        return tokenizer.countTokens();
    }

    /**
     * 특정 delim를 기준으로 하는 토큰의 갯수
     *
     * @param str 워드 의 갯수를 판단한 target
     */
    public static int getWordCount(String str, String delim) {
        StringTokenizer tokenizer = new StringTokenizer(str, delim);
        return tokenizer.countTokens();
    }

    // -------------------------------------------------------------- padding

    /**
     * java.lang.String.trim()이 문자열이 <code>null</code>인 경우
     * <code>NullPointException</code> 던지지만 이 메소드는 <codE>null</code>를 리턴한다
     *
     * @see #clean(String str)
     */
    public static String trim(String str) {
        return trim(str, null);
    }

    /**
     * java.lang.String.trim()과 같이 공백을 제거해주고 문자열이 null인 경우는 빈 문자열을 리턴한다.
     */
    public static String clean(String str) {
        return trim(str, "");
    }

    /**
     * 문자열의 공백을 제거한다. 문자열이 null이라면 default value를 리턴한다.
     *
     * @param str trim하고자 하는 string을 나타낸다
     * @param def default value를 나타낸다
     */
    public static String trim(String str, String def) {
        return (null == str ? def : str.trim());
    }

    /**
     * 만약 string이 null이라면 빈문자열을 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.parseString(null);
     * // output --> ""
     * </pre>
     *
     * </blockquote>
     *
     * @see #parseString(String, String)
     */
    public static String parseString(String str) {
        return parseString(str, "");
    }

    /**
     * 만약 String이 null이라면 default value를 리턴한다
     */
    public static String parseString(String str, String defaultValue) {
        return (str == null ? defaultValue : str);
    }

    /**
     * 앞쪽의 공백을 제거한다. 만약 문자열이 null이라면 null를 리턴한다.
     *
     * @see #stripStart(String, String)
     */
    public static String ltrim(String str) {
        return stripStart(str, null);
    }

    /**
     * 뒤쪽의 공백을 제거한다. 만약 문자열이 null이라면 null를 리턴한다.
     *
     * @see #stripEnd(String, String)
     */
    public static String rtrim(String str) {
        return stripEnd(str, null);
    }

    /**
     * padding를 나타낸다.
     *
     * @param direction 왼쪽, 오른쪽, 가운데를 나타낸다. 값은 LEFT, CENTER, RIGHT 방향이 틀리면 단순히
     *                  string을 리턴한다
     * @see #left(String, int, String)
     * @see #center(String, int, String)
     * @see #right(String, int, String)
     */
    public static String padding(String str, int n, String delim, int direction) {
        if (direction == LEFT) {
            return left(str, n, delim);
        } else if (direction == CENTER) {
            return center(str, n, delim);
        } else if (direction == RIGHT) {
            return right(str, n, delim);
        }
        return str;
    }

    // public static String padding(String str, int n, String delim, AlignType type,
    // boolean masking) {
    // if (type == AlignType.LEFT) {
    // return left(str, n, delim);
    // } else if (type == AlignType.RIGHT) {
    // return right(str, n, delim);
    // }
    // return str;
    // }

    /**
     * 특정 길이보다 문자열의 길이가 작다면 좌측에 공백을 넣어준다.
     *
     * @see #left(String, int, String)
     */
    public static String left(String str, int n) {
        return left(str, n, ' ');
    }

    /**
     * 문자열이 특정 길이보다 작다면 좌측에 delimiter 문자를 삽입해 준다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.left("abcd", 6, ' '); // output --> " abcd"
     * </pre>
     *
     * </blockquote>
     *
     * @see #left(String, int, String)
     */
    public static String left(String str, int n, char delim) {
        return left(str, n, delim + "");
    }

    /**
     * 문자열이 특정 길이보다 작다면 좌측에 delimiter를 삽입해 준다. 문자열이 null이라면 delimiter로 모두 채운 후에
     * 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = Stringutil.left("1234", 7, "%&amp;");
     * // output --> "%&amp;1234"
     * </pre>
     *
     * </blockquote>
     */
    public static String left(String str, int n, String delim) {
        if (null == str) {
            str = "";
        }
        int len = str.length();
        n = (n - len) / delim.length();
        if (n > 0) {
            StringBuffer sb = new StringBuffer(len + n);
            repeat(sb, delim, n);
            sb.append(str);
            return sb.toString();
        } else {
            return str;
        }
    }

    /**
     * 특정 길이보다 string의 길이가 작다면 우측에 공백을 넣어준다.
     *
     * @see #left(String, int, String)
     */
    public static String right(String str, int n) {
        return right(str, n, ' ');
    }

    /**
     * 문자열이 특정 길이보다 작다면 우측에 delimiter 문자를 넣어준다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.right("abcd", 6, ' '); // output --> "abcd "
     * </pre>
     *
     * </blockquote>
     *
     * @see #right(String, int, String)
     */
    public static String right(String str, int n, char delim) {
        return right(str, n, delim + "");
    }

    /**
     * 문자열이 특정 길이보다 작다면 우측에 delimiter 문자를 넣어준다. 문자열이 null이라면 delimiter로 모두 채운 후에
     * 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = Stringutil.right("1234", 7, "%&amp;");
     * // output --> "1234%&amp;"
     * </pre>
     *
     * </blockquote>
     */
    public static String right(String str, int n, String delim) {
        if (null == str) {
            str = "";
        }
        int len = str.length();
        n = (n - str.length()) / delim.length();
        if (n > 0) {
            StringBuffer sb = new StringBuffer(n + len);
            sb.append(str);
            repeat(sb, delim, n);
            return sb.toString();
        } else {
            return str;
        }
    }

    /**
     * 특정 길이보다 string의 길이가 작다면 좌우에 공백을 넣어준다.
     *
     * @see #left(String, int, String)
     */
    public static String center(String str, int n) {
        return center(str, n, ' ');
    }

    /**
     * 문자열이 특정 길이보다 작다면 좌우에 delimiter 문자를 삽입해 준다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.center("abcd", 6, ' '); // output --> " abcd "
     * String str = StringUtil.center("abcd", 7, ' '); // output --> " abcd "
     * </pre>
     *
     * </blockquote>
     */
    public static String center(String str, int n, char delim) {
        return center(str, n, delim + "");
    }

    /**
     * 문자열이 특정 길이보다 작다면 좌우에 delimiter를 삽입해 준다. 만약 여백에 이등분이 안된다면 right에 채운다. 모자라면 채우지
     * 않고 남겨 둔다. 문자열이 null이라면 delimiter로
     * 무두 채운 후에 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = Stringutil.center("1234", 7, "%&amp;");
     * // output --> "1234%&amp;"
     * </pre>
     *
     * </blockquote>
     */
    public static String center(String str, int n, String delim) {
        if (null == str) {
            str = "";
        }
        int len = str.length();
        int i = (n - str.length()) / delim.length();
        if (i > 0) {
            str = left(str, len + i / 2, delim);
            str = right(str, n, delim);
        }
        return str;
    }

    // ------------------------------------------------------------- replacement

    /**
     * 특정 글자를 특정 문자열로 바꿔줍니다.
     * <p>
     * 예를 들어, 특정 글자를 escape 처리하고자 할 때 이용할 수 있다.<br>
     * <blockquote>
     *
     * <pre>
     * pstmt = con.prepareStatement(...);
     * pstmt.setString(1, SringUtil.replace("IMS'S HOPE", '\'', "''"));
     * </pre>
     *
     * </blockquote>
     *
     * @param source  원본 문자열이다.
     * @param ch      바꾸고자 하는 글자이다.
     * @param replace 대치하고자 하는 문자열이다.
     * @see #replace(String source, char ch, String replace, int max)
     */
    public static String replace(String source, char ch, String replace) {
        return replace(source, ch, replace, -1);
    }

    /**
     * 특정 글자를 특정 문자열로 바꿔줍니다.
     *
     * @param source  원본 문자열이다.
     * @param ch      바꾸고자 하는 글자이다.
     * @param replace 대치하고자 하는 문자열이다.
     * @param max     몇번 바꿀 것인지를 나타낸다. <code>-1</code>이면 모두 바꾼다
     * @see #replace(String source, String original, String replace, int max)
     */
    public static String replace(String source, char ch, String replace, int max) {
        return replace(source, ch + "", replace, max);
    }

    /**
     * 특정 문자열을 특정 문자열로 바꿔줍니다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.replace("Java \r\n is \r\n Wonderful", "\r\n", "<BR>");
     * </pre>
     *
     * </blockquote>
     *
     * @param source   원본 문자열이다.
     * @param original 바꾸고자 하는 문자열입니다.
     * @param replace  대치하고자 하는 문자열이다.
     */
    public static String replace(String source, String original, String replace) {
        return replace(source, original, replace, -1);
    }

    /**
     * 특정 문자열을 특정 문자열로 바꿔줍니다. 문자열이 null이라면 null를 리턴한다.
     *
     * @param source   원본 문자열이다.
     * @param original 바꾸고자 하는 문자열입니다.
     * @param replace  대치하고자 하는 문자열이다.
     * @param max      몇번 바꿀것인지를 나타낸다. <code>-1</code>이면 모두 바꾼다.
     */
    public static String replace(String source, String original, String replace, int max) {
        if (null == source) {
            return null;
        }
        int nextPos = 0; // 다음 position
        int currentPos = 0; // 현재 position
        int len = original.length();
        StringBuffer result = new StringBuffer(source.length());
        while ((nextPos = source.indexOf(original, currentPos)) != -1) {
            result.append(source, currentPos, nextPos);
            result.append(replace);
            currentPos = nextPos + len;
            if (--max == 0) { // 바꿀 횟수를 줄어준다
                break;
            }
        }
        if (currentPos < source.length()) {
            result.append(source.substring(currentPos));
        }
        return result.toString();
    }

    /**
     * String을 특정 범위를 다른 문자로 대치한다. 문자열이 null이라면 null를 리턴하다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.overlay("Java", 'V', 2, 3);
     * // output --> "JaVa"
     * </pre>
     *
     * </blockquote>
     *
     * @param ch    대치하고자 하는 문자를 나타낸다
     * @param ch    대치 하고자 하는 문자
     * @param start 대치하고자 문자열의 시작 index를 나타낸다
     * @param start 대치하고자 문자열의 종료 index를 나타낸다
     */
    public static String overlay(String str, char ch, int start, int end) {
        return overlay(str, ch + "", start, end);
    }

    /**
     * String을 특정 범위를 다른 문자열으로 대치한다. 문자열이 null이라면 null를 리턴하다.
     *
     * @throws IndexOutOfBoundsException start, end의 index가 범위를 벗어나면 발생한다
     */
    public static String overlay(String str, String overlay, int start, int end) {
        if (null == str) {
            return null;
        }
        String pre = str.substring(0, start);
        String post = str.substring(end);
        return pre + overlay + post;
    }

    /**
     * String에 특정 key를 값으로 바꾼다. 만약 text가 null이라면 null를 리턴하고 map이 null이라면 원본 문자열을
     * 리턴한다. 예를 들어 xml에서 특정 속성을 읽어서 String의
     * 부분을 바꾸고자 할때 이용한다.
     *
     * <blockquote>
     *
     * <pre>
     * String text = "${ip} ${port}";
     * Map map = HashMap();
     * map.put("ip", "203.252.157.66");
     * map.put("port", "2002");
     * String str = StringUtil.interpolate(text, map);
     * </pre>
     *
     * </blockquote>
     */
    public static String interpolate(String text, Map<String, String> map) {
        if (null == text) {
            return null;
        }

        if (null == map) {
            return text;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            text = replace(text, "${" + key + "}", value);
        }
        return text;
    }

    // ----------------------- replacement/case

    /**
     * 첫 글자만 대문자로 바꾸어 준다. 입력 String이 null이라면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.toFirstUpperCase("java"); // result --> "Java"
     * </pre>
     *
     * </blockquote>
     */
    public static String toFirstUpperCase(String str) {
        return (null == str ? null : str.substring(0, 1).toUpperCase() + str.substring(1));
    }

    /**
     * 특정 길이만큼 글자를 대문자로 바꾸어 준다. 입력 String이 null이라면 null 를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.toUpperCase("java", 4) // result -> "JAVA"
     * </pre>
     *
     * </blockquote>
     */
    public static String toUpperCase(String str, int len) {
        if (null == str) {
            return null;
        }
        int strLen = str.length();
        int index = 0;
        StringBuffer sb = new StringBuffer(str.length());
        while ((index < len) && (index < strLen)) {
            sb.append(Character.toUpperCase(str.charAt(index)));
            ++index;
        }
        if (index < strLen) {
            sb.append(str.substring(index));
        }
        return sb.toString();
    }

    /**
     * Character.toTitleCase()를 이용하여 대문자 한다
     *
     * @see #toUpperCase(String, int)
     */
    public static String toTitleCase(String str, int len) {
        if (null == str) {
            return null;
        }

        int strLen = str.length();
        int index = 0;
        StringBuffer sb = new StringBuffer(str.length());
        while ((index < len) && (index < strLen)) {
            sb.append(Character.toTitleCase(str.charAt(index)));
            ++index;
        }
        if (index < strLen) {
            sb.append(str.substring(index));
        }
        return sb.toString();
    }

    /**
     * 첫 글자만 소문자로 바꾸어 준다. 입력 String이 null이라면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.toFirstLowerCase("Java"); // result --> "java"
     * </pre>
     *
     * </blockquote>
     */
    public static String toFirstLowerCase(String str) {
        return (null == str ? null : str.substring(0, 1).toLowerCase() + str.substring(1));
    }

    /**
     * 특정 길이만큼 글자를 소문자로 바꾸어 준다. 입력 String이 null이라면 null 를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.toLowerCase("JAVA", 4) // result -> "java"
     * </pre>
     *
     * </blockquote>
     */
    public static String toLowerCase(String str, int len) {
        if (null == str) {
            return null;
        }
        int strLen = str.length();
        int index = 0;
        StringBuffer sb = new StringBuffer(str.length());
        while ((index < len) && (index < strLen)) {
            sb.append(Character.toLowerCase(str.charAt(index)));
            ++index;
        }
        if (index < strLen) {
            sb.append(str.substring(index));
        }
        return sb.toString();
    }

    /**
     * 문자열의 대소문자를 바꾸어 준다. 문자열이 null이라면 null를 리턴한다
     */
    public static String swapCase(String str) {
        if (null == str) {
            return null;
        }
        int size = str.length();
        StringBuffer sb = new StringBuffer(size);

        char ch = 0;
        char tmp = 0;
        boolean whitespace = false;
        for (int i = 0; i < size; i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch) || Character.isTitleCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                if (whitespace) {
                    tmp = Character.toTitleCase(ch);
                } else {
                    tmp = Character.toUpperCase(ch);
                }
            }
            sb.append(tmp);
            whitespace = Character.isWhitespace(ch);
        }
        return sb.toString();
    }

    /**
     * 만약 string를 거꾸로 바꾸어 준다. null이라면 null를 리턴한다
     */
    public static String reverse(String str) {
        if (null == str) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    /**
     * delimeter에 구별되는 string을 반대로 바꾸어 준다. 만약 str이 null이면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.reverse("abcd.cdef", ".");
     * // output --> "cdef.abcd"
     * </pre>
     *
     * </blockquote>
     */
    public static String reverse(String str, String delim) {
        String[] strs = split(str, delim);
        if (strs == null) {
            return null;
        }
        reverseAsArray(strs);
        return join(strs, delim);
    }

    /**
     * Reverse as array.
     *
     * @param array
     * @return the object[]
     */
    public static Object[] reverseAsArray(Object[] array) {
        int i = 0;
        for (int j = array.length - 1; j > i; i++, j--) {
            Object tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
        return array;
    }

    /**
     * Returns value argument, left-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     * <p>
     * If the value argument is null, value argument think of empty string
     * (&quot;&quot;).
     *
     * @param value   a string value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return left padded string.
     */
    public static String lpad(String value, int padLen, char padChar) {
        if (value == null) {
            value = "";
        }

        while (value.length() < padLen) {
            value = padChar + value;
        }

        return value;
    }

    /**
     * Returns value argument, left-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a short value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return left padded string.
     */
    public static String lpad(short value, int padLen, char padChar) {
        return lpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, left-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a int value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return left padded string.
     */
    public static String lpad(int value, int padLen, char padChar) {
        return lpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, left-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a long value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return left padded string.
     */
    public static String lpad(long value, int padLen, char padChar) {
        return lpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, left-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a float value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return left padded string.
     */
    public static String lpad(float value, int padLen, char padChar) {
        return lpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, left-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a double value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return left padded string.
     */
    public static String lpad(double value, int padLen, char padChar) {
        return lpad(String.valueOf(value), padLen, padChar);
    }

    // ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

    /**
     * Returns value argument, right-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     * <p>
     * If the value argument is null, value argument think of empty string
     * (&quot;&quot;).
     *
     * @param value   a string value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return right padded string.
     */
    public static String rpad(String value, int padLen, char padChar) {
        if (value == null) {
            value = "";
        }

        if (value.length() >= padLen) {
            return value;
        }

        StringBuilder sb = new StringBuilder(padLen);
        sb.append(value);

        while (sb.length() < padLen) {
            sb.append(padChar);
        }

        return sb.toString();
    }

    /**
     * Returns value argument, right-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a short value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return right padded string.
     */
    public static String rpad(short value, int padLen, char padChar) {
        return rpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, right-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a int value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return right padded string.
     */
    public static String rpad(int value, int padLen, char padChar) {
        return rpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, right-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a long value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return right padded string.
     */
    public static String rpad(long value, int padLen, char padChar) {
        return rpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, right-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a float value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return right padded string.
     */
    public static String rpad(float value, int padLen, char padChar) {
        return rpad(String.valueOf(value), padLen, padChar);
    }

    /**
     * Returns value argument, right-padded to length padLen argument with the
     * sequence of character in padChar
     * argument.
     *
     * @param value   a double value.
     * @param padLen  the total length of the return value.
     * @param padChar padded character.
     * @return right padded string.
     */
    public static String rpad(double value, int padLen, char padChar) {
        return rpad(String.valueOf(value), padLen, padChar);
    }

    // ------------------------------------------------------------------- split

    /**
     * String을 공백으로 분리시켜 문자열을 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String[] strs = StringUtil.split("Java is wonderful");
     * // strs[0] = "Java"; strs[1] = "is"; strs[2] = "wonderful"
     * </pre>
     *
     * </blockquote>
     * <p>
     * #see split(String str, String delim)
     */
    public static String[] split(String str) {
        return split(str, " ", -1);
    }

    /**
     * String을 특정 글자로 분리하여 문자열 배열을 리턴한다.
     * <p>
     * #see split(String str, char delim, int max)
     */
    public static String[] split(String str, char delim) {
        return split(str, delim + "", -1);
    }

    /**
     * String을 특정 글자로 분리하여 문자열 배열을 리턴한다.
     *
     * @see #split(String str, String delim, int max)
     */
    public static String[] split(String str, char delim, int max) {
        return split(str, delim + "", max);
    }

    /**
     * String을 특정 문자열로 분리시켜 문자열 배열을 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String[] strs = StringUtil.split("a=b&amp;b=c&amp;c=d", "&amp;");
     * // strs[0] = "a=b"; strs[1] = "b=c"; strs[2] = "c=d"
     * </pre>
     *
     * </blockquote>
     *
     * @see #split(String str, String delim, int max)
     * @see #tokenize(String str, String delims)
     */
    public static String[] split(String str, String delim) {
        return split(str, delim, -1);
    }

    /**
     * String을 특정 문자열로 특정 횟수만큼 분리해서 문자열을 리턴한다.
     *
     * @param str   분리하고자 하는 문자열을 나타낸다.
     * @param delim 분리자를 나타낸다.
     * @param max   분리 횟수를 나타낸다. <code>-1</code>는 전체를 분리한다.
     */
    public static String[] split(String str, String delim, int max) {
        int nextPos = 0;
        int currentPos = 0;
        int len = delim.length();
        List<String> list = new ArrayList<>();
        while ((nextPos = str.indexOf(delim, currentPos)) != -1) {
            list.add(str.substring(currentPos, nextPos));
            currentPos = nextPos + len;
            if (--max == 0) { // 분리 횟수를 줄어준다
                break;
            }
        }
        if (currentPos < str.length()) {
            list.add(str.substring(currentPos));
        }
        return list.toArray(new String[0]);
    }
    /*
     * public static String[] split(String s, char c) { String as[] = {s,""}; int i
     * = s.indexOf(c); if(i != -1) { as[0] = s.substring(0,i); as[1] = s.substring(i
     * + 1); } return as; }
     *
     */

    /**
     * String을 특정 character들을 이용하여 분리시켜 문자열을 리턴한다. {@link #split(String, String)}
     * 메소드와 다르게 여러개의 delimeter character를 받아
     * tokenizing을 수행한다.
     *
     * <blockquote>
     *
     * <pre>
     * String[] strs = StringUtil.split("a=b&amp;b=c&amp;c=d", "=&amp;");
     * // strs[0] = "a"; strs[1] = "b"; strs[2] = "c"; ... strs[5] = "d";
     * </pre>
     *
     * </blockquote>
     *
     * @see #tokenize(String str, String delims, int max)
     * @see #split(String, String)
     */
    public static String[] tokenize(String str, String delims) {
        return tokenize(str, delims, -1);
    }

    /**
     * 문자열을 특정 character들을 이용하여 분리시켜 문자열 배열로 리턴한다.
     *
     * @param delims 구분자들을 나타낸다. 글자 단위이고 여러 글자가 가능하다.
     * @param max    분리 횟수를 나타낸다. <code>-1</code>는 전체를 분리한다.
     */
    public static String[] tokenize(String str, String delims, int max) {
        StringTokenizer st = new StringTokenizer(str, delims);
        int size = st.countTokens();
        if (max != -1 && size > max) {
            size = max;
        }
        String[] list = new String[size];
        int i = 0;
        while (st.hasMoreTokens()) {
            if ((max != -1) && (i == size - 1)) {
                break;
            } else {
                list[i] = st.nextToken();
            }
            i++;
        }
        return list;
    }

    /**
     * String을 특정 문자열로 분리해 List 리턴한다.
     *
     * @see #splitAsList(str, delim, max)
     */
    public static List<String> splitAsList(String str, String delim) {
        return splitAsList(str, delim, -1);
    }

    /**
     * String을 특정 문자열로 특정 횟수만큼 분리해 List 리턴한다.
     *
     * @param str   분리하고자 하는 문자열을 나타낸다
     * @param delim 분리자를 나타낸다.
     * @param max   분리 횟수를 나타낸다. <code>-1</code>는 전체를 분리한다.
     */
    public static List<String> splitAsList(String str, String delim, int max) {
        String[] strs = split(str, delim, max);
        return Arrays.asList(strs);
    }

    // -------------------------------------------------------------------- join

    /**
     * 객체 배열의 element들을 특정 " "를 이용하여 모두 연결시킨다. 객체 배열이 null이면 null를 리턴한다
     *
     * @see #join(Object[] list, String delim)
     */
    public static String join(Object[] list) {
        return join(list, " ");
    }

    /**
     * 객체 배열의 element들을 특정 delimiter 문자를 이용하여 모두 연결시킨다. 객체 배열이 null이면 null를 리턴한다
     *
     * @see #join(Object[] list, String delim)
     */
    public static String join(Object[] list, char delim) {
        return join(list, delim + "");
    }

    /**
     * 객체 배열의 element들을 특정 delimiter 문자열을 이용하여 모두 연결시킨다. 객체 배열이 null이면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * Strings[] strs = new Strings[] { "a=b", "b=c", "c=d" };
     * String str = StringUtil.join(strs, "&amp;"); // result --> "a=b&amp;b=c&amp;c=d"
     * </pre>
     *
     * </blockquote>
     */
    public static String join(Object[] list, String delim) {
        if (null == list) {
            return null;
        }
        int size = list.length;
        int len = (size == 0 ? 0 : (list[0].toString().length() + delim.length()) * size);
        StringBuffer sb = new StringBuffer(len);
        if (list.length > 0) {
            sb.append(list[0].toString());
        }
        for (int i = 1; i < size; i++) {
            sb.append(delim);
            sb.append(list[i]);
        }
        return sb.toString();
    }

    /**
     * pattern를 특정 횟수만큼 반복해서 더해준다. pattern이 null이면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.repeat("1", 5);
     * // output --> 11111
     * </pre>
     *
     * </blockquote>
     */
    public static String repeat(String pattern, int n) {
        if (null == pattern) {
            return null;
        }
        StringBuffer sb = new StringBuffer(n * pattern.length());
        repeat(sb, pattern, n);
        return sb.toString();
    }

    /**
     * Repeat.
     *
     * @param sb
     * @param ch
     * @param n
     */
    public static void repeat(StringBuffer sb, char ch, int n) {
        repeat(sb, ch + "", n);
    }

    /**
     * pattern를 특정 횟수만큼 반복해서 buffer에 넣어준다. buffer가 null이면 그냥 리턴된다
     */
    public static void repeat(StringBuffer sb, String pattern, int n) {
        if (null == pattern) {
            return;
        }
        for (int i = 0; i < n; i++) {
            sb.append(pattern);
        }
    }

    // ------------------------------------------------------------------ insert

    /**
     * 숫자의 pos 위치마다 char를 삽입해준다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.insert(1234, 3, '.');
     * // output --> "1.234"
     * </pre>
     *
     * </blockquote>
     *
     * @see #insertRight(String, int, String)
     */
    public static String insertRight(long l, int pos, char ch) {
        return insertRight(Long.toString(l), pos, ch + "");
    }

    /**
     * 문자열의 특정 위치에 특정 글자를 삽입해 준다.
     *
     * @see #insertRight(String, int, String)
     */
    public static String insertRight(String value, int pos, char ch) {
        return insertRight(value, pos, ch + "");
    }

    /**
     * 숫자의 특정 위치에 특정 문자열를 삽입해 준다.
     *
     * @see #insertRight(String, int, String)
     */
    public static String insertRight(long l, int pos, String sep) {
        return insertRight(Long.toString(l), pos, sep);
    }

    /**
     * 문자열의 특정 위치에 특정 문자열을 삽입해 준다. 문자열이 null이라면 단순히 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.insert("020311", 2, "-");
     * // output --> "02-03-01"
     * </pre>
     *
     * </blockquote>
     */
    public static String insertRight(String value, int pos, String sep) {
        if (null == value) {
            return null;
        }
        int len = value.length();
        StringBuffer sb = new StringBuffer(value);
        for (int i = len; i > pos; i -= pos) {
            sb.insert(i - pos, sep);
        }
        return sb.toString();
    }

    /**
     * 숫자의 특정 위치에 특정 문자열를 삽입해 준다.
     *
     * @see #insertLeft(String, int, String)
     */
    public static String insertLeft(long l, int pos, String sep) {
        return insertLeft(Long.toString(l), pos, sep);
    }

    /**
     * 문자열의 특정 위치에 특정 문자열을 삽입해 준다. 앞에서부터 삽입한다.
     */
    public static String insertLeft(String value, int pos, String sep) {
        if (null == value) {
            return null;
        }
        int len = value.length();
        StringBuffer sb = new StringBuffer(len);
        int currentPos = 0;
        int nextPos = 0;
        while ((nextPos = currentPos + pos) < len) {
            sb.append(value, currentPos, nextPos);
            sb.append(sep);
            currentPos = nextPos;
        }
        if (currentPos < len) {
            sb.append(value.substring(currentPos));
        }
        return sb.toString();
    }

    /**
     * 삽입을 앞쪽부터 수행할 것이지 뒤쪽부터 수행할 것인지를 결정하여 삽입해 준다
     *
     * @param direction LEFT나 RIGHT가 된다
     * @see #insertLeft(String, int, String)
     * @see #insertRight(String, int, String)
     */
    public static String insert(String value, int pos, String sep, int direction) {
        if (LEFT == direction) {
            return insertLeft(value, pos, sep);
        } else if (RIGHT == direction) {
            return insertRight(value, pos, sep);
        }
        return null;
    }

    // ----------------------------------------------------------------- extract

    /**
     * 문자열의 앞뒤에서 특정 문자열을 잘라낸다. 만약 문자열이 null이라면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.strip("abcdab", "ab");
     * // output --> "cd"
     * </pre>
     *
     * </blockquote>
     */
    public static String strip(String str, String delim) {
        str = stripStart(str, delim);
        return stripEnd(str, delim);
    }

    /**
     * 시작 부분의 매칭되는 substring을 strip한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.stripStart("abcdef", "abcd");
     * // output --> "ef"
     * </pre>
     *
     * </blockquote>
     *
     * @param prefix strip하고자 하는 문자열이다. null이라면 공백을 제거한다. 즉 {@link #ltrim(String)}과
     *               같은 역활을 수행한다.
     * @return String string이 null이라면 null를 리턴한다. 만약 string과 prefix가 같다면 빈문자열을 리턴한다
     */
    public static String stripStart(String str, String prefix) {
        if (str == null) {
            return str;
        }
        int start = 0;
        if (prefix == null) {
            while (Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (str.startsWith(prefix)) {
            if (str.equals(prefix)) // 만약 같다면 빈문자렬을 리턴
            {
                return "";
            }
            return str.substring(prefix.length());
        }
        return str.substring(start);
    }

    /**
     * 끝 부분의 매칭되는 substring을 strip한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.stripEnd("abcdef", "ef");
     * // output --> "abcd"
     * </pre>
     *
     * </blockquote>
     *
     * @param postfix strip하고자 하는 문자열이다. null이라면 공백을 제거한다. 즉 {@link #rtrim(String)}과
     *                같은 역활을 수행한다.
     * @return String Stirng이 null이라면 null를 리턴한다. string이 postfix와 같다면 빈문자열을 리턴한다
     */
    public static String stripEnd(String str, String postfix) {
        if (str == null) {
            return str;
        }
        int end = str.length();
        if (null == postfix) {
            while (Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (str.endsWith(postfix)) {
            if (str.equals(postfix)) // 만약 같다면 빈문자렬을 리턴
            {
                return "";
            }
            return str.substring(0, str.lastIndexOf(postfix));
        }
        return str.substring(0, end);
    }

    /**
     * string에서 특정 string의 이전 이후 것을 리턴한다
     *
     * @param direction 방향을 나타낸다. StringUtil.LEFT인 경우 chompLast()이고
     *                  StringUtil.RIGHT인 경우는 chompFirst()로 작동한다
     * @see #chompLast(String, String, int)
     * @see #chompFirst(String, String, int)
     */
    public static String chomp(String str, String sep, int max, int direction) {
        if (direction == LEFT) {
            return chompLast(str, sep, max);
        } else if (direction == RIGHT) {
            return chompFirst(str, sep, max);
        }
        return null;
    }

    /**
     * string에서 특정 string이 이전 것만 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.chompFirst("abcd\cdef\de", "\\");
     * output --> "de"
     * </pre>
     *
     * </blockquote>
     */
    public static String chompFirst(String str, String sep) {
        return chompFirst(str, sep, -1);
    }

    /**
     * string에서 특정 string이 이전 것만 리턴한다. string이 null이면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.chompFirst("abcd\cdef\de", "\\", 1);
     * output --> "cdef\de"
     * </pre>
     *
     * </blockquote>
     *
     * @param max chomp할 횟수를 나타낸다. 만약 -1이라면 모두 제거한다
     */
    public static String chompFirst(String str, String sep, int max) {
        if (null == str) {
            return null;
        }
        int index = str.indexOf(sep);
        if (index != -1) {
            if (--max != 0) { // chomp할 횟수를 줄어준다
                return chompFirst(str.substring(index + sep.length()), sep, max);
            }
            return str.substring(index);
        } else {
            return str;
        }
    }

    /**
     * string에서 특정 string의 이전 것만 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.chompLast("abcd\cdef\de", "\\");
     * output --> "abcd"
     * </pre>
     *
     * </blockquote>
     */
    public static String chompLast(String str, String sep) {
        return chompLast(str, sep, -1);
    }

    /**
     * string에서 특정 string의 이전 것만 리턴한다. string이 null이라면 null를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.chompLast("abcd\cdef\de", "\\", 1);
     * output --> "abcd\cdef"
     * </pre>
     *
     * </blockquote>
     *
     * @param max chomp할 횟수를 나타낸다. 만약 -1이라면 모두 제거한다
     */
    public static String chompLast(String str, String sep, int max) {
        if (null == str) {
            return null;
        }
        int index = str.lastIndexOf(sep);
        if (index != -1) {
            if (--max != 0) { // chomp할 횟수를 줄어준다
                return chompLast(str.substring(0, index), sep, max);
            }
            return str.substring(0, index);
        } else {
            return str;
        }
    }

    /**
     * String의 끝에서 \n나 \r\n, \r를 제거한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.v("abcd\r\n");
     * // output --> "abc"
     * </pre>
     *
     * </blockquote>
     */
    public static String chopNewline(String str) {
        if (null == str) {
            return null;
        }
        int index = str.length() - 1;
        char last = str.charAt(index);
        if (last == '\n') {
            if (str.charAt(index - 1) == '\r') {
                index--;
            }
        } else if (last != '\r') {
            return str;
        }
        return str.substring(0, index);
    }

    /**
     * 특정 문자열 사이에 있는 글자를 추출한다. matching되는 것이 없다면 null를 리턴한다. string이 null이면 null를
     * 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * String str = StringUtil.substring("abcdef", "ab", "ef");
     * // output --> "cd"
     * </pre>
     *
     * </blockquote>
     */
    public static String substring(String str, String open, String close) {
        if (null == str) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    // ------------------------------------------------------------------- index

    /**
     * 배열에 담긴 문자열중 문자열을 포함하고 있는 가장 앞쪽에 존재하는 index값을 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * int index = StringUtil.indexOf("abcd", new String[] { "bc", "ab" });
     * // output --> -1
     * </pre>
     *
     * </blockquote>
     *
     * @return 배열의 index 값을 리턴한다. string이 <code>null</code>이면 이거나 발견되지 되지 않으면
     *         <code>-1</code>를 리턴한다.
     */
    public static int indexOf(String str, String[] strs) {
        if (null == str) {
            return -1;
        }
        int len = strs.length;
        int tmp = 0;
        int ret = Integer.MAX_VALUE;

        for (int i = 0; i < len; i++) {
            tmp = str.indexOf(strs[i]);
            if (tmp == -1) {
                continue;
            }
            if (tmp < ret) {
                ret = tmp;
                break;
            }
        }
        return (ret == Integer.MAX_VALUE ? -1 : ret);
    }

    /**
     * 배열에 담긴 문자열중 string의 일부분 위치중 가장 뒤쪽에 있는 것을 찾는다. 만약 발견되지 않으면 -1를 리턴한다. string이
     * null이면 -1를 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * int index = StringUtil.lastIndexOf("abcd", new String[] { "ab", "bc" });
     * // output --> 1;
     * </pre>
     *
     * </blockquote>
     */
    public static int lastIndexOf(String str, String[] strs) {
        if (null == str) {
            return -1;
        }
        int len = strs.length;
        int ret = -1;
        int tmp = 0;
        for (int i = 0; i < len; i++) {
            tmp = str.lastIndexOf(strs[i]);
            if (tmp > ret) {
                ret = tmp;
            }
        }
        return ret;
    }

    /**
     * 얼마나 sub string이 matching되는지를 리턴한다. string이 null이면 0을 리턴 한다
     */
    public static int countMatches(String str, String sub) {
        if (null == str) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(sub, index)) != -1) {
            count++;
            index += sub.length();
        }
        return count;
    }

    /**
     * 특정 글자를 다른 글자로 바꾼다. target이 null이면 null를 리턴하고 rep나 with가 null이면 원본 문자열을 리턴한다.
     *
     * <blockquote>
     *
     * <pre>
     * translate("hello", "ho", "jy") => jelly
     * </pre>
     *
     * </blockquote>
     */
    public static String translate(String target, String rep, String with) {
        if (null == target) {
            return null;
        }
        StringBuffer sb = new StringBuffer(target.length());
        char[] chars = target.toCharArray();
        char[] withChars = with.toCharArray();
        int len = chars.length;
        int withMax = with.length() - 1;
        for (int i = 0; i < len; i++) {
            int index = rep.indexOf(chars[i]);
            if (index != -1) {
                if (index > withMax) {
                    index = withMax;
                }
                sb.append(withChars[index]);
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Align.
     *
     * @param str
     * @param length
     * @param isleft
     * @param fillChar
     * @return the byte[]
     */
    public static byte[] align(String str, int length, boolean isleft, char fillChar) {
        if (str == null) {
            return new byte[0];
        }

        byte[] buf = new byte[length];
        byte[] src = str.getBytes();

        int diff = length - src.length;

        if (diff >= 0) {
            if (isleft) {
                System.arraycopy(src, 0, buf, 0, src.length);
                if (diff > 0) {
                    for (int i = src.length; i < length; i++) {
                        buf[i] = (byte) fillChar;
                    }
                }
            } else {
                System.arraycopy(src, 0, buf, diff, src.length);
                if (diff > 0) {
                    for (int i = 0; i < diff; i++) {
                        buf[i] = (byte) fillChar;
                    }
                }
            }
        } else {
            if (isleft) {
                System.arraycopy(src, 0, buf, 0, buf.length);
            } else {
                System.arraycopy(src, -diff, buf, 0, buf.length);
            }
        }

        return buf;
    }

    /**
     * Fill space to Source string's tail and returns a String length : return
     * string's length src : source string
     */
    public static String fillSpace(String src, int length) {
        char[] sourceArray = src.toCharArray();
        char[] targetArray = new char[length];
        int count = 0;

        if (sourceArray.length > length) {
            count = length;
        } else {
            count = sourceArray.length;
        }

        System.arraycopy(sourceArray, 0, targetArray, 0, count);
        for (int i = count; i < length; i++) {
            targetArray[i] = ' ';
        }

        return new String(targetArray);
    }

    /**
     * Fill '0' to Source string's header and returns a String length : return
     * string's length src : source string
     */
    public static String fillZero(String src, int length) {
        char[] sourceArray = src.toCharArray();
        char[] targetArray = new char[length];
        int count = 0;

        int sourceStart = sourceArray.length - length;
        if (sourceStart > 0) {
            count = length;
        } else {
            sourceStart = 0;
            count = sourceArray.length;
        }
        int targetStart = length - count;

        for (int i = 0; i < targetStart; i++) {
            targetArray[i] = '0';
        }
        System.arraycopy(sourceArray, sourceStart, targetArray, targetStart, count);

        return new String(targetArray);
    }

    /**
     * Concat path.
     *
     * @param fileName1
     * @param fileName2
     * @return the string
     */
    public static String concatPath(String fileName1, String fileName2) {
        if (fileName1 == null || fileName1.length() == 0) {
            return fileName2;
        }
        if (fileName2 == null || fileName2.length() == 0) {
            return fileName1;
        }

        if (fileName1.substring(fileName1.length() - 1).equals(File.separator) && fileName2
                .startsWith(File.separator)) {
            return fileName1 + fileName2.substring(1);
        } else if (!fileName1.substring(fileName1.length() - 1).equals(File.separator) && !fileName2
                .startsWith(File.separator)) {
            return fileName1 + File.separator + fileName2;
        }

        return fileName1 + fileName2;
    }

    /**
     * Nvl.
     *
     * @param str
     * @param defaultValue
     * @return the string
     */
    public static String nvl(String str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            return str;
        }
    }

    /**
     * Prints the hex.
     *
     * @param s
     * @param bytes
     */
    public static void printHex(String s, byte[] bytes) {
        System.out.println("[" + s + "] " + bytes.length + "\uBC14\uC774\uD2B8");
        printHex(bytes);
    }

    /**
     * Prints the hex.
     *
     * @param bytes
     */
    public static void printHex(byte[] bytes) {
        System.out.println(hexDump(bytes));
    }
    
    /**
     * hex dump 문자열을 가져온다.
     * @param bytes hex dump 대상 byte array
     * @return
     */
    public static String hexDump(byte[] bytes) {
    	
        StringBuffer stringbuffer = new StringBuffer(83);
        stringbuffer.append("================================================   ================").append("\r\n");
        stringbuffer.append("01:02:03:04:05:06:07:08:09:10:11:12:13:14:15:16:   1234567890123456").append("\r\n");
        stringbuffer.append("================================================   ================").append("\r\n");
        
        if (bytes == null || bytes.length == 0) {
        	stringbuffer.append("NULL OR LENGTH IS ZERO");
        	return stringbuffer.toString();
        }
        
        int i = bytes.length;
        int j = i / 16;
        
        for (int k = 0; k <= j; k++) {
            int k1 = Math.min(16, i - k * 16);
            for (int l = 0; l < k1; l++) {
                char c = Character.forDigit(bytes[k * 16 + l] >> 4 & 0xf, 16);
                char c2 = Character.forDigit(bytes[k * 16 + l] & 0xf, 16);
                stringbuffer.append(Character.toUpperCase(c));
                stringbuffer.append(Character.toUpperCase(c2));
                stringbuffer.append(':');
            }

            for (int i1 = 16; i1 >= k1; i1--) {
                stringbuffer.append("   ");
            }

            for (int j1 = 0; j1 < k1; j1++) {
                char c1 = (char) bytes[k * 16 + j1];
                if (Character.isISOControl(c1)) {
                    stringbuffer.append('.');
                } else {
                    stringbuffer.append((char) bytes[k * 16 + j1]);
                }
            }
            
            stringbuffer.append("\r\n");
        }
        
        return stringbuffer.toString();
    }

    /**
     * Gets the hex.
     *
     * @param bytes
     * @return the hex
     */
    public static String getHex(byte[] bytes) {
        int i = bytes.length;
        int j = i / 16;
        StringBuffer stringbuffer = new StringBuffer(256);
        for (int k = 0; k <= j; k++) {
            StringBuffer stringbuffer1 = new StringBuffer(83);
            int i1 = Math.min(16, i - k * 16);
            for (int l = 0; l < i1; l++) {
                char c = Character.forDigit(bytes[k * 16 + l] >> 4 & 0xf, 16);
                char c1 = Character.forDigit(bytes[k * 16 + l] & 0xf, 16);
                stringbuffer1.append(Character.toLowerCase(c));
                stringbuffer1.append(Character.toLowerCase(c1));
            }

            stringbuffer.append(stringbuffer1);
        }

        return stringbuffer.toString();
    }

    /**
     * Gets the hex upper.
     *
     * @param bytes
     * @return the hex upper
     */
    public static String getHexUpper(byte[] bytes) {
        int i = bytes.length;
        int j = i / 16;
        StringBuffer stringbuffer = new StringBuffer(256);
        for (int k = 0; k <= j; k++) {
            StringBuffer stringbuffer1 = new StringBuffer(83);
            int i1 = Math.min(16, i - k * 16);
            for (int l = 0; l < i1; l++) {
                char c = Character.forDigit(bytes[k * 16 + l] >> 4 & 0xf, 16);
                char c1 = Character.forDigit(bytes[k * 16 + l] & 0xf, 16);
                stringbuffer1.append(Character.toUpperCase(c));
                stringbuffer1.append(Character.toUpperCase(c1));
            }

            stringbuffer.append(stringbuffer1);
        }

        return stringbuffer.toString();
    }

    /**
     * Decode hex.
     *
     * @param bytes
     * @return the byte[]
     */
    public static byte[] decodeHex(byte[] bytes) {
        if (bytes.length % 2 == 1) {
            return null;
        }
        byte[] abyte1 = new byte[bytes.length / 2];
        // boolean flag = false;
        for (int j = 0; j < bytes.length; j += 2) {
            int i = bytes[j] < 97 ? bytes[j] < 65 ? bytes[j] - 48 << 4 : (bytes[j] - 65) + 10 << 4
                    : (bytes[j] - 97) + 10 << 4;
            i += bytes[j + 1] < 97 ? bytes[j + 1] < 65 ? bytes[j + 1] - 48 : (bytes[j + 1] - 65) + 10
                    : (bytes[j + 1] - 97) + 10;
            abyte1[j / 2] = (byte) i;
        }

        return abyte1;
    }

    /**
     * 입력된 문자열 중에 한글이 들어 가 있더라도 len 길이 만큼 한글이 안깨지도록 짤라 주는 메소드
     *
     * @param raw 문자열
     * @param len 길이
     * @return 길이 만큼 자른 문자열의 array
     */
    public static String[] makeWithHangulLen(String raw, int len, String encoding) {
        if (raw == null) {
            return null;
        }

        String[] ary = null;
        try {
            // raw 의 byte
            byte[] rawBytes = raw.getBytes(encoding);
            int rawLength = rawBytes.length;

            if (rawLength > len) {
                int aryLength = (rawLength / len) + (rawLength % len != 0 ? 1 : 0);
                ary = new String[aryLength];

                int endCharIndex = 0; // 문자열이 끝나는 위치
                String tmp;
                for (int i = 0; i < aryLength; i++) {
                    if (i == (aryLength - 1)) {
                        tmp = raw.substring(endCharIndex);
                    } else {
                        int useByteLength = 0;
                        int rSize = 0;
                        for (; endCharIndex < raw.length(); endCharIndex++) {
                            if (raw.charAt(endCharIndex) > 0x007F) {
                                useByteLength += 2;
                            } else {
                                useByteLength++;
                            }
                            if (useByteLength > len) {
                                break;
                            }
                            rSize++;
                        }
                        tmp = raw.substring((endCharIndex - rSize), endCharIndex);
                    }
                    ary[i] = tmp;
                }
            } else {
                ary = new String[] { raw };
            }

        } catch (java.io.UnsupportedEncodingException e) {
        }

        return ary;
    }

    /**
     * <pre>
     * SO/SI 추가하는 기능을 수행<br> 
     * ex) addSOSI("1234567890") => 0x0e+"123456789"+0x0f
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String addSOSI(String str) {
        return addSOSI(str, -1);
    }

    /**
     * <pre>
     * SO/SI 추가하는 기능을 수행<br> 
     * ex) addSOSI("1234567890") => 0x0e+"123456789"+0x0f
     * </pre>
     * 
     * @param str
     * @param size
     * @return
     */
    public static String addSOSI(String str, int size) {
        byte[] sep1 = new byte[] { 0x0E };
        byte[] sep2 = new byte[] { 0x0F };
        byte[] strchar = str.trim().getBytes();

        if (str.trim().length() == 0) {
            return str;
        }

        if (size > 0) {
            if (strchar.length > (size - 2)) {
                byte[] buf = new byte[size - 2];
                System.arraycopy(strchar, 0, buf, 0, size - 2);
                strchar = buf;
            }
        }

        byte[] generateChar = new byte[sep1.length + strchar.length + sep2.length];

        System.arraycopy(sep1, 0, generateChar, 0, sep1.length);
        System.arraycopy(strchar, 0, generateChar, 1, strchar.length);
        System.arraycopy(sep2, 0, generateChar, strchar.length + 1, sep2.length);

        return new String(generateChar);
    }

    /**
     * SO/SI 제거하는 기능을 수행<br>
     * ex) delSOSI(0x0e+"1234567890"+0x0f) => "123456789"
     *
     * @param str
     * @return
     */
    public static String delSOSI(String str) {
        byte so = 0x0E;
        byte si = 0x0F;
        int size = 0;

        byte[] strchar = str.getBytes();
        for (int i = 0; i < strchar.length; i++) {
            if (strchar[i] == so || strchar[i] == si) {
                continue;
            }
            size++;
        }

        byte[] buf = new byte[size];
        for (int i = 0, j = 0; i < strchar.length; i++) {
            if (strchar[i] == so || strchar[i] == si) {
                continue;
            }
            buf[j++] = strchar[i];
        }
        return new String(buf);
    }

    /**
     * 반각문자로 변경한다
     *
     * @param src 변경할값
     * @return String 변경된값
     */
    public static String toHalfChar(String src) {
        StringBuffer buf = new StringBuffer();

        String[] halfChar = new String[] { " ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*",
                "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";",
                "<", "=", ">", "?", "@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "", "]",
                "^", "_", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "{", "|", "}", "~" };

        char[] fullChar = src.toCharArray();
        char ascii;

        for (int i = 0; i < fullChar.length; i++) {
            ascii = fullChar[i];
            /**
             * 문자에 스페이스가 2개가 있는 경우 1개로 변환 박대성 대리와 협의 내용 (2008/09/25)
             */
            if (ascii == '　') {
                if (fullChar.length > (i + 1) && fullChar[i + 1] == '　') {
                    buf.append(" ");
                    i++;
                } else {
                    buf.append(" ");
                }
            } else if (ascii == 0x20) {
                if (fullChar.length > (i + 1) && fullChar[i + 1] == 0x20) {
                    buf.append(" ");
                    i++;
                } else {
                    buf.append(" ");
                }
            } else if (65280 < ascii && ascii < 65375) {
                buf.append(halfChar[ascii - 65280]);
            } else if (12288 == ascii) {
                buf.append(halfChar[ascii - 12288]);
            } else if (65510 == ascii) { 
            	//통화코드 관련 원문 그대로.. 리턴한다.
                //buf.append(halfChar[60]);
                buf.append(fullChar[i]);
            } else {
                buf.append(fullChar[i]);
            }
        }

        return buf.toString();
    }

    /**
     * 전각문자로 변경한다.
     *
     * @param src 변경할값
     * @return String 변경된값
     */
    public static String toFullChar(String src) {
        StringBuffer buf = new StringBuffer();

        String[] fullChar = new String[] { "　", "！", "＂", "＃", "＄", "％", "＆", "＇", "（", // 33~
                "）", "＊", "＋", "，", "－", "．", "／", "０", "１", "２", // 41~
                "３", "４", "５", "６", "７", "８", "９", "：", "；", "＜", // 51~
                "＝", "＞", "？", "＠", "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", // 61~
                "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ", "Ｍ", "Ｎ", "Ｏ", "Ｐ", // 71~
                "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ", "Ｙ", "Ｚ", // 81~
                "［", "￦", "］", "＾", "＿", "｀", "ａ", "ｂ", "ｃ", "ｄ", // 91~
                "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ", // 101~
                "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", // 111~
                "ｙ", "ｚ", "｛", "｜", "｝", "～" // 121~
        };

        char[] halfchar = src.toCharArray();
        char ascii;

        for (int i = 0; i < halfchar.length; i++) {
            ascii = halfchar[i];
            if ((31 < ascii && ascii < 128)) {
                buf.append(fullChar[ascii - 32]);
            } else {
                buf.append(halfchar[i]);
            }
        }

        return buf.toString();
    }

    public static Set<String> commaDelimitedListToSet(String input) {
        if (input == null || input.isBlank())
            return Set.of();
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * 한글인지 체크해서 한글이면 true 리턴
     * 
     * @param chr
     * @return
     */
    public static Boolean isKorean(char chr) {
        if (!(chr < 0xac00 || 0xd7a3 < chr)) // 한글이면
        {
            return true;
        } else if (!(chr < 0x3131 || 0x318f < chr)) {
            // 한글 compatibility jamo
            return true;
        } else if (!(chr < 0x1100 || 0x117f < chr)) {
            // 한글 jamo
            return true;
        } else if (!(chr < 0xff00 || 0xff5e < chr)) // 두바이트 영문 및 숫자이면
        {
            return true;
        } else if (!(chr < 0x3000 || 0x303f < chr)) // 두바이트 공백이거나 요상한 문자들
        {
            return true;
        }

        return false;
    }
    
    /**
     * 문자열에서 XSS 에 취약한 문자열을 변환한다.
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public static String rplXSSreplaceAll(String pStrSrc) {
    	if (pStrSrc == null || pStrSrc.isEmpty()) {
    		return pStrSrc;
    	}
        pStrSrc = pStrSrc.replaceAll("'", "&#039");
        pStrSrc = pStrSrc.replaceAll("\"", "&quot");
        pStrSrc = pStrSrc.replaceAll("<", "&lt;");
        pStrSrc = pStrSrc.replaceAll(">", "&gt;");
        pStrSrc = pStrSrc.trim();
        return pStrSrc;
    }
}
