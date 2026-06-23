package com.scbank.process.api.svc.shared.components.alchera;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import com.scbank.process.api.fw.base.exception.PRCServiceException;

import kr.co.useb.AES256;

@DisplayName("알체라 AES256 암복호화")
public class AlcAES256Test {

	@Nested
	@DisplayName("빈 입력은 기본값 반환")
	class EmptyInput {

		@Test
		@DisplayName("암복호화 메서드들은 빈 입력 시 빈/널 반환")
		public void emptyTest() {
			assertEquals("", AlcAES256.encryptBytesToString(""));
			assertNull(AlcAES256.encryptStringToBytes(""));
			assertEquals("", AlcAES256.decrypt(""));
			assertNull(AlcAES256.decryptStringToBytes(""));
		}
	}

	@Nested
	@DisplayName("정상 암복호화")
	class Success {

		@Test
		@DisplayName("AES256 위임 호출 결과 반환")
		public void delegateTest() {
			try (MockedConstruction<AES256> con = Mockito.mockConstruction(AES256.class, (m, ctx) -> {
				when(m.encryptBytesToString(any())).thenReturn("ENC");
				when(m.encryptStringToBytes(anyString())).thenReturn(new byte[] { 1 });
				when(m.decrypt(anyString())).thenReturn("DEC");
				when(m.decryptByteArray(any())).thenReturn(new byte[] { 2 });
				when(m.decryptStringToBytes(anyString())).thenReturn(new byte[] { 3 });
				when(m.decryptBytesToString(any())).thenReturn("DEC2");
			})) {
				assertEquals("ENC", AlcAES256.encryptBytesToString("plain"));
				assertEquals(1, AlcAES256.encryptStringToBytes("plain").length);
				assertEquals("DEC", AlcAES256.decrypt("enc"));
				assertEquals(1, AlcAES256.decryptByteArray("x".getBytes()).length);
				assertEquals(1, AlcAES256.decryptStringToBytes("enc").length);
				assertEquals("DEC2", AlcAES256.decryptStringToBytes("x".getBytes()));
			}
		}
	}

	@Nested
	@DisplayName("예외 처리")
	class Errors {

		@Test
		@DisplayName("암호화 오류 시 ERR_MA008")
		public void encryptErrorTest() {
			try (MockedConstruction<AES256> con = Mockito.mockConstruction(AES256.class, (m, ctx) -> {
				when(m.encryptBytesToString(any())).thenThrow(new RuntimeException("enc fail"));
			})) {
				assertThrows(PRCServiceException.class, () -> AlcAES256.encryptBytesToString("plain"));
			}
		}

		@Test
		@DisplayName("복호화 오류 시 ERR_MA007")
		public void decryptErrorTest() {
			try (MockedConstruction<AES256> con = Mockito.mockConstruction(AES256.class, (m, ctx) -> {
				when(m.decrypt(anyString())).thenThrow(new RuntimeException("dec fail"));
			})) {
				assertThrows(PRCServiceException.class, () -> AlcAES256.decrypt("enc"));
			}
		}
	}
}
