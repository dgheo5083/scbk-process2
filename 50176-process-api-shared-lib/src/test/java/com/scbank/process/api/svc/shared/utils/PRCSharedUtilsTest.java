package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.base.utils.CodeUtils;

/**
 * {@link PRCSharedUtils} 채널/언어/계좌명/디바이스 헤더 유틸 커버리지 테스트.
 */
@DisplayName("PRCSharedUtils")
class PRCSharedUtilsTest {

	// ---------------- getScBankName ----------------

	@DisplayName("getScBankName : ';;' 구분자 앞부분을 반환한다")
	@Test
	void getScBankName() {
		try (MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			code.when(() -> CodeUtils.getCodeValue(eq("BKCODE_OBS"), anyString()))
					.thenReturn("SC제일은행;;기타");
			assertEquals("SC제일은행", PRCSharedUtils.getScBankName());
		}
	}

	@DisplayName("getScBankName : 코드값이 없으면 빈 문자열")
	@Test
	void getScBankName_null() {
		try (MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			code.when(() -> CodeUtils.getCodeValue(eq("BKCODE_OBS"), anyString())).thenReturn(null);
			assertEquals("", PRCSharedUtils.getScBankName()); // nvl -> ";;" -> split[0] = ""
		}
	}

	// ---------------- getAccountName ----------------

	@DisplayName("getAccountName : assort 가 null 이면 빈 문자열")
	@Test
	void getAccountName_nullAssort() {
		assertEquals("", PRCSharedUtils.getAccountName("ko", "12345", null));
	}

