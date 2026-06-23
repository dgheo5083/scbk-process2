package com.scbank.process.api.svc.shared.components.mydata.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("암복호화 유틸 CryptoUtil")
public class CryptoUtilTest {

	private static final String ID = "0123456789abcdef"; // 16 byte

	@Nested
	@DisplayName("SHA 해시")
	class Sha {

		@Test
		@DisplayName("SHA-256/384/512 해시 생성")
		public void shaTest() {
			assertTrue(CryptoUtil.generateSHA256("key", "message").length() > 0);
			assertTrue(CryptoUtil.generateSHA384("key", "message").length() > 0);
			assertTrue(CryptoUtil.generateSHA512("key", "message").length() > 0);
		}

		@Test
		@DisplayName("getEncrypt - 잘못된 알고리즘이면 원본 반환")
		public void invalidAlgorithmTest() {
			assertEquals("source", CryptoUtil.getEncrypt("source", "key".getBytes(), "INVALID-ALG"));
		}
	}

	@Nested
	@DisplayName("ARIA 암복호화")
	class Aria {

		@Test
		@DisplayName("encrypt → decrypt 라운드트립")
		public void roundTripTest() {
			String enc = CryptoUtil.encrypt(ID, "마이데이터 테스트");
			assertNotNull(enc);

			String dec = CryptoUtil.decrypt(ID, enc);
			assertEquals("마이데이터 테스트", dec);
		}

		@Test
		@DisplayName("encrypt - 부적합 키 길이는 오류 메시지 반환(예외 흡수)")
		public void invalidKeyTest() {
			assertNotNull(CryptoUtil.encrypt("short", "message"));
		}

		@Test
		@DisplayName("내부통신용 암복호화 호출")
		public void innerTransTest() {
			assertNotNull(CryptoUtil.innerTransEncrypt(ID, "data"));
			assertNotNull(CryptoUtil.innerTransDecrypt(ID, "data"));
		}
	}
}
