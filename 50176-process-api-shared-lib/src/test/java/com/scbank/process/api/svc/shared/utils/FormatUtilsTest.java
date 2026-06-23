package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * {@link FormatUtils} 금액/계좌/날짜/마스킹 등 표시형식 유틸 커버리지 테스트.
 * setDelim 패턴(9/s -> 원본문자, * -> 마스킹, 그 외 -> 리터럴) 의미에 기반해 기대값을 산출한다.
 */
@DisplayName("FormatUtils")
class FormatUtilsTest {

	// ---------------- 금액 ----------------

	@DisplayName("getFrmMoney : 천단위 구분 / 널·빈값·숫자아님 처리")
	@Test
	void getFrmMoney() {
		assertEquals("0", FormatUtils.getFrmMoney(null));
		assertEquals("0", FormatUtils.getFrmMoney("  "));
		assertEquals("0", FormatUtils.getFrmMoney("0"));
		assertEquals("1,234,567", FormatUtils.getFrmMoney("1234567"));
		assertEquals("abc", FormatUtils.getFrmMoney("abc"));
	}

	@DisplayName("getFrmWon : 끝 4자리 제거 후 만단위 표시")
	@Test
	void getFrmWon() {
		assertEquals("0", FormatUtils.getFrmWon(null));
		assertEquals("12,345", FormatUtils.getFrmWon("123456789"));
		assertEquals("0", FormatUtils.getFrmWon("1234")); // 4자리 제거 시 빈값
		assertEquals("abc1", FormatUtils.getFrmWon("abc12345")); // 숫자 아님
	}

	@DisplayName("getFrmMoney2 : 하이픈 분리 금액")
	@Test
	void getFrmMoney2() {
		assertEquals("0", FormatUtils.getFrmMoney2(null));
		assertEquals("0", FormatUtils.getFrmMoney2("  "));
		assertEquals("34", FormatUtils.getFrmMoney2("12-34"));
		assertEquals("123", FormatUtils.getFrmMoney2("123")); // split 후 예외 -> getFrmMoney
	}

	@DisplayName("getFrmMoney3 : 끝 3자리 제거 후 천단위")
	@Test
	void getFrmMoney3() {
		assertEquals("0", FormatUtils.getFrmMoney3(null));
		assertEquals("0", FormatUtils.getFrmMoney3(""));
		assertEquals("0", FormatUtils.getFrmMoney3("0"));
		assertEquals("1,234", FormatUtils.getFrmMoney3("1234567"));
		assertEquals("a", FormatUtils.getFrmMoney3("abc1"));
	}

	// ---------------- 계좌 ----------------

	@DisplayName("getAcct : 자행/타행/마스킹 분기")
	@Test
	void getAcct() {
		assertEquals("", FormatUtils.getAcct("23", null, "N"));
		assertEquals("111 22 233344", FormatUtils.getAcct("23", "11122233344", "N"));
		assertEquals("111 2* ***34*", FormatUtils.getAcct("23", "11122233344", "Y"));
		assertEquals("1234******", FormatUtils.getAcct("99", "1234567890", "N")); // 타행
		assertEquals("123", FormatUtils.getAcct("23", "123", "N")); // 미해당 길이
	}

	@DisplayName("getFrmAcct : 1·2·3 인자 오버로드 및 마스킹")
	@Test
	void getFrmAcct() {
		assertEquals("", FormatUtils.getFrmAcct("23", null, "N"));
		assertEquals("111-22-233344", FormatUtils.getFrmAcct("11122233344")); // 기본 23
		assertEquals("111-22-233344", FormatUtils.getFrmAcct("23", "11122233344"));
		assertEquals("111-2*-***34*", FormatUtils.getFrmAcct("23", "11122233344", "Y"));
		assertEquals("1234******", FormatUtils.getFrmAcct("99", "1234567890", "N"));
		assertEquals("123", FormatUtils.getFrmAcct("23", "123", "N"));
	}

	@DisplayName("getFrmAcct(4) : 금소법 마스킹 타입")
	@Test
	void getFrmAcct_maskType() {
		assertEquals("", FormatUtils.getFrmAcct("23", null, "Y", "1"));
		assertEquals("111-22-***344", FormatUtils.getFrmAcct("23", "11122233344", "Y", "1"));
		assertEquals("111-2*-***34*", FormatUtils.getFrmAcct("23", "11122233344", "Y", ""));
		assertEquals("111-22-233344", FormatUtils.getFrmAcct("23", "11122233344", "N", ""));
	}

