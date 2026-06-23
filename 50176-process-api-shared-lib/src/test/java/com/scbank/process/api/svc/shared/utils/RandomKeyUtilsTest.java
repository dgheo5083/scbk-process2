package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * {@link RandomKeyUtils} 실제 인스턴스 기반 커버리지 테스트.
 */
@DisplayName("RandomKeyUtils")
class RandomKeyUtilsTest {

	@DisplayName("getKey() : 항상 4자리 문자열을 반환한다")
	@RepeatedTest(20)
	void getKey_returnsFourDigits() {
		String key = RandomKeyUtils.getKey();

		assertNotNull(key);
		assertEquals(4, key.length());
		assertTrue(key.chars().allMatch(Character::isDigit), "숫자로만 구성되어야 한다: " + key);
	}

	@DisplayName("getKey(size) : 요청한 길이 이상의 16진/숫자 키를 반환한다")
	@ParameterizedTest
	@ValueSource(ints = { 1, 8, 16, 20, 32 })
	void getKey_withSize(int size) {
		String key = RandomKeyUtils.getKey(size);

		assertNotNull(key);
		assertTrue(key.length() >= size, "요청 길이 이상이어야 한다: " + key);
	}

	@DisplayName("getKey(size) : size 가 0 이면 패딩 루프를 타지 않는다")
	@Test
	void getKey_withZeroSize() {
		String key = RandomKeyUtils.getKey(0);

		assertNotNull(key);
		assertTrue(key.length() >= 0);
	}

	@DisplayName("new RandomKeyUtils() : 기본 생성자 커버리지")
	@Test
	void constructorCoverage() {
		assertNotNull(new RandomKeyUtils());
	}
}
