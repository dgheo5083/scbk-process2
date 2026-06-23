package com.scbank.process.api.svc.shared.service.interceptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SignInfo;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class SignVerifyServiceInterceptorTest {

	@Mock private ISessionContextManager sessionManager;

	@InjectMocks private SignVerifyServiceInterceptor interceptor;

	@Nested
	@DisplayName("전자서명 service preHandle")
	class PreHandle {

		@Test
		@DisplayName("SecureContext 미존재 시 통과")
		public void emptyContextTest() {
			IServiceContext sCtx = mock(IServiceContext.class);
			IMessageObject request = mock(IMessageObject.class);

			try (MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class)) {
				store.when(SecureContextStore::getContext).thenReturn(Optional.empty());

				assertTrue(interceptor.preHandle(sCtx, request));
				verify(sCtx, never()).setAttribute(eq(SignConstants.SIGN_VERIFY_SERVICE_REQUEST), org.mockito.ArgumentMatchers.any());
			}
		}

		@Test
		@DisplayName("서명데이터(SignInfo) 미존재 시 통과")
		public void noSignInfoTest() {
			IServiceContext sCtx = mock(IServiceContext.class);
			IMessageObject request = mock(IMessageObject.class);
			SecureContext secureContext = mock(SecureContext.class);

			try (MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class)) {
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				when(secureContext.getSign()).thenReturn(null);

				assertTrue(interceptor.preHandle(sCtx, request));
				verify(sCtx, never()).setAttribute(eq(SignConstants.SIGN_VERIFY_SERVICE_REQUEST), org.mockito.ArgumentMatchers.any());
			}
		}

		@Test
		@DisplayName("성공_서명데이터 존재 시 context에 request 저장")
		public void signInfoExistTest() {
			IServiceContext sCtx = mock(IServiceContext.class);
			IMessageObject request = mock(IMessageObject.class);
			SecureContext secureContext = mock(SecureContext.class);
			SignInfo signInfo = mock(SignInfo.class);

			try (MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class)) {
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				when(secureContext.getSign()).thenReturn(signInfo);

				assertTrue(interceptor.preHandle(sCtx, request));
				verify(sCtx).setAttribute(SignConstants.SIGN_VERIFY_SERVICE_REQUEST, request);
			}
		}

		@Test
		@DisplayName("성공_request가 null이어도 정상 처리")
		public void signInfoExistNullRequestTest() {
			IServiceContext sCtx = mock(IServiceContext.class);
			SecureContext secureContext = mock(SecureContext.class);
			SignInfo signInfo = mock(SignInfo.class);

			try (MockedStatic<SecureContextStore> store = mockStatic(SecureContextStore.class)) {
				store.when(SecureContextStore::getContext).thenReturn(Optional.of(secureContext));
				when(secureContext.getSign()).thenReturn(signInfo);

				assertTrue(interceptor.preHandle(sCtx, null));
				verify(sCtx).setAttribute(eq(SignConstants.SIGN_VERIFY_SERVICE_REQUEST), org.mockito.ArgumentMatchers.isNull());
			}
		}
	}

	@Test
	@DisplayName("postHandle은 동작 없이 정상 수행")
	public void postHandleTest() {
		IServiceContext sCtx = mock(IServiceContext.class);
		IMessageObject request = mock(IMessageObject.class);

		interceptor.postHandle(sCtx, request);
	}
}
