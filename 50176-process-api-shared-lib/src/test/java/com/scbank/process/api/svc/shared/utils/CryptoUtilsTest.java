package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * {@link CryptoUtils} AES/CBC/PKCS5Padding 암복호화 실제 동작 커버리지 테스트.
 */
@DisplayName("CryptoUtils")
class CryptoUtilsTest {

	@DisplayName("기본 키 encrypt -> decrypt 라운드트립")
	@ParameterizedTest
	@ValueSource(strings = { "hello", "한글평문테스트", "1234567890", "{\"a\":1}" })
	void defaultKeyRoundTrip(String plain) throws Exception {
		String encrypted = CryptoUtils.encrypt(plain);

		assertNotNull(encrypted);
		assertNotEquals(plain, encrypted);
		assertEquals(plain, CryptoUtils.decrypt(encrypted));
	}

	@DisplayName("지정 키(16바이트 이하) encrypt -> decrypt 라운드트립")
	@Test
	void explicitShortKeyRoundTrip() throws Exception {
		String key = "mykey123";
		String encrypted = CryptoUtils.encrypt("payload", key);

		assertEquals("payload", CryptoUtils.decrypt(encrypted, key));
	}

	@DisplayName("지정 키(16바이트 초과) 는 16바이트로 잘려 처리된다 (len 분기)")
	@Test
	void explicitLongKeyRoundTrip() throws Exception {
		String test = "0123456789ABCDEFGHIJ"; // 20 bytes > 16
		String encrypted = CryptoUtils.encrypt("payload", test);

		assertEquals("payload", CryptoUtils.decrypt(encrypted, test));
	}

	@DisplayName("잘못된 암호문 복호화 시 예외가 발생한다")
	@Test
	void decryptInvalidThrows() {
		assertThrows(Exception.class, () -> CryptoUtils.decrypt("AAAA", "mykey123"));
	}
}
