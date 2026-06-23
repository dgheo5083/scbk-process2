package com.scbank.process.api.svc.shared.components.frs.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import jakarta.servlet.http.HttpServletRequest;

@DisplayName("KftcFrsHelper")
public class KftcFrsHelperTest {

	@Test
	@DisplayName("상수 정의 확인")
	public void constantsTest() {
		assertEquals("023", KftcFrsHelper.ORG_CODE);
		assertEquals("face_id", KftcFrsHelper.SCOPE);
		assertEquals("000", KftcFrsHelper.SUCCESS_RSP_CODE);
	}

	@Test
	@DisplayName("getClientId / getClientSecret - 프로퍼티 값 반환")
	public void clientCredentialTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("client-id");
			assertEquals("client-id", KftcFrsHelper.getClientId());
			assertEquals("client-id", KftcFrsHelper.getClientSecret());
		}
	}

	@Test
	@DisplayName("getClientId - 미설정 시 기본값 '-'")
	public void clientIdDefaultTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn(null);
			assertEquals("-", KftcFrsHelper.getClientId());
		}
	}

	@Test
	@DisplayName("getBaseUrl / isHttps")
	public void baseUrlTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("https://faceid.kftc.or.kr");
			assertNotNull(KftcFrsHelper.getBaseUrl());
			assertTrue(KftcFrsHelper.isHttps());

			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("http://faceid.kftc.or.kr");
			assertFalse(KftcFrsHelper.isHttps());
		}
	}

	@Test
	@DisplayName("getCurrentRequest - 현재 요청 획득")
	public void getCurrentRequestTest() {
		try (MockedStatic<RequestContextHolder> holder = mockStatic(RequestContextHolder.class)) {
			ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
			when(attributes.getRequest()).thenReturn(mock(HttpServletRequest.class));
			holder.when(RequestContextHolder::currentRequestAttributes).thenReturn(attributes);

			assertNotNull(KftcFrsHelper.getCurrentRequest());
		}
	}
}