	@DisplayName("getFrmAcct_hidden : 마스킹 위임")
	@Test
	void getFrmAcct_hidden() {
		assertEquals("111-2*-***34*", FormatUtils.getFrmAcct_hidden("11122233344"));
		assertEquals("111-2*-***34*", FormatUtils.getFrmAcct_hidden("23", "11122233344"));
		assertEquals("111-22-***344", FormatUtils.getFrmAcct_hidden("11122233344", "Y", "1"));
	}

	// ---------------- 날짜/시간 ----------------

	@DisplayName("getFrmTime : HH:MM / HH:MM:SS")
	@Test
	void getFrmTime() {
		assertEquals("", FormatUtils.getFrmTime(null));
		assertEquals("12:34:56", FormatUtils.getFrmTime("123456"));
		assertEquals("12:34", FormatUtils.getFrmTime("1234"));
		assertEquals("12345", FormatUtils.getFrmTime("12345")); // 미해당
	}

	@DisplayName("getFrmDateTime / getFrmDateMonth / getFrmTax")
	@Test
	void dateTimeTaxFormats() {
		assertEquals("", FormatUtils.getFrmDateTime(null));
		assertEquals("2024-01-15 12:30:45", FormatUtils.getFrmDateTime("20240115123045"));
		assertEquals("202401", FormatUtils.getFrmDateTime("202401")); // 길이 != 14
		assertEquals("", FormatUtils.getFrmDateMonth(null));
		assertEquals("2024-01", FormatUtils.getFrmDateMonth("202401"));
		assertEquals("", FormatUtils.getFrmTax(null));
		assertEquals("123-4567-8901", FormatUtils.getFrmTax("12345678901"));
	}

	@DisplayName("getFrmDcgj / getFrmFexAcct : 길이별 포맷")
	@Test
	void loanAndFexFormats() {
		assertEquals("", FormatUtils.getFrmDcgj(null));
		assertEquals("123-45-678901-2345", FormatUtils.getFrmDcgj("123456789012345")); // 15
		assertEquals("123-4567-8901-23", FormatUtils.getFrmDcgj("1234567890123")); // 13
		assertEquals("123-45-678901-2", FormatUtils.getFrmDcgj("123456789012")); // 12
		assertEquals("12", FormatUtils.getFrmDcgj("12")); // 미해당

		assertEquals("", FormatUtils.getFrmFexAcct(null));
		assertEquals("123-45-6789012345", FormatUtils.getFrmFexAcct("123456789012345")); // 15
		assertEquals("123-4567-8901-23", FormatUtils.getFrmFexAcct("1234567890123")); // 13
		assertEquals("12", FormatUtils.getFrmFexAcct("12")); // 미해당
	}

	@DisplayName("getFrmDate 계열 : 구분자별 날짜 포맷")
	@Test
	void dateFormats() {
		assertEquals("", FormatUtils.getFrmDate(null));
		assertEquals("2024.01.15", FormatUtils.getFrmDate("20240115"));
		assertEquals("2024-01-15", FormatUtils.getFrmmDate("20240115"));
		assertEquals("20240115", FormatUtils.getFrmDatedate("20240115"));
		assertEquals("2024", FormatUtils.getFrmDate("2024")); // 길이 != 8
		assertEquals("2024-01-15", FormatUtils.getDate("20240115", "-"));
		assertEquals("2024.01.15", FormatUtils.getDate("20240115", "")); // 기본 구분자 .
	}

	// ---------------- 카드/펀드/전화 ----------------

	@DisplayName("getFrmCard : 카드번호 포맷/마스킹")
	@Test
	void getFrmCard() {
		assertEquals("1234-5678-9012-3456", FormatUtils.getFrmCard("1234567890123456", false));
		assertEquals("1234-5678-****-3456", FormatUtils.getFrmCard("1234567890123456", true));
	}

	@DisplayName("getFrmFund : 펀드 계좌번호")
	@Test
	void getFrmFund() {
		assertEquals("", FormatUtils.getFrmFund(null));
		assertEquals("123-4567-890", FormatUtils.getFrmFund("1234567890"));
		assertEquals("123", FormatUtils.getFrmFund("123")); // 미해당
	}

	@DisplayName("getFrmPhone : 전화번호 포맷/마스킹")
	@Test
	void getFrmPhone() {
		assertEquals("010-1234-5678", FormatUtils.getFrmPhone("01012345678", "-"));
		assertEquals("010-****-5678", FormatUtils.getFrmPhone("01012345678", "-", "Y"));
	}

	// ---------------- setDelim ----------------

