package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContextManager;

/**
 * {@link SessionUtils} 세션값 조회 분기 커버리지 테스트.
 */
@DisplayName("SessionUtils")
class SessionUtilsTest {

	private MockedStatic<RuntimeContext> mockRuntime(ISessionContextManager sm) {
		MockedStatic<RuntimeContext> mocked = mockStatic(RuntimeContext.class);
		mocked.when(() -> RuntimeContext.getBean(ISessionContextManager.class)).thenReturn(sm);
		return mocked;
	}

	@DisplayName("getSessionValue : 로그인 상태 - 로그인 세션값이 있으면 그 값을 반환")
	@Test
	void getSessionValue_loginWithLoginValue() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(true);
		when(sm.getLoginValue("K", String.class)).thenReturn("L");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("L", SessionUtils.getSessionValue("K", String.class));
		}
	}

	@DisplayName("getSessionValue : 로그인 상태 - 로그인 세션값이 없으면 글로벌 세션값을 반환")
	@Test
	void getSessionValue_loginFallbackGlobal() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(true);
		when(sm.getLoginValue("K", String.class)).thenReturn(null);
		when(sm.getGlobalValue("K", String.class)).thenReturn("G");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("G", SessionUtils.getSessionValue("K", String.class));
		}
	}

	@DisplayName("getSessionValue : 비로그인 - UserID 는 기본 사용자 ID 를 반환")
	@Test
	void getSessionValue_userId() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(false);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("FIRST999", SessionUtils.getSessionValue("UserID", String.class));
		}
	}

	@DisplayName("getSessionValue : 비로그인 - TSPassword 는 기본 통신비밀번호를 반환")
	@Test
	void getSessionValue_tsPassword() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(false);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("111111", SessionUtils.getSessionValue("TSPassword", String.class));
		}
	}

	@DisplayName("getSessionValue : 비로그인 - 인증상태이면 로그인 세션값을 반환")
	@Test
	void getSessionValue_authenticatedLoginValue() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(false);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("Y");
		when(sm.getLoginValue("OTHER", String.class)).thenReturn("L");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("L", SessionUtils.getSessionValue("OTHER", String.class));
		}
	}

	@DisplayName("getSessionValue : 비로그인 - 인증상태 + 로그인값 없으면 글로벌값을 반환")
	@Test
	void getSessionValue_authenticatedFallbackGlobal() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(false);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("Y");
		when(sm.getLoginValue("OTHER", String.class)).thenReturn(null);
		when(sm.getGlobalValue("OTHER", String.class)).thenReturn("G");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("G", SessionUtils.getSessionValue("OTHER", String.class));
		}
	}

	@DisplayName("getSessionValue : 비로그인 - 미인증이면 글로벌값을 반환")
	@Test
	void getSessionValue_notAuthenticatedGlobal() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(false);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("N");
		when(sm.getGlobalValue("OTHER", String.class)).thenReturn("G");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("G", SessionUtils.getSessionValue("OTHER", String.class));
		}
	}

	@DisplayName("getSessionValue : String 이 아닌 타입은 defaultString 없이 캐스팅한다")
	@Test
	void getSessionValue_nonStringType() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(true);
		when(sm.getLoginValue("NUM", Integer.class)).thenReturn(5);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals(5, SessionUtils.getSessionValue("NUM", Integer.class));
		}
	}

	@DisplayName("getSessionValue(key) : String.class 로 위임한다")
	@Test
	void getSessionValue_singleArg() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(true);
		when(sm.getLoginValue("K", String.class)).thenReturn("L");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertEquals("L", SessionUtils.getSessionValue("K"));
		}
	}

	@DisplayName("isLoginOrAuth : LOGIN 타입은 로그인 여부를 반환")
	@Test
	void isLoginOrAuth_login() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(true);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertTrue(SessionUtils.isLoginOrAuth("LOGIN"));
		}
	}

	@DisplayName("isLoginOrAuth : AUTH 타입은 인증 여부를 반환")
	@Test
	void isLoginOrAuth_auth() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("Y");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertTrue(SessionUtils.isLoginOrAuth("AUTH"));
		}
	}

	@DisplayName("isLoginOrAuth : 그 외 타입은 로그인 OR 인증 여부를 반환")
	@Test
	void isLoginOrAuth_default() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.isLogin()).thenReturn(false);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("N");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertFalse(SessionUtils.isLoginOrAuth("ETC"));
		}
	}

	@DisplayName("isAuthenticated : BIZ_AUTH_FLAG 가 Y 이면 true")
	@Test
	void isAuthenticated_true() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn("Y");

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertTrue(SessionUtils.isAuthenticated());
		}
	}

	@DisplayName("isAuthenticated : BIZ_AUTH_FLAG 가 null 이면 false")
	@Test
	void isAuthenticated_nullFalse() {
		ISessionContextManager sm = mock(ISessionContextManager.class);
		when(sm.getGlobalValue("BIZ_AUTH_FLAG", String.class)).thenReturn(null);

		try (MockedStatic<RuntimeContext> r = mockRuntime(sm)) {
			assertFalse(SessionUtils.isAuthenticated());
		}
	}
}
