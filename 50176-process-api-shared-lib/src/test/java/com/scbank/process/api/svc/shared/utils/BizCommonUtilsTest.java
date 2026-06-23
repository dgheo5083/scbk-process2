package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

/**
 * {@link BizCommonUtils#getMaskCustData(String, String)} 실제 로직 커버리지 테스트.
 */
@DisplayName("BizCommonUtils")
class BizCommonUtilsTest {

	@DisplayName("maskType 01(이름) : 길이별 마스킹 처리")
	@ParameterizedTest
	@CsvSource({
		// 2자 -> 첫글자 + *
		"'홍길', '홍*'",
		// 3자 -> 첫글자 + * + 마지막글자
		"'홍길동', '홍*동'",
		// 4자 -> 첫글자 + **(가운데전부) + 마지막글자
		"'을지문덕', '을**덕'",
		// 5자
		"'남궁민수자', '남***자'",
		// 1자(조건 미해당) -> 원본 유지
		"'김', '김'",
	})
	void getMaskCustData_name(String target, String expected) {
		assertEquals(expected, BizCommonUtils.getMaskCustData(target, "01"));
	}

	@DisplayName("maskType 02(계좌) : 끝 3~5번째 자리 마스킹")
	@ParameterizedTest
	@CsvSource({
		"'123456789', '1234***89'",
		"'4512345650', '45123***50'",
		// 길이 5 (>4) 경계
		"'12345', '***45'",
	})
	void getMaskCustData_account(String target, String expected) {
		assertEquals(expected, BizCommonUtils.getMaskCustData(target, "02"));
	}

	@DisplayName("maskType 02 : 길이 4 이하면 원본 유지")
	@ParameterizedTest
	@CsvSource({
		"'1234', '1234'",
		"'12', '12'",
	})
	void getMaskCustData_accountTooShort(String target, String expected) {
		assertEquals(expected, BizCommonUtils.getMaskCustData(target, "02"));
	}

	@DisplayName("정의되지 않은 maskType : 원본 유지")
	@Test
	void getMaskCustData_unknownType() {
		assertEquals("홍길동", BizCommonUtils.getMaskCustData("홍길동", "99"));
	}

	@DisplayName("targetStr 가 null : NPE 없이 null 반환 (catch 또는 분기)")
	@ParameterizedTest
	@NullSource
	void getMaskCustData_null(String target) {
		assertEquals(null, BizCommonUtils.getMaskCustData(target, "01"));
		assertEquals(null, BizCommonUtils.getMaskCustData(target, "02"));
	}

	@DisplayName("생성자 커버리지")
	@Test
	void constructorCoverage() {
		new BizCommonUtils();
	}
}
