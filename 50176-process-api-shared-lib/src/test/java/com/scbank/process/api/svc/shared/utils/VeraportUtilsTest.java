package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.MockedStatic;

import com.wizvera.util.Base64;

/**
 * {@link VeraportUtils} Base64 URL-Safe 변환 커버리지 테스트.
 */
@DisplayName("VeraportUtils")
class VeraportUtilsTest {

	@DisplayName("base64SafeDecode : 빈/널 입력은 null 을 반환한다")
	@ParameterizedTest
	@NullAndEmptySource
	void base64SafeDecode_emptyReturnsNull(String input) {
		assertNull(VeraportUtils.base64SafeDecode(input));
	}

	@DisplayName("base64SafeDecode : URL-Safe 치환 + 패딩 후 Base64 디코딩한다")
	@Test
	void base64SafeDecode_decodes() {
		byte[] decoded = "hello".getBytes(StandardCharsets.UTF_8);
		try (MockedStatic<Base64> mocked = mockStatic(Base64.class)) {
			mocked.when(() -> Base64.decode(anyString())).thenReturn(decoded);

			// '-' '_' 및 개행이 포함된 URL-Safe 문자열
			byte[] result = VeraportUtils.base64SafeDecode("aGV-sb_G8\r\n");

			assertArrayEquals(decoded, result);
		}
	}

	@DisplayName("base64SafeReplace : 빈/널 입력은 빈 문자열을 반환한다")
	@ParameterizedTest
	@NullAndEmptySource
	void base64SafeReplace_empty(String input) {
		assertEquals("", VeraportUtils.base64SafeReplace(input));
	}

	@DisplayName("base64SafeReplace : - -> + , _ -> / 치환 및 개행 제거")
	@Test
	void base64SafeReplace_replaces() {
		assertEquals("ab+cd/ef", VeraportUtils.base64SafeReplace("ab-cd_ef\r\n"));
	}

	@DisplayName("base64EncodeSafeReplace : 빈/널 입력은 빈 문자열을 반환한다")
	@ParameterizedTest
	@NullAndEmptySource
	void base64EncodeSafeReplace_empty(String input) {
		assertEquals("", VeraportUtils.base64EncodeSafeReplace(input));
	}

	@DisplayName("base64EncodeSafeReplace : + -> - , / -> _ 치환 및 개행 제거")
	@Test
	void base64EncodeSafeReplace_replaces() {
		assertEquals("ab-cd_ef", VeraportUtils.base64EncodeSafeReplace("ab+cd/ef\n\r"));
	}
}
