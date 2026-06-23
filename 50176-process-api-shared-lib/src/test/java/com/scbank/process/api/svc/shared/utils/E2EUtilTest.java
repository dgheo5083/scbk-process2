package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import com.nshc.NFilter;

import jakarta.servlet.http.HttpSession;

/**
 * {@link E2EUtil} nFilter 키 생성/저장 커버리지 테스트.
 */
@DisplayName("E2EUtil")
class E2EUtilTest {

//	@DisplayName("getNkey : 키가 모두 없으면 NFilter 로 신규 생성 후 세션에 저장한다")
//	@Test
//	void getNkey_generatesWhenAbsent() {
//		HttpSession session = mock(HttpSession.class);
//		when(session.getAttribute(E2EUtil.NFILTER_PUBLICKEY)).thenReturn(null);
//		when(session.getAttribute(E2EUtil.NFILTER_PRIVATEKEY)).thenReturn(null);
//
//		try (MockedConstruction<NFilter> mocked = mockConstruction(NFilter.class, (m, ctx) -> {
//			when(m.GenerateKeypair()).thenReturn(new byte[][] { { 1, 2 }, { 3, 4 } });
//			when(m.Base64Encode(any())).thenReturn("ENCODED");
//		})) {
//			Map<String, String> result = E2EUtil.getNkey(session);
//
//			assertEquals("ENCODED", result.get(E2EUtil.NFILTER_PUBLICKEY));
//			assertEquals("ENCODED", result.get(E2EUtil.NFILTER_PRIVATEKEY));
//			verify(session).setAttribute(eq(E2EUtil.NFILTER_PUBLICKEY), eq("ENCODED"));
//			verify(session).setAttribute(eq(E2EUtil.NFILTER_PRIVATEKEY), eq("ENCODED"));
//		}
//	}
//
//	@DisplayName("getNkey : 키가 이미 존재하면 NFilter 를 생성하지 않고 기존 값을 반환한다")
//	@Test
//	void getNkey_reusesExisting() {
//		HttpSession session = mock(HttpSession.class);
//		when(session.getAttribute(E2EUtil.NFILTER_PUBLICKEY)).thenReturn("PUB");
//		when(session.getAttribute(E2EUtil.NFILTER_PRIVATEKEY)).thenReturn("PRIV");
//
//		try (MockedConstruction<NFilter> mocked = mockConstruction(NFilter.class)) {
//			Map<String, String> result = E2EUtil.getNkey(session);
//
//			assertEquals("PUB", result.get(E2EUtil.NFILTER_PUBLICKEY));
//			assertEquals("PRIV", result.get(E2EUtil.NFILTER_PRIVATEKEY));
//			verify(session, never()).setAttribute(any(), any());
//			assertEquals(0, mocked.constructed().size());
//		}
//	}
//
//	@DisplayName("getNkey : public 키만 존재해도 신규 생성 분기를 타지 않는다")
//	@Test
//	void getNkey_publicKeyPresentOnly() {
//		HttpSession session = mock(HttpSession.class);
//		when(session.getAttribute(E2EUtil.NFILTER_PUBLICKEY)).thenReturn("PUB");
//		when(session.getAttribute(E2EUtil.NFILTER_PRIVATEKEY)).thenReturn(null);
//
//		try (MockedConstruction<NFilter> mocked = mockConstruction(NFilter.class)) {
//			Map<String, String> result = E2EUtil.getNkey(session);
//
//			assertEquals("PUB", result.get(E2EUtil.NFILTER_PUBLICKEY));
//			assertNull(result.get(E2EUtil.NFILTER_PRIVATEKEY));
//			assertEquals(0, mocked.constructed().size());
//		}
//	}
//
//	@DisplayName("getNkey : private 키만 존재해도 신규 생성 분기를 타지 않는다")
//	@Test
//	void getNkey_privateKeyPresentOnly() {
//		HttpSession session = mock(HttpSession.class);
//		when(session.getAttribute(E2EUtil.NFILTER_PUBLICKEY)).thenReturn(null);
//		when(session.getAttribute(E2EUtil.NFILTER_PRIVATEKEY)).thenReturn("PRIV");
//
//		try (MockedConstruction<NFilter> mocked = mockConstruction(NFilter.class)) {
//			Map<String, String> result = E2EUtil.getNkey(session);
//
//			assertNull(result.get(E2EUtil.NFILTER_PUBLICKEY));
//			assertEquals("PRIV", result.get(E2EUtil.NFILTER_PRIVATEKEY));
//			assertEquals(0, mocked.constructed().size());
//		}
//	}
//
//	@DisplayName("getNkey : NFilter 처리 중 예외가 발생하면 catch 후 null 값으로 반환한다")
//	@Test
//	void getNkey_handlesException() {
//		HttpSession session = mock(HttpSession.class);
//		when(session.getAttribute(E2EUtil.NFILTER_PUBLICKEY)).thenReturn(null);
//		when(session.getAttribute(E2EUtil.NFILTER_PRIVATEKEY)).thenReturn(null);
//
//		try (MockedConstruction<NFilter> mocked = mockConstruction(NFilter.class, (m, ctx) -> {
//			when(m.GenerateKeypair()).thenThrow(new RuntimeException("boom"));
//		})) {
//			Map<String, String> result = E2EUtil.getNkey(session);
//
//			assertNull(result.get(E2EUtil.NFILTER_PUBLICKEY));
//			assertNull(result.get(E2EUtil.NFILTER_PRIVATEKEY));
//			verify(session, never()).setAttribute(any(), any());
//		}
//	}
//
//	@DisplayName("setNkey : 전달받은 맵 값을 세션에 저장한다")
//	@Test
//	void setNkey_storesIntoSession() {
//		HttpSession session = mock(HttpSession.class);
//		Map<String, String> map = new HashMap<>();
//		map.put(E2EUtil.NFILTER_PUBLICKEY, "PUB");
//		map.put(E2EUtil.NFILTER_PRIVATEKEY, "PRIV");
//
//		E2EUtil.setNkey(session, map);
//
//		verify(session, times(1)).setAttribute(E2EUtil.NFILTER_PUBLICKEY, "PUB");
//		verify(session, times(1)).setAttribute(E2EUtil.NFILTER_PRIVATEKEY, "PRIV");
//	}
//
//	@DisplayName("생성자 커버리지")
//	@Test
//	void constructorCoverage() {
//		new E2EUtil();
//	}
}
