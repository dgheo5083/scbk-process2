package com.scbank.process.api.fw.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.StringTokenizer;

import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 Byte 유틸리티 클래스
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 14.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteUtils {

	public static final byte[] ZERO_PAD_BYTES = { (byte) 0x30 };

	public static final byte[] SPACE_PAD_BYTES = { (byte) 0x20 };

	public static final byte[] MAKING_PAD_BYTES = { (byte) '*' };

	public static final byte SOSI_SO = 0x0E;
	public static final byte SOSI_SI = 0x0F;

	/**
	 * @param source byte array
	 * @return ByteBuffWrap
	 */
	public static ByteBuffWrap wrap(byte[] source) {
		if (source == null || source.length <= 0) {
			return null;
		}
		return new ByteBuffWrap(source);
	}

	/**
	 * @param source byte array
	 * @param offset 옵셋
	 * @param length 길이
	 * @return ByteBuffWrap
	 */
	public static ByteBuffWrap wrap(byte[] source, int offset, int length) {
		if (source == null || source.length <= 0) {
			return null;
		}
		return new ByteBuffWrap(source, offset, length);
	}

	/**
	 * @param wrap {@link ByteBuffWrap}
	 * @return byte array
	 */
	public static byte[] unwrap(ByteBuffWrap wrap) {
		return wrap.getByteArray();
	}

	/**
	 * @param wrap   {@link ByteBuffWrap}
	 * @param offset 옵셋
	 * @param length 길이
	 * @return byte array
	 */
	public static byte[] unwrap(ByteBuffWrap wrap, int offset, int length) {
		return wrap.getByteArray(offset, length);
	}

	public static byte[] merge(byte[]... arrays) {
		int length = 0;
		for (byte[] arr : arrays) {
			length += arr.length;
		}

		byte[] merged = new byte[length];
		int pos = 0;
		for (byte[] arr : arrays) {
			System.arraycopy(arr, 0, merged, pos, arr.length);
			pos += arr.length;
		}
		return merged;
	}

	/**
	 * 문자열 byte[]에 SOSI 시작/종료 마크 삽입 (한글 범위 감지)
	 * 
	 * @param source      원문
	 * @param charsetName charset 문자열
	 * @return
	 */
	@Deprecated
	public static String addSOSI(byte[] source, String charsetName) {
		StringBuilder result = new StringBuilder();
		boolean inMultibyte = false;
		Charset charset = Charset.forName(charsetName);

		for (int i = 0; i < source.length;) {
			int remain = source.length - i;
			byte b1 = source[i];

			if ((b1 & 0x80) != 0 && remain >= 2) { // 멀티바이트
				if (!inMultibyte) {
					result.append((char) SOSI_SO);
					inMultibyte = true;
				}
				result.append(new String(source, i, 2, charset));
				i += 2;
			} else { // 단일바이트
				if (inMultibyte) {
					result.append((char) SOSI_SI);
					inMultibyte = false;
				}
				result.append((char) b1);
				i++;
			}
		}
		if (inMultibyte) {
			result.append((char) SOSI_SI);
		}
		return result.toString();
	}

	/**
	 * SO/SI 추가하는 기능을 수행<br>
	 * ex) addSOSI("1234567890") => 0x0e+"123456789"+0x0f
	 *
	 * @param str
	 * @param size
	 * @return
	 */
	public static byte[] addSOSI(byte[] strchar, int size) {
		byte[] sep1 = new byte[] { 0x0E };
		byte[] sep2 = new byte[] { 0x0F };

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

		return generateChar;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] delSOSI(byte[] source) {
		if (source == null || source.length == 0) {
			return new byte[] {};
		}

		// SOSI 제어 문자: 0x0E (Shift Out), 0x0F (Shift In)
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		for (byte b : source) {
			if (b != 0x0E && b != 0x0F) {
				output.write(b);
			}
		}
		return output.toByteArray();
	}
	/**
	 * SOSI 마크 제거
	 * 
	 * @param source      원문
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String delSOSI(byte[] source, String charsetName) throws Exception {
		return new String(delSOSI(source), charsetName);
	}
	
	/**
     * SO/SI 바이트 포함여부를 체크한다. 
     * @param source
     * @return
     */
    public static boolean checkSosiBytes(byte[] source) {
    	for (int i = 0; i < source.length; i++) {
			if (source[i] == 0x0E || source[i] == 0x0F) {
				return true;
			}
		}
    	return false;
    }
    
    /**
     * 
     * @param source
     * @return
     */
    public static boolean checkWrongSosiBytes(byte[] source) {
    	if (source == null || source.length <= 1) {
    		return false;
    	}
    	
    	if (!checkSosiBytes(source)) {
    		return false;
    	}
    	
    	byte firstByte = source[0];
    	byte secondByte = source[1];
    	
    	return firstByte == secondByte;
    }

	/**
	 * 멀티바이트 문자가 포함되어 있는지 검사
	 */
	public static boolean containsMultibyteCharacter(byte[] source, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		String decoded = new String(source, charset);
		return decoded.codePoints().anyMatch(cp -> cp > 127);
	}

	/**
	 * @param value     필드 값
	 * @param length    필드 길이
	 * @param padding   패딩 문자
	 * @param alignType 필드 정렬타입
	 * @return 바이트 배열
	 * @throws IOException {@link IOException}
	 */
	public static byte[] writeBytes(byte[] value, int length, byte[] padding, AlignType alignType) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeBytes(out, value, length, padding, alignType);
		return out.toByteArray();
	}

	/**
	 * @param out       {@link ByteArrayOutputStream}
	 * @param value     필드 값
	 * @param length    필드 길이
	 * @param padding   피댕 문자
	 * @param alignType 필드 정렬타입 {@link AlignType}
	 * @throws IOException {@link IOException}
	 */
	public static void writeBytes(ByteArrayOutputStream out, byte[] value, int length, byte[] padding,
			AlignType alignType) throws IOException {
		ByteUtils.writeWithPadding(out, value, length, padding, AlignType.LEFT.equals(alignType));
	}

	/**
	 * 문자열을 바이트배열로 변환한다.
	 *
	 * @param value       필드 값
	 * @param length      필드 길이
	 * @param padding     패딩 문자
	 * @param alignType   필드 정렬 {@link AlignType}
	 * @param charsetName 인코딩
	 * @return 바이트 배열
	 * @throws IOException {@link IOException}
	 */
	public static byte[] writeString(String value, int length, byte[] padding, AlignType alignType, String charsetName)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeBytes(out, value.getBytes(charsetName), length, padding, alignType);
		return out.toByteArray();
	}

	/**
	 * @param out         {@link ByteArrayOutputStream}
	 * @param value       필드 값
	 * @param length      필드 길이
	 * @param padding     피댕 문자
	 * @param alignType   필드 정렬타입 {@link AlignType}
	 * @param charsetName 인코딩 문자열
	 * @throws IOException {@link IOException}
	 */
	public static void writeString(ByteArrayOutputStream out, String value, int length, byte[] padding,
			AlignType alignType, String charsetName) throws IOException {
		ByteUtils.writeBytes(out, value.getBytes(charsetName), length, padding, alignType);
	}

	/**
	 * @param value
	 * @param length
	 * @param padding
	 * @param alignType
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static byte[] writeInteger(String value, int length, byte[] padding, AlignType alignType, String charsetName)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeInteger(out, value, length, padding, alignType, charsetName);
		return out.toByteArray();
	}

	/**
	 * @param out         {@link ByteArrayOutputStream}
	 * @param value       필드 값
	 * @param length      필드 길이
	 * @param padding     피댕 문자
	 * @param alignType   필드 정렬타입 {@link AlignType}
	 * @param charsetName 인코딩 문자열
	 * @throws IOException {@link IOException}
	 */
	public static void writeInteger(ByteArrayOutputStream out, String value, int length, byte[] padding,
			AlignType alignType, String charsetName) throws IOException {
		if (value.charAt(0) == '-') {
			if (AlignType.LEFT.equals(alignType)) {
				out.write((byte) '-');
				ByteUtils.writeWithPadding(out, value.substring(1), (length - 1), padding, true, charsetName);
			} else {
				ByteUtils.writeWithPadding(out, value, length, padding, false, charsetName);
			}
		} else {
			ByteUtils.writeWithPadding(out, value, length, padding, AlignType.LEFT.equals(alignType), charsetName);
		}
	}

	/**
	 * @param out
	 * @param value
	 * @param length
	 * @param padding
	 * @param alignType
	 * @param charsetName
	 * @throws IOException
	 */
	public static void writeNumber(ByteArrayOutputStream out, Object value, int length, byte[] padding,
			AlignType alignType, String charsetName) throws IOException {

	}

	/**
	 * @param out         {@link ByteArrayOutputStream}
	 * @param value       필드 값
	 * @param length      필드 길이
	 * @param padding     피댕 문자
	 * @param alignType   필드 정렬타입 {@link AlignType}
	 * @param charsetName 인코딩 문자열
	 * @throws IOException {@link IOException}
	 */
	public static void writeBigDecimal(ByteArrayOutputStream out, BigDecimal value, int length, byte[] padding,
			AlignType alignType, String charsetName) throws IOException {

	}

	/**
	 * 고정 길이 필드에서 패딩을 제거하여 문자열을 반환
	 */
	public static String removePadding(byte[] source, byte[] paddingBytes, AlignType align, String charsetName)
			throws UnsupportedEncodingException {
		String value = new String(source, charsetName);

		if (AlignType.LEFT.equals(align)) {
			// 왼쪽 정렬 → 오른쪽 패딩 제거
			return removeTrailingPadding(value, paddingBytes, charsetName);
		} else {
			// 기본 또는 RIGHT → 왼쪽 패딩 제거
			return removeLeadingPadding(value, paddingBytes, charsetName);
		}
	}

	private static String removeLeadingPadding(String value, byte[] paddingBytes, String charsetName)
			throws UnsupportedEncodingException {
		String padChar = new String(paddingBytes, charsetName);
		int i = 0;
		while (i < value.length() && value.startsWith(padChar, i)) {
			i += padChar.length();
		}
		return value.substring(i);
	}

	private static String removeTrailingPadding(String value, byte[] paddingBytes, String charsetName)
			throws UnsupportedEncodingException {
		String padChar = new String(paddingBytes, charsetName);
		int end = value.length();
		while (end > 0 && value.startsWith(padChar, end - padChar.length())) {
			end -= padChar.length();
		}
		return value.substring(0, end);
	}

	/**
	 * @param out       {@link ByteArrayOutputStream}
	 * @param value     필드 값
	 * @param length    필드 길이
	 * @param padding   피댕 문자
	 * @param leftAlign 왼쪽 정렬 여부
	 * @param encoding  인코딩 문자열
	 * @throws IOException {@link IOException}
	 */
	public static void writeWithPadding(ByteArrayOutputStream out, String value, int length, byte[] padding,
			boolean leftAlign, String charsetName) throws IOException {
		ByteUtils.writeWithPadding(out, value.getBytes(charsetName), length, padding, leftAlign);
	}

	/**
	 * @param out       {@link ByteArrayOutputStream}
	 * @param value     필드 값
	 * @param length    필드 길이
	 * @param padding   피댕 문자
	 * @param leftAlign 왼쪽 정렬 여부
	 * @throws IOException {@link IOException}
	 */
	public static void writeWithPadding(ByteArrayOutputStream out, byte[] value, int length, byte[] padding,
			boolean leftAlign) throws IOException {
		byte[] data = (value == null) ? new byte[0] : value;

		int diff = length - data.length;
		if (diff > 0) {
			if (leftAlign) {
				out.write(data, 0, data.length);
				for (int i = 1; i <= diff;) {
					// out.write((byte) padding);
					for (byte p : padding) {
						out.write(p);
						i++;
					}
				}
			} else {
				for (int i = 1; i <= diff;) {
					for (byte p : padding) {
						out.write(p);
						i++;
					}
				}
				out.write(data, 0, data.length);
			}
		} else if (diff < 0) {
			out.write(data, 0, length);
		} else {
			out.write(data);
		}
	}

	public static String readString(ByteArrayInputStream input, int length, String charsetName) throws IOException {
		byte[] buff = new byte[length];
		input.read(buff, 0, length);
		return new String(buff, charsetName);
	}

	/**
	 * @param input       {@link ByteArrayInputStream}
	 * @param length      길이
	 * @param padding     패딩문자열
	 * @param alignType   정렬타입 {@link AlignType}
	 * @param charsetName 인코딩 문자열
	 * @param masking     마스킹 처리 여부
	 * @return
	 * @throws IOException
	 */
	public static String readString(ByteArrayInputStream input, int length, byte[] padding, AlignType alignType,
			String charsetName) throws IOException {
		byte[] destBytes = ByteUtils.readBytes(input, length, padding, alignType, charsetName);
		return new String(destBytes, charsetName);
	}

	public static String readNumber(ByteArrayInputStream input, int length, byte[] padding, AlignType alignType,
			String charsetName) throws IOException {
		return ByteUtils.readString(input, length, padding, alignType, charsetName);
	}

	/**
	 * @param input     {@link ByteArrayInputStream}
	 * @param length    길이
	 * @param padding   패딩문자열
	 * @param alignType 정렬타입
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(ByteArrayInputStream input, int length, byte[] padding, AlignType alignType,
			String charsetName) throws IOException {

		byte[] buff = new byte[length];
		int readCount = input.read(buff, 0, length);
		log.trace("ByteUtils.readBytes read count :: {}", readCount);

		if (alignType == null || AlignType.NONE.equals(alignType)) {
			log.trace("ByteUtils.readBytes alignType is null or unknown, set default align left");
			alignType = AlignType.LEFT;
		}

		if (AlignType.LEFT.equals(alignType)) {
			return ByteUtils.leftPaddingBytes(buff, 0, length, padding);
		}

		if (AlignType.RIGHT.equals(alignType)) {
			return ByteUtils.rightPaddingBytes(buff, 0, length, padding);
		}
		return buff;
	}

	/**
	 * @param buffer
	 * @param offset
	 * @param length
	 * @param padding
	 * @return
	 */
	public static byte[] leftPaddingBytes(byte[] buffer, int offset, int length, byte[] padding) {
		int paddingCount = 0;
		for (int i = buffer.length - 1; i >= 0; i--) {
			if (buffer[i] == padding[0]) {
				paddingCount++;
			} else {
				break;
			}
		}
		byte[] bytes = new byte[buffer.length - paddingCount];
		System.arraycopy(buffer, 0, bytes, 0, bytes.length);
		return bytes;
	}

	/**
	 * @param buffer
	 * @param offset
	 * @param length
	 * @param padding
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String leftPadding(byte[] buffer, int offset, int length, byte[] padding, String charsetName)
			throws IOException {
		byte[] destBytes = ByteUtils.leftPaddingBytes(buffer, offset, length, padding);
		return new String(destBytes, charsetName);
	}

	/**
	 * @param buffer
	 * @param offset
	 * @param length
	 * @param padding
	 * @return
	 */
	public static byte[] rightPaddingBytes(byte[] buffer, int offset, int length, byte[] padding) {
		int paddingCount = 0;
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] == padding[0]) {
				paddingCount++;
			} else {
				break;
			}
		}
		byte[] bytes = new byte[buffer.length - paddingCount];
		System.arraycopy(buffer, paddingCount, bytes, 0, bytes.length);
		return bytes;
	}

	/**
	 * @param buffer
	 * @param offset
	 * @param length
	 * @param padding
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String rightPadding(byte[] buffer, int offset, int length, byte[] padding, String charsetName)
			throws IOException {
		byte[] bytes = ByteUtils.rightPaddingBytes(buffer, offset, length, padding);
		return new String(bytes, charsetName);
	}

	/**
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
		if (!s.toLowerCase().startsWith("0x")) {
			throw new IllegalArgumentException("hexStringToByteArray error");
		}

		StringTokenizer st = new StringTokenizer(s, ", ");
		byte[] buf = new byte[st.countTokens()];

		for (int i = 0; st.hasMoreTokens();) {
			String aHex = st.nextToken();
			if (aHex.toLowerCase().startsWith("0x")) {
				aHex = aHex.substring(2);
			}
			buf[i++] = (byte) Integer.parseInt(aHex, 16);
		}
		return buf;
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return sb.toString();
	}

	public static byte[] masking(int length, String charsetName) {
		byte[] buff = new byte[length];
		try {
			Arrays.fill(buff, "*".getBytes(charsetName)[0]);
		} catch (Exception e) {

		}
		return buff;
	}

	/**
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToBytes(String hex) {
		if (hex.length() % 2 != 0) {
			throw new IllegalStateException("Invalid hex string length");
		}
		
		int len = hex.length();
		byte[] result = new byte[len / 2];
		
		
		for (int i = 0; i < len ; i += 2) {
			result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
		}
		
		return result;
	}
	
	/**
	 * SO/SI 바이트 중복제거
	 * @param source
	 * @return
	 */
	public static byte[] normalizeSosiBytes(byte[] source) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int prev = -1;
		
		for (byte b : source) {
			int curr = b & 0xFF;
			if ((curr == SOSI_SO && prev == SOSI_SO) ||
				(curr == SOSI_SI && prev == SOSI_SI)) {
				continue;
			}
			
			out.write(b);
			prev = curr;
		}
		
		return out.toByteArray();
	}
}