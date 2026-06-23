package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.springframework.test.util.ReflectionTestUtils;

import com.interezen.common.util.coder.Coder;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;

/**
 * {@link CommonBizUtils} 디바이스정보/주민번호/영업일/세션 유틸 커버리지 테스트.
 */
@DisplayName("CommonBizUtils")
class CommonBizUtilsTest {

	@AfterEach
	void clearStaticField() {
		//ReflectionTestUtils.setField(CommonBizUtils.class, "sessionManager", null);
	}

	/** getAge 가 참조하는 현재일자(DateUtils) 를 2026-06-01 로 고정한다. */
	private MockedStatic<DateUtils> fixedToday() {
		MockedStatic<DateUtils> mocked = mockStatic(DateUtils.class);
		mocked.when(() -> DateUtils.getCurrentDate(DateUtils.YYYYMMDD)).thenReturn("20260601");
		mocked.when(() -> DateUtils.getYear("20260601")).thenReturn(2026);
		mocked.when(() -> DateUtils.getMonth("20260601")).thenReturn(6);
		mocked.when(() -> DateUtils.getDay("20260601")).thenReturn(1);
		return mocked;
	}

	// ---------------- getBirthday ----------------

	@DisplayName("getBirthday : 빈값/길이오류는 빈 문자열")
	@Test
	void getBirthday_invalid() {
		assertEquals("", CommonBizUtils.getBirthday(""));
		assertEquals("", CommonBizUtils.getBirthday("123"));
	}

	@DisplayName("getBirthday : 성별코드별 세기(1800/1900/2000) 변환")
	@Test
	void getBirthday_centuries() {
		assertEquals("18900101", CommonBizUtils.getBirthday("9001010234567")); // 0 -> 1800
		assertEquals("18900101", CommonBizUtils.getBirthday("9001019234567")); // 9 -> 1800
		assertEquals("19900101", CommonBizUtils.getBirthday("9001011234567")); // 1 -> 1900
		assertEquals("19900101", CommonBizUtils.getBirthday("9001016234567")); // 6 -> 1900
		assertEquals("20900101", CommonBizUtils.getBirthday("9001013234567")); // 3 -> 2000
		assertEquals("20900101", CommonBizUtils.getBirthday("9001018234567")); // 8 -> 2000
	}

	// ---------------- getGender ----------------

	@DisplayName("getGender : 빈값/길이오류는 빈 문자열")
	@Test
	void getGender_invalid() {
		assertEquals("", CommonBizUtils.getGender("12345"));
	}

	@DisplayName("getGender : 성별 7번째 자리 코드 매핑")
	@Test
	void getGender_mapping() {
		assertEquals("1", CommonBizUtils.getGender("9001011234567")); // 1 -> 1
		assertEquals("1", CommonBizUtils.getGender("9001013234567")); // 3 -> 1
		assertEquals("2", CommonBizUtils.getGender("9001012234567")); // 2 -> 2
		assertEquals("2", CommonBizUtils.getGender("9001014234567")); // 4 -> 2
		assertEquals("5", CommonBizUtils.getGender("9001015234567")); // 5 -> 5
		assertEquals("6", CommonBizUtils.getGender("9001016234567")); // 6 -> 6
		assertEquals("7", CommonBizUtils.getGender("9001017234567")); // 7 -> 7
		assertEquals("8", CommonBizUtils.getGender("9001018234567")); // 8 -> 8
		assertEquals("", CommonBizUtils.getGender("9001010234567")); // 0 -> 미매핑
	}

	// ---------------- isForeigner ----------------

	@DisplayName("isForeigner : 5/6/7/8 은 외국인, 그 외는 내국인")
	@Test
	void isForeigner() {
		assertTrue(CommonBizUtils.isForeigner("9001015234567"));
		assertTrue(CommonBizUtils.isForeigner("9001018234567"));
		assertFalse(CommonBizUtils.isForeigner("9001011234567"));
	}

	// ---------------- getAge / isMinor ----------------

	@DisplayName("getAge : 생일/성별 코드에 따른 만 나이 계산")
	@Test
	void getAge_branches() {
		try (MockedStatic<DateUtils> d = fixedToday()) {
			// 생일이 지남 (1월) -> 만 36
			assertEquals(36, CommonBizUtils.getAge("9001011234567"));
			// 생월이 현재(6)보다 큼(7월) -> -1
			assertEquals(35, CommonBizUtils.getAge("9007011234567"));
			// 같은 달, 생일(15) 이 오늘(1) 보다 큼 -> -1
			assertEquals(35, CommonBizUtils.getAge("9006151234567"));
			// 같은 달, 생일(01) <= 오늘(01) -> 그대로
			assertEquals(36, CommonBizUtils.getAge("9006011234567"));
			// 성별 3 -> 2000년대
			assertEquals(2026 - 2090, CommonBizUtils.getAge("9001013234567"));
			// 성별 5 -> 1900년대
			assertEquals(36, CommonBizUtils.getAge("9001015234567"));
			// 성별 7 -> 2000년대
			assertEquals(2026 - 2090, CommonBizUtils.getAge("9001017234567"));
		}
	}

