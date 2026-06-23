package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * {@link SecurityFilterUtils#getPMFilter(String)} Path Manipulation 필터 커버리지 테스트.
 * (lombok @UtilityClass 로 static 메서드)
 */
@DisplayName("SecurityFilterUtils")
class SecurityFilterUtilsTest {

	@DisplayName("null / 빈 문자열은 빈 문자열을 반환한다")
	@ParameterizedTest
	@NullAndEmptySource
	void getPMFilter_nullOrEmpty(String input) {
		assertEquals("", SecurityFilterUtils.getPMFilter(input));
	}

	@DisplayName("../ 경로 조작 패턴을 제거한다")
	@ParameterizedTest
	@CsvSource({
		"'../etc/passwd', 'etc/passwd'",
		"'../../a/b', 'a/b'",
		"'normal/path.txt', 'normal/path.txt'",
		"'no-traversal', 'no-traversal'",
	})
	void getPMFilter_removesTraversal(String input, String expected) {
		assertEquals(expected, SecurityFilterUtils.getPMFilter(input));
	}
}