	@DisplayName("setDelim : 기본 동작 및 가드")
	@Test
	void setDelim() {
		assertEquals("", FormatUtils.setDelim(null, "9999-99-99"));
		assertEquals("", FormatUtils.setDelim("", "9999-99-99"));
		assertEquals("", FormatUtils.setDelim("0", "9999-99-99"));
		assertEquals("2000-12-15", FormatUtils.setDelim("20001215", "9999-99-99"));
		assertEquals("123", FormatUtils.setDelim("123", "9999-99-99")); // 길이 불일치 -> 원본
	}

	// ---------------- 주민/사업자/외국인 ----------------

	@DisplayName("getFrmSabh : 사업자/주민/외국인 번호 마스킹")
	@Test
	void getFrmSabh() {
		assertEquals("", FormatUtils.getFrmSabh(null));
		assertEquals("", FormatUtils.getFrmSabh(""));
		assertEquals("123-45-67890", FormatUtils.getFrmSabh("1234567890")); // 사업자 평문
		assertEquals("123-**-****0", FormatUtils.getFrmSabh("1234567890", "Y")); // 사업자 마스킹
		assertEquals("123456-7890123", FormatUtils.getFrmSabh("1234567890123")); // 주민 평문
		assertEquals("123456-7******", FormatUtils.getFrmSabh("1234567890123", "Y")); // 주민 마스킹
		assertEquals("123456-******-3", FormatUtils.getFrmSabh("1234567890123", "Y", "Y")); // 외국인 마스킹
		assertEquals("123456-789012-3", FormatUtils.getFrmSabh("1234567890123", "N", "Y")); // 외국인 평문
		assertEquals("123", FormatUtils.getFrmSabh("123")); // 미해당 길이
	}

	@DisplayName("getFrmForeignNo : 외국인등록번호")
	@Test
	void getFrmForeignNo() {
		assertEquals("123456-789012-3", FormatUtils.getFrmForeignNo("1234567890123"));
		assertEquals("123456-789012-3", FormatUtils.getFrmForeignNo("1234567890123", "N"));
	}

	// ---------------- 이름/여권/면허/건강보험 ----------------

	@DisplayName("getFrmName : 한글/영문 이름 마스킹")
	@Test
	void getFrmName() {
		assertEquals("홍길동", FormatUtils.getFrmName("홍길동", "N")); // 마스킹 미적용
		assertEquals("홍*", FormatUtils.getFrmName("홍길"));
		assertEquals("홍*동", FormatUtils.getFrmName("홍길동"));
		assertEquals("홍*동순", FormatUtils.getFrmName("홍길동순"));
		assertEquals("***", FormatUtils.getFrmName("abc"));      // 영문 6자 이하
		assertEquals("******gh", FormatUtils.getFrmName("abcdefgh")); // 영문 6자 초과
	}

	@DisplayName("getFrmPassPort : 여권번호 마스킹")
	@Test
	void getFrmPassPort() {
		assertEquals("*12345***", FormatUtils.getFrmPassPort("M12345678"));
		assertEquals("M12345678", FormatUtils.getFrmPassPort("M12345678", "N"));
	}

	@DisplayName("getFrmDriverLicenseNo : 운전면허번호")
	@Test
	void getFrmDriverLicenseNo() {
		assertEquals("11-12-345678-90", FormatUtils.getFrmDriverLicenseNo("111234567890", "N"));
		assertEquals("11-1*-3456**-*0", FormatUtils.getFrmDriverLicenseNo("111234567890"));
	}

	@DisplayName("getFrmHealthInsuranceNo : 건강보험번호")
	@Test
	void getFrmHealthInsuranceNo() {
		assertEquals("1-23456789", FormatUtils.getFrmHealthInsuranceNo("123456789", "N"));
		assertEquals("*-2****789", FormatUtils.getFrmHealthInsuranceNo("123456789"));
	}

	// ---------------- MAC/이메일/주소/IP ----------------

	@DisplayName("getMacAddress : 뒷자리 마스킹")
	@Test
	void getMacAddress() {
		assertEquals("00:11:22:33:**-**", FormatUtils.getMacAddress("00:11:22:33:44:55"));
		assertEquals("0011", FormatUtils.getMacAddress("0011")); // 17자 아님
	}

