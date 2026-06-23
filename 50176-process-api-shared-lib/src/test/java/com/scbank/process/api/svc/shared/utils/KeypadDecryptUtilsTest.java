package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import com.ahnlab.astx2.servlet.ASTX2LibLocal;
import com.nshc.NFilter;
import com.nshc.exception.NFilterException;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SecureKeypadInfo;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.nshc.nfilter.openweb.OpenWebNFilterConfig;
import net.nshc.nfilter.openweb.util.crypto.OpenWebNFilterDecryptor;

/**
 * {@link KeypadDecryptUtils} 가상키패드 복호화(ASTX2/openWebNFilter/nFilter) 커버리지 테스트.
 */
@DisplayName("KeypadDecryptUtils")
class KeypadDecryptUtilsTest {

	private HttpServletRequest mockRequest() {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(session.getId()).thenReturn("sid-1234");
		when(req.getSession(false)).thenReturn(session);
		when(req.getSession()).thenReturn(session);
		return req;
	}

	private MockedStatic<SecureContextStore> mockContext(SecureKeypadInfo keypad) {
		SecureContext ctx = new SecureContext();
		ctx.setKeypad(keypad);
		MockedStatic<SecureContextStore> mocked = mockStatic(SecureContextStore.class);
		mocked.when(SecureContextStore::getContext).thenReturn(Optional.of(ctx));
		return mocked;
	}

	private SecureKeypadInfo keypad(String encType, String encValue, Map<String, String> encMap,
			Map<String, String> extE2EPlainMap) {
		SecureKeypadInfo k = new SecureKeypadInfo();
		k.setEncType(encType);
		k.setEncValue(encValue);
		k.setEncMap(encMap);
		k.setExtE2EPlainMap(extE2EPlainMap);
		return k;
	}

	// ---------------- decrypt() 유효성 검증 ----------------

