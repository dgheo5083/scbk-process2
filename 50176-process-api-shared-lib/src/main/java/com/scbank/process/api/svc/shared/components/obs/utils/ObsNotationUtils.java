package com.scbank.process.api.svc.shared.components.obs.utils;

/**
 * 
 * long to 32진수 또는 64진수로 변환한다.
 * 
 * @author 929948
 *
 */
public class ObsNotationUtils {

	final static char[] digits32 = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V'
	};

	final static char[] digits64 = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', '#', '$'
	};

	public static String digits32(long x) {

		int shift = 5;
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		long mask = radix - 1;
		long number = x;

		do {
			buf[--charPos] = digits32[(int) (number & mask)];
			number >>>= shift;
		} while (number != 0);

		return new String(buf, charPos, (32 - charPos));
	}

	public static String digits64(long x) {
		int shift = 6;
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		long number = x;

		do {
			buf[--charPos] = digits64[(int) (number & mask)];
			number >>>= shift;
		} while (number != 0);

		return new String(buf, charPos, (64 - charPos));

	}
}
