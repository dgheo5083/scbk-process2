package com.scbank.process.api.svc.shared.channel.interceptors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.dao.LoginSsoHisDao;
import com.scbank.process.api.svc.shared.dao.dto.DuplicationLoginCheckInfoParameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoginDuplicateCheckInterceptorTest {

	@Mock private ISessionContextManager sessionContextManager;
	@Mock private LoginSsoHisDao loginSsoHisDao;

	@InjectMocks private LoginDuplicateCheckInterceptor interceptor;

	private HttpServletRequest request;
	private HttpServletResponse response;

	private void initRequest(MockedStatic<PropertiesUtils> props) {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		when(request.getRequestURI()).thenReturn("/test/uri");
	}

	@Nested
	@DisplayName("중복 로그인 체크 preHandle")
	class PreHandle {

		@Test
		@DisplayName("중복로그인 허용(ALLOW_DUPLICATE_LOGIN != N)인 경우 통과")
		public void allowDuplicateLoginTest() throws Exception {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				initRequest(props);
				props.when(() -> PropertiesUtils.getString("ALLOW_DUPLICATE_LOGIN")).thenReturn("Y");

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(loginSsoHisDao, never()).selectDuplicationLoginCheckInfo(any());
			}
		}

		@Test
		@DisplayName("로그인 상태가 아닌 경우 통과")
		public void notLoginTest() throws Exception {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				initRequest(props);
				props.when(() -> PropertiesUtils.getString("ALLOW_DUPLICATE_LOGIN")).thenReturn("N");
				when(sessionContextManager.isLogin()).thenReturn(false);

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(loginSsoHisDao, never()).selectDuplicationLoginCheckInfo(any());
			}
		}

		@Test
		@DisplayName("중복로그인 아님(result != 0)인 경우 통과")
		public void noDuplicationTest() throws Exception {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				initRequest(props);
				props.when(() -> PropertiesUtils.getString("ALLOW_DUPLICATE_LOGIN")).thenReturn("N");
				when(sessionContextManager.isLogin()).thenReturn(true);
				when(sessionContextManager.getLoginValue(eq("UserID"), eq(String.class))).thenReturn("user01");
				when(sessionContextManager.getLoginValue(eq("LoginDateTime"), eq(String.class))).thenReturn("20260601120000");
				when(loginSsoHisDao.selectDuplicationLoginCheckInfo(any(DuplicationLoginCheckInfoParameter.class))).thenReturn(1);

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(sessionContextManager, never()).logout();
			}
		}

		@Test
		@DisplayName("LoginDateTime 미존재 시 0으로 처리")
		public void nullLoginDatetimeTest() throws Exception {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				initRequest(props);
				props.when(() -> PropertiesUtils.getString("ALLOW_DUPLICATE_LOGIN")).thenReturn("N");
				when(sessionContextManager.isLogin()).thenReturn(true);
				when(sessionContextManager.getLoginValue(eq("UserID"), eq(String.class))).thenReturn("user01");
				when(sessionContextManager.getLoginValue(eq("LoginDateTime"), eq(String.class))).thenReturn(null);
				when(loginSsoHisDao.selectDuplicationLoginCheckInfo(any(DuplicationLoginCheckInfoParameter.class))).thenReturn(1);

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
			}
		}

		@Test
		@DisplayName("실패_타 기기에서 로그인되어 로그아웃 처리(result == 0)")
		public void duplicateLogoutTest() throws Exception {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				initRequest(props);
				props.when(() -> PropertiesUtils.getString("ALLOW_DUPLICATE_LOGIN")).thenReturn("N");
				when(sessionContextManager.isLogin()).thenReturn(true);
				when(sessionContextManager.getLoginValue(eq("UserID"), eq(String.class))).thenReturn("user01");
				when(sessionContextManager.getLoginValue(eq("LoginDateTime"), eq(String.class))).thenReturn("20260601120000");
				when(loginSsoHisDao.selectDuplicationLoginCheckInfo(any(DuplicationLoginCheckInfoParameter.class))).thenReturn(0);

				PRCServiceException ex = assertThrows(PRCServiceException.class,
						() -> interceptor.preHandle(request, response, new Object()));

				assertTrue(ex.getMessage().contains("PRCLGN0014") || ex.getErrorCode().contains("PRCLGN0014"));
				verify(sessionContextManager, times(1)).logout();
			}
		}
	}
}
