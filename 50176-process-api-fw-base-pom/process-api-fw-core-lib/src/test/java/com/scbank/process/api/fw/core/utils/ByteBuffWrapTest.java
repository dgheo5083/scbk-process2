package com.scbank.process.api.fw.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ByteBuffWrap 테스트")
class ByteBuffWrapTest {

    @Test
    @DisplayName("생성자 - byte 배열만 전달")
    void constructor_withByteArray() {
        byte[] bytes = "Hello".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        assertArrayEquals(bytes, wrap.getByteArray());
        assertEquals(0, wrap.getOffset());
        assertEquals(bytes.length, wrap.getLength());
    }

    @Test
    @DisplayName("생성자 - byte 배열, offset, length 전달")
    void constructor_withByteArrayOffsetLength() {
        byte[] bytes = "HelloWorld".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 2, 5);

        assertArrayEquals(bytes, wrap.getByteArray());
        assertEquals(2, wrap.getOffset());
        assertEquals(5, wrap.getLength());
    }

    @Test
    @DisplayName("wrap 정적 메서드 - byte 배열만 전달")
    void wrap_withByteArray() {
        byte[] bytes = "Test".getBytes();
        ByteBuffWrap wrap = ByteBuffWrap.wrap(bytes);

        assertNotNull(wrap);
        assertArrayEquals(bytes, wrap.getByteArray());
        assertEquals(0, wrap.getOffset());
        assertEquals(bytes.length, wrap.getLength());
    }

    @Test
    @DisplayName("wrap 정적 메서드 - byte 배열, offset, length 전달")
    void wrap_withByteArrayOffsetLength() {
        byte[] bytes = "TestData".getBytes();
        ByteBuffWrap wrap = ByteBuffWrap.wrap(bytes, 1, 4);

        assertNotNull(wrap);
        assertEquals(1, wrap.getOffset());
        assertEquals(4, wrap.getLength());
    }

    @Test
    @DisplayName("getByte - 특정 위치의 바이트 조회")
    void getByte() {
        byte[] bytes = "ABCDE".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        assertEquals((byte) 'A', wrap.getByte(0));
        assertEquals((byte) 'C', wrap.getByte(2));
        assertEquals((byte) 'E', wrap.getByte(4));
    }

    @Test
    @DisplayName("getByte - offset이 있는 경우")
    void getByte_withOffset() {
        byte[] bytes = "ABCDE".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 1, 3);

        assertEquals((byte) 'B', wrap.getByte(0));
        assertEquals((byte) 'C', wrap.getByte(1));
    }

    @Test
    @DisplayName("setByte - 특정 위치에 바이트 설정")
    void setByte() {
        byte[] bytes = "ABCDE".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        wrap.setByte(2, (byte) 'X');

        assertEquals((byte) 'X', wrap.getByte(2));
        assertEquals((byte) 'X', bytes[2]);
    }

    @Test
    @DisplayName("setByte - offset이 있는 경우")
    void setByte_withOffset() {
        byte[] bytes = "ABCDE".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 1, 3);

        wrap.setByte(1, (byte) 'Y');

        assertEquals((byte) 'Y', bytes[2]);
    }

    @Test
    @DisplayName("getString - 기본 인코딩")
    void getString() {
        byte[] bytes = "HelloWorld".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        assertEquals("Hello", wrap.getString(0, 5));
        assertEquals("World", wrap.getString(5, 5));
    }

    @Test
    @DisplayName("getString - offset이 있는 경우")
    void getString_withOffset() {
        byte[] bytes = "HelloWorld".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 5, 5);

        assertEquals("World", wrap.getString(0, 5));
    }

    @Test
    @DisplayName("getString - 특정 charset 사용")
    void getString_withCharset() throws UnsupportedEncodingException {
        byte[] bytes = "테스트".getBytes("UTF-8");
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        String result = wrap.getString(0, bytes.length, "UTF-8");
        assertEquals("테스트", result);
    }

    @Test
    @DisplayName("getByteArray - 부분 배열 조회")
    void getByteArray_partial() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        byte[] result = wrap.getByteArray(2, 4);

        assertEquals(4, result.length);
        assertEquals("CDEF", new String(result));
    }

    @Test
    @DisplayName("getByteArray - offset이 있는 경우")
    void getByteArray_withOffset() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 2, 4);

        byte[] result = wrap.getByteArray(1, 2);

        assertEquals(2, result.length);
        assertEquals("DE", new String(result));
    }

    @Test
    @DisplayName("setByteArray - 바이트 배열 설정")
    void setByteArray() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        wrap.setByteArray(2, "XY".getBytes());

        assertEquals("ABXYEFGH", new String(bytes));
    }

    @Test
    @DisplayName("setByteArray - offset, length 지정")
    void setByteArray_withOffsetAndLength() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);
        byte[] newBytes = "WXYZ".getBytes();

        wrap.setByteArray(2, newBytes, 1, 2);

        assertEquals("ABXYEFGH", new String(bytes));
    }

    @Test
    @DisplayName("reuse - 재사용 가능한 새 ByteBuffWrap 생성")
    void reuse() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        ByteBuffWrap reused = wrap.reuse(2, 4);

        assertArrayEquals(bytes, reused.getByteArray());
        assertEquals(2, reused.getOffset());
        assertEquals(4, reused.getLength());
        assertEquals("CDEF", reused.toString());
    }

    @Test
    @DisplayName("reuse - offset이 있는 경우")
    void reuse_withOffset() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 1, 6);

        ByteBuffWrap reused = wrap.reuse(2, 3);

        assertEquals(3, reused.getOffset());
        assertEquals(3, reused.getLength());
    }

    @Test
    @DisplayName("clone - 복제본 생성")
    void cloneTest() {
        byte[] bytes = "ABCDEFGH".getBytes();
        ByteBuffWrap wrap = new ByteBuffWrap(bytes, 2, 4);

        ByteBuffWrap cloned = wrap.clone();

        assertEquals(4, cloned.getLength());
        assertEquals(0, cloned.getOffset());
        assertEquals("CDEF", cloned.toString());

        // 원본과 독립적인지 확인
        assertNotSame(bytes, cloned.getByteArray());
    }

    @Test
    @DisplayName("merge - 두 ByteBuffWrap 병합")
    void merge() {
        ByteBuffWrap wrap1 = new ByteBuffWrap("Hello".getBytes());
        ByteBuffWrap wrap2 = new ByteBuffWrap("World".getBytes());

        ByteBuffWrap merged = wrap1.merge(wrap2);

        assertEquals(10, merged.getLength());
        assertEquals("HelloWorld", merged.toString());
    }

    @Test
    @DisplayName("merge - offset이 있는 경우")
    void merge_withOffset() {
        byte[] bytes1 = "AABBCC".getBytes();
        byte[] bytes2 = "XXYYZZ".getBytes();
        ByteBuffWrap wrap1 = new ByteBuffWrap(bytes1, 2, 2);
        ByteBuffWrap wrap2 = new ByteBuffWrap(bytes2, 2, 2);

        ByteBuffWrap merged = wrap1.merge(wrap2);

        assertEquals(4, merged.getLength());
        assertEquals("BBYY", merged.toString());
    }

    @Test
    @DisplayName("toString - 기본")
    void toStringTest() {
        ByteBuffWrap wrap = new ByteBuffWrap("Hello".getBytes());
        assertEquals("Hello", wrap.toString());
    }

    @Test
    @DisplayName("toString - offset이 있는 경우")
    void toString_withOffset() {
        ByteBuffWrap wrap = new ByteBuffWrap("HelloWorld".getBytes(), 5, 5);
        assertEquals("World", wrap.toString());
    }

    @Test
    @DisplayName("toString - 특정 인코딩 사용")
    void toString_withEncoding() throws UnsupportedEncodingException {
        byte[] bytes = "테스트".getBytes("UTF-8");
        ByteBuffWrap wrap = new ByteBuffWrap(bytes);

        String result = wrap.toString("UTF-8");
        assertEquals("테스트", result);
    }

    @Test
    @DisplayName("toString - 지원하지 않는 인코딩")
    void toString_unsupportedEncoding() {
        ByteBuffWrap wrap = new ByteBuffWrap("Test".getBytes());

        assertThrows(UnsupportedEncodingException.class, () -> {
            wrap.toString("INVALID-ENCODING");
        });
    }
}