	@DisplayName("decrypt : 시큐어 컨텍스트가 없으면 예외")
	@Test
	void decrypt_noContext() {
		try (MockedStatic<SecureContextStore> c = mockStatic(SecureContextStore.class)) {
			c.when(SecureContextStore::getContext).thenReturn(Optional.empty());
			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	@DisplayName("decrypt : keypad 정보가 없으면 예외")
	@Test
	void decrypt_noKeypad() {
		try (MockedStatic<SecureContextStore> c = mockContext(null)) {
			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	@DisplayName("decrypt : 암호화 타입이 없으면 예외")
	@Test
	void decrypt_noEncType() {
		try (MockedStatic<SecureContextStore> c = mockContext(keypad("", "v", null, null))) {
			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	@DisplayName("decrypt : 지원하지 않는 암호화 타입이면 예외")
	@Test
	void decrypt_unsupportedType() {
		try (MockedStatic<SecureContextStore> c = mockContext(keypad("UNKNOWN", "v", null, null))) {
			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	// ---------------- ASTX2 ----------------

	@DisplayName("decrypt(ASTX2) : 정상 복호화 - 금액 콤마 제거")
	@Test
	void decrypt_astx2_success() {
		try (MockedStatic<SecureContextStore> c = mockContext(keypad("ASTX2", "ENC", null, null));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedConstruction<ASTX2LibLocal> astx = mockConstruction(ASTX2LibLocal.class, (m, ctx) -> {
					when(m.setE2EDataValue2("ENC")).thenReturn(true);
					when(m.getE2ENames2()).thenReturn("amount");
					when(m.getE2EValue2("amount")).thenReturn("1,234,000");
				})) {
			rc.when(() -> RuntimeContext.getEnv("astx.config.path")).thenReturn("/cfg");

			Map<String, String> result = KeypadDecryptUtils.decrypt(mockRequest());
			assertEquals("1234000", result.get("amount"));
		}
	}

	@DisplayName("decrypt(ASTX2) : 확장 E2E 평문과 불일치하면 빈 값 처리")
	@Test
	void decrypt_astx2_extMismatch() {
		Map<String, String> ext = new HashMap<>();
		ext.put("amount", "999");
		try (MockedStatic<SecureContextStore> c = mockContext(keypad("ASTX2", "ENC", null, ext));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedConstruction<ASTX2LibLocal> astx = mockConstruction(ASTX2LibLocal.class, (m, ctx) -> {
					when(m.setE2EDataValue2("ENC")).thenReturn(true);
					when(m.getE2ENames2()).thenReturn("amount");
					when(m.getE2EValue2("amount")).thenReturn("1,234");
				})) {
			rc.when(() -> RuntimeContext.getEnv("astx.config.path")).thenReturn("/cfg");

			Map<String, String> result = KeypadDecryptUtils.decrypt(mockRequest());
			assertEquals("", result.get("amount"));
		}
	}

	@DisplayName("decrypt(ASTX2) : setE2EDataValue2 실패 시 예외")
	@Test
	void decrypt_astx2_setFail() {
		try (MockedStatic<SecureContextStore> c = mockContext(keypad("ASTX2", "ENC", null, null));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedConstruction<ASTX2LibLocal> astx = mockConstruction(ASTX2LibLocal.class, (m, ctx) -> {
					when(m.setE2EDataValue2(anyString())).thenReturn(false);
					when(m.getLastError()).thenReturn(99);
				})) {
			rc.when(() -> RuntimeContext.getEnv("astx.config.path")).thenReturn("/cfg");
			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	@DisplayName("decrypt(ASTX2) : e2eNames 가 비면 예외")
	@Test
	void decrypt_astx2_noNames() {
		try (MockedStatic<SecureContextStore> c = mockContext(keypad("ASTX2", "ENC", null, null));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedConstruction<ASTX2LibLocal> astx = mockConstruction(ASTX2LibLocal.class, (m, ctx) -> {
					when(m.setE2EDataValue2(anyString())).thenReturn(true);
					when(m.getE2ENames2()).thenReturn("");
				})) {
			rc.when(() -> RuntimeContext.getEnv("astx.config.path")).thenReturn("/cfg");
			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	// ---------------- openWebNFilter ----------------

	@DisplayName("decrypt(openWebNFilter) : 미사용세션 사용=true 분기")
	@Test
	void decrypt_openWeb_unusedSessionTrue() {
		Map<String, String> dec = new HashMap<>();
		dec.put("pwd", "1111");
		IOpenWebNFilterSessionStorage storage = mock(IOpenWebNFilterSessionStorage.class);
		when(storage.read(anyString())).thenReturn(new HashMap<>());

		try (MockedStatic<SecureContextStore> c = mockContext(keypad("openWebNFilter", "EV", null, null));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedStatic<OpenWebNFilterConfig> cfg = mockStatic(OpenWebNFilterConfig.class);
				MockedConstruction<OpenWebNFilterDecryptor> dp = mockConstruction(OpenWebNFilterDecryptor.class,
						(m, ctx) -> when(m.nFilterDecrypt("EV")).thenReturn(dec))) {
			rc.when(() -> RuntimeContext.getBean(IOpenWebNFilterSessionStorage.class)).thenReturn(storage);
			cfg.when(OpenWebNFilterConfig::getUnusedSessionUse).thenReturn("true");

			Map<String, String> result = KeypadDecryptUtils.decrypt(mockRequest());
			assertEquals("1111", result.get("pwd"));
		}
	}

	@DisplayName("decrypt(openWebNFilter) : 미사용세션 사용=false 분기")
	@Test
	void decrypt_openWeb_unusedSessionFalse() {
		Map<String, String> dec = new HashMap<>();
		dec.put("pwd", "2222");
		IOpenWebNFilterSessionStorage storage = mock(IOpenWebNFilterSessionStorage.class);
		when(storage.read(anyString())).thenReturn(new HashMap<>());

		try (MockedStatic<SecureContextStore> c = mockContext(keypad("openWebNFilter", "EV", null, null));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedStatic<OpenWebNFilterConfig> cfg = mockStatic(OpenWebNFilterConfig.class);
				MockedConstruction<OpenWebNFilterDecryptor> dp = mockConstruction(OpenWebNFilterDecryptor.class,
						(m, ctx) -> when(m.nFilterDecrypt(any(HttpServletRequest.class), eq("EV"),
								any(HttpSession.class))).thenReturn(dec))) {
			rc.when(() -> RuntimeContext.getBean(IOpenWebNFilterSessionStorage.class)).thenReturn(storage);
			cfg.when(OpenWebNFilterConfig::getUnusedSessionUse).thenReturn("false");

			Map<String, String> result = KeypadDecryptUtils.decrypt(mockRequest());
			assertEquals("2222", result.get("pwd"));
		}
	}

	@DisplayName("decrypt(openWebNFilter) : 처리 중 예외 발생 시 PRCServiceException")
	@Test
	void decrypt_openWeb_exception() {
		IOpenWebNFilterSessionStorage storage = mock(IOpenWebNFilterSessionStorage.class);
		when(storage.read(anyString())).thenThrow(new RuntimeException("session expired"));

		try (MockedStatic<SecureContextStore> c = mockContext(keypad("openWebNFilter", "EV", null, null));
				MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
				MockedStatic<OpenWebNFilterConfig> cfg = mockStatic(OpenWebNFilterConfig.class);
				MockedConstruction<OpenWebNFilterDecryptor> dp = mockConstruction(OpenWebNFilterDecryptor.class)) {
			rc.when(() -> RuntimeContext.getBean(IOpenWebNFilterSessionStorage.class)).thenReturn(storage);
			cfg.when(OpenWebNFilterConfig::getUnusedSessionUse).thenReturn("true");

			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}

	// ---------------- nFilter ----------------

	@DisplayName("decrypt(nFilter) : NUM/STR 복호화 및 짧은 값/빈 값 스킵")
	@Test
	void decrypt_nFilter_success() {
		Map<String, String> encMap = new HashMap<>();
		encMap.put("num", "NUM|numData|x|y");
		encMap.put("str", "STR|strData|x|y");
		encMap.put("short", "short");           // length<=3 -> 스킵
		encMap.put("empty", "NUM|emptyData|x|y"); // 복호화 결과 "" -> 미저장

		Map<String, String> nKey = new HashMap<>();
		nKey.put("nFilter_publicKey", "pub");
		nKey.put("nFilter_PrivateKey", "priv");

		try (MockedStatic<SecureContextStore> c = mockContext(keypad("nFilter", null, encMap, null));
				MockedStatic<E2EUtil> e2e = mockStatic(E2EUtil.class);
				MockedConstruction<NFilter> nf = mockConstruction(NFilter.class, (m, ctx) -> {
					when(m.decNum("numData")).thenReturn("5678");
					when(m.decNum("emptyData")).thenReturn("");
					when(m.decStr("strData")).thenReturn("abc");
				})) {
			e2e.when(() -> E2EUtil.getNkey(any(HttpSession.class))).thenReturn(nKey);

			Map<String, String> result = KeypadDecryptUtils.decrypt(mockRequest());

			assertEquals("5678", result.get("num"));
			assertEquals("ABC", result.get("str")); // toUpperCase
			assertEquals(null, result.get("short"));
			assertEquals(null, result.get("empty"));
		}
	}

	@DisplayName("decrypt(nFilter) : 복호화 중 NFilterException 발생 시 PRCServiceException")
	@Test
	void decrypt_nFilter_exception() {
		Map<String, String> encMap = new HashMap<>();
		encMap.put("num", "NUM|numData|x|y");

		Map<String, String> nKey = new HashMap<>();
		nKey.put("nFilter_publicKey", "pub");
		nKey.put("nFilter_PrivateKey", "priv");

		try (MockedStatic<SecureContextStore> c = mockContext(keypad("nFilter", null, encMap, null));
				MockedStatic<E2EUtil> e2e = mockStatic(E2EUtil.class);
				MockedConstruction<NFilter> nf = mockConstruction(NFilter.class, (m, ctx) -> {
					when(m.decNum(anyString())).thenThrow(mock(NFilterException.class));
				})) {
			e2e.when(() -> E2EUtil.getNkey(any(HttpSession.class))).thenReturn(nKey);

			assertThrows(PRCServiceException.class, () -> KeypadDecryptUtils.decrypt(mockRequest()));
		}
	}
}
