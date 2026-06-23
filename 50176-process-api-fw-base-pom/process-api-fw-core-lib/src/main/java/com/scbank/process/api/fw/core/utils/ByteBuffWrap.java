package com.scbank.process.api.fw.core.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author sungdon.choi
 */
public class ByteBuffWrap {

    private final byte[] _bytes;
    private final int _offset;
    private final int _length;

    /**
     * @param bytes
     */
    public ByteBuffWrap(final byte[] bytes) {
        this._bytes = bytes;
        this._offset = 0;
        this._length = bytes.length;
    }

    /**
     * @param bytes
     * @param offset
     * @param length
     */
    public ByteBuffWrap(final byte[] bytes, final int offset, final int length) {
        this._bytes = bytes;
        this._offset = offset;
        this._length = length;
    }

    /**
     * @param bytes
     * @return
     */
    public static ByteBuffWrap wrap(byte[] bytes) {
        return new ByteBuffWrap(bytes);
    }

    /**
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public static ByteBuffWrap wrap(byte[] bytes, int offset, int length) {
        return new ByteBuffWrap(bytes, offset, length);
    }

    /**
     * @return
     */
    public byte[] getByteArray() {
        return this._bytes;
    }

    /**
     * @return
     */
    public int getOffset() {
        return this._offset;
    }

    /**
     * @return
     */
    public int getLength() {
        return this._length;
    }

    /**
     * @param offset
     * @return
     */
    public byte getByte(final int offset) {
        return this._bytes[this._offset + offset];
    }

    /**
     * @param offset
     * @param value
     */
    public void setByte(final int offset, final byte value) {
        this._bytes[this._offset + offset] = value;
    }

    /**
     * @param offset
     * @param length
     * @return
     */
    public String getString(final int offset, final int length) {
        return new String(this._bytes, this._offset + offset, length);
    }

    /**
     * @param offset
     * @param length
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getString(final int offset, final int length, final String charset)
            throws UnsupportedEncodingException {
        return new String(this._bytes, this._offset + offset, length, charset);
    }

    /**
     * @param offset
     * @param length
     * @return
     */
    public byte[] getByteArray(final int offset, final int length) {
        final byte[] newBytes = new byte[length];
        System.arraycopy(this._bytes, this._offset + offset, newBytes, 0, length);
        return newBytes;
    }

    /**
     * @param offset
     * @param newBytes
     */
    public void setByteArray(final int offset, final byte[] newBytes) {
        System.arraycopy(newBytes, 0, this._bytes, this._offset + offset, newBytes.length);
    }

    /**
     * @param offset
     * @param newBytes
     * @param newBytesOffset
     * @param length
     */
    public void setByteArray(final int offset, final byte[] newBytes, final int newBytesOffset,
            final int length) {
        System.arraycopy(newBytes, newBytesOffset, this._bytes, this._offset + offset, length);
    }

    /**
     * @param offset
     * @param length
     * @return
     */
    public ByteBuffWrap reuse(final int offset, final int length) {
        return new ByteBuffWrap(this._bytes, this._offset + offset, length);
    }

    /**
     *
     */
    public ByteBuffWrap clone() {
        final byte[] newBytes = new byte[this._length];
        System.arraycopy(this._bytes, this._offset, newBytes, 0, this._length);
        return new ByteBuffWrap(newBytes);
    }

    /**
     * @param input
     * @return
     */
    public ByteBuffWrap merge(final ByteBuffWrap input) {
        final byte[] newBytes = new byte[this._length + input._length];
        System.arraycopy(this._bytes, this._offset, newBytes, 0, this._length);
        System.arraycopy(input._bytes, input._offset, newBytes, this._length, input._length);
        return new ByteBuffWrap(newBytes);
    }

    @Override
    public String toString() {
        return new String(this._bytes, this._offset, this._length);
    }

    /**
     * @param encoding 인코딩 문자열
     * @return
     * @throws UnsupportedEncodingException
     */
    public String toString(final String encoding) throws UnsupportedEncodingException {
        return new String(this._bytes, this._offset, this._length, encoding);
    }
}