	@DisplayName("isMinor : 19세 미만이면 true")
	@Test
	void isMinor() {
		try (MockedStatic<DateUtils> d = fixedToday()) {
			assertTrue(CommonBizUtils.isMinor("1001013234567"));  // 2010년생 -> 16세
			assertFalse(CommonBizUtils.isMinor("9001011234567")); // 1990년생 -> 36세
		}
	}

	// ---------------- getPhoneNumberArr ----------------

	@DisplayName("getPhoneNumberArr : 10/11 자리 분할, 그 외는 null 유지")
	@Test
	void getPhoneNumberArr() {
		assertArrayEquals(new String[] { "010", "1234", "5678" },
				CommonBizUtils.getPhoneNumberArr("010-1234-5678")); // 11
		assertArrayEquals(new String[] { "031", "123", "4567" },
				CommonBizUtils.getPhoneNumberArr("031-123-4567")); // 10
		assertArrayEquals(new String[] { null, null, null },
				CommonBizUtils.getPhoneNumberArr("123")); // 미해당
	}

	// ---------------- getDecodeStr / getDeviceDataArray ----------------

	@DisplayName("getDecodeStr : Coder 로 복호화한 문자열을 반환한다")
	@Test
	void getDecodeStr() throws Exception {
		try (MockedConstruction<Coder> coder = mockConstruction(Coder.class,
				(m, ctx) -> when(m.Decode(anyString())).thenReturn("DECODED"))) {
			assertEquals("DECODED", CommonBizUtils.getDecodeStr("raw"));
		}
	}

	@DisplayName("getDeviceDataArray : 복호화 결과를 ; 로 분할한다")
	@Test
	void getDeviceDataArray() throws Exception {
		try (MockedConstruction<Coder> coder = mockConstruction(Coder.class,
				(m, ctx) -> when(m.Decode(anyString())).thenReturn("a;b;c"))) {
			assertArrayEquals(new String[] { "a", "b", "c" }, CommonBizUtils.getDeviceDataArray("raw"));
		}
	}

	// ---------------- getDataMap ----------------

	@DisplayName("getDataMap : 안드로이드 + AndroidId/WidevineId 조합")
	@Test
	void getDataMap_androidWidevine() throws Exception {
		String decoded = "[androidididididid:xx];[widevineididididi:yy]";
		try (MockedStatic<PRCSharedUtils> prc = mockStatic(PRCSharedUtils.class);
				MockedConstruction<Coder> coder = mockConstruction(Coder.class,
						(m, ctx) -> when(m.Decode(anyString())).thenReturn(decoded))) {
			prc.when(PRCSharedUtils::isAndroid).thenReturn(true);
			prc.when(PRCSharedUtils::getOsVersion).thenReturn("10.0.0");

			Map<String, String> result = CommonBizUtils.getDataMap("raw");

			assertEquals(CommonBizConstants.IPINSIDE_ANDROID_ID_AND_WIDEVINE_ID,
					result.get(CommonBizConstants.IPINSIDE_DEVICE_DATA_TYPE));
			assertEquals("androidididididid", result.get(CommonBizConstants.IPINSIDE_ANDROID_ID));
			assertEquals("widevineididididi", result.get(CommonBizConstants.IPINSIDE_WIDEVINE_ID));
		}
	}

	@DisplayName("getDataMap : 안드로이드 + MAC/IMEI 조합 (구버전), isRawStr=true")
	@Test
	void getDataMap_androidMacImei() throws Exception {
		String decoded = "[aa:bb];[cc:dd]";
		try (MockedStatic<PRCSharedUtils> prc = mockStatic(PRCSharedUtils.class);
				MockedConstruction<Coder> coder = mockConstruction(Coder.class,
						(m, ctx) -> when(m.Decode(anyString())).thenReturn(decoded))) {
			prc.when(PRCSharedUtils::isAndroid).thenReturn(true);
			prc.when(PRCSharedUtils::getOsVersion).thenReturn("8.0.0");

			Map<String, String> result = CommonBizUtils.getDataMap("raw", true);

			assertEquals(CommonBizConstants.IPINSIDE_MAC_AND_IMEI,
					result.get(CommonBizConstants.IPINSIDE_DEVICE_DATA_TYPE));
			assertEquals("[aa:bb]", result.get(CommonBizConstants.IPINSIDE_MAC)); // raw
			assertEquals("[cc:dd]", result.get(CommonBizConstants.IPINSIDE_IMEI)); // raw
		}
	}

	@DisplayName("getDataMap : iOS 조합")
	@Test
	void getDataMap_ios() throws Exception {
		String decoded = "[vender:bb];[cfuuid:dd]";
		try (MockedStatic<PRCSharedUtils> prc = mockStatic(PRCSharedUtils.class);
				MockedConstruction<Coder> coder = mockConstruction(Coder.class,
						(m, ctx) -> when(m.Decode(anyString())).thenReturn(decoded))) {
			prc.when(PRCSharedUtils::isAndroid).thenReturn(false);

			Map<String, String> result = CommonBizUtils.getDataMap("raw");

			assertEquals(CommonBizConstants.IPINSIDE_VENDER_UUID_AND_UUID,
					result.get(CommonBizConstants.IPINSIDE_DEVICE_DATA_TYPE));
			assertEquals("vender", result.get(CommonBizConstants.IPINSIDE_VENDER_UUID));
			assertEquals("cfuuid", result.get(CommonBizConstants.IPINSIDE_UUID));
		}
	}