	@DisplayName("getAccountName : 1차 조회(kwamok+assort) 성공")
	@Test
	void getAccountName_primary() {
		try (MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "1234501", "ko")).thenReturn("보통예금");
			assertEquals("보통예금", PRCSharedUtils.getAccountName("ko", "12345", "01"));
		}
	}

	@DisplayName("getAccountName : 1차 실패 시 kwamok+FF, 그 다음 kwamok 으로 폴백")
	@Test
	void getAccountName_fallback() {
		try (MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "1234501", "ko")).thenReturn("");
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "12345FF", "ko")).thenReturn(null);
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "12345", "ko")).thenReturn("과목명");
			assertEquals("과목명", PRCSharedUtils.getAccountName("ko", "12345", "01"));
		}
	}

	@DisplayName("getAccountName(2-arg) : 로케일 언어로 위임한다")
	@Test
	void getAccountName_twoArg() {
		try (MockedStatic<LocaleContextHolder> loc = mockStatic(LocaleContextHolder.class);
				MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			loc.when(LocaleContextHolder::getLocale).thenReturn(Locale.KOREAN);
			code.when(() -> CodeUtils.getCodeValue(eq("ACCTNM"), eq("1234501"), anyString())).thenReturn("보통예금");
			assertEquals("보통예금", PRCSharedUtils.getAccountName("12345", "01"));
		}
	}

	// ---------------- getCardNameByKind ----------------

	@DisplayName("getCardNameByKind : 종류 코드별 카드명")
	@Test
	void getCardNameByKind() {
		assertEquals("", PRCSharedUtils.getCardNameByKind(null));
		assertEquals("", PRCSharedUtils.getCardNameByKind(""));
		assertEquals("BC", PRCSharedUtils.getCardNameByKind("1"));
		assertEquals("VISA", PRCSharedUtils.getCardNameByKind("2"));
		assertEquals("JCB", PRCSharedUtils.getCardNameByKind("3"));
		assertEquals("MASTER", PRCSharedUtils.getCardNameByKind("4"));
		assertEquals("CUP", PRCSharedUtils.getCardNameByKind("6"));
		assertEquals("GLOBAL", PRCSharedUtils.getCardNameByKind("7"));
		assertEquals("", PRCSharedUtils.getCardNameByKind("9")); // 미정의
	}

	// ---------------- getLoanAccountName ----------------

	@DisplayName("getLoanAccountName : kwamok 없으면 안내 문구")
	@Test
	void getLoanAccountName_noKwamok() {
		assertEquals("code값이 없습니다.", PRCSharedUtils.getLoanAccountName("ko", "", "01", "L", "K"));
	}

	@DisplayName("getLoanAccountName : assort null 이면 빈 문자열")
	@Test
	void getLoanAccountName_nullAssort() {
		assertEquals("", PRCSharedUtils.getLoanAccountName("ko", "12345", null, "L", "K"));
	}

	@DisplayName("getLoanAccountName : 대출계정/자금용도 코드 조회 성공")
	@Test
	void getLoanAccountName_loanCode() {
		try (MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			code.when(() -> CodeUtils.getCodeValue("OGYECDACCTNM", "K|L")).thenReturn("대출상품");
			assertEquals("대출상품", PRCSharedUtils.getLoanAccountName("ko", "12345", "01", "L", "K"));
		}
	}

	@DisplayName("getLoanAccountName : 모든 조회 실패 시 마지막 과목조회까지 폴백")
	@Test
	void getLoanAccountName_fallbackChain() {
		try (MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			// loanKind/loanAcctKmCd 비어있어 첫 조회 스킵, 이후 단계적으로 폴백
			code.when(() -> CodeUtils.getCodeValue("OGYECDACCTNM", "K|FFF")).thenReturn("");
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "1234501")).thenReturn("");
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "12345FF")).thenReturn(null);
			code.when(() -> CodeUtils.getCodeValue("ACCTNM", "12345")).thenReturn("최종과목명");
			assertEquals("최종과목명", PRCSharedUtils.getLoanAccountName("ko", "12345", "01", "", "K"));
		}
	}

	@DisplayName("getLoanAccountName(4-arg) : 로케일 언어로 위임한다")
	@Test
	void getLoanAccountName_fourArg() {
		try (MockedStatic<LocaleContextHolder> loc = mockStatic(LocaleContextHolder.class);
				MockedStatic<CodeUtils> code = mockStatic(CodeUtils.class)) {
			loc.when(LocaleContextHolder::getLocale).thenReturn(Locale.KOREAN);
			code.when(() -> CodeUtils.getCodeValue("OGYECDACCTNM", "K|L")).thenReturn("대출상품");
			assertEquals("대출상품", PRCSharedUtils.getLoanAccountName("12345", "01", "L", "K"));
		}
	}

	// ---------------- 언어/채널/OS 판별 ----------------

	private MockedStatic<ThreadLocalStoreDelegator> tlsd() {
		return mockStatic(ThreadLocalStoreDelegator.class);
	}

	@DisplayName("isKorean / isEnglish : 언어 헤더 기반 판별")
	@Test
	void isKoreanEnglish() {
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getLanguageHeader).thenReturn("ko");
			assertTrue(PRCSharedUtils.isKorean());
			assertFalse(PRCSharedUtils.isEnglish());
		}
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getLanguageHeader).thenReturn("en");
			assertFalse(PRCSharedUtils.isKorean());
			assertTrue(PRCSharedUtils.isEnglish());
		}
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getLanguageHeader).thenReturn("zz");
			assertFalse(PRCSharedUtils.isKorean());
			assertFalse(PRCSharedUtils.isEnglish());
		}
	}

	@DisplayName("ibVanType : 언어별 IB VAN 코드")
	@Test
	void ibVanType() {
		assertEquals("15", PRCSharedUtils.ibVanType("ko"));
		assertEquals("14", PRCSharedUtils.ibVanType("en"));
		assertEquals("", PRCSharedUtils.ibVanType("zz"));
	}

	@DisplayName("sbVanType : 언어별 SB VAN 코드")
	@Test
	void sbVanType() {
		assertEquals("47", PRCSharedUtils.sbVanType("ko"));
		assertEquals("47", PRCSharedUtils.sbVanType("en"));
		assertEquals("", PRCSharedUtils.sbVanType("zz"));
	}

	@DisplayName("getVanType : 채널 분기를 타지만 반환값은 항상 빈 문자열")
	@Test
	void getVanType() {
		assertEquals("", PRCSharedUtils.getVanType("ko", "MB")); // SMART
		assertEquals("", PRCSharedUtils.getVanType("ko", "IB")); // INTERNET
		assertEquals("", PRCSharedUtils.getVanType("ko", "XX")); // 미해당

		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getChannelId).thenReturn("MB");
			t.when(ThreadLocalStoreDelegator::getLanguageHeader).thenReturn("ko");
			assertEquals("", PRCSharedUtils.getVanType());
		}
	}

	@DisplayName("isSB / isIB : 채널 ID 기반 판별")
	@Test
	void isSbIb() {
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getChannelId).thenReturn("MB");
			assertTrue(PRCSharedUtils.isSB());
			assertFalse(PRCSharedUtils.isIB());
		}
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getChannelId).thenReturn("IB");
			assertFalse(PRCSharedUtils.isSB());
			assertTrue(PRCSharedUtils.isIB());
		}
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getChannelId).thenReturn("XX");
			assertFalse(PRCSharedUtils.isSB());
			assertFalse(PRCSharedUtils.isIB());
		}
	}

	@DisplayName("isAndroid : OS 타입 기반 판별 (대소문자 무시)")
	@Test
	void isAndroid() {
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getOsType).thenReturn("ANDROID");
			assertTrue(PRCSharedUtils.isAndroid());
		}
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getOsType).thenReturn("ios");
			assertFalse(PRCSharedUtils.isAndroid());
		}
	}

	// ---------------- 헤더/디바이스 위임 메서드 ----------------

	@DisplayName("ThreadLocalStoreDelegator 위임 메서드들")
	@Test
	void delegators() {
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getLanguageHeader).thenReturn("ko");
			t.when(ThreadLocalStoreDelegator::getChannelId).thenReturn("MB");
			t.when(ThreadLocalStoreDelegator::getDeviceId).thenReturn("DEV");
			t.when(ThreadLocalStoreDelegator::getAppVersion).thenReturn("1.0.0");
			t.when(ThreadLocalStoreDelegator::getOsType).thenReturn("ANDROID");
			t.when(ThreadLocalStoreDelegator::getOsVersion).thenReturn("13");
			t.when(ThreadLocalStoreDelegator::getScreenId).thenReturn("SCR");
			t.when(ThreadLocalStoreDelegator::getMenuId).thenReturn("MENU");
			t.when(ThreadLocalStoreDelegator::getIpinsideIp).thenReturn("1.1.1.1");
			t.when(ThreadLocalStoreDelegator::getIpinsideAx).thenReturn("AX");
			t.when(ThreadLocalStoreDelegator::getIpinsideMac).thenReturn("MAC");
			t.when(ThreadLocalStoreDelegator::getIpinsideHdd).thenReturn("HDD");
			t.when(ThreadLocalStoreDelegator::getDeviceUUID).thenReturn("UUID");
			t.when(ThreadLocalStoreDelegator::getSimSerial).thenReturn("SIM");

			assertEquals("ko", PRCSharedUtils.getLanguageHeader());
			assertEquals("MB", PRCSharedUtils.getChannelId());
			assertEquals("DEV", PRCSharedUtils.getDeviceId());
			assertEquals("1.0.0", PRCSharedUtils.getAppVersion());
			assertEquals("android", PRCSharedUtils.getOsType()); // toLowerCase
			assertEquals("13", PRCSharedUtils.getOsVersion());
			assertEquals("SCR", PRCSharedUtils.getScreenId());
			assertEquals("MENU", PRCSharedUtils.getMenuId());
			assertEquals("1.1.1.1", PRCSharedUtils.getIpinsideIp());
			assertEquals("AX", PRCSharedUtils.getIpinsideAx());
			assertEquals("MAC", PRCSharedUtils.getIpinsideMac());
			assertEquals("HDD", PRCSharedUtils.getIpinsideHdd());
			assertEquals("UUID", PRCSharedUtils.getDeviceUUID());
			assertEquals("SIM", PRCSharedUtils.getSimSerial());
		}
	}

	@DisplayName("getOsType : null 이면 빈 문자열")
	@Test
	void getOsType_null() {
		try (MockedStatic<ThreadLocalStoreDelegator> t = tlsd()) {
			t.when(ThreadLocalStoreDelegator::getOsType).thenReturn(null);
			assertEquals("", PRCSharedUtils.getOsType());
		}
	}
}
