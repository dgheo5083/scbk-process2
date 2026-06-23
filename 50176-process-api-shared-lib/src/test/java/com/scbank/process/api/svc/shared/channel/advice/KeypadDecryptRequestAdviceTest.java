package com.scbank.process.api.svc.shared.channel.advice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.svc.shared.utils.KeypadDecryptUtils;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class KeypadDecryptRequestAdviceTest {

	private KeypadDecryptRequestAdvice advice;

	@BeforeEach
	void setUp() {
		advice = new KeypadDecryptRequestAdvice();
		// 알수없는 필드 병합 시 테스트가 깨지지 않도록 관대한 ObjectMapper 주입
		ReflectionTestUtils.setField(advice, "objectMapper",
				new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
	}

	private HttpInputMessage inputMessage(String json) {
		HttpInputMessage msg = mock(HttpInputMessage.class);
		try {
			when(msg.getBody()).thenReturn(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception ignored) {
		}
		when(msg.getHeaders()).thenReturn(new HttpHeaders());
		return msg;
	}

	@Nested
	@DisplayName("supports - @RequestBody 파라미터만 처리")
	class Supports {

		@Test
		@DisplayName("@RequestBody 있으면 true")
		public void hasRequestBodyTest() {
			MethodParameter parameter = mock(MethodParameter.class);
			when(parameter.hasParameterAnnotation(RequestBody.class)).thenReturn(true);

			assertTrue(advice.supports(parameter, String.class, null));
		}

		@Test
		@DisplayName("@RequestBody 없으면 false")
		public void noRequestBodyTest() {
			MethodParameter parameter = mock(MethodParameter.class);
			when(parameter.hasParameterAnnotation(RequestBody.class)).thenReturn(false);

			assertFalse(advice.supports(parameter, String.class, null));
		}
	}

	@Nested
	@DisplayName("beforeBodyRead - 키패드 복호화")
	class BeforeBodyRead {

		@Test
		@DisplayName("SecureContext 미존재 시 원본 body 반환")
		public void emptySecureContextTest() throws Exception {
			HttpInputMessage msg = inputMessage("{\"acctNo\":\"110\"}");

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.request()).thenReturn(mock(HttpServletRequest.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.empty());

				HttpInputMessage result = advice.beforeBodyRead(msg, null, String.class, null);

				assertNotNull(result.getBody());
				assertNotNull(result.getHeaders());
			}
		}

		@Test
		@DisplayName("키패드 정보 미존재 시 원본 body 반환")
		public void noKeypadTest() throws Exception {
			HttpInputMessage msg = inputMessage("{\"acctNo\":\"110\"}");

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				SecureContext secureContext = mock(SecureContext.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.request()).thenReturn(mock(HttpServletRequest.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				when(secureContext.getKeypad()).thenReturn(null);

				HttpInputMessage result = advice.beforeBodyRead(msg, null, String.class, null);

				assertNotNull(result.getBody());
			}
		}

		@Test
		@DisplayName("복호화 결과가 비어있으면 원본 body 반환")
		public void emptyDecryptedMapTest() throws Exception {
			HttpInputMessage msg = inputMessage("{\"acctNo\":\"110\"}");

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<KeypadDecryptUtils> keypad = mockStatic(KeypadDecryptUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				SecureContext secureContext = mock(SecureContext.class, RETURNS_DEEP_STUBS);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.request()).thenReturn(mock(HttpServletRequest.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				keypad.when(() -> KeypadDecryptUtils.decrypt(any())).thenReturn(new LinkedHashMap<>());

				HttpInputMessage result = advice.beforeBodyRead(msg, null, String.class, null);

				assertNotNull(result.getBody());
			}
		}

		@Test
		@DisplayName("복호화 결과가 null이면 원본 body 반환")
		public void nullDecryptedMapTest() throws Exception {
			HttpInputMessage msg = inputMessage("{\"acctNo\":\"110\"}");

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<KeypadDecryptUtils> keypad = mockStatic(KeypadDecryptUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				SecureContext secureContext = mock(SecureContext.class, RETURNS_DEEP_STUBS);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.request()).thenReturn(mock(HttpServletRequest.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				keypad.when(() -> KeypadDecryptUtils.decrypt(any())).thenReturn(null);

				HttpInputMessage result = advice.beforeBodyRead(msg, null, String.class, null);

				assertNotNull(result.getBody());
			}
		}

		@Test
		@DisplayName("성공_복호화 값으로 body 병합 (기존값 우선/신규키 추가)")
		public void decryptAndMergeTest() throws Exception {
			HttpInputMessage msg = inputMessage("{\"pin\":\"orig\"}");

			Map<String, String> decrypted = new LinkedHashMap<>();
			decrypted.put("pin", "decValue");   // body에 존재 → 기존값 유지
			decrypted.put("otp", "");           // body에 미존재, 빈값 → 그대로 set

			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class);
				 MockedStatic<KeypadDecryptUtils> keypad = mockStatic(KeypadDecryptUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				SecureContext secureContext = mock(SecureContext.class, RETURNS_DEEP_STUBS);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.request()).thenReturn(mock(HttpServletRequest.class));
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				when(secureContext.getTokens()).thenReturn(null); // 실제 SecureTokensInfo 신규 생성 경로
				keypad.when(() -> KeypadDecryptUtils.decrypt(any())).thenReturn(decrypted);

				HttpInputMessage result = advice.beforeBodyRead(msg, null, String.class, null);

				InputStream body = result.getBody();
				String merged = new String(body.readAllBytes(), StandardCharsets.UTF_8);
				assertTrue(merged.contains("pin"));
				assertTrue(merged.contains("otp"));
			}
		}
	}
}