	// ---------------- getBusinessDay ----------------

	@DisplayName("getBusinessDay : 양수 영업일 계산 (+1)")
	@Test
	void getBusinessDay_positive() {
		IHolidayManager hm = mock(IHolidayManager.class);
		when(hm.isHoliday(anyString())).thenReturn(false);

		try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedStatic<DateUtils> d = mockStatic(DateUtils.class)) {
			rc.when(() -> RuntimeContext.getBean(IHolidayManager.class)).thenReturn(hm);
			d.when(() -> DateUtils.getDate(anyString(), eq("yyyyMMdd"), anyChar(), anyInt())).thenReturn("20260602");

			assertEquals("20260602", CommonBizUtils.getBusinessDay("2026-06-01", 1));
		}
	}

	@DisplayName("getBusinessDay : 휴일 스킵 후 영업일 계산 (+1)")
	@Test
	void getBusinessDay_skipHoliday() {
		IHolidayManager hm = mock(IHolidayManager.class);
		when(hm.isHoliday(anyString())).thenReturn(true, false); // 첫 날 휴일, 다음 날 영업일

		try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedStatic<DateUtils> d = mockStatic(DateUtils.class)) {
			rc.when(() -> RuntimeContext.getBean(IHolidayManager.class)).thenReturn(hm);
			d.when(() -> DateUtils.getDate(anyString(), eq("yyyyMMdd"), anyChar(), anyInt()))
					.thenReturn("20260606", "20260607");

			assertEquals("20260607", CommonBizUtils.getBusinessDay("20260605", 1));
		}
	}

	@DisplayName("getBusinessDay : 음수 영업일 계산 (-1)")
	@Test
	void getBusinessDay_negative() {
		IHolidayManager hm = mock(IHolidayManager.class);
		when(hm.isHoliday(anyString())).thenReturn(false);

		try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedStatic<DateUtils> d = mockStatic(DateUtils.class)) {
			rc.when(() -> RuntimeContext.getBean(IHolidayManager.class)).thenReturn(hm);
			d.when(() -> DateUtils.getDate(anyString(), eq("yyyyMMdd"), anyChar(), anyInt())).thenReturn("20260531");

			assertEquals("20260531", CommonBizUtils.getBusinessDay("20260601", -1));
		}
	}

	// ---------------- getSessionValue / isAuthenticated ----------------

	private ISessionContextManager injectSession() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		ReflectionTestUtils.setField(CommonBizUtils.class, "sessionManager", sm);
		return sm;
	}

	@DisplayName("getSessionValue : 로그인 상태면 로그인세션 우선")
	@Test
	void getSessionValue_login() {
		ISessionContextManager sm = injectSession();
		when(sm.isLogin()).thenReturn(true);
		when(sm.getLoginValue("K", String.class)).thenReturn("L");

		assertEquals("L", CommonBizUtils.getSessionValue("K"));
	}

	@DisplayName("getSessionValue : 비로그인 UserID/TSPassword 기본값")
	@Test
	void getSessionValue_defaults() {
		ISessionContextManager sm = injectSession();
		when(sm.isLogin()).thenReturn(false);

		assertEquals(CommonBizConstants.DEFAULT_USER_ID, CommonBizUtils.getSessionValue("UserID"));
		assertEquals(CommonBizConstants.DEFAULT_TS_PASS_WORD, CommonBizUtils.getSessionValue("TSPassword"));
	}

	@DisplayName("getSessionValue : 비로그인 + 인증상태면 글로벌세션 우선")
	@Test
	void getSessionValue_authenticated() {
		ISessionContextManager sm = injectSession();
		when(sm.isLogin()).thenReturn(false);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("Y");
		when(sm.getGlobalValue("K", String.class)).thenReturn("G");

		assertEquals("G", CommonBizUtils.getSessionValue("K"));
	}

	@DisplayName("getSessionValue : 비로그인 + 미인증이면 글로벌세션 반환")
	@Test
	void getSessionValue_notAuthenticated() {
		ISessionContextManager sm = injectSession();
		when(sm.isLogin()).thenReturn(false);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("N");
		when(sm.getGlobalValue("K", String.class)).thenReturn("G2");

		assertEquals("G2", CommonBizUtils.getSessionValue("K"));
	}

	@DisplayName("isAuthenticated : BIZ_AUTH_FLAG=Y 이면 true")
	@Test
	void isAuthenticated() {
		ISessionContextManager sm = injectSession();
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("Y");
		assertTrue(CommonBizUtils.isAuthenticated());

		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn(null);
		assertFalse(CommonBizUtils.isAuthenticated());
	}
}
