package com.scbank.process.api.svc.shared.channel.interceptors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.accesscontrol.MenuAuthorityCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.MenuAuthorityCheckRequest;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServiceAuthCheckInterceptorTest {

	@Mock private MenuAuthorityCheckComponent authorityCheckComponent;

	@InjectMocks private ServiceAuthCheckInterceptor interceptor;

	@Nested
	@DisplayName("요청 서비스 권한 체크 preHandle")
	class PreHandle {

		@Test
		@DisplayName("권한체크 무시 메뉴ID인 경우 권한체크 없이 통과")
		public void ignoreMenuIdTest() throws Exception {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);

			try (MockedStatic<PRCSharedUtils> prcUtils = mockStatic(PRCSharedUtils.class);
				 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {

				prcUtils.when(PRCSharedUtils::getMenuId).thenReturn("IGNORE1");
				props.when(() -> PropertiesUtils.getString("PAGECHECK_IGNORE_MENU_ID", "")).thenReturn("IGNORE1|IGNORE2");

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(authorityCheckComponent, never()).checkAuthority(any());
			}
		}

		@Test
		@DisplayName("권한체크 무시 요청 URI인 경우 권한체크 없이 통과")
		public void ignoreRequestUriTest() throws Exception {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);

			try (MockedStatic<PRCSharedUtils> prcUtils = mockStatic(PRCSharedUtils.class);
				 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {

				prcUtils.when(PRCSharedUtils::getMenuId).thenReturn("");
				props.when(() -> PropertiesUtils.getString("PAGECHECK_IGNORE_URI_LIST", "")).thenReturn("/ignore/uri");
				when(request.getRequestURI()).thenReturn("/ignore/uri");

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(authorityCheckComponent, never()).checkAuthority(any());
			}
		}

		@Test
		@DisplayName("메뉴식별자 정보가 없는 경우 권한체크 없이 통과")
		public void noMenuIdTest() throws Exception {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);

			try (MockedStatic<PRCSharedUtils> prcUtils = mockStatic(PRCSharedUtils.class);
				 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<ThreadLocalStoreDelegator> store = mockStatic(ThreadLocalStoreDelegator.class)) {

				prcUtils.when(PRCSharedUtils::getMenuId).thenReturn("");
				props.when(() -> PropertiesUtils.getString(anyString(), anyString())).thenReturn("");
				when(request.getRequestURI()).thenReturn("/some/uri");
				store.when(ThreadLocalStoreDelegator::getMenuId).thenReturn("");

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(authorityCheckComponent, never()).checkAuthority(any());
			}
		}

		@Test
		@DisplayName("성공_권한체크 컴포넌트 호출")
		public void checkAuthorityTest() throws Exception {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);

			try (MockedStatic<PRCSharedUtils> prcUtils = mockStatic(PRCSharedUtils.class);
				 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<ThreadLocalStoreDelegator> store = mockStatic(ThreadLocalStoreDelegator.class)) {

				prcUtils.when(PRCSharedUtils::getMenuId).thenReturn("");
				props.when(() -> PropertiesUtils.getString(anyString(), anyString())).thenReturn("");
				when(request.getRequestURI()).thenReturn("/some/uri");
				store.when(ThreadLocalStoreDelegator::getMenuId).thenReturn("M001");
				store.when(ThreadLocalStoreDelegator::getAcType).thenReturn("01");
				store.when(ThreadLocalStoreDelegator::getForceCheckCode).thenReturn("F");

				boolean result = interceptor.preHandle(request, response, new Object());

				assertTrue(result);
				verify(authorityCheckComponent, times(1)).checkAuthority(any(MenuAuthorityCheckRequest.class));
			}
		}
	}
}
