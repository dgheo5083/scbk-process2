package com.scbank.process.api.fw.core.utils;

import com.scbank.process.api.fw.message.enums.AlignType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ByteUtils 테스트")
class ByteUtilsTest {

    private static final String UTF_8 = "UTF-8";

    @Test
    @DisplayName("상수 값 확인")
    void constants() {
        assertArrayEquals(new byte[]{0x30}, ByteUtils.ZERO_PAD_BYTES);
        assertArrayEquals(new byte[]{0x20}, ByteUtils.SPACE_PAD_BYTES);
        assertArrayEquals(new byte[]{'*'}, ByteUtils.MAKING_PAD_BYTES);
        assertEquals(0x0E, ByteUtils.SOSI_SO);
        assertEquals(0x0F, ByteUtils.SOSI_SI);
    }

    @Nested
    @DisplayName("wrap 메서드 테스트")
    class WrapTests {

        @Test
        @DisplayName("wrap - 정상 케이스")
        void wrap_success() {
            byte[] bytes = "Hello".getBytes();
            ByteBuffWrap wrap = ByteUtils.wrap(bytes);

            assertNotNull(wrap);
            assertArrayEquals(bytes, wrap.getByteArray());
        }

        @Test
        @DisplayName("wrap - null 입력")
        void wrap_null() {
            assertNull(ByteUtils.wrap(null));
        }

        @Test
        @DisplayName("wrap - 빈 배열")
        void wrap_empty() {
            assertNull(ByteUtils.wrap(new byte[0]));
        }

        @Test
        @DisplayName("wrap - offset, length 지정")
        void wrap_withOffsetLength() {
            byte[] bytes = "HelloWorld".getBytes();
            ByteBuffWrap wrap = ByteUtils.wrap(bytes, 5, 5);

            assertNotNull(wrap);
            assertEquals(5, wrap.getOffset());
            assertEquals(5, wrap.getLength());
        }

        @Test
        @DisplayName("wrap - offset, length 지정 시 null 입력")
        void wrap_withOffsetLength_null() {
            assertNull(ByteUtils.wrap(null, 0, 5));
        }

        @Test
        @DisplayName("wrap - offset, length 지정 시 빈 배열")
        void wrap_withOffsetLength_empty() {
            assertNull(ByteUtils.wrap(new byte[0], 0, 0));
        }
    }

    @Nested
    @DisplayName("unwrap 메서드 테스트")
    class UnwrapTests {

        @Test
        @DisplayName("unwrap - 전체 배열")
        void unwrap() {
            byte[] bytes = "Hello".getBytes();
            ByteBuffWrap wrap = new ByteBuffWrap(bytes);

            assertArrayEquals(bytes, ByteUtils.unwrap(wrap));
        }

        @Test
        @DisplayName("unwrap - offset, length 지정")
        void unwrap_withOffsetLength() {
            byte[] bytes = "HelloWorld".getBytes();
            ByteBuffWrap wrap = new ByteBuffWrap(bytes);

            byte[] result = ByteUtils.unwrap(wrap, 5, 5);
            assertEquals("World", new String(result));
        }
    }

    @Nested
    @DisplayName("merge 메서드 테스트")
    class MergeTests {

        @Test
        @DisplayName("merge - 두 배열 병합")
        void merge_twoArrays() {
            byte[] arr1 = "Hello".getBytes();
            byte[] arr2 = "World".getBytes();

            byte[] result = ByteUtils.merge(arr1, arr2);

            assertEquals("HelloWorld", new String(result));
        }

        @Test
        @DisplayName("merge - 여러 배열 병합")
        void merge_multipleArrays() {
            byte[] arr1 = "A".getBytes();
            byte[] arr2 = "B".getBytes();
            byte[] arr3 = "C".getBytes();

            byte[] result = ByteUtils.merge(arr1, arr2, arr3);

            assertEquals("ABC", new String(result));
        }
    }

    @Nested
    @DisplayName("addSOSI 메서드 테스트")
    class AddSOSITests {

        @Test
        @DisplayName("addSOSI - 한글 포함 문자열 (byte[], String)")
        void addSOSI_withKorean() {
            byte[] source = "테스트ABC".getBytes();
            String result = ByteUtils.addSOSI(source, UTF_8);

            assertNotNull(result);
            assertTrue(result.contains(String.valueOf((char) ByteUtils.SOSI_SO))
                    || result.contains(String.valueOf((char) ByteUtils.SOSI_SI)));
        }

