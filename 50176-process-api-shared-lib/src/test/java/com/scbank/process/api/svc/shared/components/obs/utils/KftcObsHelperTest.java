package com.scbank.process.api.svc.shared.components.obs.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import jakarta.servlet.http.HttpServletRequest;

@DisplayName("KftcObsHelper")
public class KftcObsHelperTest {

	@Test
	@DisplayName("상수 정의 확인")
	public void constantsTest() {
		assertEquals("A0000", KftcObsHelper.SUCCESS_RSP_CODE);
		assertEquals("000", KftcObsHelper.SUCCESS_BANK_RSP_CODE);
		assertEquals("client_credentials", KftcObsHelper.TOKEN_GRANTTYPE);
	}

	@Nested
	@DisplayName("클라이언트 인증정보")
	class ClientCredential {

		@Test
		@DisplayName("getClientId - 미설정 시 PRCServiceException")
		public void clientIdMissingTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn(null);
				assertThrows(PRCServiceException.class, KftcObsHelper::getClientId);
			}
		}

		@Test
		@DisplayName("getClientId - 설정 시 복호화 값 반환")
		public void clientIdPresentTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<ObsCryptoUtils> crypto = mockStatic(ObsCryptoUtils.class)) {
				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("encClientId");
				crypto.when(() -> ObsCryptoUtils.decrypt(anyString())).thenReturn("CLIENT_ID");

				assertEquals("CLIENT_ID", KftcObsHelper.getClientId());
			}
		}

		@Test
		@DisplayName("getClientSecret - 설정 시 복호화 값 반환")
		public void clientSecretPresentTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<ObsCryptoUtils> crypto = mockStatic(ObsCryptoUtils.class)) {
				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("encSecret");
				crypto.when(() -> ObsCryptoUtils.decrypt(anyString())).thenReturn("SECRET");

				assertEquals("SECRET", KftcObsHelper.getClientSecret());
			}
		}
	}

	@Nested
	@DisplayName("Base URL / Https")
	class BaseUrl {

		@Test
		@DisplayName("getBaseUrl / isHttps - https URL")
		public void httpsTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("https://openapi.kftc.or.kr");
				assertNotNull(KftcObsHelper.getBaseUrl());
				assertTrue(KftcObsHelper.isHttps());
			}
		}

		@Test
		@DisplayName("isHttps - http URL 이면 false")
		public void httpTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("http://openapi.kftc.or.kr");
				assertFalse(KftcObsHelper.isHttps());
			}
		}
	}

	@Test
	@DisplayName("getCurrentRequest - 현재 요청 획득")
	public void getCurrentRequestTest() {
		try (MockedStatic<RequestContextHolder> holder = mockStatic(RequestContextHolder.class)) {
			ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
			HttpServletRequest request = mock(HttpServletRequest.class);
			when(attributes.getRequest()).thenReturn(request);
			holder.when(RequestContextHolder::currentRequestAttributes).thenReturn(attributes);

			assertNotNull(KftcObsHelper.getCurrentRequest());
		}
	}
}