	@DisplayName("getFrmEmail : 길이별 로컬파트 마스킹")
	@Test
	void getFrmEmail() {
		assertEquals("a@x.com", FormatUtils.getFrmEmail("a@x.com", "N")); // 미적용
		assertEquals("*@x.com", FormatUtils.getFrmEmail("a@x.com"));
		assertEquals("**@x.com", FormatUtils.getFrmEmail("ab@x.com"));
		assertEquals("**c@x.com", FormatUtils.getFrmEmail("abc@x.com"));
		assertEquals("**cd@x.com", FormatUtils.getFrmEmail("abcd@x.com"));
		assertEquals("**cd*@x.com", FormatUtils.getFrmEmail("abcde@x.com"));
		assertEquals("**cd**@x.com", FormatUtils.getFrmEmail("abcdef@x.com"));
		assertEquals("**cd**g@x.com", FormatUtils.getFrmEmail("abcdefg@x.com"));
	}

	@DisplayName("getFrmAddr : 숫자만 마스킹")
	@Test
	void getFrmAddr() {
		assertEquals("서울 ***번지", FormatUtils.getFrmAddr("서울 123번지"));
		assertEquals("서울 123번지", FormatUtils.getFrmAddr("서울 123번지", "N"));
	}

	@DisplayName("getFrmIp : IP 마스킹")
	@Test
	void getFrmIp() {
		assertEquals("192.168.0.1", FormatUtils.getFrmIp("192.168.0.1", "N"));
		assertTrue(FormatUtils.getFrmIp("192.168.0.1").contains("***"));
	}

	// ---------------- 외화 ----------------

	@DisplayName("getFrmFore : 외화 소수점 표시")
	@Test
	void getFrmFore() {
		assertEquals("0.00", FormatUtils.getFrmFore("", null, 2));
		assertEquals("0.00", FormatUtils.getFrmFore("", "", 2));
		assertEquals("1234", FormatUtils.getFrmFore("JPY", "1234", 2)); // JPY 예외
		assertEquals("12.34", FormatUtils.getFrmFore("", "1234", 2));
		assertEquals("1,234.56", FormatUtils.getFrmFore("123456", 2)); // 2-arg
		assertEquals("0.00", FormatUtils.getFrmFore("", "000000", 2));
		assertEquals("-123.45", FormatUtils.getFrmFore("", "-12345", 2));
	}

	@DisplayName("getFrmFore2 : 외화 3자리 표시")
	@Test
	void getFrmFore2() {
		assertEquals("0", FormatUtils.getFrmFore2("", "", 0));
		assertEquals("0.000", FormatUtils.getFrmFore2("", "", 3));
		assertEquals("123", FormatUtils.getFrmFore2("JPY", "123")); // JPY 예외
		assertEquals("123.456", FormatUtils.getFrmFore2("", "123456", 3));
		assertEquals("123.456", FormatUtils.getFrmFore2("123456")); // 1-arg
		assertEquals("1,234.567", FormatUtils.getFrmFore2("1234567", 3));
	}

	@DisplayName("getFrmFore3 : DecimalFormat 외화 표시")
	@Test
	void getFrmFore3() {
		assertEquals("0", FormatUtils.getFrmFore3("", "", 0, 0));
		assertEquals("0.000", FormatUtils.getFrmFore3("", "", 3, 3));
		assertEquals("123", FormatUtils.getFrmFore3("JPY", "123")); // JPY 예외
		assertEquals("123.456", FormatUtils.getFrmFore3("", "123456", 3, 3));
		assertEquals("123.456", FormatUtils.getFrmFore3("123456", 3)); // 2-arg
		assertEquals("123.46", FormatUtils.getFrmFore3("123456", 3, 2)); // 반올림
		assertEquals("123.456", FormatUtils.getFrmFore3("123456")); // 1-arg -> Fore2
	}

//	@DisplayName("getDisplayNumber : 외화 소수점 조합")
//	@Test
//	void getDisplayNumber() {
//		assertEquals("0", FormatUtils.getDisplayNumber("", "x", "y"));
//		assertEquals("100", FormatUtils.getDisplayNumber("100", "x", "y"));
//		assertEquals("100.50", FormatUtils.getDisplayNumber("100.50", "x", "y"));
//		assertEquals("10.50", FormatUtils.getDisplayNumber("10.5", "x", "y")); // 소수 2자리 -> 0 패딩
//	}

	// ---------------- getNumberFormat (가드/0 분기만) ----------------

	@DisplayName("getNumberFormat : 널/빈값 및 0 처리")
	@Test
	void getNumberFormat() {
		assertEquals("", FormatUtils.getNumberFormat(null, "1", "#,#00"));
		assertEquals("0", FormatUtils.getNumberFormat("0", "1", "#,#00"));
		assertEquals("0.00", FormatUtils.getNumberFormat("0", "1", "#,#00.00"));
	}

	// ---------------- 이율 ----------------