        @Test
        @DisplayName("addSOSI - 영문만 있는 경우")
        void addSOSI_asciiOnly() {
            byte[] source = "ABC".getBytes();
            String result = ByteUtils.addSOSI(source, UTF_8);

            assertEquals("ABC", result);
        }

        @Test
        @DisplayName("addSOSI - byte[], size 지정")
        void addSOSI_withSize() {
            byte[] source = "Test".getBytes();
            byte[] result = ByteUtils.addSOSI(source, 10);

            assertEquals(6, result.length);
            assertEquals(0x0E, result[0]);
            assertEquals(0x0F, result[result.length - 1]);
        }

        @Test
        @DisplayName("addSOSI - size가 0인 경우")
        void addSOSI_zeroSize() {
            byte[] source = "Test".getBytes();
            byte[] result = ByteUtils.addSOSI(source, 0);

            assertEquals(source.length + 2, result.length);
        }

        @Test
        @DisplayName("addSOSI - 문자열이 size보다 큰 경우 잘림")
        void addSOSI_truncated() {
            byte[] source = "HelloWorld".getBytes();
            byte[] result = ByteUtils.addSOSI(source, 6);

            assertEquals(6, result.length);
        }
    }

    @Nested
    @DisplayName("delSOSI 메서드 테스트")
    class DelSOSITests {

        @Test
        @DisplayName("delSOSI - SOSI 마크 제거")
        void delSOSI_removesMarks() throws Exception {
            byte[] source = new byte[]{0x0E, 'T', 'e', 's', 't', 0x0F};
            String result = ByteUtils.delSOSI(source, UTF_8);

            assertEquals("Test", result);
        }

        @Test
        @DisplayName("delSOSI - null 입력")
        void delSOSI_null() throws Exception {
            assertEquals("", ByteUtils.delSOSI(null, UTF_8));
        }

        @Test
        @DisplayName("delSOSI - 빈 배열")
        void delSOSI_empty() throws Exception {
            assertEquals("", ByteUtils.delSOSI(new byte[0], UTF_8));
        }

        @Test
        @DisplayName("delSOSI - SOSI 마크가 없는 경우")
        void delSOSI_noMarks() throws Exception {
            byte[] source = "Test".getBytes();
            String result = ByteUtils.delSOSI(source, UTF_8);

            assertEquals("Test", result);
        }
    }

    @Test
    @DisplayName("containsMultibyteCharacter - 멀티바이트 문자 포함 여부")
    void containsMultibyteCharacter() {
        assertTrue(ByteUtils.containsMultibyteCharacter("테스트".getBytes(), UTF_8));
        assertFalse(ByteUtils.containsMultibyteCharacter("ASCII".getBytes(), UTF_8));
    }

    @Nested
    @DisplayName("writeBytes 메서드 테스트")
    class WriteBytesTests {

        @Test
        @DisplayName("writeBytes - 왼쪽 정렬")
        void writeBytes_leftAlign() throws IOException {
            byte[] value = "ABC".getBytes();
            byte[] result = ByteUtils.writeBytes(value, 6, ByteUtils.SPACE_PAD_BYTES, AlignType.LEFT);

            assertEquals(6, result.length);
            assertEquals("ABC   ", new String(result));
        }

        @Test
        @DisplayName("writeBytes - 오른쪽 정렬")
        void writeBytes_rightAlign() throws IOException {
            byte[] value = "ABC".getBytes();
            byte[] result = ByteUtils.writeBytes(value, 6, ByteUtils.SPACE_PAD_BYTES, AlignType.RIGHT);

            assertEquals(6, result.length);
            assertEquals("   ABC", new String(result));
        }

        @Test
        @DisplayName("writeBytes - OutputStream 사용")
        void writeBytes_withOutputStream() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] value = "Test".getBytes();

            ByteUtils.writeBytes(out, value, 8, ByteUtils.SPACE_PAD_BYTES, AlignType.LEFT);

