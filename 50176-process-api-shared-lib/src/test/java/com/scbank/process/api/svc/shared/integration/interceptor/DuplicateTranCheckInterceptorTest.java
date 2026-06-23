package com.scbank.process.api.svc.shared.integration.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class DuplicateTranCheckInterceptorTest {

	@Mock private StringRedisTemplate redisTemplate;
	@Mock private ValueOperations<String, String> valueOperations;

	@InjectMocks private DuplicateTranCheckInterceptor interceptor;

	private void mockHttpRequest(MockedStatic<RequestContextHolder> holder, HttpServletRequest httpRequest) {
		ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
		holder.when(RequestContextHolder::currentRequestAttributes).thenReturn(attributes);
		when(attributes.getRequest()).thenReturn(httpRequest);
	}

	@Nested
	@DisplayName("전문 중복거래 체크 before")
	class Before {

		@Test
		@DisplayName("거래코드가 없으면 체크하지 않음")
		public void emptyTranCdTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("tranCd")).thenReturn("");
			when(context.getInterfaceId()).thenReturn("IF001");

			interceptor.before(context, new Object());

			verify(redisTemplate, never()).opsForValue();
		}

		@Test
		@DisplayName("성공_정상 거래키 선점(lock 성공)")
		public void lockAcquiredTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("tranCd")).thenReturn("TR001");

			HttpServletRequest httpRequest = mock(HttpServletRequest.class);
			HttpSession session = mock(HttpSession.class);
			when(httpRequest.getSession()).thenReturn(session);
			when(session.getId()).thenReturn("SESSION01");

			when(redisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.setIfAbsent(anyString(), eq("Y"), any(Duration.class))).thenReturn(Boolean.TRUE);

			try (MockedStatic<RequestContextHolder> holder = mockStatic(RequestContextHolder.class)) {
				mockHttpRequest(holder, httpRequest);

				interceptor.before(context, new Object());

				verify(context).setAttribute(eq("txKey"), anyString());
				verify(valueOperations).setIfAbsent(anyString(), eq("Y"), any(Duration.class));
			}
		}

		@Test
		@DisplayName("중복거래 발생(lock 실패)")
		public void lockFailedTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("tranCd")).thenReturn("TR001");

			HttpServletRequest httpRequest = mock(HttpServletRequest.class);
			HttpSession session = mock(HttpSession.class);
			when(httpRequest.getSession()).thenReturn(session);
			when(session.getId()).thenReturn("SESSION01");

			when(redisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.setIfAbsent(anyString(), eq("Y"), any(Duration.class))).thenReturn(Boolean.FALSE);

			try (MockedStatic<RequestContextHolder> holder = mockStatic(RequestContextHolder.class)) {
				mockHttpRequest(holder, httpRequest);

				interceptor.before(context, new Object());

				verify(valueOperations).setIfAbsent(anyString(), eq("Y"), any(Duration.class));
			}
		}

		@Test
		@DisplayName("HttpRequest가 없으면 세션ID를 빈값으로 처리")
		public void noHttpRequestTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("tranCd")).thenReturn("TR001");

			when(redisTemplate.opsForValue()).thenReturn(valueOperations);
			when(valueOperations.setIfAbsent(anyString(), eq("Y"), any(Duration.class))).thenReturn(Boolean.TRUE);

			try (MockedStatic<RequestContextHolder> holder = mockStatic(RequestContextHolder.class)) {
				mockHttpRequest(holder, null);

				interceptor.before(context, new Object());

				verify(context).setAttribute(eq("txKey"), anyString());
			}
		}
	}

	@Nested
	@DisplayName("거래 종료/오류 시 거래키 삭제")
	class Release {

		@Test
		@DisplayName("after - 거래키 존재 시 삭제")
		public void afterWithKeyTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("txKey")).thenReturn("tx:dup:SESSION01:TR001");
			when(redisTemplate.delete(anyString())).thenReturn(Boolean.TRUE);

			interceptor.after(context, new Object(), new Object());

			verify(redisTemplate).delete("tx:dup:SESSION01:TR001");
			verify(context).removeAttribute("txKey");
		}

		@Test
		@DisplayName("onError - 거래키 없으면 삭제 호출 안함")
		public void onErrorWithoutKeyTest() {
			IntegrationContext context = mock(IntegrationContext.class);
			when(context.getAttribute("txKey")).thenReturn("");

			interceptor.onError(context, new Object(), new RuntimeException("err"));

			verify(redisTemplate, never()).delete(anyString());
			verify(context).removeAttribute("txKey");
		}
	}
}