	@DisplayName("getRateFormat : 자릿수별 이율 (type=0)")
	@Test
	void getRateFormat() {
		assertEquals("", FormatUtils.getRateFormat(""));
		assertEquals("12.340%", FormatUtils.getRateFormat("1234567"));
		assertEquals("1.230%", FormatUtils.getRateFormat("123456"));
		assertEquals("12.30%", FormatUtils.getRateFormat("12345"));
		assertEquals("1.200%", FormatUtils.getRateFormat("1234")); // type 0
		assertEquals("0.123%", FormatUtils.getRateFormat("123"));
		assertEquals("0.012%", FormatUtils.getRateFormat("12"));
		assertEquals("0.001%", FormatUtils.getRateFormat("1"));
		assertEquals("0%", FormatUtils.getRateFormat("123456789")); // 미해당 길이
	}

	@DisplayName("getRateFormat : type=1 분기 (4자리)")
	@Test
	void getRateFormat_type1() {
		ReflectionTestUtils.setField(FormatUtils.class, "type", 1);
		try {
			assertEquals("1.230%", FormatUtils.getRateFormat("1234"));
		} finally {
			ReflectionTestUtils.setField(FormatUtils.class, "type", 0);
		}
	}

	@DisplayName("getFundRate : 펀드 이율")
	@Test
	void getFundRate() {
		assertEquals("", FormatUtils.getFundRate(""));
		assertEquals("12345.67%", FormatUtils.getFundRate("1234567"));
		assertEquals("1234.56%", FormatUtils.getFundRate("123456"));
		assertEquals("123.45%", FormatUtils.getFundRate("12345"));
		assertEquals("12.34%", FormatUtils.getFundRate("1234"));
		assertEquals("1.23%", FormatUtils.getFundRate("123"));
		assertEquals("0.12%", FormatUtils.getFundRate("12"));
		assertEquals("0.05%", FormatUtils.getFundRate("5")); // 1자리
		assertEquals("0%", FormatUtils.getFundRate("12345678")); // 미해당
	}

	@DisplayName("getForRate : 외화 이율 (type=0)")
	@Test
	void getForRate() {
		assertEquals("", FormatUtils.getForRate(""));
		assertEquals("12.340%", FormatUtils.getForRate("1234567"));
		assertEquals("1.200%", FormatUtils.getForRate("1234"));
		assertEquals("0.123%", FormatUtils.getForRate("123"));
		assertEquals("0%", FormatUtils.getForRate("123456789"));
	}

	@DisplayName("getForeignFormat : 외환 이율")
	@Test
	void getForeignFormat() {
		assertEquals("123.67%", FormatUtils.getForeignFormat("1234567"));
		assertEquals("12.56%", FormatUtils.getForeignFormat("123456"));
		assertEquals("1.45%", FormatUtils.getForeignFormat("12345"));
		assertEquals("0.12%", FormatUtils.getForeignFormat("1234"));
		assertEquals("0.01%", FormatUtils.getForeignFormat("123"));
		assertEquals("0.001%", FormatUtils.getForeignFormat("12"));
		assertEquals("0.0001%", FormatUtils.getForeignFormat("1"));
		assertEquals("0%", FormatUtils.getForeignFormat("12345678"));
	}

	// ---------------- 나이/성별/숫자추출 ----------------

	@DisplayName("getCurrentYear : 현재 연도")
	@Test
	void getCurrentYear() {
		assertTrue(FormatUtils.getCurrentYear() >= 2024);
	}

	@DisplayName("getInsuAge : 보험연령")
	@Test
	void getInsuAge() {
		assertEquals(0, FormatUtils.getInsuAge("1111111111111", "20240101"));
		assertEquals(0, FormatUtils.getInsuAge("0000000000000", "20240101"));
		assertEquals(34, FormatUtils.getInsuAge("9001011234567", "20240101")); // 1990년생
		assertEquals(0, FormatUtils.getInsuAge("abc", "x")); // 예외 -> 0
	}

	@DisplayName("formatNumber : 숫자만 추출")
	@Test
	void formatNumber() {
		assertEquals("", FormatUtils.formatNumber(null));
		assertEquals("12", FormatUtils.formatNumber("a1b2"));
		assertEquals("", FormatUtils.formatNumber("abc"));
	}

	@DisplayName("getGender : 주민번호 성별")
	@Test
	void getGender() {
		assertEquals("1", FormatUtils.getGender(null));
		assertEquals("1", FormatUtils.getGender("123")); // 7자리 미만
		assertEquals("1", FormatUtils.getGender("9001011234567")); // 성별 1
		assertEquals("2", FormatUtils.getGender("9001012234567")); // 성별 2
	}
}