            assertEquals("Test    ", out.toString());
        }
    }

    @Nested
    @DisplayName("writeString 메서드 테스트")
    class WriteStringTests {

        @Test
        @DisplayName("writeString - 기본 사용")
        void writeString() throws IOException {
            byte[] result = ByteUtils.writeString("ABC", 6, ByteUtils.SPACE_PAD_BYTES, AlignType.LEFT, UTF_8);

            assertEquals(6, result.length);
            assertEquals("ABC   ", new String(result, UTF_8));
        }

        @Test
        @DisplayName("writeString - OutputStream 사용")
        void writeString_withOutputStream() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeString(out, "Test", 8, ByteUtils.SPACE_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals("    Test", out.toString(UTF_8));
        }
    }

    @Nested
    @DisplayName("writeInteger 메서드 테스트")
    class WriteIntegerTests {

        @Test
        @DisplayName("writeInteger - 양수, 왼쪽 정렬")
        void writeInteger_positiveLeftAlign() throws IOException {
            byte[] result = ByteUtils.writeInteger("123", 6, ByteUtils.ZERO_PAD_BYTES, AlignType.LEFT, UTF_8);

            assertEquals(6, result.length);
            assertEquals("123000", new String(result, UTF_8));
        }

        @Test
        @DisplayName("writeInteger - 양수, 오른쪽 정렬")
        void writeInteger_positiveRightAlign() throws IOException {
            byte[] result = ByteUtils.writeInteger("123", 6, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals(6, result.length);
            assertEquals("000123", new String(result, UTF_8));
        }

        @Test
        @DisplayName("writeInteger - 음수, 왼쪽 정렬")
        void writeInteger_negativeLeftAlign() throws IOException {
            byte[] result = ByteUtils.writeInteger("-123", 6, ByteUtils.ZERO_PAD_BYTES, AlignType.LEFT, UTF_8);

            assertEquals(6, result.length);
            assertTrue(new String(result, UTF_8).startsWith("-"));
        }

        @Test
        @DisplayName("writeInteger - 음수, 오른쪽 정렬")
        void writeInteger_negativeRightAlign() throws IOException {
            byte[] result = ByteUtils.writeInteger("-123", 6, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals(6, result.length);
        }

        @Test
        @DisplayName("writeInteger - OutputStream 사용")
        void writeInteger_withOutputStream() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeInteger(out, "456", 6, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals("000456", out.toString(UTF_8));
        }
    }

    @Test
    @DisplayName("writeNumber - 빈 메서드 확인")
    void writeNumber() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteUtils.writeNumber(out, 123, 6, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);
        // 빈 메서드이므로 출력 없음
        assertEquals(0, out.size());
    }

    @Test
    @DisplayName("writeBigDecimal - 빈 메서드 확인")
    void writeBigDecimal() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteUtils.writeBigDecimal(out, null, 6, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);
        // 빈 메서드이므로 출력 없음
        assertEquals(0, out.size());
    }

    @Nested
    @DisplayName("removePadding 메서드 테스트")
    class RemovePaddingTests {

        @Test
        @DisplayName("removePadding - 왼쪽 정렬 (오른쪽 패딩 제거)")
        void removePadding_leftAlign() throws UnsupportedEncodingException {
            byte[] source = "ABC   ".getBytes(UTF_8);
            String result = ByteUtils.removePadding(source, ByteUtils.SPACE_PAD_BYTES, AlignType.LEFT, UTF_8);

            assertEquals("ABC", result);
        }

        @Test
        @DisplayName("removePadding - 오른쪽 정렬 (왼쪽 패딩 제거)")
        void removePadding_rightAlign() throws UnsupportedEncodingException {
            byte[] source = "   ABC".getBytes(UTF_8);
            String result = ByteUtils.removePadding(source, ByteUtils.SPACE_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals("ABC", result);
        }

        @Test
        @DisplayName("removePadding - 제로 패딩 제거")
        void removePadding_zeroPadding() throws UnsupportedEncodingException {
            byte[] source = "000123".getBytes(UTF_8);
            String result = ByteUtils.removePadding(source, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals("123", result);
        }
    }

    @Nested
    @DisplayName("writeWithPadding 메서드 테스트")
    class WriteWithPaddingTests {

        @Test
        @DisplayName("writeWithPadding - 문자열 입력, 왼쪽 정렬")
        void writeWithPadding_string_leftAlign() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeWithPadding(out, "ABC", 6, ByteUtils.SPACE_PAD_BYTES, true, UTF_8);

            assertEquals("ABC   ", out.toString(UTF_8));
        }

        @Test
        @DisplayName("writeWithPadding - byte[] 입력, 오른쪽 정렬")
        void writeWithPadding_bytes_rightAlign() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeWithPadding(out, "ABC".getBytes(), 6, ByteUtils.SPACE_PAD_BYTES, false);

            assertEquals("   ABC", out.toString());
        }

        @Test
        @DisplayName("writeWithPadding - null 값")
        void writeWithPadding_null() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeWithPadding(out, (byte[]) null, 6, ByteUtils.SPACE_PAD_BYTES, true);

            assertEquals("      ", out.toString());
        }

        @Test
        @DisplayName("writeWithPadding - 값이 길이보다 긴 경우")
        void writeWithPadding_truncated() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeWithPadding(out, "ABCDEFGH".getBytes(), 4, ByteUtils.SPACE_PAD_BYTES, true);

            assertEquals("ABCD", out.toString());
        }

        @Test
        @DisplayName("writeWithPadding - 값과 길이가 같은 경우")
        void writeWithPadding_exactLength() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteUtils.writeWithPadding(out, "ABCD".getBytes(), 4, ByteUtils.SPACE_PAD_BYTES, true);

            assertEquals("ABCD", out.toString());
        }
    }

    @Nested
    @DisplayName("readString 메서드 테스트")
    class ReadStringTests {

        @Test
        @DisplayName("readString - 기본 사용")
        void readString() throws IOException {
            byte[] bytes = "HelloWorld".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);

            String result = ByteUtils.readString(input, 5, UTF_8);

            assertEquals("Hello", result);
        }

        @Test
        @DisplayName("readString - 패딩 포함")
        void readString_withPadding() throws IOException {
            byte[] bytes = "ABC   ".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);

            String result = ByteUtils.readString(input, 6, ByteUtils.SPACE_PAD_BYTES, AlignType.LEFT, UTF_8);

            assertEquals("ABC", result);
        }
    }

    @Test
    @DisplayName("readNumber")
    void readNumber() throws IOException {
        byte[] bytes = "000123".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);

        String result = ByteUtils.readNumber(input, 6, ByteUtils.ZERO_PAD_BYTES, AlignType.RIGHT, UTF_8);

        assertEquals("123", result);
    }

    @Nested
    @DisplayName("readBytes 메서드 테스트")
    class ReadBytesTests {

        @Test
        @DisplayName("readBytes - 왼쪽 정렬")
        void readBytes_leftAlign() throws IOException {
            byte[] bytes = "ABC   ".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);

            byte[] result = ByteUtils.readBytes(input, 6, ByteUtils.SPACE_PAD_BYTES, AlignType.LEFT, UTF_8);

            assertEquals("ABC", new String(result));
        }

        @Test
        @DisplayName("readBytes - 오른쪽 정렬")
        void readBytes_rightAlign() throws IOException {
            byte[] bytes = "   ABC".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);

            byte[] result = ByteUtils.readBytes(input, 6, ByteUtils.SPACE_PAD_BYTES, AlignType.RIGHT, UTF_8);

            assertEquals("ABC", new String(result));
        }

        @Test
        @DisplayName("readBytes - null alignType")
        void readBytes_nullAlignType() throws IOException {
            byte[] bytes = "ABC   ".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);

            byte[] result = ByteUtils.readBytes(input, 6, ByteUtils.SPACE_PAD_BYTES, null, UTF_8);

            assertEquals("ABC", new String(result));
        }

        @Test
        @DisplayName("readBytes - NONE alignType")
        void readBytes_noneAlignType() throws IOException {
            byte[] bytes = "ABC   ".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);

            byte[] result = ByteUtils.readBytes(input, 6, ByteUtils.SPACE_PAD_BYTES, AlignType.NONE, UTF_8);

            assertEquals("ABC", new String(result));
        }
    }

    @Nested
    @DisplayName("leftPaddingBytes 메서드 테스트")
    class LeftPaddingBytesTests {

        @Test
        @DisplayName("leftPaddingBytes - 오른쪽 패딩 제거")
        void leftPaddingBytes() {
            byte[] buffer = "ABC   ".getBytes();
            byte[] result = ByteUtils.leftPaddingBytes(buffer, 0, 6, ByteUtils.SPACE_PAD_BYTES);

            assertEquals("ABC", new String(result));
        }

        @Test
        @DisplayName("leftPaddingBytes - 패딩 없음")
        void leftPaddingBytes_noPadding() {
            byte[] buffer = "ABCDEF".getBytes();
            byte[] result = ByteUtils.leftPaddingBytes(buffer, 0, 6, ByteUtils.SPACE_PAD_BYTES);

            assertEquals("ABCDEF", new String(result));
        }
    }

    @Test
    @DisplayName("leftPadding - 문자열 반환")
    void leftPadding() throws IOException {
        byte[] buffer = "ABC   ".getBytes();
        String result = ByteUtils.leftPadding(buffer, 0, 6, ByteUtils.SPACE_PAD_BYTES, UTF_8);

        assertEquals("ABC", result);
    }

    @Nested
    @DisplayName("rightPaddingBytes 메서드 테스트")
    class RightPaddingBytesTests {

        @Test
        @DisplayName("rightPaddingBytes - 왼쪽 패딩 제거")
        void rightPaddingBytes() {
            byte[] buffer = "   ABC".getBytes();
            byte[] result = ByteUtils.rightPaddingBytes(buffer, 0, 6, ByteUtils.SPACE_PAD_BYTES);

            assertEquals("ABC", new String(result));
        }

        @Test
        @DisplayName("rightPaddingBytes - 패딩 없음")
        void rightPaddingBytes_noPadding() {
            byte[] buffer = "ABCDEF".getBytes();
            byte[] result = ByteUtils.rightPaddingBytes(buffer, 0, 6, ByteUtils.SPACE_PAD_BYTES);

            assertEquals("ABCDEF", new String(result));
        }
    }

    @Test
    @DisplayName("rightPadding - 문자열 반환")
    void rightPadding() throws IOException {
        byte[] buffer = "   ABC".getBytes();
        String result = ByteUtils.rightPadding(buffer, 0, 6, ByteUtils.SPACE_PAD_BYTES, UTF_8);

        assertEquals("ABC", result);
    }

    @Nested
    @DisplayName("hexStringToByteArray 메서드 테스트")
    class HexStringToByteArrayTests {

        @Test
        @DisplayName("hexStringToByteArray - 정상 변환")
        void hexStringToByteArray() {
            byte[] result = ByteUtils.hexStringToByteArray("0x48, 0x65, 0x6C, 0x6C, 0x6F");

            assertEquals("Hello", new String(result));
        }

        @Test
        @DisplayName("hexStringToByteArray - 단일 값")
        void hexStringToByteArray_single() {
            byte[] result = ByteUtils.hexStringToByteArray("0x41");

            assertEquals(1, result.length);
            assertEquals((byte) 0x41, result[0]);
        }

        @Test
        @DisplayName("hexStringToByteArray - 0x 없으면 예외")
        void hexStringToByteArray_invalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                ByteUtils.hexStringToByteArray("48, 65");
            });
        }
    }

    @Test
    @DisplayName("byteArrayToHexString")
    void byteArrayToHexString() {
        byte[] bytes = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        String result = ByteUtils.byteArrayToHexString(bytes);

        assertEquals("48656c6c6f", result);
    }

    @Test
    @DisplayName("masking - 마스킹 바이트 배열 생성")
    void masking() {
        byte[] result = ByteUtils.masking(5, UTF_8);

        assertEquals(5, result.length);
        assertEquals("*****", new String(result));
    }

    @Test
    @DisplayName("masking - 잘못된 charset")
    void masking_invalidCharset() {
        byte[] result = ByteUtils.masking(5, "INVALID");

        // 예외를 잡고 빈 값이 반환됨
        assertEquals(5, result.length);
    }
    
    @Test
    @DisplayName("대문자 HEX 문자열 처리")
    void hexStringToByteArray_success() {
    	String hex = "1B";
    	
    	byte[] acutual = ByteUtils.hexStringToBytes(hex);
    	
    	assertArrayEquals(new byte[] { 0x1B}, acutual);
    }
    
    @Test
    @DisplayName("소문자 HEX 문자열 처리")
    void hexStringToByteArray_success_lowercase() {
    	String hex = "1b";
    	
    	byte[] acutual = ByteUtils.hexStringToBytes(hex);
    	
    	assertArrayEquals(new byte[] { 0x1b }, acutual);
    }
    
    @Test
    @DisplayName("hexStringToBytes 에러처리")
    void hexStringToBytes_error() {
    	assertThrows(IllegalStateException.class, () -> ByteUtils.hexStringToBytes("A1B"));
    }
}
