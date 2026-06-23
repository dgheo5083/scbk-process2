package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.dreammirae.kfb.mobile.SecureAnyOtp;
import com.dreammirae.kfb.mobile.SecureAnyOtpException;
import com.scbank.process.api.fw.base.exception.PRCServiceException;

/**
 * {@link MotpUtils} mOTP 암복호화 래퍼 커버리지 테스트.
 */
@DisplayName("MotpUtils")
class MotpUtilsTest {

	@DisplayName("decryptOTP : 정상 복호화 값을 반환한다")
	@Test
	void decryptOTP_success() {
		try (MockedStatic<SecureAnyOtp> mocked = mockStatic(SecureAnyOtp.class)) {
			mocked.when(() -> SecureAnyOtp.decryptOTP("ENC", "DEV")).thenReturn("PLAIN");

			assertEquals("PLAIN", MotpUtils.decryptOTP("ENC", "DEV"));
		}
	}

	@DisplayName("decryptOTP : 예외 발생 시 빈 문자열을 반환한다")
	@Test
	void decryptOTP_exception() {
		try (MockedStatic<SecureAnyOtp> mocked = mockStatic(SecureAnyOtp.class)) {
			mocked.when(() -> SecureAnyOtp.decryptOTP(anyString(), anyString()))
					.thenThrow(mock(SecureAnyOtpException.class));

			assertEquals("", MotpUtils.decryptOTP("ENC", "DEV"));
		}
	}

	@DisplayName("encryptTokenKey : 정상 암호화 값을 반환한다")
	@Test
	void encryptTokenKey_success() {
		try (MockedStatic<SecureAnyOtp> mocked = mockStatic(SecureAnyOtp.class)) {
			mocked.when(() -> SecureAnyOtp.encryptTokenKey("S", "D", "R", "P")).thenReturn("TOKEN");

			assertEquals("TOKEN", MotpUtils.encryptTokenKey("S", "D", "R", "P"));
		}
	}

	@DisplayName("encryptTokenKey : 예외 발생 시 PRCServiceException 으로 감싼다")
	@Test
	void encryptTokenKey_exception() {
		try (MockedStatic<SecureAnyOtp> mocked = mockStatic(SecureAnyOtp.class)) {
			mocked.when(() -> SecureAnyOtp.encryptTokenKey(anyString(), anyString(), anyString(), anyString()))
					.thenThrow(mock(SecureAnyOtpException.class));

			assertThrows(PRCServiceException.class, () -> MotpUtils.encryptTokenKey("S", "D", "R", "P"));
		}
	}

	@DisplayName("encryptServerChallenge : 정상 암호화 값을 반환한다")
	@Test
	void encryptServerChallenge_success() {
		try (MockedStatic<SecureAnyOtp> mocked = mockStatic(SecureAnyOtp.class)) {
			mocked.when(() -> SecureAnyOtp.encryptServerChallenge("C", "D", "R", "P")).thenReturn("CH");

			assertEquals("CH", MotpUtils.encryptServerChallenge("C", "D", "R", "P"));
		}
	}

	@DisplayName("encryptServerChallenge : 예외 발생 시 PRCServiceException 으로 감싼다")
	@Test
	void encryptServerChallenge_exception() {
		try (MockedStatic<SecureAnyOtp> mocked = mockStatic(SecureAnyOtp.class)) {
			mocked.when(() -> SecureAnyOtp.encryptServerChallenge(anyString(), anyString(), anyString(), anyString()))
					.thenThrow(mock(SecureAnyOtpException.class));

			assertThrows(PRCServiceException.class, () -> MotpUtils.encryptServerChallenge("C", "D", "R", "P"));
		}
	}
}
