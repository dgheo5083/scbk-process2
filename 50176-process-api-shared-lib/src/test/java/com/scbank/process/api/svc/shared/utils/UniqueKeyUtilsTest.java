package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

/**
 * {@link UniqueKeyUtils} 유니크키/64진수 변환 커버리지 테스트.
 */
@DisplayName("UniqueKeyUtils")
class UniqueKeyUtilsTest {

	@DisplayName("toBase64 : 0 은 'A' 를 반환한다")
	@Test
	void toBase64_zero() {
		assertEquals("A", UniqueKeyUtils.toBase64(0));
	}

	@DisplayName("toBase64 : 양수 64진 변환")
	@Test
	void toBase64_positive() {
		assertEquals("B", UniqueKeyUtils.toBase64(1));
		// 64 -> 'A'(0), 1 -> 'B'(1) => reverse "BA"
		assertEquals("BA", UniqueKeyUtils.toBase64(64));
		// 63 -> '/'(index 63)
		assertEquals("/", UniqueKeyUtils.toBase64(63));
	}

	@DisplayName("UniqueKey : 정상 호스트IP 로 ip64+now64+random64 키를 생성한다")
	@Test
	void uniqueKey_success() {
		InetAddress local = mock(InetAddress.class);
		when(local.getHostAddress()).thenReturn("192.168.0.10");

		try (MockedStatic<InetAddress> mocked = mockStatic(InetAddress.class)) {
			mocked.when(InetAddress::getLocalHost).thenReturn(local);

			String key = UniqueKeyUtils.UniqueKey();

			assertNotNull(key);
			assertTrue(key.length() > 0);
		}
	}

	@DisplayName("UniqueKey : 호스트 조회 실패 시 IP 가 빈 문자열이 되어 NumberFormatException 이 발생한다")
	@Test
	void uniqueKey_unknownHost() {
		try (MockedStatic<InetAddress> mocked = mockStatic(InetAddress.class)) {
			mocked.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException("no host"));

			// getServerIp() 가 "" 를 반환 -> Long.valueOf("") 에서 NumberFormatException
			assertThrows(NumberFormatException.class, UniqueKeyUtils::UniqueKey);
		}
	}

	@DisplayName("생성자 커버리지")
	@Test
	void constructorCoverage() {
		assertNotNull(new UniqueKeyUtils());
	}
}
